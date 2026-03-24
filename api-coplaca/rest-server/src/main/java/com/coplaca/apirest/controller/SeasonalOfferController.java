package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.SeasonalOfferDTO;
import com.coplaca.apirest.entity.SeasonalOffer;
import com.coplaca.apirest.service.SeasonalOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class SeasonalOfferController {

    private final SeasonalOfferService offerService;

    @GetMapping
    public ResponseEntity<List<SeasonalOfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllActiveOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeasonalOfferDTO> getOffer(@PathVariable Long id) {
        SeasonalOfferDTO dto = offerService.getOfferById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<SeasonalOfferDTO> createOffer(@RequestBody SeasonalOffer offer) {
        SeasonalOfferDTO dto = offerService.createOffer(offer);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<SeasonalOfferDTO> updateOffer(@PathVariable Long id,
                                                        @RequestBody SeasonalOffer offerDetails) {
        SeasonalOfferDTO dto = offerService.updateOffer(id, offerDetails);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<Void> deactivateOffer(@PathVariable Long id) {
        offerService.deactivateOffer(id);
        return ResponseEntity.noContent().build();
    }
}