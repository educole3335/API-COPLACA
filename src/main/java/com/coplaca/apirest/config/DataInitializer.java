package com.coplaca.apirest.config;

import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.repository.RoleRepository;
import com.coplaca.apirest.repository.UserRepository;
import com.coplaca.apirest.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Stream;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeReferenceData(
            RoleRepository roleRepository,
            WarehouseRepository warehouseRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin.email:admin@coplaca.local}") String adminEmail,
            @Value("${app.bootstrap.admin.password:Admin12345!}") String adminPassword) {
        return args -> {
            Stream.of(
                    new String[]{"ROLE_CUSTOMER", "Cliente final"},
                    new String[]{"ROLE_LOGISTICS", "Personal de logistica"},
                    new String[]{"ROLE_DELIVERY", "Repartidor"},
                    new String[]{"ROLE_ADMIN", "Administrador"}
            ).forEach(roleData -> roleRepository.findByName(roleData[0]).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleData[0]);
                role.setDescription(roleData[1]);
                return roleRepository.save(role);
            }));

            if (warehouseRepository.findByIsActiveTrue().isEmpty()) {
                warehouseRepository.save(createWarehouse(
                        "Almacen Tenerife",
                        "Poligono Industrial Guimar, Tenerife",
                        28.3172,
                        -16.4133,
                        "922000001",
                        "Encargado Tenerife"
                ));
                warehouseRepository.save(createWarehouse(
                        "Almacen Gran Canaria",
                        "Mercalaspalmas, Gran Canaria",
                        28.0997,
                        -15.4134,
                        "928000002",
                        "Encargado Gran Canaria"
                ));
                warehouseRepository.save(createWarehouse(
                        "Almacen La Palma",
                        "Zona Industrial El Paso, La Palma",
                        28.6500,
                        -17.8830,
                        "922000003",
                        "Encargado La Palma"
                ));
            }

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setFirstName("Admin");
                admin.setLastName("Coplaca");
                admin.setEnabled(true);
                admin.setRoles(Set.of(roleRepository.findByName("ROLE_ADMIN").orElseThrow()));
                Address address = new Address();
                address.setStreet("Sede central Coplaca");
                address.setCity("Santa Cruz de Tenerife");
                address.setPostalCode("38001");
                address.setProvince("Santa Cruz de Tenerife");
                address.setLatitude(28.4636);
                address.setLongitude(-16.2518);
                admin.setAddress(address);
                userRepository.save(admin);
            }
        };
    }

    private Warehouse createWarehouse(String name, String address, double latitude, double longitude,
                                      String phoneNumber, String managerName) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(name);
        warehouse.setAddress(address);
        warehouse.setLatitude(latitude);
        warehouse.setLongitude(longitude);
        warehouse.setCapacity(1000);
        warehouse.setPhoneNumber(phoneNumber);
        warehouse.setManagerName(managerName);
        warehouse.setActive(true);
        return warehouse;
    }
}
