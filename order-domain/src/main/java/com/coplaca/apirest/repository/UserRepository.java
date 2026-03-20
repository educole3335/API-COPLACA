package com.coplaca.apirest.repository;

import com.coplaca.apirest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndEnabledTrue(String email);
    List<User> findByWarehouseIdAndEnabledTrue(Long warehouseId);
    List<User> findByWarehouseIdAndEnabledTrueAndRolesName(Long warehouseId, String roleName);
    List<User> findByEnabledTrue();
}
