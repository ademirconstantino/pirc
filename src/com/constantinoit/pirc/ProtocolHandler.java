package com.constantinoit.pirc;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Component
public class ProtocolHandler {

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public void connect(String server, int port) throws IOException {
        socket = new Socket(server, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to IRC server: " + server);
    }

    public void sendMessage(String message) throws IOException {
        if (writer != null) {
            writer.write(message + "\r\n");
            writer.flush();
        }
    }

    public void listenForMessages(MessageParser parser) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            parser.parseMessage(line);
        }
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}