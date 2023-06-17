package br.com.alura.bytebank.infra.client;

import br.com.alura.bytebank.domain.client.Client;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.domain.client.ClientUpdateDto;
import br.com.alura.bytebank.model.ClientModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.stream.Collectors;

public class ClientRepositoryDb implements ClientRepository {
    private final EntityManager entityManager;

    public ClientRepositoryDb(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void register(Client client) {
        ClientDto clientDto = new ClientDto(client);
        entityManager.persist(new ClientModel(clientDto));
    }

    @Override
    public List<Client> list() {
        String jpql = "select clients from ClientModel clients";
        TypedQuery<ClientModel> query = entityManager.createQuery(jpql, ClientModel.class);

        return query.getResultList().stream().map(clientModel -> {
            ClientDto clientDto = new ClientDto(clientModel.name(), clientModel.cpf(), clientModel.email());
            return new Client(clientDto);
        }).collect(Collectors.toList());
    }

    @Override
    public Client searchByCpf(String cpf) {
        try {
            ClientModel clientModel = entityManager.find(ClientModel.class, cpf);
            ClientDto clientDto = new ClientDto(clientModel.name(), clientModel.cpf(), clientModel.email());
            return new Client(clientDto);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Client searchByEmail(String email) {
        try {
            String jpql = "select client from ClientModel client where client.email = :email";
            Query query = entityManager.createQuery(jpql);
            ClientModel clientModel = (ClientModel) query.setParameter("email", email).getSingleResult();
            ClientDto clientDto = new ClientDto(clientModel.name(), clientModel.cpf(), clientModel.email());
            return new Client(clientDto);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void update(Client client) {
        ClientModel clientModel = entityManager.find(ClientModel.class, client.getCpf());
        ClientUpdateDto updateDto = new ClientUpdateDto(client.getName(), client.getEmail());
        clientModel.update(updateDto);
    }

    @Override
    public void remove(Client client) {
        ClientModel clientModel = entityManager.find(ClientModel.class, client.getCpf());
        entityManager.remove(clientModel);
    }
}
