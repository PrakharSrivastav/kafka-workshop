package no.toyota.serviceautomation.kafkaapi.kafka;

import no.toyota.serviceautomation.kafkaapi.entity.Consent;

public interface Producer {
    void SendMessage(Integer key, Consent value);

}
