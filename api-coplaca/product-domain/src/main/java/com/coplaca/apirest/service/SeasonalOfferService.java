package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.SeasonalOfferDTO;
import com.coplaca.apirest.entity.SeasonalOffer;

import java.util.List;
import java.util.Optional;

public interface SeasonalOfferService {
    List<SeasonalOfferDTO> getAllActiveOffers();
    List<SeasonalOffer> getCurrentActiveOffers();
    Optional<SeasonalOffer> getActiveOfferByProductId(Long productId);
    SeasonalOfferDTO getOfferById(Long id);
    SeasonalOfferDTO createOffer(SeasonalOffer offer);
    SeasonalOfferDTO updateOffer(Long id, SeasonalOffer offer);
    void deactivateOffer(Long id);
}
