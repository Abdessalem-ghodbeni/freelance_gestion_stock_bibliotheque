package com.freelance.gestion_stock_bibliotheque.Services.Strategy;

import com.freelance.gestion_stock_bibliotheque.Entities.CommandeClient;
import com.freelance.gestion_stock_bibliotheque.Entities.EtatCommande;
import com.freelance.gestion_stock_bibliotheque.Entities.LigneCommandeClient;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientService {

    CommandeClient save(CommandeClient dto);

    CommandeClient updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

    CommandeClient updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CommandeClient updateClient(Integer idCommande, Integer idClient);

    CommandeClient updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

    // Delete article ==> delete LigneCommandeClient
    CommandeClient deleteArticle(Integer idCommande, Integer idLigneCommande);

    CommandeClient findById(Integer id);

    CommandeClient findByCode(String code);

    List<CommandeClient> findAll();

    List<LigneCommandeClient> findAllLignesCommandesClientByCommandeClientId(Integer idCommande);

    void delete(Integer id);
}
