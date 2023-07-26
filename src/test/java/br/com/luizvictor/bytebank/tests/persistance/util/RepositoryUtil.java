package br.com.luizvictor.bytebank.tests.persistance.util;

import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.infra.account.repository.AccountRepositoryDb;
import br.com.luizvictor.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class RepositoryUtil {
    private static EntityManager entityManager;

    public static EntityManager createEntityManager(String persistenceUnit) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistenceUnit);
        entityManager = factory.createEntityManager();
        return entityManager;
    }

    public static ClientRepository clientRepository() {
        return new ClientRepositoryDb(entityManager);
    }

    public static AccountRepository accountRepository() {
        return new AccountRepositoryDb(entityManager);
    }
}
