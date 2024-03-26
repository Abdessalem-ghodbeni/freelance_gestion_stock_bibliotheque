package com.freelance.gestion_stock_bibliotheque.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name="Stcok")
public class Stock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStock")
    private int id;

   @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArticle")
   @JsonIgnore
    private Article article;

    @Column(name = "quantite")
    private BigDecimal quantite;

    public Stock() {
        this.quantite = BigDecimal.ZERO;
    }

    public Stock(Article article, BigDecimal quantite) {
        this.article = article;
        this.quantite = quantite;
    }
}
