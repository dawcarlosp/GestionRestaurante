package com.dwes.gestionrestaurante.repositories;

import com.dwes.gestionrestaurante.entities.Reserva;
import jakarta.validation.constraints.Future;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findAllByFechaReserva(LocalDate fechaReserva);
}
