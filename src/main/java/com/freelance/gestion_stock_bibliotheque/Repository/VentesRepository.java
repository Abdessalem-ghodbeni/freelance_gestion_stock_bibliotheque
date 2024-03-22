package com.freelance.gestion_stock_bibliotheque.Repository;

import com.freelance.gestion_stock_bibliotheque.Entities.Ventes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VentesRepository extends JpaRepository<Ventes, Integer> {

    Optional<Ventes> findVentesByCode(String code);
}
