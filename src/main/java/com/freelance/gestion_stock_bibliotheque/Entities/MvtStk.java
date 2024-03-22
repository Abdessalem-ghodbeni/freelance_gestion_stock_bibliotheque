package com.freelance.gestion_stock_bibliotheque.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name="Stock")
public class MvtStk implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStock")
    private int idStock;

    @Column(name = "quantité")
    private BigDecimal quantite;
    @Column(name = "datemvt")
    private Instant dateMvt;

    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;

    @Column(name = "typemvt")
    @Enumerated(EnumType.STRING)
    private TypeMvtStk typeMvt;

    @Column(name = "sourcemvt")
    @Enumerated(EnumType.STRING)
    private SourceMvtStk sourceMvt;

}
