package com.freelance.gestion_stock_bibliotheque.Services;

import com.freelance.gestion_stock_bibliotheque.Entities.Article;

import java.util.List;

public interface IArticleService {
//    Article article(Article article);
List<Article> addArticles(List<Article> articles);
    List<Article> retrieveAllArticles();
    Article retrieveArticle(Long id);
}
