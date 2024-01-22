package no.toyota.serviceautomation.kafkaspring.repository;

import no.toyota.serviceautomation.kafkaspring.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentRepository extends JpaRepository<Consent,Integer> {
}
