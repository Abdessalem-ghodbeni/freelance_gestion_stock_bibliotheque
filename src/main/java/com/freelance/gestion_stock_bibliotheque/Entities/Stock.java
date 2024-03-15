package com.freelance.gestion_stock_bibliotheque.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="Stock")
public class Stock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStock")
    private long idStock;

    @Column(name = "quantité")
    private int quantite;
 @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    private Article article;

}
