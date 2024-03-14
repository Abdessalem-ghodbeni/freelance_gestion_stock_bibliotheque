package com.freelance.gestion_stock_bibliotheque.Services;

import com.freelance.gestion_stock_bibliotheque.Entities.*;
import com.freelance.gestion_stock_bibliotheque.Repository.IAdminRepository;
import com.freelance.gestion_stock_bibliotheque.Repository.IEmployeRepository;
import com.freelance.gestion_stock_bibliotheque.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class IAuthenticationServicesImpl implements IAuthenticationServices{
    private final IUserRepository userRepository;
    private final IEmployeRepository employeRepository;
    private final IAdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJWTServices jwtServices;
    @Override
    public Employe registerEmploye(Employe employe) {
       employe.setPassword(passwordEncoder.encode(employe.getPassword()));
       employe.setRole(Role.EMPLOYE);
       return employeRepository.save(employe);
    }

    @Override
    public AuthenticationResponse login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        var jwt = jwtServices.generateToken(user);
        var refreshToken = jwtServices.generateRefreshToken(new HashMap<>(), user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setAccessToken(jwt);
        authenticationResponse.setRefreshToken(refreshToken);

        if (user.getRole() == Role.EMPLOYE) {
            Employe bibliothecaire = (Employe) user;
            Employe employeDto = convertToEmployetDto(bibliothecaire);
            authenticationResponse.setUserDetails(employeDto);
        } else {
            User userDetails = convertToUserDto(user);
            authenticationResponse.setUserDetails(userDetails);
        }

        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken) {
        String userEmail = jwtServices.extractUsername(refreshToken.getRefreshToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        if(jwtServices.isTokenValid(refreshToken.getRefreshToken(), user)) {
            var jwt = jwtServices.generateToken(user);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();

            authenticationResponse.setAccessToken(jwt);
            authenticationResponse.setRefreshToken(refreshToken.getRefreshToken());
            return authenticationResponse;
        }
        return null;
    }

    @Override
    public Admin registerAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    private User convertToUserDto(User user) {
        User dto = new User();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }
    private Employe convertToEmployetDto(Employe bibliothecaire) {
        Employe dto = new Employe();
        dto.setId(bibliothecaire.getId());
        dto.setNom(bibliothecaire.getNom());
        dto.setPrenom(bibliothecaire.getPrenom());
        dto.setEmail(bibliothecaire.getEmail());
        dto.setPassword(bibliothecaire.getPassword());
        dto.setRole(bibliothecaire.getRole());

        return dto;
    }
}
