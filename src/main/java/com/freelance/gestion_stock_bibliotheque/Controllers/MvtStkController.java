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

//    ajouter un mouvement de stock de type "ENTREE"
@PostMapping(path= "/entree")
public ResponseEntity<?> entreeStock(@RequestBody MvtStk dto){
    try{
        MvtStk mvtEntred=mvtStkService.entreeStock(dto);
        return new ResponseEntity<>(mvtEntred,HttpStatus.CREATED);

    }
    catch (RessourceNotFound exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @PostMapping(path= "/sortie")
    public ResponseEntity<?> sortieStock(@RequestBody MvtStk dto){
        try{
            MvtStk mvtSorted=mvtStkService.sortieStock(dto);
            return new ResponseEntity<>(mvtSorted,HttpStatus.CREATED);

        }
        catch (RessourceNotFound exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(path= "/correctionpos")
    public ResponseEntity<?> correctionStockPos(@RequestBody MvtStk dto){
        try{
            MvtStk mvtCoorigee=mvtStkService.correctionStockPos(dto);
            return new ResponseEntity<>(mvtCoorigee,HttpStatus.CREATED);

        }
        catch (RessourceNotFound exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path= "/correctionneg")
    public ResponseEntity<?> correctionStockNeg(@RequestBody MvtStk dto){
        try{
            MvtStk mvtCoorigee=mvtStkService.correctionStockNeg(dto);
            return new ResponseEntity<>(mvtCoorigee,HttpStatus.CREATED);

        }
        catch (RessourceNotFound exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
