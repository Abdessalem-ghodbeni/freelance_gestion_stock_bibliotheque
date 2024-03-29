package com.freelance.gestion_stock_bibliotheque.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="Article")
public class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArticle")
    private int id;
    @Column(name = "codearticle")
    private String codeArticle;
    @Column(name = "nomArticle")
    private String nomArticle;
    @Column(name = "description")
    private String description;

    @Column(name = "codeQr")
    private String codeQr;
//    @Column(name = "prix")
//    private float prix;

    @Column(name = "prixunitaireht")
    private BigDecimal prixUnitaireHt;
//   pas d inclue de toutes les taxes et impôts applicables

    @Column(name = "tauxtva")
    private BigDecimal tauxTva;

    @Column(name = "remise")
    private BigDecimal remise;

    @Column(name = "prixunitairettc")
    private BigDecimal prixUnitaireTtc;
//    incluant toutes les taxes et impôts applicables

//@OneToOne (mappedBy = "article",cascade = CascadeType.ALL)
//    private Stock stock;

    @OneToMany(mappedBy = "article")
    private List<LigneVente> ligneVentes;
@JsonIgnore
    @OneToMany(mappedBy = "article")
    private List<LigneCommandeClient> ligneCommandeClients;

    @OneToOne(mappedBy = "article", cascade = CascadeType.ALL)
    private Stock stock;
    @JsonIgnore
    @OneToMany(mappedBy = "article")
    private List<MvtStk> mvtStks;

}
