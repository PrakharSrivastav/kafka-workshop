package no.toyota.serviceautomation.kafkaspring.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class Producer {

    private static final String TOPIC = "ConsentTopic";
    private static final Logger log = LoggerFactory.getLogger(Producer.class);
    private KafkaTemplate<Integer, String> consentStringTemplate;

    public Producer(KafkaTemplate<Integer, String> consentStringTemplate) {
        this.consentStringTemplate = consentStringTemplate;
    }

    public void send(Integer key, String value) {


        ProducerRecord<Integer, String> pr = new ProducerRecord<>(TOPIC, key, value);
        CompletableFuture<SendResult<Integer, String>> future = this.consentStringTemplate.send(pr);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                Map<String, Object> data = new HashMap<>();
                data.put("topic", result.getRecordMetadata().topic());
                data.put("partition", result.getRecordMetadata().partition());
                data.put("offset", result.getRecordMetadata().offset());
                data.put("timestamp", result.getRecordMetadata().timestamp());
                log.info("kafka.producer.send.ok {}", data);
            } else {
                log.error("kafka.producer.send.error ", ex);
            }
        });


    }
}
