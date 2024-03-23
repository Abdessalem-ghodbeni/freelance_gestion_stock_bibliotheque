package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.Client;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Services.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin("*")
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientContoller {
   private  final ClientServiceImpl clientService;

    @PostMapping(path = "/add")
    public ResponseEntity<?> ajouterClient(@RequestBody Client client){
        try{
            Client newclient=clientService.save(client);
            return new ResponseEntity<>(newclient, HttpStatus.CREATED);
        }catch (RessourceNotFound exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
