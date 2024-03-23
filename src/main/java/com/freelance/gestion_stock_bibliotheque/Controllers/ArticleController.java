package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.Article;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Services.ArticleServiceImpl;
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
 private final ArticleServiceImpl articleService;

 @PostMapping(path = "/add")
    public ResponseEntity<?>ajouterArticle(@RequestBody Article article){
try{
    Article Newarticle=articleService.save(article);
    return new ResponseEntity<>(article,HttpStatus.CREATED);
}catch (RessourceNotFound exception){
    return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
}
 }
}
