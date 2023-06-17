package br.com.alura.bytebank.app.account;

import br.com.alura.bytebank.domain.account.*;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.alura.bytebank.domain.account.exceptions.AccountNotFoundException;

import java.util.List;

public class ListAccount {
    private final AccountRepository accountRepository;

    public ListAccount(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDetailDto> list() {
        List<Account> accounts = accountRepository.list();

        if (accounts.isEmpty()) {
            throw new AccountDomainException("There are no registered accounts");
        }

        return accounts.stream().map(AccountDetailDto::new).toList();
    }

    public AccountDetailDto searchByNumber(Integer number) {
        Account account = accountRepository.searchByNumber(number);

        if (account == null) {
            throw new AccountNotFoundException("Account not exist");
        }

        return new AccountDetailDto(account);
    }

    public List<AccountDetailDto> searchByCpf(String cpf) {
        List<Account> accounts = accountRepository.searchByClientCpf(cpf);

        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("There is no account linked to this client");
        }

        return accounts.stream().map(AccountDetailDto::new).toList();
    }
}
