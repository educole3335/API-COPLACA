package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.SeasonalOfferDTO;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.entity.SeasonalOffer;
import com.coplaca.apirest.mapper.SeasonalOfferMapper;
import com.coplaca.apirest.repository.SeasonalOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SeasonalOfferServiceImpl implements SeasonalOfferService {

    private final SeasonalOfferRepository offerRepository;
    private final SeasonalOfferMapper offerMapper;

    public SeasonalOfferServiceImpl(SeasonalOfferRepository offerRepository, SeasonalOfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeasonalOfferDTO> getAllActiveOffers() {
        return offerRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeasonalOffer> getCurrentActiveOffers() {
        LocalDateTime now = LocalDateTime.now();
        return offerRepository.findByIsActiveTrue().stream()
                .filter(offer -> offer.getStartDate() != null && offer.getEndDate() != null)
                .filter(offer -> offer.getStartDate().isBefore(now) && offer.getEndDate().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeasonalOffer> getActiveOfferByProductId(Long productId) {
        return offerRepository.findByProductIdAndIsActiveTrue(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public SeasonalOfferDTO getOfferById(Long id) {
        SeasonalOffer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        return convertToDTO(offer);
    }

    @Override
    public SeasonalOfferDTO createOffer(SeasonalOffer offer) {
        SeasonalOffer saved = offerRepository.save(offer);
        return convertToDTO(saved);
    }

    @Override
    public SeasonalOfferDTO updateOffer(Long id, SeasonalOffer offerDetails) {
        SeasonalOffer o = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        o.setDiscountPercentage(offerDetails.getDiscountPercentage());
        o.setReason(offerDetails.getReason());
        o.setStartDate(offerDetails.getStartDate());
        o.setEndDate(offerDetails.getEndDate());
        o.setActive(offerDetails.isActive());
        offerRepository.save(o);
        return convertToDTO(o);
    }

    @Override
    public void deactivateOffer(Long id) {
        SeasonalOffer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
        offer.setActive(false);
        offerRepository.save(offer);
    }

    private SeasonalOfferDTO convertToDTO(SeasonalOffer offer) {
        return offerMapper.toDTO(offer);
    }
}