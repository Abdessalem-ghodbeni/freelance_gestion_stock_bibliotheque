package com.freelance.gestion_stock_bibliotheque.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "client")
@Getter
@Setter


public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClient")
    private int id;
    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;


    @Column(name = "adresse")
    private String adresse;
  @Column(name = "mail")
    private String mail;

    @Column(name = "numTel")
    private String numTel;

    @OneToMany(mappedBy = "client")
    private List<CommandeClient> commandeClients;
}
