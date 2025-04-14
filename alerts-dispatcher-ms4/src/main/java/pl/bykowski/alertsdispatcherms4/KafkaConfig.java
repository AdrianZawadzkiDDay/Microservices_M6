package pl.bykowski.alertsdispatcherms4;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.bykowski.Alert;
import pl.bykowski.RamAlert;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, Alert> alertConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "alerts-dispatcher");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "pl.bykowski");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Alert> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Alert> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(alertConsumerFactory());
        factory.setConcurrency(3);
        return factory;
    }


//    @Bean
//    public ConsumerFactory<String, RamAlert> consumerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        config.put(ConsumerConfig.GROUP_ID_CONFIG, "data-alerts-dispatcher");
//        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        JsonDeserializer<RamAlert> deserializer = new JsonDeserializer<>();
//        deserializer.addTrustedPackages("pl.bykowski");
//        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
//    }
//
//    @Bean
//    public KafkaListenerContainerFactory<?> getKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, RamAlert> factory
//                = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
////        factory.setConcurrency(3);
////        factory.setCommonErrorHandler(new DefaultErrorHandler());
////
////        factory.setRecordFilterStrategy(record -> {
////            return record.value().getRamUsage() > 50;
////        });
//        return factory;
//    }

}
