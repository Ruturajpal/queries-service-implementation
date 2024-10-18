package com.example;

import java.util.*;

public class InMemoryPriorityQueue<T> {
    // A map to store lists of requests by priority
    private Map<Integer, Queue<T>> priorityQueueMap;
    private TreeSet<Integer> priorities;  // Sorted priorities

    public InMemoryPriorityQueue() {
        priorityQueueMap = new HashMap<>();
        priorities = new TreeSet<>(Collections.reverseOrder());  // Highest priority first
    }

    // Method to add request to the queue
    public void addRequest(T request, int priority) {
        priorityQueueMap.putIfAbsent(priority, new LinkedList<>());
        priorityQueueMap.get(priority).add(request);
        priorities.add(priority);
    }

    // Method to poll the request with highest priority
    public T pollRequest() {
        if (priorities.isEmpty()) return null;

        int highestPriority = priorities.first();
        Queue<T> queue = priorityQueueMap.get(highestPriority);

        T request = queue.poll();
        if (queue.isEmpty()) {
            priorityQueueMap.remove(highestPriority);
            priorities.remove(highestPriority);
        }

        return request;
    }

    // Other necessary methods (peek, size, etc.)
}
