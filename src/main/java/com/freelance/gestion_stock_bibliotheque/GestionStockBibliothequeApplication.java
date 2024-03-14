package com.freelance.gestion_stock_bibliotheque;

import com.freelance.gestion_stock_bibliotheque.Entities.Admin;
import com.freelance.gestion_stock_bibliotheque.Entities.Role;
import com.freelance.gestion_stock_bibliotheque.Entities.User;
import com.freelance.gestion_stock_bibliotheque.Repository.IAdminRepository;
import com.freelance.gestion_stock_bibliotheque.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.freelance.gestion_stock_bibliotheque","com.freelance.gestion_stock_bibliotheque.CorsConfiguration"})
public class GestionStockBibliothequeApplication implements CommandLineRunner {
	private final IUserRepository userRepository;
	private final IAdminRepository adminRepository;
	public static void main(String[] args) {
		SpringApplication.run(GestionStockBibliothequeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if (adminAccount == null) {
			Admin admin = new Admin();
			admin.setEmail("admin@gmail.com");
			admin.setNom("admin");
			admin.setPrenom("admin");
			admin.setRole(Role.ADMIN);
			admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
			adminRepository.save(admin);
		}
	}
}
