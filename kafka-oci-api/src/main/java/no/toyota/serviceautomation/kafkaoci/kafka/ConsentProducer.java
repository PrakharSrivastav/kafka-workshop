package no.toyota.serviceautomation.kafkaoci.kafka;

import no.toyota.serviceautomation.kafkaoci.entity.Consent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConsentProducer implements Producer {

    private KafkaProducer<Integer, String> kafkaProducer;

    // Not a correct way to declare a topic
    private static final String TOPIC = "PS_STREAM_TEST";

    private static Logger log = LoggerFactory.getLogger(ConsentProducer.class);

    public ConsentProducer() {
        log.info("***********************");
        log.info("ConsentProducer");
        log.info("***********************");

        try {
            var properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "cell-1.streaming.eu-frankfurt-1.oci.oraclecloud.com:9092");
            properties.put("security.protocol", "SASL_SSL");
            properties.put("sasl.mechanism", "PLAIN");
            properties.put("sasl.jaas.config", "");

            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

            // throughput related settings
            properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 10000); // 10 kb
            properties.put(ProducerConfig.LINGER_MS_CONFIG, 3000); // 3 sec

            // acks = 0 : producer acts in fire and forget mode
            // acks = 1 : only to the leader
            // acks = all/-1 : waits for All ISR
            properties.put(ProducerConfig.ACKS_CONFIG, "all");
            this.kafkaProducer = new KafkaProducer<>(properties);
        } catch (Exception e) {
            log.error("kafka.producer.create.error", e);
            throw new IllegalStateException("Error creating Kafka Producer", e);
        }
    }

    public void SendMessage(Integer key, Consent value) {
        try {
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


        } catch (Exception exception) {
            log.error("kafka.producer.send.error ", exception);
        }


    }
}
