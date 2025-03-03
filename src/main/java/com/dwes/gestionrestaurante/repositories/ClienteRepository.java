package com.dwes.gestionrestaurante.repositories;

import com.dwes.gestionrestaurante.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<UserEntity, Long> {
}
