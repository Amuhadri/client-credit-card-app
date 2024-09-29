package hr.rba.creditcard.management.repository;

import hr.rba.creditcard.management.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
