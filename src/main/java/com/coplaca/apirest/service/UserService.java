package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.AddressDTO;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.repository.UserRepository;
import com.coplaca.apirest.repository.AddressRepository;
import com.coplaca.apirest.entity.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, 
                       AddressRepository addressRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAndEnabledTrue(email);
    }
    
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public UserDTO changeRoles(Long userId, Set<Role> newRoles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setRoles(newRoles);
        return convertToDTO(userRepository.save(user));
    }
    
    public UserDTO updateUser(Long id, User userDetails) {
        Optional<User> user = userRepository.findById(id != null ? id : 0L);
        if (user.isPresent()) {
            User u = user.get();
            u.setFirstName(userDetails.getFirstName());
            u.setLastName(userDetails.getLastName());
            u.setPhoneNumber(userDetails.getPhoneNumber());
            u.setProfileImage(userDetails.getProfileImage());
            
            if (userDetails.getAddress() != null && u.getAddress() != null) {
                Address address = u.getAddress();
                Address newAddress = userDetails.getAddress();
                address.setStreet(newAddress.getStreet());
                address.setCity(newAddress.getCity());
                address.setPostalCode(newAddress.getPostalCode());
                address.setProvince(newAddress.getProvince());
                address.setLatitude(newAddress.getLatitude());
                address.setLongitude(newAddress.getLongitude());
                addressRepository.save(address);
            }
            
            User updated = userRepository.save(u);
            return convertToDTO(updated);
        }
        return null;
    }
    
    public void disableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setEnabled(false);
        userRepository.save(user);
    }
    
    private UserDTO convertToDTO(User user) {
        AddressDTO addressDTO = null;
        if (user.getAddress() != null) {
            addressDTO = AddressDTO.builder()
                    .id(user.getAddress().getId())
                    .street(user.getAddress().getStreet())
                    .streetNumber(user.getAddress().getStreetNumber())
                    .apartment(user.getAddress().getApartment())
                    .city(user.getAddress().getCity())
                    .postalCode(user.getAddress().getPostalCode())
                    .province(user.getAddress().getProvince())
                    .latitude(user.getAddress().getLatitude())
                    .longitude(user.getAddress().getLongitude())
                    .additionalInfo(user.getAddress().getAdditionalInfo())
                    .isDefault(user.getAddress().isDefault())
                    .build();
        }
        
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .profileImage(user.getProfileImage())
                .address(addressDTO)
                .warehouseId(user.getWarehouse() != null ? user.getWarehouse().getId() : null)
                .warehouseName(user.getWarehouse() != null ? user.getWarehouse().getName() : null)
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
                        .collect(Collectors.toSet()))
                .enabled(user.isEnabled())
                .build();
    }
}
