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

public class ConsentWithTxProducer implements Producer {

    private KafkaProducer<Integer, String> kafkaProducer;

    // Not a correct way to declare a topic
    private static final String TOPIC = "ConsentTopic";

    private static Logger log = LoggerFactory.getLogger(ConsentWithTxProducer.class);

    public ConsentWithTxProducer() {
        log.info("***********************");
        log.info("ConsentWithTxProducer");
        log.info("***********************");

        try {
            var properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

            // throughput related settings
            properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 256);

            // acks = 0 : producer acts in fire and forget mode
            // acks = 1 : only to the leader
            // acks = all/-1 : waits for All ISR
            properties.put(ProducerConfig.ACKS_CONFIG, "all");

            // tx related props
            properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
            properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "kafka-producer-tx-committed");
            properties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "5000");


            this.kafkaProducer = new KafkaProducer<>(properties);

            // enable producer in transaction mode
            this.kafkaProducer.initTransactions();


        } catch (Exception e) {
            log.error("kafka.producer.create.error", e);
            throw new IllegalStateException("Error creating Kafka Producer", e);
        }
    }

    public void SendMessage(Integer key, Consent value) {
        try {
            kafkaProducer.beginTransaction();
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

            this.kafkaProducer.commitTransaction();

        } catch (Exception exception) {
            log.error("kafka.producer.send.error ", exception);
            this.kafkaProducer.abortTransaction();
        }


    }
}
