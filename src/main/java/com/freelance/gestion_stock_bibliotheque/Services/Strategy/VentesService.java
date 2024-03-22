package com.freelance.gestion_stock_bibliotheque.Services.Strategy;

import com.freelance.gestion_stock_bibliotheque.Entities.Ventes;

import java.util.List;

public interface VentesService {

    Ventes save(Ventes dto);

    Ventes findById(Integer id);

    Ventes findByCode(String code);

    List<Ventes> findAll();

    void delete(Integer id);
}
