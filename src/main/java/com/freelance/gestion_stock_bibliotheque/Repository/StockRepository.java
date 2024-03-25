package com.freelance.gestion_stock_bibliotheque.Repository;

import com.freelance.gestion_stock_bibliotheque.Entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,Integer> {
}
