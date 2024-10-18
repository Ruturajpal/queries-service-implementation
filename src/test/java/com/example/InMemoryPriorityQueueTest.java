package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InMemoryPriorityQueueTest {

    @Test
    public void testPriorityQueue() {
        InMemoryPriorityQueue<String> queue = new InMemoryPriorityQueue<>();

        queue.addRequest("Low priority", 1);
        queue.addRequest("High priority", 5);
        queue.addRequest("Medium priority", 3);

        assertEquals("High priority", queue.pollRequest());
        assertEquals("Medium priority", queue.pollRequest());
        assertEquals("Low priority", queue.pollRequest());
    }
}

