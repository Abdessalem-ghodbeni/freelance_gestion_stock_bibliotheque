package com.freelance.gestion_stock_bibliotheque.Controllers;

import com.freelance.gestion_stock_bibliotheque.Entities.*;
import com.freelance.gestion_stock_bibliotheque.Exception.RessourceNotFound;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.IAuthenticationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationServices authenticationServices;

    @PostMapping(path = "/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        try {
            return new ResponseEntity<>(authenticationServices.registerAdmin(admin), HttpStatus.CREATED);

        } catch (RessourceNotFound ressourceNotFound) {
            return new ResponseEntity<>(ressourceNotFound.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/registerEmploye")
    public ResponseEntity<?> registerEmploye(@RequestBody Employe employe) {
        try {

            return new ResponseEntity<>(authenticationServices.registerEmploye(employe), HttpStatus.CREATED);
        } catch (RessourceNotFound ressourceNotFound) {
            return new ResponseEntity<>(ressourceNotFound.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody User user) {
        return authenticationServices.login(user.getEmail(), user.getPassword());
    }
    @PostMapping("/refreshToken")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        return authenticationServices.refreshToken(refreshToken);
    }

}
