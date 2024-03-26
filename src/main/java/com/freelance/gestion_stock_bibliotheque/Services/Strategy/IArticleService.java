package com.freelance.gestion_stock_bibliotheque.Services.Strategy;

import com.freelance.gestion_stock_bibliotheque.Entities.Article;
import com.freelance.gestion_stock_bibliotheque.Entities.LigneCommandeClient;
import com.freelance.gestion_stock_bibliotheque.Entities.LigneVente;

import java.util.List;

public interface IArticleService {

  public  Article save(Article dto);

    Article findById(Integer id);

    Article findByCodeArticle(String codeArticle);

    List<Article> findAll();

    List<LigneVente> findHistoriqueVentes(Integer idArticle);

    List<LigneCommandeClient> findHistoriaueCommandeClient(Integer idArticle);

//    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle);

Article UpdateArticle(Article updatedArticle);

    void delete(Integer id);
}
