package com.freelance.gestion_stock_bibliotheque.Services;

import com.freelance.gestion_stock_bibliotheque.Entities.Article;
import com.freelance.gestion_stock_bibliotheque.Entities.Stock;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Repository.IArticleRepository;
import com.freelance.gestion_stock_bibliotheque.Repository.IStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class IArticleServiceImpl implements IArticleService{
    private final IArticleRepository articleRepository;
    private final IStockRepository stockRepository;

    @Transactional
    @Override
    public List<Article> addArticles(List<Article> articles) {
        List<Article> articlesEnregistres = new ArrayList<>();
        for (Article article : articles) {
            Stock stock = article.getStock();
            stock.setArticle(article);
            stockRepository.save(stock);

            Article ar=articleRepository.save(article);
            articlesEnregistres.add(ar);
        }
        return articlesEnregistres;
    }

    @Override
    public List<Article> retrieveAllArticles() {
        List<Article> articles=(List<Article>)  articleRepository.findAll();
        return articles;
    }

    @Override
    public Article retrieveArticle(Long id) {
       Article article=articleRepository.findById(id).orElseThrow(()->new RessourceNotFound("accun article avec l'id :" +id));
       return article;
    }

    @Override
    public void supprimerArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RessourceNotFound("Article introuvable avec id "+id));

        Stock stock = article.getStock();
        if (stock != null) {
            stockRepository.delete(stock);
        }

         articleRepository.delete(article);
    }
    }

