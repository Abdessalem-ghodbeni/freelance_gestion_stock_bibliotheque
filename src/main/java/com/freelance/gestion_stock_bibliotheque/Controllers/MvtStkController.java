package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.MvtStk;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Services.MvtStkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/mvtStcok")
@RequiredArgsConstructor
public class MvtStkController {

    private final MvtStkServiceImpl mvtStkService;

    @GetMapping(path = "/stockreel/{idArticle}")
    public ResponseEntity<?> stockReelArticle(@PathVariable("idArticle") Integer idArticle){
        try{
            BigDecimal reelArticleStock=mvtStkService.stockReelArticle(idArticle);
            return ResponseEntity.ok(reelArticleStock);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

//    filtrer les mouvements de stock (MvtStk) par rapport à un article spécifique, identifié par son ID
    @GetMapping(path="/filter/article/{idArticle}")
    public List<MvtStk> mvtStkArticle(Integer idArticle) {
        return mvtStkService.mvtStkArticle(idArticle);
    }
}
