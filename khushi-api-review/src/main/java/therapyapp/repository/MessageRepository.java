package therapyapp.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import therapyapp.model.Message;

public class MessageRepository {

    private final Table table;

    public MessageRepository(AmazonDynamoDB dynamoDB) {
        this.table = new DynamoDB(dynamoDB).getTable("Messages");
    }

    
    public void saveMessage(Message message) {
        Item item = new Item()
                .withPrimaryKey("conversationId", message.getConversationId(), "timestamp", message.getTimestamp().toString())
                .withString("senderId", message.getSenderId())
                .withString("receiverId", message.getReceiverId())
                .withString("message", message.getMessage());

        table.putItem(item);
    }


    public List<Message> getMessagesByConversationId(String conversationId) {
        List<Message> messages = new ArrayList<>();

        table.query("conversationId", conversationId)
                .forEach(item -> messages.add(new Message(
                        item.getString("conversationId"),
                        item.getString("senderId"),
                        item.getString("receiverId"),
                        item.getString("message"),
                        Instant.parse(item.getString("timestamp"))
                )));

        return messages;
    }

    
    public List<Message> getMessagesBySenderId(String senderId) {
        List<Message> messages = new ArrayList<>();

        table.getIndex("sender-index")
                .query("senderId", senderId)
                .forEach(item -> messages.add(new Message(
                        item.getString("conversationId"),
                        item.getString("senderId"),
                        item.getString("receiverId"),
                        item.getString("message"),
                        Instant.parse(item.getString("timestamp"))
                )));

        return messages;
    }
}
