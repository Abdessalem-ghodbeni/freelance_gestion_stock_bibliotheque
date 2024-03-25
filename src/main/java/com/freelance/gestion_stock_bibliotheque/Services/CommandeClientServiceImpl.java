package com.freelance.gestion_stock_bibliotheque.Services;
import com.freelance.gestion_stock_bibliotheque.Entities.*;
import com.freelance.gestion_stock_bibliotheque.Exceptions.EntityNotFoundException;
import com.freelance.gestion_stock_bibliotheque.Exceptions.ErrorCodes;
import com.freelance.gestion_stock_bibliotheque.Exceptions.InvalidEntityException;
import com.freelance.gestion_stock_bibliotheque.Exceptions.InvalidOperationException;
import com.freelance.gestion_stock_bibliotheque.Repository.*;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.CommandeClientService;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.MvtStkService;
import com.freelance.gestion_stock_bibliotheque.Validators.ArticleValidator;
import com.freelance.gestion_stock_bibliotheque.Validators.CommandeClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

@RequiredArgsConstructor
@Service
@Slf4j

public class CommandeClientServiceImpl implements CommandeClientService {
    private final CommandeClientRepository commandeClientRepository;
    private final LigneCommandeClientRepository ligneCommandeClientRepository;
    private  final ClientRepository clientRepository;

