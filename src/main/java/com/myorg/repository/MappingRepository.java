package com.myorg.repository;

import com.myorg.models.Mapping;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

public class MappingRepository {

    private final DynamoDbClient db = DynamoDbClient.builder()
            .region(Region.of(System.getenv("AWS_REGION")))
            .build();

    private final String tableName = System.getenv("MAPPING_TABLE");
    private final String GSI_NAME = "client-therapist-index";

    public Mapping getMapping(String clientId, String therapistId) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":clientId", AttributeValue.builder().s(clientId).build());
        expressionValues.put(":therapistId", AttributeValue.builder().s(therapistId).build());
//        expressionValues.put(":clientId", AttributeValue.fromS(clientId));
//        expressionValues.put(":therapistId", AttributeValue.fromS(therapistId));

        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName(GSI_NAME)
                .keyConditionExpression("client_id = :clientId and therapist_id = :therapistId")
                .expressionAttributeValues(expressionValues)
                .build();

        List<Map<String, AttributeValue>> items = db.query(request).items();

        if (items.isEmpty()) return null;

        Map<String, AttributeValue> item = items.get(0);
        return mapToModel(item);
    }

    public Mapping createMapping(String clientId, String therapistId, boolean journalAccess) {
        String id = UUID.randomUUID().toString();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(id).build());
        item.put("client_id", AttributeValue.builder().s(clientId).build());
        item.put("therapist_id", AttributeValue.builder().s(therapistId).build());
        item.put("journalAccess", AttributeValue.builder().bool(journalAccess).build());
//        item.put("id", AttributeValue.fromS(id));
//        item.put("client_id", AttributeValue.fromS(clientId));
//        item.put("therapist_id", AttributeValue.fromS(therapistId));
//        item.put("journalAccess", AttributeValue.fromBool(journalAccess));

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        db.putItem(request);
        return new Mapping(id, clientId, therapistId, journalAccess);
    }

    public boolean updateMapping(String clientId, String therapistId, boolean journalAccess) {
        Mapping existing = getMapping(clientId, therapistId);
        if (existing == null) return false;

        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder().s(existing.getId()).build()
        );

        Map<String, AttributeValueUpdate> updates = Map.of(
                "journalAccess", AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().bool(journalAccess).build())
                        .action(AttributeAction.PUT)
                        .build()
        );
//        Map<String, AttributeValue> key = Map.of("id", AttributeValue.fromS(existing.getId()));
//
//        Map<String, AttributeValueUpdate> updates = Map.of(
//                "journalAccess", AttributeValueUpdate.builder()
//                        .value(AttributeValue.fromBool(journalAccess))
//                        .action(AttributeAction.PUT)
//                        .build()
//        );

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(updates)
                .build();

        db.updateItem(request);
        return true;
    }

    public boolean deleteMapping(String clientId, String therapistId) {
        Mapping existing = getMapping(clientId, therapistId);
        if (existing == null) return false;

        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder().s(existing.getId()).build()
        );
//        Map<String, AttributeValue> key = Map.of("id", AttributeValue.fromS(existing.getId()));

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        db.deleteItem(request);
        return true;
    }

    private Mapping mapToModel(Map<String, AttributeValue> item) {
        return new Mapping(
                item.get("id").s(),
                item.get("client_id").s(),
                item.get("therapist_id").s(),
                item.get("journalAccess").bool()
        );
    }
}
