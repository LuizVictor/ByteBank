package br.com.alura.bytebank.infra.account.service;

import br.com.alura.bytebank.domain.account.Account;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.domain.account.TransferService;

import java.math.BigDecimal;

public class TransferServiceMemory implements TransferService {
    private final AccountRepository repository;
    private final AccountService service;

    public TransferServiceMemory(AccountRepository repository, AccountService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void execute(Account from, Account to, BigDecimal amount) {
        Account fromAccount = repository.searchByNumber(from.number());
        Account toAccount = repository.searchByNumber(to.number());

        service.withdraw(fromAccount, amount);
        service.deposit(toAccount, amount);
    }
}
