package com.freelance.gestion_stock_bibliotheque.Services;

import com.freelance.gestion_stock_bibliotheque.Entities.Article;
import com.freelance.gestion_stock_bibliotheque.Entities.LigneCommandeClient;
import com.freelance.gestion_stock_bibliotheque.Entities.LigneVente;
import com.freelance.gestion_stock_bibliotheque.Exceptions.EntityNotFoundException;
import com.freelance.gestion_stock_bibliotheque.Exceptions.ErrorCodes;
import com.freelance.gestion_stock_bibliotheque.Exceptions.InvalidOperationException;
import com.freelance.gestion_stock_bibliotheque.Repository.IArticleRepository;
import com.freelance.gestion_stock_bibliotheque.Repository.LigneCommandeClientRepository;
import com.freelance.gestion_stock_bibliotheque.Repository.LigneVenteRepository;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.IArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements IArticleService {
   private  final IArticleRepository articleRepository;
    private final LigneVenteRepository venteRepository;
//    private LigneCommandeFournisseurRepository commandeFournisseurRepository;
    private final  LigneCommandeClientRepository commandeClientRepository;

    @Override
    public Article save(Article dto) {
        return  articleRepository.save(dto);
    }

    @Override
    public Article findById(Integer id) {
        if (id == null) {
            log.error("Article ID is null");
            return null;
        }

        return articleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun article avec l'ID = " + id + " n' ete trouve dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public Article findByCodeArticle(String codeArticle) {
        if (!StringUtils.hasLength(codeArticle)) {
            log.error("Article CODE is null");
            return null;
        }

        return articleRepository.findArticleByCodeArticle(codeArticle)

                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucun article avec le CODE = " + codeArticle + " n' ete trouve dans la BDD",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public List<LigneVente> findHistoriqueVentes(Integer idArticle) {
        return venteRepository.findAllByArticleId(idArticle).stream().collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClient> findHistoriaueCommandeClient(Integer idArticle) {
        return commandeClientRepository.findAllByArticleId(idArticle).stream().collect(Collectors.toList());
    }



    @Override
    public void delete(Integer id) {
//        if (id == null) {
//            log.error("Article ID is null");
//            return;
//        }
//        List<LigneCommandeClient> ligneCommandeClients = commandeClientRepository.findAllByArticleId(id);
//        if (!ligneCommandeClients.isEmpty()) {
//            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes client", ErrorCodes.ARTICLE_ALREADY_IN_USE);
//        }
//        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = commandeFournisseurRepository.findAllByArticleId(id);
//        if (!ligneCommandeFournisseurs.isEmpty()) {
//            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes fournisseur",
//                    ErrorCodes.ARTICLE_ALREADY_IN_USE);
//        }
//        List<LigneVente> ligneVentes = venteRepository.findAllByArticleId(id);
//        if (!ligneVentes.isEmpty()) {
//            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des ventes",
//                    ErrorCodes.ARTICLE_ALREADY_IN_USE);
//        }
//        articleRepository.deleteById(id);
    }
}
