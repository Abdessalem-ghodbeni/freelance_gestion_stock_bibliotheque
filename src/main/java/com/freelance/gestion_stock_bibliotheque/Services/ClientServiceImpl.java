package com.freelance.gestion_stock_bibliotheque.Services;

import com.freelance.gestion_stock_bibliotheque.Entities.Client;
import com.freelance.gestion_stock_bibliotheque.Entities.CommandeClient;
import com.freelance.gestion_stock_bibliotheque.Exceptions.ErrorCodes;
import com.freelance.gestion_stock_bibliotheque.Exceptions.InvalidOperationException;
import com.freelance.gestion_stock_bibliotheque.Repository.ClientRepository;
import com.freelance.gestion_stock_bibliotheque.Repository.CommandeClientRepository;
import com.freelance.gestion_stock_bibliotheque.Services.Strategy.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final CommandeClientRepository commandeClientRepository;

    @Override
    public Client save(Client dto) {
      return clientRepository.save(dto);
    }

    @Override
    public Client findById(Integer id) {
      Client client=clientRepository.findById(id).orElseThrow(()->new RuntimeException("client non trouvble avec id : "+id));
      return client;
    }

    @Override
    public List<Client> findAll() {
       return clientRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Client ID is null");
            return;
        }
        List<CommandeClient> commandeClients = commandeClientRepository.findAllByClientId(id);
        if (!commandeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un client qui a deja des commande clients",
                    ErrorCodes.CLIENT_ALREADY_IN_USE);
        }
        clientRepository.deleteById(id);
    }
}
