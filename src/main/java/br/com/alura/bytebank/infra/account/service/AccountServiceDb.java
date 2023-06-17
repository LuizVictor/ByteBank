package br.com.alura.bytebank.infra.account.service;

import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.infra.orm.AccountModel;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

public class AccountServiceDb implements AccountService {
    private final EntityManager entityManager;

    public AccountServiceDb(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deposit(Integer accountNumber, BigDecimal amount) {
        entityManager.find(AccountModel.class, accountNumber).setBalance(amount);
    }

    @Override
    public void withdraw(Integer accountNumber, BigDecimal amount) {
        entityManager.find(AccountModel.class, accountNumber).setBalance(amount);
    }
}
