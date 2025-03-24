package com.app.repository;

import com.app.model.Mapping;
import com.app.util.DynamoDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class MappingRepository {

    private final DynamoDbTable<Mapping> mappingTable;
    private final ClientRepository clientRepository;
    private final TherapistRepository therapistRepository;

    public MappingRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDBUtil.getDynamoDbEnhancedClient();
        this.mappingTable = enhancedClient.table("Mappings", TableSchema.fromBean(Mapping.class));
        this.clientRepository = new ClientRepository();
        this.therapistRepository = new TherapistRepository();
    }

    public boolean clientAndTherapistExist(String clientId, String therapistId) {
        return clientRepository.getClientById(clientId) != null && therapistRepository.getTherapistById(therapistId) != null;
    }

    public void saveMapping(Mapping mapping) {
        mappingTable.putItem(mapping);
    }

//    public void deleteMapping(String clientId, String therapistId) {
//        // Attempt to delete the mapping
//        Key key = Key.builder()
//                .partitionValue(clientId)
//                .sortValue(therapistId)
//                .build();
//
//        mappingTable.deleteItem(key);
//    }

    public boolean deleteMapping(String clientId, String therapistId) {
        Mapping mapping = mappingTable.getItem(Key.builder().partitionValue(clientId).sortValue(therapistId).build());
        if (mapping == null) {
            return false;
        }

        mappingTable.deleteItem(mapping);
        return true;
    }
}
