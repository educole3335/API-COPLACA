package com.coplaca.apirest.service;

import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }
}