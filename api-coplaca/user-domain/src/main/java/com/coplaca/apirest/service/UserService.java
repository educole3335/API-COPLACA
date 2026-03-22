package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.AddressDTO;
import com.coplaca.apirest.dto.SignUpRequest;
import com.coplaca.apirest.dto.UpdateUserRequest;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.DeliveryAgentStatus;
import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final RoleService roleService;
    private final WarehouseService warehouseService;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, 
                       AddressService addressService,
                       RoleService roleService,
                       WarehouseService warehouseService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.roleService = roleService;
        this.warehouseService = warehouseService;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public com.coplaca.apirest.entity.Warehouse resolveCustomerWarehouse(Address address) {
        return warehouseService.assignWarehouse(address);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAndEnabledTrue(email);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmailAndEnabledTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
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
    
    public UserDTO changeRoles(Long userId, Set<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setRoles(resolveRoles(roleNames));
        return convertToDTO(userRepository.save(user));
    }

    public User createManagedUser(SignUpRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A user with that email already exists");
        }

        String normalizedRole = normalizeRoleName(request.getRole());
        if (!Set.of("ROLE_LOGISTICS", "ROLE_DELIVERY", "ROLE_ADMIN").contains(normalizedRole)) {
            throw new IllegalArgumentException("Managed users can only be logistics, delivery or admin accounts");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRoles(resolveRoles(Set.of(normalizedRole)));

        if (request.getAddress() != null) {
            user.setAddress(toAddressEntity(request.getAddress()));
        }

        if (!"ROLE_ADMIN".equals(normalizedRole)) {
            if (request.getWarehouseId() == null) {
                throw new IllegalArgumentException("Warehouse is required for logistics and delivery users");
            }
            user.setWarehouse(warehouseService.getWarehouseById(request.getWarehouseId()));
        }

        if ("ROLE_DELIVERY".equals(normalizedRole)) {
            user.setDeliveryStatus(DeliveryAgentStatus.AT_WAREHOUSE);
        }

        return userRepository.save(user);
    }
    
    public UserDTO updateUser(Long id, UpdateUserRequest userDetails) {
        Optional<User> user = userRepository.findById(id != null ? id : 0L);
        if (user.isPresent()) {
            User u = user.get();
            if (userDetails.getFirstName() != null) {
                u.setFirstName(userDetails.getFirstName());
            }
            if (userDetails.getLastName() != null) {
                u.setLastName(userDetails.getLastName());
            }
            if (userDetails.getPhoneNumber() != null) {
                u.setPhoneNumber(userDetails.getPhoneNumber());
            }
            if (userDetails.getProfileImage() != null) {
                u.setProfileImage(userDetails.getProfileImage());
            }
            
            if (userDetails.getAddress() != null && u.getAddress() != null) {
                Address address = u.getAddress();
                AddressDTO newAddress = userDetails.getAddress();
                if (newAddress.getStreet() != null) {
                    address.setStreet(newAddress.getStreet());
                }
                if (newAddress.getStreetNumber() != null) {
                    address.setStreetNumber(newAddress.getStreetNumber());
                }
                if (newAddress.getApartment() != null) {
                    address.setApartment(newAddress.getApartment());
                }
                if (newAddress.getCity() != null) {
                    address.setCity(newAddress.getCity());
                }
                if (newAddress.getPostalCode() != null) {
                    address.setPostalCode(newAddress.getPostalCode());
                }
                if (newAddress.getProvince() != null) {
                    address.setProvince(newAddress.getProvince());
                }
                if (newAddress.getLatitude() != null) {
                    address.setLatitude(newAddress.getLatitude());
                }
                if (newAddress.getLongitude() != null) {
                    address.setLongitude(newAddress.getLongitude());
                }
                if (newAddress.getAdditionalInfo() != null) {
                    address.setAdditionalInfo(newAddress.getAdditionalInfo());
                }
                if (newAddress.getIsDefault() != null) {
                    address.setDefault(newAddress.getIsDefault());
                }
                addressService.save(address);
                if (hasRole(u, "ROLE_CUSTOMER")) {
                    u.setWarehouse(warehouseService.assignWarehouse(address));
                }
            } else if (userDetails.getAddress() != null) {
                AddressDTO newAddress = userDetails.getAddress();
                if (newAddress.getStreet() == null || newAddress.getCity() == null
                        || newAddress.getPostalCode() == null || newAddress.getProvince() == null
                        || newAddress.getLatitude() == null || newAddress.getLongitude() == null) {
                    throw new IllegalArgumentException("Address requires street, city, postalCode, province, latitude and longitude");
                }
                Address address = new Address();
                address.setStreet(newAddress.getStreet());
                address.setStreetNumber(newAddress.getStreetNumber());
                address.setApartment(newAddress.getApartment());
                address.setCity(newAddress.getCity());
                address.setPostalCode(newAddress.getPostalCode());
                address.setProvince(newAddress.getProvince());
                address.setLatitude(newAddress.getLatitude());
                address.setLongitude(newAddress.getLongitude());
                address.setAdditionalInfo(newAddress.getAdditionalInfo());
                if (newAddress.getIsDefault() != null) {
                    address.setDefault(newAddress.getIsDefault());
                }
                u.setAddress(addressService.save(address));
                if (hasRole(u, "ROLE_CUSTOMER")) {
                    u.setWarehouse(warehouseService.assignWarehouse(address));
                }
            }
            
            User updated = userRepository.save(u);
            return convertToDTO(updated);
        }
        return null;
    }

    public UserDTO getCurrentUser(String email) {
        return convertToDTO(getUserEntityByEmail(email));
    }

    public UserDTO updateCurrentUser(String email, UpdateUserRequest userDetails) {
        User currentUser = getUserEntityByEmail(email);
        return updateUser(currentUser.getId(), userDetails);
    }
    
    public void disableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void disableCurrentUser(String email) {
        User user = getUserEntityByEmail(email);
        user.setEnabled(false);
        userRepository.save(user);
    }

    public UserDTO updateDeliveryStatus(String email, DeliveryAgentStatus status) {
        User user = getUserEntityByEmail(email);
        if (!hasRole(user, "ROLE_DELIVERY")) {
            throw new IllegalArgumentException("Only delivery users can update their delivery status");
        }
        user.setDeliveryStatus(status);
        return convertToDTO(userRepository.save(user));
    }

    public List<UserDTO> getAvailableDeliveryAgents(Long warehouseId) {
        return userRepository.findByWarehouseIdAndEnabledTrueAndRolesName(warehouseId, "ROLE_DELIVERY").stream()
                .filter(user -> user.getDeliveryStatus() == DeliveryAgentStatus.AT_WAREHOUSE)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Set<Role> resolveRoles(Set<String> roleNames) {
        return roleNames.stream()
                .map(this::normalizeRoleName)
            .map(roleService::getRoleByName)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String normalizeRoleName(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return "ROLE_CUSTOMER";
        }
        return roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName.toUpperCase();
    }

    private Address toAddressEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setStreetNumber(addressDTO.getStreetNumber());
        address.setApartment(addressDTO.getApartment());
        address.setCity(addressDTO.getCity());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setProvince(addressDTO.getProvince());
        if (addressDTO.getLatitude() == null || addressDTO.getLongitude() == null) {
            throw new IllegalArgumentException("Address latitude and longitude are required");
        }
        address.setLatitude(addressDTO.getLatitude());
        address.setLongitude(addressDTO.getLongitude());
        address.setAdditionalInfo(addressDTO.getAdditionalInfo());
        if (addressDTO.getIsDefault() != null) {
            address.setDefault(addressDTO.getIsDefault());
        }
        return address;
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
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
                .deliveryStatus(user.getDeliveryStatus())
                .enabled(user.isEnabled())
                .build();
    }

    public UserDTO reactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setEnabled(true);
        return convertToDTO(userRepository.save(user));
    }
}
