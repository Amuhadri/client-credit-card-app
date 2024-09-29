package hr.rba.creditcard.management.repository;

import hr.rba.creditcard.management.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByOib(String oib);
}
