package no.toyota.serviceautomation.kafkaapi.repository;

import no.toyota.serviceautomation.kafkaapi.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentRepository extends JpaRepository<Consent,Integer> {
}
