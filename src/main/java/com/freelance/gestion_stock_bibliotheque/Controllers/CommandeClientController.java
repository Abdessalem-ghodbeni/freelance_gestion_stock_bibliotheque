package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.*;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Services.CommandeClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/CommandeClient")
@RequiredArgsConstructor
public class CommandeClientController {
    private final CommandeClientServiceImpl commandeClientService;


    @PostMapping(path = "/add")
    public ResponseEntity<?> ajouterCommandeClient(@RequestBody CommandeClient commandeClientt){
        try{
            CommandeClient newCommande=commandeClientService.save(commandeClientt);
            return new ResponseEntity<>(newCommande, HttpStatus.CREATED);
        }catch (RessourceNotFound exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(path="/update/etat/{idCommande}/{etatCommande}")
    public ResponseEntity<?> updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande){
        try{
            CommandeClient commandeUpdated=commandeClientService.updateEtatCommande(idCommande,etatCommande);
            return ResponseEntity.ok(commandeUpdated);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
//   modifier la quantité du  produit dans une commande client
    @PatchMapping(path="/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    public ResponseEntity<?> updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
                                                    @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite){
        try{
            CommandeClient commandeUpdated=commandeClientService.updateQuantiteCommande(idCommande,idLigneCommande,quantite);
            return ResponseEntity.ok(commandeUpdated);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

//  MODIFIER LE   client associe a une commande specifié
    @PatchMapping(path="/update/client/{idCommande}/{idClient}")
    public ResponseEntity<?> updateClient(@PathVariable("idCommande") Integer idCommande, @PathVariable("idClient") Integer idClient){
        try{
            CommandeClient commandeUpdated=commandeClientService.updateClient(idCommande,idClient);
            return ResponseEntity.ok(commandeUpdated);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
//   update un  article associé a une ligne de commande speecifique dans une commande client ddonné
    @PatchMapping(path="/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    public ResponseEntity<?> updateArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle){
        try{
            CommandeClient commandeUpdated=commandeClientService.updateArticle(idCommande, idLigneCommande, idArticle);
            return ResponseEntity.ok(commandeUpdated);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
    @DeleteMapping(path="/delete/article/{idCommande}/{idLigneCommande}")
    public ResponseEntity<?>deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande){
        try{
            CommandeClient commandeDeleted=commandeClientService.deleteArticle(idCommande, idLigneCommande);
            return ResponseEntity.ok(commandeDeleted);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());

        }
    }


    @GetMapping(path="/{idCommandeClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Integer idCommandeClient){
        try{
            CommandeClient commandeGetted=commandeClientService.findById(idCommandeClient);
            return ResponseEntity.ok(commandeGetted);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }



    @GetMapping(path="/filter/{codeCommandeClient}")
    public ResponseEntity<?>findByCode(@PathVariable("codeCommandeClient") String code){
        try{
            CommandeClient commandeGetted=commandeClientService.findByCode(code);
            return ResponseEntity.ok(commandeGetted);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @GetMapping( path="/all")
    public ResponseEntity<?> findAll(){
        try{
            List<CommandeClient> commandeListeGetted=commandeClientService.findAll();
            return ResponseEntity.ok(commandeListeGetted);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
    @GetMapping(path="/lignesCommande/{idCommande}")
    public ResponseEntity<?> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande){
        try{
            List<LigneCommandeClient> liste=commandeClientService.findAllLignesCommandesClientByCommandeClientId(idCommande);
            return ResponseEntity.ok(liste);
        }catch (RessourceNotFound exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

@DeleteMapping(path = "/{idCommandeClient}")
    public void delete(@PathVariable("idCommandeClient") Integer id){
         commandeClientService.delete(id);
    }


}
