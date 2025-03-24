package com.app.repository;

import com.app.model.Client;
import com.app.model.Therapist;
import com.app.util.DynamoDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.UUID;

public class TherapistRepository {

    private final DynamoDbTable<Therapist> therapistTable;

    public TherapistRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDBUtil.getDynamoDbEnhancedClient();
        this.therapistTable = enhancedClient.table("Therapists", TableSchema.fromBean(Therapist.class));
    }

    public void saveTherapist(Therapist therapist) {
        therapistTable.putItem(therapist);
    }

    public Therapist getTherapistById(String therapistId) {
        return therapistTable.getItem(Key.builder().partitionValue(therapistId).build());
    }

//    public void deleteTherapist(String therapistId) {
//        Therapist therapistToDelete = new Therapist();
//        therapistToDelete.setTherapistId(therapistId);  // Set only the key attributes
//        therapistTable.deleteItem(therapistToDelete);
//    }

    public boolean deleteTherapist(String therapistId) {
        Therapist therapist = therapistTable.getItem(Key.builder().partitionValue(therapistId).build());
        if (therapist == null) {
            return false;
        }

        therapistTable.deleteItem(therapist);
        return true;
    }
}
