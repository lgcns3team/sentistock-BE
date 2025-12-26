package com.example.SentiStock_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import com.example.SentiStock_backend.event.StockEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        return new DefaultErrorHandler(
                (record, ex) ->
                        log.error("Kafka consume failed: {}", record, ex),
                new FixedBackOff(0L, 0)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockEvent>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, StockEvent> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, StockEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties()
               .setAckMode(ContainerProperties.AckMode.RECORD);

        return factory;
    }
}
