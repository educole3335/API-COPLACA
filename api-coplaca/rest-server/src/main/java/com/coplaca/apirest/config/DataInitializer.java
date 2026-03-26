package com.coplaca.apirest.config;

import com.coplaca.apirest.entity.*;
import com.coplaca.apirest.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeReferenceData(
            RoleRepository roleRepository,
            WarehouseRepository warehouseRepository,
            UserRepository userRepository,
            ProductCategoryRepository categoryRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin.email:admin@coplaca.local}") String adminEmail,
            @Value("${app.bootstrap.admin.password:Admin12345!}") String adminPassword) {
        return args -> {
            // Inicializar Roles
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

            // Inicializar / actualizar Almacenes (coordenadas Canarias actualizadas)
            upsertWarehouse(
                    warehouseRepository,
                    "Almacen Tenerife",
                    "Poligono Industrial de Guimar, Tenerife",
                    28.3128,
                    -16.3972,
                    "922000001",
                    "Encargado Tenerife"
            );

            upsertWarehouse(
                    warehouseRepository,
                    "Almacen Gran Canaria",
                    "Mercalaspalmas, Gran Canaria",
                    28.0506,
                    -15.4210,
                    "928000002",
                    "Encargado Gran Canaria"
            );

            upsertWarehouse(
                    warehouseRepository,
                    "Almacen La Palma",
                    "Zona Industrial El Paso, La Palma",
                    28.6516,
                    -17.8799,
                    "922000003",
                    "Encargado La Palma"
            );

            // Inicializar Admin
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setFirstName("Admin");
                admin.setLastName("Coplaca");
                admin.setPhoneNumber("922111111");
                admin.setEnabled(true);
                admin.setRoles(Set.of(roleRepository.findByName("ROLE_ADMIN").orElseThrow()));
                Address adminAddress = new Address();
                adminAddress.setStreet("Sede central Coplaca");
                adminAddress.setCity("Santa Cruz de Tenerife");
                adminAddress.setPostalCode("38001");
                adminAddress.setProvince("Santa Cruz de Tenerife");
                adminAddress.setLatitude(28.4636);
                adminAddress.setLongitude(-16.2518);
                admin.setAddress(adminAddress);
                userRepository.save(admin);
            }

            // Inicializar Categorías de Productos
            if (categoryRepository.count() == 0) {
                categoryRepository.save(createProductCategory(
                        "Frutas Tropicales",
                        "Frutas tropicales frescas de Coplaca",
                        "🍌",
                        "#FFD700"
                ));
                categoryRepository.save(createProductCategory(
                        "Frutas Subtropicales",
                        "Frutas subtropicales de calidad premium",
                        "🥑",
                        "#7FBF7F"
                ));
                categoryRepository.save(createProductCategory(
                        "Otras Frutas Frescas",
                        "Frutas frescas variadas",
                        "🍉",
                        "#FF6B6B"
                ));
                categoryRepository.save(createProductCategory(
                        "Ortalizas",
                        "Hortalizas y vegetales frescos",
                        "🥬",
                        "#8FBC8F"
                ));
            }

            // Inicializar Productos
                        Map<String, String> productImages = buildProductImageCatalog();
            if (productRepository.count() == 0) {
                ProductCategory frutasTropicales = categoryRepository.findByName("Frutas Tropicales").orElseThrow();
                ProductCategory frutasSubtropicales = categoryRepository.findByName("Frutas Subtropicales").orElseThrow();
                ProductCategory otrosFrutas = categoryRepository.findByName("Otras Frutas Frescas").orElseThrow();
                ProductCategory ortalizas = categoryRepository.findByName("Ortalizas").orElseThrow();

                // FRUTAS TROPICALES
                productRepository.save(createProduct(
                        "Plátano de Canarias (IGP)",
                        "Plátano de Canarias con Indicación Geográfica Protegida. Producto estrella de Coplaca. Cultivado bajo los más altos estándares de calidad.",
                        "kg",
                        new BigDecimal("2.50"),
                        new BigDecimal("3.00"),
                        new BigDecimal("500"),
                        frutasTropicales,
                        productImages.get("Plátano de Canarias (IGP)")
                ));
                productRepository.save(createProduct(
                        "Mango",
                        "Mango fresco y tropical de excelente calidad",
                        "unidad",
                        new BigDecimal("3.80"),
                        new BigDecimal("4.50"),
                        new BigDecimal("200"),
                        frutasTropicales,
                        productImages.get("Mango")
                ));
                productRepository.save(createProduct(
                        "Papaya",
                        "Papaya tropical fresca jugosa",
                        "unidad",
                        new BigDecimal("2.90"),
                        new BigDecimal("3.50"),
                        new BigDecimal("150"),
                        frutasTropicales,
                        productImages.get("Papaya")
                ));
                productRepository.save(createProduct(
                        "Piña",
                        "Piña tropical dulce y fresca",
                        "unidad",
                        new BigDecimal("3.50"),
                        new BigDecimal("4.20"),
                        new BigDecimal("180"),
                        frutasTropicales,
                        productImages.get("Piña")
                ));

                // FRUTAS SUBTROPICALES
                productRepository.save(createProduct(
                        "Aguacate",
                        "Aguacate fresco de calidad premium",
                        "unidad",
                        new BigDecimal("2.20"),
                        new BigDecimal("2.80"),
                        new BigDecimal("250"),
                        frutasSubtropicales,
                        productImages.get("Aguacate")
                ));

                // OTRAS FRUTAS FRESCAS
                productRepository.save(createProduct(
                        "Sandía",
                        "Sandía fresca y jugosa",
                        "unidad",
                        new BigDecimal("6.50"),
                        new BigDecimal("8.00"),
                        new BigDecimal("100"),
                        otrosFrutas,
                        productImages.get("Sandía")
                ));
                productRepository.save(createProduct(
                        "Manzana",
                        "Manzana fresca de buena calidad",
                        "kg",
                        new BigDecimal("2.80"),
                        new BigDecimal("3.50"),
                        new BigDecimal("300"),
                        otrosFrutas,
                        productImages.get("Manzana")
                ));
                productRepository.save(createProduct(
                        "Naranja",
                        "Naranja fresca jugosa",
                        "kg",
                        new BigDecimal("1.90"),
                        new BigDecimal("2.50"),
                        new BigDecimal("400"),
                        otrosFrutas,
                        productImages.get("Naranja")
                ));
                productRepository.save(createProduct(
                        "Fresa",
                        "Fresa fresca y aromática",
                        "caja",
                        new BigDecimal("3.50"),
                        new BigDecimal("4.50"),
                        new BigDecimal("280"),
                        otrosFrutas,
                        productImages.get("Fresa")
                ));
                productRepository.save(createProduct(
                        "Kiwi",
                        "Kiwi fresco con sabor agradable",
                        "kg",
                        new BigDecimal("4.20"),
                        new BigDecimal("5.00"),
                        new BigDecimal("220"),
                        otrosFrutas,
                        productImages.get("Kiwi")
                ));

                // ORTALIZAS
                productRepository.save(createProduct(
                        "Tomate Local",
                        "Tomate fresco cultivado localmente",
                        "kg",
                        new BigDecimal("2.50"),
                        new BigDecimal("3.20"),
                        new BigDecimal("350"),
                        ortalizas,
                        productImages.get("Tomate Local")
                ));
                productRepository.save(createProduct(
                        "Lechuga Fresca",
                        "Lechuga crujiente y fresca",
                        "unidad",
                        new BigDecimal("1.50"),
                        new BigDecimal("2.00"),
                        new BigDecimal("400"),
                        ortalizas,
                        productImages.get("Lechuga Fresca")
                ));
            }

            syncProductImages(productRepository, productImages);

            // Inicializar Clientes
            if (userRepository.findByEmail("cliente@example.com").isEmpty()) {
                Warehouse warehouse = warehouseRepository.findByIsActiveTrue().stream().findFirst().orElseThrow();
                
                User cliente = new User();
                cliente.setEmail("cliente@example.com");
                cliente.setPassword(passwordEncoder.encode("Cliente123!"));
                cliente.setFirstName("Juan");
                cliente.setLastName("García");
                cliente.setPhoneNumber("659123456");
                cliente.setEnabled(true);
                cliente.setWarehouse(warehouse);
                cliente.setRoles(Set.of(roleRepository.findByName("ROLE_CUSTOMER").orElseThrow()));
                
                Address clienteAddress = new Address();
                clienteAddress.setStreet("Calle Principal");
                clienteAddress.setStreetNumber("42");
                clienteAddress.setCity("Santa Cruz de Tenerife");
                clienteAddress.setPostalCode("38003");
                clienteAddress.setProvince("Santa Cruz de Tenerife");
                clienteAddress.setLatitude(28.4635);
                clienteAddress.setLongitude(-16.2520);
                clienteAddress.setAdditionalInfo("Apto 3B");
                cliente.setAddress(clienteAddress);
                userRepository.save(cliente);
                
                // Segundo cliente
                User cliente2 = new User();
                cliente2.setEmail("maria@example.com");
                cliente2.setPassword(passwordEncoder.encode("Maria123!"));
                cliente2.setFirstName("María");
                cliente2.setLastName("López");
                cliente2.setPhoneNumber("659654321");
                cliente2.setEnabled(true);
                cliente2.setWarehouse(warehouse);
                cliente2.setRoles(Set.of(roleRepository.findByName("ROLE_CUSTOMER").orElseThrow()));
                
                Address cliente2Address = new Address();
                cliente2Address.setStreet("Avenida de la Paz");
                cliente2Address.setStreetNumber("78");
                cliente2Address.setCity("La Laguna");
                cliente2Address.setPostalCode("38200");
                cliente2Address.setProvince("Santa Cruz de Tenerife");
                cliente2Address.setLatitude(28.4891);
                cliente2Address.setLongitude(-16.3183);
                cliente2Address.setAdditionalInfo("Planta baja");
                cliente2.setAddress(cliente2Address);
                userRepository.save(cliente2);
            }

            // Inicializar Repartidores
            if (userRepository.findByEmail("repartidor@example.com").isEmpty()) {
                Warehouse warehouse = warehouseRepository.findByIsActiveTrue().stream().findFirst().orElseThrow();
                
                User repartidor = new User();
                repartidor.setEmail("repartidor@example.com");
                repartidor.setPassword(passwordEncoder.encode("Repartidor123!"));
                repartidor.setFirstName("Carlos");
                repartidor.setLastName("Martínez");
                repartidor.setPhoneNumber("695987654");
                repartidor.setEnabled(true);
                repartidor.setWarehouse(warehouse);
                repartidor.setDeliveryStatus(DeliveryAgentStatus.AT_WAREHOUSE);
                repartidor.setRoles(Set.of(roleRepository.findByName("ROLE_DELIVERY").orElseThrow()));
                
                Address repartidorAddress = new Address();
                repartidorAddress.setStreet("Calle del Comercio");
                repartidorAddress.setStreetNumber("15");
                repartidorAddress.setCity("Santa Cruz de Tenerife");
                repartidorAddress.setPostalCode("38001");
                repartidorAddress.setProvince("Santa Cruz de Tenerife");
                repartidorAddress.setLatitude(28.4636);
                repartidorAddress.setLongitude(-16.2518);
                repartidor.setAddress(repartidorAddress);
                userRepository.save(repartidor);
                
                // Segundo repartidor
                User repartidor2 = new User();
                repartidor2.setEmail("ana@example.com");
                repartidor2.setPassword(passwordEncoder.encode("Ana123!"));
                repartidor2.setFirstName("Ana");
                repartidor2.setLastName("Rodríguez");
                repartidor2.setPhoneNumber("695112233");
                repartidor2.setEnabled(true);
                repartidor2.setWarehouse(warehouse);
                repartidor2.setDeliveryStatus(DeliveryAgentStatus.AT_WAREHOUSE);
                repartidor2.setRoles(Set.of(roleRepository.findByName("ROLE_DELIVERY").orElseThrow()));
                
                Address repartidor2Address = new Address();
                repartidor2Address.setStreet("Avenida Trinitaria");
                repartidor2Address.setStreetNumber("33");
                repartidor2Address.setCity("Santa Cruz de Tenerife");
                repartidor2Address.setPostalCode("38002");
                repartidor2Address.setProvince("Santa Cruz de Tenerife");
                repartidor2Address.setLatitude(28.465);
                repartidor2Address.setLongitude(-16.260);
                repartidor2.setAddress(repartidor2Address);
                userRepository.save(repartidor2);
            }

            // Inicializar Personal de Logística
            if (userRepository.findByEmail("logistica@example.com").isEmpty()) {
                Warehouse warehouse = warehouseRepository.findByIsActiveTrue().stream().findFirst().orElseThrow();
                
                User logistica = new User();
                logistica.setEmail("logistica@example.com");
                logistica.setPassword(passwordEncoder.encode("Logistica123!"));
                logistica.setFirstName("Pedro");
                logistica.setLastName("Fernández");
                logistica.setPhoneNumber("695345678");
                logistica.setEnabled(true);
                logistica.setWarehouse(warehouse);
                logistica.setRoles(Set.of(roleRepository.findByName("ROLE_LOGISTICS").orElseThrow()));
                
                Address logisticaAddress = new Address();
                logisticaAddress.setStreet("Polígono Industrial");
                logisticaAddress.setStreetNumber("1");
                logisticaAddress.setCity("Santa Cruz de Tenerife");
                logisticaAddress.setPostalCode("38001");
                logisticaAddress.setProvince("Santa Cruz de Tenerife");
                logisticaAddress.setLatitude(28.4636);
                logisticaAddress.setLongitude(-16.2518);
                logistica.setAddress(logisticaAddress);
                userRepository.save(logistica);
                
                // Segundo personal de logística
                User logistica2 = new User();
                logistica2.setEmail("alejandro@example.com");
                logistica2.setPassword(passwordEncoder.encode("Alejandro123!"));
                logistica2.setFirstName("Alejandro");
                logistica2.setLastName("Sánchez");
                logistica2.setPhoneNumber("695567890");
                logistica2.setEnabled(true);
                logistica2.setWarehouse(warehouse);
                logistica2.setRoles(Set.of(roleRepository.findByName("ROLE_LOGISTICS").orElseThrow()));
                
                Address logistica2Address = new Address();
                logistica2Address.setStreet("Calle del Almacén");
                logistica2Address.setStreetNumber("50");
                logistica2Address.setCity("Santa Cruz de Tenerife");
                logistica2Address.setPostalCode("38003");
                logistica2Address.setProvince("Santa Cruz de Tenerife");
                logistica2Address.setLatitude(28.4620);
                logistica2Address.setLongitude(-16.2530);
                logistica2.setAddress(logistica2Address);
                userRepository.save(logistica2);
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

        private void upsertWarehouse(
                        WarehouseRepository warehouseRepository,
                        String name,
                        String address,
                        double latitude,
                        double longitude,
                        String phoneNumber,
                        String managerName
        ) {
                Warehouse warehouse = warehouseRepository.findByName(name)
                                .orElseGet(() -> createWarehouse(name, address, latitude, longitude, phoneNumber, managerName));

                warehouse.setAddress(address);
                warehouse.setLatitude(latitude);
                warehouse.setLongitude(longitude);
                warehouse.setPhoneNumber(phoneNumber);
                warehouse.setManagerName(managerName);
                warehouse.setActive(true);

                warehouseRepository.save(warehouse);
        }

    private ProductCategory createProductCategory(String name, String description, String icon, String color) {
        ProductCategory category = new ProductCategory();
        category.setName(name);
        category.setDescription(description);
        category.setIcon(icon);
        category.setColor(color);
        category.setActive(true);
        return category;
    }

        private Product createProduct(String name, String description, String unit, BigDecimal unitPrice,
                                                                  BigDecimal originalPrice, BigDecimal stockQuantity, ProductCategory category,
                                                                  String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setUnit(unit);
        product.setUnitPrice(unitPrice);
        product.setOriginalPrice(originalPrice);
        product.setStockQuantity(stockQuantity);
                product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setActive(true);
        return product;
    }

        private Map<String, String> buildProductImageCatalog() {
                return Map.ofEntries(
                        Map.entry("Tomate Local", "https://loremflickr.com/800/600/tomato"),
                        Map.entry("Lechuga Fresca", "https://loremflickr.com/800/600/lettuce")
                );
        }

        private Set<String> buildFruitProductNames() {
                return Set.of(
                        "Plátano de Canarias (IGP)",
                        "Mango",
                        "Papaya",
                        "Piña",
                        "Aguacate",
                        "Sandía",
                        "Manzana",
                        "Naranja",
                        "Fresa",
                        "Kiwi"
                );
        }

        private void syncProductImages(ProductRepository productRepository, Map<String, String> productImages) {
                Set<String> fruitProducts = buildFruitProductNames();
                productRepository.findAll().forEach(product -> {
                        if (fruitProducts.contains(product.getName())) {
                                if (product.getImageUrl() != null && !product.getImageUrl().isBlank()) {
                                        product.setImageUrl(null);
                                        productRepository.save(product);
                                }
                                return;
                        }

                        String imageUrl = productImages.get(product.getName());
                        if (imageUrl != null && (product.getImageUrl() == null || product.getImageUrl().isBlank())) {
                                product.setImageUrl(imageUrl);
                                productRepository.save(product);
                        }
                });
        }
}
