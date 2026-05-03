package com.leonardo.orderprocessing.infrastructure.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class KafkaConfig {

    // Jackson 3.x (tools.jackson) with built-in java.time support via DateTimeFeature.
    private JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(KafkaProperties props) {
        JacksonJsonSerializer<Object> serializer = new JacksonJsonSerializer<>(jsonMapper());

        ProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(
                props.buildProducerProperties(),
                new StringSerializer(),
                serializer);

        return new KafkaTemplate<>(factory);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties props) {
        JacksonJsonDeserializer<Object> deserializer = new JacksonJsonDeserializer<>(jsonMapper());
        deserializer.addTrustedPackages("com.leonardo.orderprocessing.*");

        return new DefaultKafkaConsumerFactory<>(
                props.buildConsumerProperties(),
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
