package com.freelance.gestion_stock_bibliotheque.Repository;

import com.freelance.gestion_stock_bibliotheque.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
