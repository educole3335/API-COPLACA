package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.SeasonalOfferDTO;
import com.coplaca.apirest.entity.SeasonalOffer;

import java.util.List;

public interface SeasonalOfferService {
    List<SeasonalOfferDTO> getAllActiveOffers();
    SeasonalOfferDTO getOfferById(Long id);
    SeasonalOfferDTO createOffer(SeasonalOffer offer);
    SeasonalOfferDTO updateOffer(Long id, SeasonalOffer offer);
    void deactivateOffer(Long id);
}
