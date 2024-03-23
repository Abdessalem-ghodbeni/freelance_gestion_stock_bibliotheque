package com.freelance.gestion_stock_bibliotheque.Services;

import com.freelance.gestion_stock_bibliotheque.Entities.MvtStk;
import com.freelance.gestion_stock_bibliotheque.Entities.TypeMvtStk;
import com.freelance.gestion_stock_bibliotheque.Exceptions.ErrorCodes;
import com.freelance.gestion_stock_bibliotheque.Exceptions.InvalidEntityException;
import com.freelance.gestion_stock_bibliotheque.Repository.MvtStkRepository;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.MvtStkService;
import com.freelance.gestion_stock_bibliotheque.Validators.MvtStkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MvtStkServiceImpl implements MvtStkService {
   private final MvtStkRepository mvtStkRepository;
   private  final ArticleServiceImpl articleService;

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        if (idArticle == null) {
            log.warn("ID article is NULL");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(idArticle);
        return mvtStkRepository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStk> mvtStkArticle(Integer idArticle) {
        return mvtStkRepository.findAllByArticleId(idArticle).stream().collect(Collectors.toList());
    }

    @Override
    public MvtStk entreeStock(MvtStk dto) {
        return entreePositive(dto, TypeMvtStk.ENTREE);
    }

    @Override
    public MvtStk sortieStock(MvtStk dto) {
        return sortieNegative(dto, TypeMvtStk.SORTIE);
    }

    @Override
    public MvtStk correctionStockPos(MvtStk dto) {
        return entreePositive(dto, TypeMvtStk.CORRECTION_POS);
    }

    @Override
    public MvtStk correctionStockNeg(MvtStk dto) {
        return sortieNegative(dto, TypeMvtStk.CORRECTION_NEG);
    }

    private MvtStk entreePositive(MvtStk dto, TypeMvtStk typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue())
                )
        );
        dto.setTypeMvt(typeMvtStk);
        MvtStk mvtStk=mvtStkRepository.save(dto);
        return mvtStk;
    }



    private MvtStk sortieNegative(MvtStk dto, TypeMvtStk typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
        }
        dto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(dto.getQuantite().doubleValue()) * -1
                )
        );
        dto.setTypeMvt(typeMvtStk);
        MvtStk mvtStk=mvtStkRepository.save(dto);
        return mvtStk;

    }


}
