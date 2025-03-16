package therapyapp.handler.clients;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;

public class JournalHandler implements RequestHandler<Map<String, Object>, String> {

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        // Placeholder for adding an emotion entry to the journal
        context.getLogger().log("Input: " + input);

        if (input.containsKey("emotion") && input.containsKey("intensity") && input.containsKey("timestamp")) {
            String emotion = (String) input.get("emotion");
            int intensity = (int) input.get("intensity");
            String timestamp = (String) input.get("timestamp");

            // Placeholder for saving journal entry
            return "Journal entry added: Emotion = " + emotion + ", Intensity = " + intensity + ", Timestamp = " + timestamp;
        } else {
            return "Invalid input: 'emotion', 'intensity', and 'timestamp' are required.";
        }
    }
}