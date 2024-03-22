package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.Article;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
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

}
