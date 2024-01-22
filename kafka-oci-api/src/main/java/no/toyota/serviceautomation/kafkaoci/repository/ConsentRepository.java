package no.toyota.serviceautomation.kafkaoci.repository;

import no.toyota.serviceautomation.kafkaoci.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentRepository extends JpaRepository<Consent,Integer> {
}
