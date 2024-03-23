package com.freelance.gestion_stock_bibliotheque.Services.Strategy;

import com.freelance.gestion_stock_bibliotheque.Entities.Client;

import java.util.List;

public interface ClientService {

    Client save(Client dto);

    Client findById(Integer id);

    List<Client> findAll();

    void delete(Integer id);
}
