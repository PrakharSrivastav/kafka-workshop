package no.toyota.serviceautomation.kafkaoci.kafka;

import no.toyota.serviceautomation.kafkaoci.entity.Consent;

public interface Producer {
    void SendMessage(Integer key, Consent value);

}
