package no.toyota.serviceautomation.kafkaoci.controller;

import no.toyota.serviceautomation.kafkaoci.kafka.Producer;
import no.toyota.serviceautomation.kafkaoci.model.Consent;
import no.toyota.serviceautomation.kafkaoci.repository.ConsentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsentController {

    private ConsentRepository repository;
    private Producer producer;


    public static final Logger log = LoggerFactory.getLogger(ConsentController.class);

    public ConsentController(ConsentRepository repository, Producer producer) {
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
