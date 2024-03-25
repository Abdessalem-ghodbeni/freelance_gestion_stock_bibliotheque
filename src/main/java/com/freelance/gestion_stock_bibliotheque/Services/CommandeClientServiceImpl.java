package com.freelance.gestion_stock_bibliotheque.Services;
import com.freelance.gestion_stock_bibliotheque.Entities.*;
import com.freelance.gestion_stock_bibliotheque.Exceptions.EntityNotFoundException;
import com.freelance.gestion_stock_bibliotheque.Exceptions.ErrorCodes;
import com.freelance.gestion_stock_bibliotheque.Exceptions.InvalidEntityException;
import com.freelance.gestion_stock_bibliotheque.Exceptions.InvalidOperationException;
import com.freelance.gestion_stock_bibliotheque.Repository.*;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.CommandeClientService;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.MvtStkService;
import com.freelance.gestion_stock_bibliotheque.Validators.CommandeClientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j

public class CommandeClientServiceImpl implements CommandeClientService {
    private final CommandeClientRepository commandeClientRepository;
    private final LigneCommandeClientRepository ligneCommandeClientRepository;
    private  final ClientRepository clientRepository;

    private final IArticleRepository articleRepository;
    private final MvtStkService mvtStkService;
    private MvtStkRepository mvtStkRepository;
    @Override
    public CommandeClient save(CommandeClient dto) {

        List<String> errors = CommandeClientValidator.validate(dto);

        if (!errors.isEmpty()) {
            log.error("Commande client n'est pas valide");
            throw new InvalidEntityException("La commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
        }

        if (dto.getClient() == null || dto.getClient().getId() == 0 && dto.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(dto.getClient().getId());
        if (client.isEmpty()) {
            log.warn("Client with ID {} was not found in the DB", dto.getClient().getId());
            throw new EntityNotFoundException("Aucun client avec l'ID" + dto.getClient().getId() + " n'a ete trouve dans la BDD",
                    ErrorCodes.CLIENT_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();

        if (dto.getLigneCommandeClients() != null) {
            dto.getLigneCommandeClients().forEach(ligCmdClt -> {
                if (ligCmdClt.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID " + ligCmdClt.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
                }
            });
        }

        if (!articleErrors.isEmpty()) {
            log.warn("");
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
            log.error("L'etat de la commande client is NULL");
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
            log.error("Commande client ID is NULL");
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
            log.error("L'ID de la ligne commande is NULL");
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
    @Override
    public CommandeClient updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClient commandeClient = checkEtatCommande(idCommande);
        Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);

        LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();
        ligneCommandeClient.setQuantite(quantite);
        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClient;
    }

    @Override
    public CommandeClient updateClient(Integer idCommande, Integer idClient) {
        return null;
    }

    @Override
    public CommandeClient updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle) {
        return null;
    }

    @Override
    public CommandeClient deleteArticle(Integer idCommande, Integer idLigneCommande) {
        return null;
    }

    @Override
    public CommandeClient findById(Integer id) {
        if (id == null) {
            log.error("Commande client ID is NULL");
            return null;
        }
        return commandeClientRepository.findById(id)

                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client n'a ete trouve avec l'ID " + id, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public CommandeClient findByCode(String code) {
        return null;
    }

    @Override
    public List<CommandeClient> findAll() {
        return null;
    }

    @Override
    public List<CommandeClient> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
