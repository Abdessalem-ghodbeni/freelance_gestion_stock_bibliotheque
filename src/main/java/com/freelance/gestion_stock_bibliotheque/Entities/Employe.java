package com.freelance.gestion_stock_bibliotheque.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="bibliothecaire")
public class Employe extends User {
    @Column(name = "code")
    private String code;
}
