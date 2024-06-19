package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    <T> Optional<T> findAllById(int num);
}
