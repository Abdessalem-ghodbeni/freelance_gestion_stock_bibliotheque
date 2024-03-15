package com.freelance.gestion_stock_bibliotheque.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="Article")
public class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArticle")
    private long idArticle;

    @Column(name = "nomArticle")
    private String nomArticle;
    @Column(name = "description")
    private String description;
    @Column(name = "categorie")
    private String categorie;
    @Column(name = "codeQr")
    private String codeQr;
    @Column(name = "prix")
    private float prix;

@OneToOne (mappedBy = "article",cascade = CascadeType.ALL)
    private Stock stock;

}
