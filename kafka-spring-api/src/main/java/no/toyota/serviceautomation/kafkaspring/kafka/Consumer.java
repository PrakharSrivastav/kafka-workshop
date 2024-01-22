package no.toyota.serviceautomation.kafkaspring.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "ConsentTopic")
    public void listen(ConsumerRecord<Integer, String> record, ConsumerRecordMetadata meta) {
        log.info("******************************************");
        log.info("Topic : {}", record.topic());
        log.info("Partition : {}", record.partition());
        log.info("Offset : {}", record.offset());
        log.info("Headers : {}", record.headers());
        log.info("Key : {} , Value : {}", record.key(), record.value());
        log.info("******************************************");
    }
}
