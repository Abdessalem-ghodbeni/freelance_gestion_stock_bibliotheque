package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.Client;
import com.freelance.gestion_stock_bibliotheque.Entities.CommandeClient;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Services.CommandeClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
