package com.app.repository;

import com.app.model.Client;
import com.app.util.DynamoDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.UUID;

public class ClientRepository {

    private final DynamoDbTable<Client> clientTable;

    public ClientRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDBUtil.getDynamoDbEnhancedClient();
        this.clientTable = enhancedClient.table("Clients", TableSchema.fromBean(Client.class));
    }

    public void saveClient(Client client) {
        clientTable.putItem(client);
    }

    public Client getClientById(String clientId) {
        return clientTable.getItem(Key.builder().partitionValue(clientId).build());
    }

//    public void deleteClient(String clientId) {
//        Client clientToDelete = new Client();
//        clientToDelete.setClientId(clientId);  // Set only the key attributes
//        clientTable.deleteItem(clientToDelete);
//    }

    public boolean deleteClient(String clientId) {
        Client client = clientTable.getItem(Key.builder().partitionValue(clientId).build());
        if (client == null) {
            return false;
        }

        clientTable.deleteItem(client);
        return true;
    }
}
