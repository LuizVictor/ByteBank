package br.com.luizvictor.bytebank.infra.client;

import br.com.luizvictor.bytebank.domain.client.Client;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.domain.client.ClientUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ClientRepositoryDb implements ClientRepository {
    private final EntityManager entityManager;

    public ClientRepositoryDb(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void register(Client client) {
        entityManager.persist(client);
    }

    @Override
    public List<Client> list() {
        String jpql = "select clients from Client clients";
        TypedQuery<Client> query = entityManager.createQuery(jpql, Client.class);

        return query.getResultList();
    }

    @Override
    public Client searchByCpf(String cpf) {
        try {
            return entityManager.find(Client.class, cpf);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Client searchByEmail(String email) {
        try {
            String jpql = "select client from Client client where client.email = :email";
            Query query = entityManager.createQuery(jpql);
            return (Client) query.setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void update(Client client) {
        Client clientUpdate = entityManager.find(Client.class, client.cpf());
        ClientUpdateDto updateDto = new ClientUpdateDto(client.name(), client.email());
        clientUpdate.update(updateDto);
    }

    @Override
    public void remove(Client client) {
        Client clientDelete = entityManager.find(Client.class, client.cpf());
        entityManager.remove(clientDelete);
    }
}
