package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.Article;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Services.IArticleService;
import com.freelance.gestion_stock_bibliotheque.Services.IArticleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleServiceImpl articleService;

    @PostMapping(path = "/add")
    public ResponseEntity<?>addArticle(@RequestBody List<Article> articles){
try{
    List<Article> articleListes = articleService.addArticles(articles);
    return new ResponseEntity<>(articleListes, HttpStatus.CREATED);
}catch (RessourceNotFound exception){
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
}
    }

    @GetMapping(path = "/all/articles")
    public ResponseEntity<?>getAllArticles(){
        try{
            List<Article>articles=articleService.retrieveAllArticles();
           return new  ResponseEntity<>(articles,HttpStatus.OK);
        }
        catch (RessourceNotFound exeption){
            return new ResponseEntity<>(exeption.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/recupere_article/{id}")
    public ResponseEntity<?> RecupererArticle(@PathVariable("id") long id) {
        try{
            Article article=articleService.retrieveArticle(id);
            if(article==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Accun article avec l'id "+id);
            }
            return ResponseEntity.ok(article);
        }
        catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
    @DeleteMapping(path = "/supprimer/article/{id}")
    public ResponseEntity<String> Supprimerarticle(@PathVariable("id") long idArticle) {
        try {
            articleService.supprimerArticle(idArticle);
            return ResponseEntity.ok("Article deleted Successfuly");
        } catch (RessourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }


    }

}
