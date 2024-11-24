package com.constantinoit.pirc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ProtocolHandler protocolHandler = context.getBean(ProtocolHandler.class);
        MessageParser messageParser = context.getBean(MessageParser.class);

        try {
            protocolHandler.connect("irc.example.com", 6667);
            protocolHandler.sendMessage("NICK MyNick");
            protocolHandler.sendMessage("USER MyUser 0 * :Real Name");
            protocolHandler.listenForMessages(messageParser);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                protocolHandler.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}