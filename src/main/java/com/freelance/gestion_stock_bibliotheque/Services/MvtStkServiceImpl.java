package com.freelance.gestion_stock_bibliotheque.Services;

import com.freelance.gestion_stock_bibliotheque.Entities.MvtStk;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.MvtStkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class MvtStkServiceImpl implements MvtStkService {
    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        return null;
    }

    @Override
    public List<MvtStk> mvtStkArticle(Integer idArticle) {
        return null;
    }

    @Override
    public MvtStk entreeStock(MvtStk dto) {
        return null;
    }

    @Override
    public MvtStk sortieStock(MvtStk dto) {
        return null;
    }

    @Override
    public MvtStk correctionStockPos(MvtStk dto) {
        return null;
    }

    @Override
    public MvtStk correctionStockNeg(MvtStk dto) {
        return null;
    }
}
