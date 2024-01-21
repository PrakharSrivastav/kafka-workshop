package no.toyota.serviceautomation.kafkaapi.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "consent")
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String externalId;
    private String customerId;

    private String source;
    @Column(name = "from_date")
    private String from;
    @Column(name = "to_date")
    private String to;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consent consent)) return false;
        return Objects.equals(id, consent.id) && Objects.equals(externalId, consent.externalId) && Objects.equals(customerId, consent.customerId) && Objects.equals(source, consent.source) && Objects.equals(from, consent.from) && Objects.equals(to, consent.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, customerId, source, from, to);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Consent{" +
            "id=" + id +
            ", externalId='" + externalId + '\'' +
            ", customerId='" + customerId + '\'' +
            ", source='" + source + '\'' +
            ", from='" + from + '\'' +
            ", to='" + to + '\'' +
            '}';
    }
}
