package no.toyota.serviceautomation.kafkaapi;

import no.toyota.serviceautomation.kafkaapi.kafka.ConsentProducer;
import no.toyota.serviceautomation.kafkaapi.kafka.Producer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Configuration for standalone consumer
    private static final String CONSUMER_GROUP = "StandAloneApp";
    private static final Logger log = LoggerFactory.getLogger(Application.class);


    @Bean
    public Producer producer() {
        return new ConsentProducer();
    }


    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> props = new HashMap<>();
        // connection details to brokers + other supporting tools like schema registry ... etc
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");

        // deserialization config
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // consumer group
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP);

        // read from the last acknowledged message
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // auto commit interval (Ack to kafka that offset for last polled set of messages can be committed)
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 200); // only when above is true

        // polling related config
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 200);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 1000); // every second

        // when to fetch data
        // either 256 bytes or 400 ms, whichever happens first
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 256);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 400);


        // create a consumer using props
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singleton("ConsentTopic"));

        while (true) {
            var records = consumer.poll(Duration.ofMillis(1000));
            if (records.count() > 0) {
                log.info("******************************************");
                log.info("Fetched {} records", records.count());
                records.forEach(data -> {
                    log.info("Topic : {}", data.topic());
                    log.info("Partition : {}", data.partition());
                    log.info("Offset : {}", data.offset());
                    log.info("Headers : {}", data.headers());
                    log.info("Key : {} , Value : {}", data.key(), data.value());
                });
                log.info("******************************************");
                // manually acknowledge after each poll
                consumer.commitSync();
            }
        }
    }
}
