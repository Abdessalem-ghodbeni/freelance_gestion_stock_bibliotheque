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
//                LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligCmdClt);
                ligCmdClt.setCommandeClient(savedCmdClt);
                LigneCommandeClient savedLigneCmd = ligneCommandeClientRepository.save(ligCmdClt);

                effectuerSortie(savedLigneCmd);
            });
        }

//        return CommandeClientDto.fromEntity(savedCmdClt);
        return savedCmdClt;
    }
//    private void effectuerSortie(LigneCommandeClient lig) {
//        MvtStk mvtStkDto = MvtStk.builder()
//
//                .article(lig.getArticle()
//                .dateMvt(Instant.now())
//                .typeMvt(TypeMvtStk.SORTIE)
//                .sourceMvt(SourceMvtStk.COMMANDE_CLIENT)
//                .quantite(lig.getQuantite())
//                .build();
//        mvtStkService.sortieStock(mvtStkDto);
//    }
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
        return null;
    }

    @Override
    public CommandeClient updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        return null;
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
        return null;
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
