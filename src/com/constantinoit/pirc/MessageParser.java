package com.constantinoit.pirc;

import org.springframework.stereotype.Component;

@Component
public class MessageParser {

    public void parseMessage(String message) {
        System.out.println("Received: " + message);

        // Parse server messages. For simplicity, this example splits by spaces.
        String[] parts = message.split(" ", 3);
        if (parts.length > 1) {
            String prefix = parts[0].startsWith(":") ? parts[0].substring(1) : null;
            String command = parts[1];
            String parameters = parts.length > 2 ? parts[2] : "";

            System.out.println("Prefix: " + prefix);
            System.out.println("Command: " + command);
            System.out.println("Parameters: " + parameters);
        } else {
            System.out.println("Invalid message format: " + message);
        }
    }
}