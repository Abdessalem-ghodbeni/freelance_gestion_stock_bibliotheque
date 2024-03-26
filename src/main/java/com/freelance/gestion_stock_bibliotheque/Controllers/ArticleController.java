package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.Article;
import com.freelance.gestion_stock_bibliotheque.Entities.LigneCommandeClient;
import com.freelance.gestion_stock_bibliotheque.Entities.LigneVente;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Services.ArticleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
 private final ArticleServiceImpl articleService;

 @PostMapping(path = "/add")
    public ResponseEntity<?>ajouterArticle(@RequestBody Article article){
try{
    Article Newarticle=articleService.save(article);
    return new ResponseEntity<>(Newarticle,HttpStatus.CREATED);
}catch (RessourceNotFound exception){
    return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
}
 }

 @PutMapping(path = "/update")
 public ResponseEntity<?> updateARTICLE(@RequestBody Article article){
     try{
         return new ResponseEntity<>(articleService.UpdateArticle(article),HttpStatus.OK);
     }
     catch (RessourceNotFound exception){
         return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
     }
 }
    @GetMapping(path="/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable("idArticle") Integer id){
        try{
            Article articleById=articleService.findById(id);
            return ResponseEntity.ok(articleById);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }


    @GetMapping(path="/filter/{codeArticle}")
    public ResponseEntity<?> findByCodeArticle(@PathVariable("codeArticle") String codeArticle){
        try{
            Article articleByCode=articleService.findByCodeArticle(codeArticle);
            return ResponseEntity.ok(articleByCode);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }



    }

    @GetMapping(path="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(){
        try{
            List<Article> listeArticle=articleService.findAll();
            return ResponseEntity.ok(listeArticle);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }


    @GetMapping(path = "/historique/vente/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findHistoriqueVentes(@PathVariable("idArticle") Integer idArticle){
        try{
            List<LigneVente> liste=articleService.findHistoriqueVentes(idArticle);
            return ResponseEntity.ok(liste);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }



    }

    @GetMapping(path = "/historique/commandeclient/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findHistoriaueCommandeClient(@PathVariable("idArticle") Integer idArticle){
        try{
            List<LigneCommandeClient> liste=articleService.findHistoriaueCommandeClient(idArticle);
            return ResponseEntity.ok(liste);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }



    }
    @DeleteMapping(path ="/delete/{idArticle}")
    void delete(@PathVariable("idArticle") Integer id){
         articleService.delete(id);

    }


}
