package com.freelance.gestion_stock_bibliotheque.Services.Strategy;

import com.freelance.gestion_stock_bibliotheque.Entities.MvtStk;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStkService {

  BigDecimal stockReelArticle(Integer idArticle);

List<MvtStk> mvtStkArticle(Integer idArticle);

MvtStk entreeStock(MvtStk dto);

    MvtStk sortieStock(MvtStk dto);

    MvtStk correctionStockPos(MvtStk dto);

    MvtStk correctionStockNeg(MvtStk dto);
}
