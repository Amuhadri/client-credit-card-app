package rba.hr.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rba.hr.api.entity.NewCardRequest;

public interface CardRequestRepository extends JpaRepository<NewCardRequest, Long> {
}
