package com.coplaca.apirest.config;

import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.repository.ProductCategoryRepository;
import com.coplaca.apirest.repository.ProductRepository;
import com.coplaca.apirest.repository.RoleRepository;
import com.coplaca.apirest.repository.UserRepository;
import com.coplaca.apirest.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductCategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void initializeReferenceDataCreatesRolesWhenMissing() throws Exception {
        DataInitializer initializer = new DataInitializer();

        when(roleRepository.findByName(any())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(warehouseRepository.findByIsActiveTrue()).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail(any())).thenAnswer(invocation -> {
            String email = invocation.getArgument(0);
            if ("admin@coplaca.local".equals(email)) {
                return Optional.empty();
            }
            return Optional.of(new com.coplaca.apirest.entity.User());
        });
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole()));
        when(passwordEncoder.encode("Admin12345!")).thenReturn("encoded");
        when(categoryRepository.count()).thenReturn(1L);
        when(productRepository.count()).thenReturn(1L);

        CommandLineRunner runner = initializer.initializeReferenceData(
                roleRepository,
                warehouseRepository,
                userRepository,
            categoryRepository,
            productRepository,
                passwordEncoder,
                "admin@coplaca.local",
                "Admin12345!");

        runner.run();

        verify(roleRepository, atLeast(3)).save(any(Role.class));
        verify(warehouseRepository, atLeast(3)).save(any());
        verify(userRepository).save(any());

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository, atLeast(1)).save(roleCaptor.capture());
        assertEquals(true, roleCaptor.getAllValues().stream().anyMatch(r -> "ROLE_CUSTOMER".equals(r.getName())));
    }

    private Role adminRole() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return role;
    }
}
