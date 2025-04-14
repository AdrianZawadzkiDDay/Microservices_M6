package pl.bykowski.cpurammetricscollectorms1;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.bykowski.SystemMetrics;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, SystemMetrics> producerFactory() {
        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.10.1:9092");
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        JsonSerializer<SystemMetrics> serializer = new JsonSerializer<>();
        // serializer.setAddTypeInfo(false);
        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, SystemMetrics> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
