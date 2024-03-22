package com.freelance.gestion_stock_bibliotheque.Repository;

import com.freelance.gestion_stock_bibliotheque.Entities.MvtStk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStockRepository extends JpaRepository<MvtStk,Long> {
}
