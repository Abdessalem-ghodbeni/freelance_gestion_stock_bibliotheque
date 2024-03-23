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
    private int id;

    @Column(name = "quantité")
    private BigDecimal quantite;
    @Column(name = "datemvt")
    private Instant dateMvt;

    @ManyToOne

    private Article article;

    @Column(name = "typemvt")
    @Enumerated(EnumType.STRING)
    private TypeMvtStk typeMvt;

    @Column(name = "sourcemvt")
    @Enumerated(EnumType.STRING)
    private SourceMvtStk sourceMvt;
    // Builder pattern
    public static MvtStkBuilder builder() {
        return new MvtStkBuilder();
    }

    public static class MvtStkBuilder {
        private int id;
        private BigDecimal quantite;
        private Instant dateMvt;
        private Article article;
        private TypeMvtStk typeMvt;
        private SourceMvtStk sourceMvt;

        public MvtStkBuilder id(int id) {
            this.id = id;
            return this;
        }

        public MvtStkBuilder quantite(BigDecimal quantite) {
            this.quantite = quantite;
            return this;
        }

        public MvtStkBuilder dateMvt(Instant dateMvt) {
            this.dateMvt = dateMvt;
            return this;
        }

        public MvtStkBuilder article(Article article) {
            this.article = article;
            return this;
        }

        public MvtStkBuilder typeMvt(TypeMvtStk typeMvt) {
            this.typeMvt = typeMvt;
            return this;
        }

        public MvtStkBuilder sourceMvt(SourceMvtStk sourceMvt) {
            this.sourceMvt = sourceMvt;
            return this;
        }

        public MvtStk build() {
            MvtStk mvtStk = new MvtStk();
            mvtStk.setId(this.id);
            mvtStk.setQuantite(this.quantite);
            mvtStk.setDateMvt(this.dateMvt);
            mvtStk.setArticle(this.article);
            mvtStk.setTypeMvt(this.typeMvt);
            mvtStk.setSourceMvt(this.sourceMvt);
            return mvtStk;
        }
    }
}
