package no.toyota.serviceautomation.kafkaapi.controller;

import no.toyota.serviceautomation.kafkaapi.model.Consent;
import no.toyota.serviceautomation.kafkaapi.repository.ConsentRepository;
import no.toyota.serviceautomation.kafkaapi.kafka.ConsentProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsentController {

    private ConsentRepository repository;
    private ConsentProducer producer;


    public static final Logger log = LoggerFactory.getLogger(ConsentController.class);

    public ConsentController(ConsentRepository repository, ConsentProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    @PostMapping(value = "/consent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void save(@RequestBody Consent request) throws InterruptedException {

        // log the request
        log.info("Input Request is {}", request);

        // some business logic
        log.info("Running some business logic ......");
        Thread.sleep(100);
        log.info("Business logic complete......");

        // transform to entity
        var entity = request.toConsentEntity();

        // save to database
        var consentWithId = this.repository.save(entity);
        log.info("Saved consent is {}", consentWithId);

        // send to kafka
        this.producer.SendMessage(
            consentWithId.getId(),
            consentWithId
        );
    }

}
