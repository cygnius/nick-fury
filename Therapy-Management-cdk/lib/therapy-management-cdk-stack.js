const cdk = require("aws-cdk-lib");
const dynamodb = require("aws-cdk-lib/aws-dynamodb");
const lambda = require("aws-cdk-lib/aws-lambda");
const apigateway = require("aws-cdk-lib/aws-apigateway");
const iam = require("aws-cdk-lib/aws-iam");
const fs = require("fs");
const yaml = require("yaml");
const path = require("path");
require("dotenv").config();

class TherapyManagementStack extends cdk.Stack {
  constructor(scope, id, props) {
    super(scope, id, props);

    const dbSchema = JSON.parse(
      fs.readFileSync(path.join(__dirname, "../schema/dynamodb.json"), "utf-8")
    );
    const apiDesign = yaml.parse(
      fs.readFileSync(
        path.join(__dirname, "../schema/api-design.yaml"),
        "utf-8"
      )
    );

    const tables = {};

    dbSchema.Tables.forEach((tableDef) => {
      const table = new dynamodb.Table(this, `${tableDef.TableName}Table`, {
        tableName: tableDef.TableName,
        partitionKey: {
          name: tableDef.PartitionKey,
          type: dynamodb.AttributeType.STRING
        },
        ...(tableDef.SortKey && {
          sortKey: {
            name: tableDef.SortKey,
            type: dynamodb.AttributeType.STRING
          }
        }),
        billingMode: dynamodb.BillingMode.PAY_PER_REQUEST,
        removalPolicy: cdk.RemovalPolicy.DESTROY,
      });

      tableDef.GSIs?.forEach(gsi => {
        table.addGlobalSecondaryIndex({
          indexName: gsi.name,
          partitionKey: {
            name: gsi.partitionKey,
            type: dynamodb.AttributeType.STRING
          },
          ...(gsi.sortKey && {
            sortKey: {
              name: gsi.sortKey,
              type: dynamodb.AttributeType.STRING
            }
          }),
          projectionType: dynamodb.ProjectionType.ALL
        });
      });

      tables[tableDef.TableName] = table;
    });

    const lambdaConfig = {
      runtime: lambda.Runtime.NODEJS_18_X,
      memorySize: 256,
      timeout: cdk.Duration.seconds(30),
      environment: {
        USERS_TABLE: tables.Users.tableName,
        CLIENTS_TABLE: tables.Clients.tableName,
        THERAPISTS_TABLE: tables.Therapists.tableName,
        MAPPING_TABLE: tables.Mapping.tableName,
        JOURNAL_TABLE: tables.Journal.tableName,
        SESSION_TABLE: tables.Session.tableName,
        NOTES_TABLE: tables.Notes.tableName,
        MESSAGES_TABLE: tables.Messages.tableName,
        JWT_SECRET: process.env.JWT_SECRET || "default-secret",
      },
    };

    const dynamoPolicy = new iam.PolicyStatement({
      actions: [
        "dynamodb:GetItem",
        "dynamodb:PutItem",
        "dynamodb:UpdateItem",
        "dynamodb:DeleteItem",
        "dynamodb:Query",
        "dynamodb:Scan",
        "dynamodb:BatchGetItem",
        "dynamodb:BatchWriteItem",
      ],
      resources: [
        ...Object.values(tables).map(table => table.tableArn),
        ...Object.values(tables).flatMap(table => [
          `${table.tableArn}/index/*`
        ]),
      ],
    });

    const lambdas = {};
    const lambdaPaths = {
      "/auth/register/client": "auth/register-client",
      "/auth/register/therapist": "auth/register-therapist",
      "/auth/login": "auth/login",
      
      "/clients/{clientId}": "clients/get-client",
      "/clients/{clientId}/journals": "clients/journals/create-journal",
      "/clients/{clientId}/journals": "clients/journals/get-journals",
      "/clients/{clientId}/journals/search": "clients/journals/search-journals",
      
      "/therapist/{therapistId}": "therapists/get-therapist",
      "/therapists/{therapistId}/clients": "therapists/get-clients",
      "/therapists/{therapistId}/clients/search": "therapists/search-clients",
      
      "/therapists/{therapistId}/availability": "sessions/availability/create-slot",
      "/therapists/{therapistId}/availability": "sessions/availability/get-slots",
      "/therapists/{therapistId}/availability/{slotId}": "sessions/availability/update-slot",
      "/therapists/{therapistId}/availability/{slotId}": "sessions/availability/delete-slot",
      "/sessions/requests": "sessions/request-session",
      "/sessions/requests/{sessionId}": "sessions/respond-session",
      "/sessions/client/{clientId}": "sessions/get-client-sessions",
      "/sessions/therapist/{therapistId}": "sessions/get-therapist-sessions",
      "/sessions/{sessionId}/status": "sessions/update-session-status",
      
      "/sessions/{sessionId}/notes": "notes/create-note",
      "/sessions/{sessionId}/notes": "notes/get-notes",
      "/sessions/{sessionId}/notes/search": "notes/search-notes",
      
      "/mappings/requests": "mappings/create-mapping",
      "/mappings/{clientId}/status": "mapping/update-mapping-status",
      "/mappings/client/{clientId}/requests": "mappings/get-mappings",
      "/mappings/client/{clientId}/requests/{mappingId}": "mappings/respond-mapping",
      "/mappings/client/{clientId}/journalAccess/{mappingId}": "mappings/update-journal-access",
      "/mappings/client/{clientId}/connected-therapist": "mappings/get-connected-therapists",
      "/mappings/therapist/{therapistId}/connected-clients": "mappings/get-connected-clients",
      
      "/messages": "messages/send-message",
      "/messages/{userId}": "messages/get-messages",
      "/messages/{messageId}/read": "messages/mark-read",
      
      "/appointments/{userId}": "appointments/get-appointments",
      "/appointments/{appointmentId}/status": "appointments/update-status",

      "/search": "search/global-search",
    };

    Object.entries(lambdaPaths).forEach(([path, lambdaPath]) => {
      const lambdaName = path.split('/').filter(Boolean).join('-').replace(/{|}/g, '');
      lambdas[path] = new lambda.Function(this, `${lambdaName}Function`, {
        ...lambdaConfig,
        code: lambda.Code.fromAsset(path.join(__dirname, `../lambda/${lambdaPath}`)),
        handler: "index.handler",
      });
      lambdas[path].addToRolePolicy(dynamoPolicy);
    });

    const api = new apigateway.RestApi(this, "TherapyApi", {
      restApiName: "Therapy Management API",
      description: apiDesign.info.description,
      deployOptions: {
        stageName: apiDesign.servers[0].url.split("/").pop() || "v1",
        throttlingRateLimit: 100,
        throttlingBurstLimit: 200,
      },
      defaultCorsPreflightOptions: {
        allowOrigins: apigateway.Cors.ALL_ORIGINS,
        allowMethods: apigateway.Cors.ALL_METHODS,
        allowHeaders: ["Content-Type", "Authorization", "X-Amz-Date"],
        allowCredentials: true,
      },
    });

    Object.entries(apiDesign.paths).forEach(([path, methods]) => {
      const resource = path.split('/').slice(1).reduce((parent, part) => {
        if (part.startsWith('{')) {
          return parent.addResource('{proxy+}');
        }
        const existing = parent.getResource(part);
        return existing || parent.addResource(part);
      }, api.root);

      Object.entries(methods).forEach(([method, config]) => {
        if (lambdas[path]) {
          resource.addMethod(
            method.toUpperCase(),
            new apigateway.LambdaIntegration(lambdas[path]),
            {
              operationName: config.operationId,
              methodResponses: this.mapApiResponses(config.responses),
            }
          );
        }
      });
    });

    new cdk.CfnOutput(this, "ApiEndpoint", {
      value: api.url,
      description: "Therapy Management API Endpoint",
    });
  }

  mapApiResponses(responses) {
    return Object.keys(responses).map(statusCode => ({
      statusCode,
      responseModels: {
        "application/json": apigateway.Model.EMPTY_MODEL,
      },
    }));
  }
}

module.exports = { TherapyManagementStack };