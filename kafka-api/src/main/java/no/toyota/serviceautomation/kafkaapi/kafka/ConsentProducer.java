package no.toyota.serviceautomation.kafkaapi.kafka;

import no.toyota.serviceautomation.kafkaapi.entity.Consent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class ConsentProducer {

    private KafkaProducer<Integer, String> kafkaProducer;

    // Not a correct way to declare a topic
    private static final String TOPIC = "ConsentTopic";

    private static Logger log = LoggerFactory.getLogger(ConsentProducer.class);

    public ConsentProducer() {
        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        this.kafkaProducer = new KafkaProducer<>(properties);
    }

    public void SendMessage(Integer key, Consent value) {
        var pr = new ProducerRecord<>(TOPIC, key, value.toString());
        this.kafkaProducer.send(pr, ((metadata, exception) -> {
            if (exception == null) {
                Map<String, Object> data = new HashMap<>();
                data.put("topic", metadata.topic());
                data.put("partition", metadata.partition());
                data.put("offset", metadata.offset());
                data.put("timestamp", metadata.timestamp());
                log.info("kafka.producer.send.ok {}", data);
            } else {
                log.error("kafka.producer.send.error ", exception);
            }
        }));

    }
}
