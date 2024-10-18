package com.example;

import okhttp3.*;

import java.io.IOException;
import java.util.Base64;

public class UpstashQueueService implements QueueService {

    private final String redisUrl = "https://<your-upstash-url>.upstash.io";
    private final String authHeader;

    public UpstashQueueService(String apiToken) {
        // Encode the token for HTTP Basic Auth
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString((apiToken + ":").getBytes());
    }

    // Helper method to send HTTP requests to Upstash
    private String sendRequest(String requestBody) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                requestBody, MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(redisUrl)
                .addHeader("Authorization", authHeader)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    // Push a message to the queue with a priority
    @Override
    public void push(String queueUrl, String messageBody) {
        int priority = extractPriorityFromMessage(messageBody); // Define your own priority extraction
        String requestBody = String.format("{\"zadd\": [\"%s\", \"%d\", \"%s\"]}", queueUrl, priority, messageBody);
        try {
            sendRequest(requestBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Pull the highest priority message from the queue
    @Override
    public Message pull(String queueUrl) {
        String requestBody = String.format("{\"zpopmax\": [\"%s\"]}", queueUrl);
        try {
            String response = sendRequest(requestBody);
            // Parse the response and create a Message object
            return parseResponseToMessage(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Delete a message by receiptId
    @Override
    public void delete(String queueUrl, String receiptId) {
        String requestBody = String.format("{\"zrem\": [\"%s\", \"%s\"]}", queueUrl, receiptId);
        try {
            sendRequest(requestBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int extractPriorityFromMessage(String messageBody) {
        // Extract priority from the message body. Implement according to your needs.
        return 0; // Placeholder
    }

    private Message parseResponseToMessage(String response) {
        // Parse the Upstash Redis response to extract message information and return a Message object
        return new Message(response, "receiptId"); // Placeholder
    }
}

