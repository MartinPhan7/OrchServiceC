package com.example.orchservicec.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Component
public class ConsumerMessage {

    private final Logger Log = LoggerFactory.getLogger(ConsumerMessage.class);
    private final StreamBridge streamBridge;

    @Autowired
    public ConsumerMessage(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Bean
    public Consumer<Message<List<String>>> messageInput() {
        return message -> {
            String serviceTarget = Objects.requireNonNull(message.getHeaders().get("serviceTarget")).toString();
            if(serviceTarget.equals("ServiceC")) {
                this.streamBridge.send("envoiMessageServiceOrch-out-0", message.getPayload());
            }
            Log.info("Message received: {} at {}", message.getPayload().getLast(), LocalDateTime.now());
        };
    }
}
