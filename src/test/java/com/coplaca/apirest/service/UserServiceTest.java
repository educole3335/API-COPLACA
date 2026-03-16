package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.SignUpRequest;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.DeliveryAgentStatus;
import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.repository.AddressRepository;
import com.coplaca.apirest.repository.RoleRepository;
import com.coplaca.apirest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private WarehouseService warehouseService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createManagedUserRequiresWarehouseForDeliveryUsers() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("delivery@coplaca.com");
        request.setPassword("1234");
        request.setRole("ROLE_DELIVERY");

        Role role = new Role();
        role.setName("ROLE_DELIVERY");

        when(userRepository.findByEmail("delivery@coplaca.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_DELIVERY")).thenReturn(Optional.of(role));

        assertThrows(IllegalArgumentException.class, () -> userService.createManagedUser(request));
    }

    @Test
    void createManagedUserCreatesDeliveryWithWarehouseAndStatus() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("delivery@coplaca.com");
        request.setPassword("1234");
        request.setFirstName("Del");
        request.setLastName("Ivery");
        request.setRole("ROLE_DELIVERY");
        request.setWarehouseId(2L);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(2L);

        Role role = new Role();
        role.setId(3L);
        role.setName("ROLE_DELIVERY");

        when(userRepository.findByEmail("delivery@coplaca.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encoded");
        when(warehouseService.getWarehouseById(2L)).thenReturn(warehouse);
        when(roleRepository.findByName("ROLE_DELIVERY")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.createManagedUser(request);

        assertEquals("encoded", saved.getPassword());
        assertEquals(DeliveryAgentStatus.AT_WAREHOUSE, saved.getDeliveryStatus());
        assertEquals(2L, saved.getWarehouse().getId());
    }

    @Test
    void updateDeliveryStatusFailsForNonDeliveryUsers() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@coplaca.com");
        user.setRoles(Set.of(role("ROLE_CUSTOMER")));

        when(userRepository.findByEmailAndEnabledTrue("user@coplaca.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateDeliveryStatus("user@coplaca.com", DeliveryAgentStatus.DELIVERING));
    }

    @Test
    void getCurrentUserMapsRolesToDto() {
        User user = new User();
        user.setId(9L);
        user.setEmail("admin@coplaca.com");
        user.setFirstName("Admin");
        user.setLastName("Root");
        user.setRoles(Set.of(role("ROLE_ADMIN")));

        when(userRepository.findByEmailAndEnabledTrue("admin@coplaca.com")).thenReturn(Optional.of(user));

        UserDTO dto = userService.getCurrentUser("admin@coplaca.com");

        assertEquals(9L, dto.getId());
        assertEquals(true, dto.getRoles().contains("ADMIN"));
    }

    @Test
    void getUserEntityByIdThrowsWhenMissing() {
        when(userRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserEntityById(50L));
    }

    private Role role(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }
}
