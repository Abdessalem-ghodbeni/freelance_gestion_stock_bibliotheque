package com.freelance.gestion_stock_bibliotheque.Repository;

import com.freelance.gestion_stock_bibliotheque.Entities.MvtStk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStkRepository extends JpaRepository<MvtStk, Integer> {

    @Query("select sum(m.quantite) from MvtStk m where m.article.idArticle = :idArticle")
    BigDecimal stockReelArticle(@Param("idArticle") Integer idArticle);

    List<MvtStk> findAllByArticleId(Integer idArticle);
}