    private final IArticleRepository articleRepository;
    private final MvtStkService mvtStkService;
    private final MvtStkRepository mvtStkRepository;
    private final StockRepository stockRepository;
   @Transactional
    @Override
    public CommandeClient save(CommandeClient dto) {

        List<String> errors = CommandeClientValidator.validate(dto);

        if (!errors.isEmpty()) {
            CommandeClientServiceImpl.log.error("Commande client n'est pas valide");
            throw new InvalidEntityException("La commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
        }

        if (dto.getClient() == null || dto.getClient().getId() == 0 && dto.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(dto.getClient().getId());
        if (client.isEmpty()) {
            CommandeClientServiceImpl.log.warn("Client with ID {} was not found in the DB", dto.getClient().getId());
            throw new EntityNotFoundException("Aucun client avec l'ID" + dto.getClient().getId() + " n'a ete trouve dans la BDD",
                    ErrorCodes.CLIENT_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();

        if (dto.getLigneCommandeClients() != null) {
            dto.getLigneCommandeClients().forEach(ligCmdClt -> {
                if (ligCmdClt.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
                    Article articleN = article.get();
                    BigDecimal quantiteCommandee = ligCmdClt.getQuantite();
                    BigDecimal quantiteProduit = articleN.getStock().getQuantite();
                    BigDecimal nouveauStock = quantiteProduit.subtract(quantiteCommandee);
                    articleN.getStock().setQuantite(nouveauStock);
                    articleRepository.save(articleN);
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID " + ligCmdClt.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
                }

            });
        }

        if (!articleErrors.isEmpty()) {
            CommandeClientServiceImpl.log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }
        dto.setDateCommande(Instant.now());
        CommandeClient savedCmdClt = commandeClientRepository.save(dto);

        if (dto.getLigneCommandeClients() != null) {
            dto.getLigneCommandeClients().forEach(ligCmdClt -> {
                ligCmdClt.setCommandeClient(savedCmdClt);
                LigneCommandeClient savedLigneCmd = ligneCommandeClientRepository.save(ligCmdClt);

                effectuerSortie(savedLigneCmd);
            });
        }


        return savedCmdClt;
    }

    private void effectuerSortie(LigneCommandeClient lig) {
        MvtStk mvtStkDto = MvtStk.builder()
                .article(lig.getArticle())
                .dateMvt(Instant.now())
                .typeMvt(TypeMvtStk.SORTIE)
                .sourceMvt(SourceMvtStk.COMMANDE_CLIENT)
                .quantite(lig.getQuantite())
                .build();
        mvtStkService.sortieStock(mvtStkDto);
    }

    @Override
    public CommandeClient updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);
        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            CommandeClientServiceImpl.log.error("L'etat de la commande client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        CommandeClient commandeClient = checkEtatCommande(idCommande);
        commandeClient.setEtatCommande(etatCommande);

        CommandeClient savedCmdClt = commandeClientRepository.save(commandeClient);
        if (commandeClient.isCommandeLivree()) {
            updateMvtStk(idCommande);
        }

        return savedCmdClt;
    }
    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            CommandeClientServiceImpl.log.error("Commande client ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private CommandeClient checkEtatCommande(int idCommande) {
        CommandeClient commandeClient = findById(idCommande);
        if (commandeClient.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        return commandeClient;
    }

    private void updateMvtStk(Integer idCommande) {
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
        ligneCommandeClients.forEach(lig -> {
            effectuerSortie(lig);
        });
    }
    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            CommandeClientServiceImpl.log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private Optional<LigneCommandeClient> findLigneCommandeClient(Integer idLigneCommande) {
        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);
        if (ligneCommandeClientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune ligne commande client n'a ete trouve avec l'ID " + idLigneCommande, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
        }
        return ligneCommandeClientOptional;
    }
@Transactional
    @Override
    public CommandeClient updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            CommandeClientServiceImpl.log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClient commandeClient = checkEtatCommande(idCommande);
        Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);


    LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();

    BigDecimal ancienneQuantite = ligneCommandeClient.getQuantite();
    BigDecimal differenceQuantite = quantite.subtract(ancienneQuantite);

    if (differenceQuantite.compareTo(BigDecimal.ZERO) != 0) {
        // Mettre à jour la quantité dans la ligne de commande
        ligneCommandeClient.setQuantite(quantite);
        ligneCommandeClientRepository.save(ligneCommandeClient);

        // Mettre à jour la quantité dans le stock
        Article article = ligneCommandeClient.getArticle();
        Stock stock = article.getStock();
        BigDecimal nouvelleQuantiteStock = stock.getQuantite().add(differenceQuantite);
        stock.setQuantite(nouvelleQuantiteStock);
        stockRepository.save(stock);
    }
        return commandeClient;
    }

    @Override
    public CommandeClient updateClient(Integer idCommande, Integer idClient) {
        checkIdCommande(idCommande);
        if (idClient == null) {
            CommandeClientServiceImpl.log.error("L'ID du client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID client null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        CommandeClient commandeClient = checkEtatCommande(idCommande);
        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if (clientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun client n'a ete trouve avec l'ID " + idClient, ErrorCodes.CLIENT_NOT_FOUND);
        }
        commandeClient.setClient(clientOptional.get());

        return  commandeClientRepository.save(commandeClient);
    }
    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            CommandeClientServiceImpl.log.error("L'ID de " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    @Override
    public CommandeClient updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(newIdArticle, "nouvel");

        CommandeClient commandeClient = checkEtatCommande(idCommande);

        Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommandeClient(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(newIdArticle);
        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune article n'a ete trouve avec l'ID " + newIdArticle, ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<String> errors = ArticleValidator.validate(articleOptional.get());
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Article invalid", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        LigneCommandeClient ligneCommandeClientToSaved = ligneCommandeClient.get();
        ligneCommandeClientToSaved.setArticle(articleOptional.get());
        ligneCommandeClientRepository.save(ligneCommandeClientToSaved);

        return commandeClient;
    }

    @Override
    public CommandeClient deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        CommandeClient commandeClient = checkEtatCommande(idCommande);
        findLigneCommandeClient(idLigneCommande);
        ligneCommandeClientRepository.deleteById(idLigneCommande);
        return commandeClient;
    }

    @Override
    public CommandeClient findById(Integer id) {
        if (id == null) {
            CommandeClientServiceImpl.log.error("Commande client ID is NULL");
            return null;
        }
        return commandeClientRepository.findById(id)

                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client n'a ete trouve avec l'ID " + id, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public CommandeClient findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            CommandeClientServiceImpl.log.error("Commande client CODE is NULL");
            return null;
        }
        return commandeClientRepository.findCommandeClientByCode(code)

                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client n'a ete trouve avec le CODE " + code, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public List<CommandeClient> findAll() {
        return commandeClientRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClient> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
        return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande).stream()
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            CommandeClientServiceImpl.log.error("Commande client ID is NULL");
            return;
        }
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(id);
        if (!ligneCommandeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une commande client deja utilisee",
                    ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE);
        }
        commandeClientRepository.deleteById(id);
    }
}
