package br.com.alura.bytebank.infra.account.service;

import br.com.alura.bytebank.domain.account.Account;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;

import java.math.BigDecimal;

public class AccountServiceMemory implements AccountService {
    private final AccountRepository repository;

    public AccountServiceMemory(AccountRepository repository) {
        this.repository = repository;
    }

    public void deposit(Account account, BigDecimal amount) {
        repository.searchByNumber(account.number()).setBalance(amount);
    }

    public void withdraw(Account account, BigDecimal amount) {
        repository.searchByNumber(account.number()).setBalance(amount);
    }
}
