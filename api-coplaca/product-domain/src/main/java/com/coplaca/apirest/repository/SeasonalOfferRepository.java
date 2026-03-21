package com.coplaca.apirest.repository;

import com.coplaca.apirest.entity.SeasonalOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonalOfferRepository extends JpaRepository<SeasonalOffer, Long> {
    Optional<SeasonalOffer> findByProductIdAndIsActiveTrue(Long productId);
    List<SeasonalOffer> findByIsActiveTrueAndEndDateAfter(LocalDateTime endDate);
    List<SeasonalOffer> findByIsActiveTrue();
}
