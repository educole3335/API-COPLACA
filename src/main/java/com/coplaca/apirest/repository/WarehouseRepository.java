package com.coplaca.apirest.repository;

import com.coplaca.apirest.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByName(String name);
    List<Warehouse> findByIsActiveTrue();
    Warehouse findFirstByOrderByIdAsc();
}
