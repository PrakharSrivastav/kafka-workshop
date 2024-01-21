package no.toyota.serviceautomation.kafkaapi.model;

public record Consent(String id, String customerId, String source, String from, String to) {

    public no.toyota.serviceautomation.kafkaapi.entity.Consent toConsentEntity() {

        var entity = new no.toyota.serviceautomation.kafkaapi.entity.Consent();
        entity.setCustomerId(this.customerId);
        entity.setFrom(this.from);
        entity.setTo(this.to);
        entity.setSource(this.source);
        entity.setExternalId(this.id);
        return entity;
    }

}
