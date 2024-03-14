package com.freelance.gestion_stock_bibliotheque.Services;

import com.freelance.gestion_stock_bibliotheque.Entities.*;

public interface IAuthenticationServices {
    Employe registerEmploye(Employe employe);
    AuthenticationResponse login(String email, String password);
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken);
    Admin registerAdmin(Admin admin);
}
