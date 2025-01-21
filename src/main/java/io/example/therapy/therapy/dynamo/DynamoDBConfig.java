package io.example.therapy.therapy.dynamo;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Configuration
@EnableDynamoDBRepositories
  (basePackages = "io.example.therapy.therapy.repo")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Value("${aws.dynamodb.region}")
    private String amazonAWSRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        // AmazonDynamoDB amazonDynamoDB 
        //   = new AmazonDynamoDBClient(amazonAWSCredentials());
        
        // if (!StringUtils.isNullOrEmpty(amazonDynamoDBEndpoint)) {
        //     amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        // }
        return AmazonDynamoDBClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonAWSRegion))
        .withCredentials(amazonAWSCredentials()).build();

        // return amazonDynamoDB;
    }

    @Bean
    public AWSCredentialsProvider amazonAWSCredentials() {
    return new AWSStaticCredentialsProvider(
        new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey));
  }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }
}