package br.com.alura.bytebank.infra.account.repository;

import br.com.alura.bytebank.domain.account.Account;
import br.com.alura.bytebank.domain.account.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AccountRepositoryDb implements AccountRepository {

    private final EntityManager entityManager;

    public AccountRepositoryDb(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void register(Account account) {
        entityManager.persist(account);
    }

    @Override
    public List<Account> list() {
        String jpql = "select accounts from Account accounts";
        TypedQuery<Account> query = entityManager.createQuery(jpql, Account.class);
        return query.getResultList();
    }

    @Override
    public Account searchByNumber(Integer accountNumber) {
        try {
            return entityManager.find(Account.class, accountNumber);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Account> searchByClientCpf(String cpf) {
        String jpql = "select accounts from Account accounts where accounts.client.cpf = :cpf";
        TypedQuery<Account> query = entityManager.createQuery(jpql, Account.class);
        return query.setParameter("cpf", cpf).getResultList();
    }

    @Override
    public void close(Account account) {
        Account accountClose = entityManager.find(Account.class, account.number());
        entityManager.remove(accountClose);
    }
}
