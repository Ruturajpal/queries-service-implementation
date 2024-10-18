package com.example;

import java.util.Scanner;

public class QueueServiceApp {

    public static void main(String[] args) {
        FileQueueService queueService = new FileQueueService();

        // You can set a time supplier if necessary (useful for testing).
        // queueService.setTimeSupplier(() -> System.currentTimeMillis());

        Scanner scanner = new Scanner(System.in);
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/1234567890/sampleQueue";

        System.out.println("Queue Service started...");
        while (true) {
            System.out.println("\nChoose an operation:");
            System.out.println("1: Push a message to the queue");
            System.out.println("2: Pull a message from the queue");
            System.out.println("3: Delete a message from the queue");
            System.out.println("4: Purge the queue");
            System.out.println("5: Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter the message to push: ");
                    String message = scanner.nextLine();
                    queueService.push(queueUrl, message);
                    System.out.println("Message pushed successfully!");
                    break;

                case 2:
                    System.out.println("Pulling a message from the queue...");
                    Message pulledMessage = queueService.pull(queueUrl);
                    if (pulledMessage != null) {
                        System.out.println("Pulled message: " + pulledMessage.getBody());
                        System.out.println("Receipt ID: " + pulledMessage.getReceiptId());
                    } else {
                        System.out.println("No visible messages in the queue.");
                    }
                    break;

                case 3:
                    System.out.print("Enter the receipt ID of the message to delete: ");
                    String receiptId = scanner.nextLine();
                    queueService.delete(queueUrl, receiptId);
                    System.out.println("Message deleted successfully (if it exists).");
                    break;

                case 4:
                    System.out.println("Purging the queue...");
                    queueService.purgeQueue(queueUrl);
                    System.out.println("Queue purged successfully.");
                    break;

                case 5:
                    System.out.println("Exiting Queue Service...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }
}

