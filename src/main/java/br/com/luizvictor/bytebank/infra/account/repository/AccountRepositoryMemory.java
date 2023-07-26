
package br.com.luizvictor.bytebank.infra.account.repository;

import br.com.luizvictor.bytebank.domain.account.Account;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;

import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryMemory implements AccountRepository {
    private final List<Account> accounts = new ArrayList<>();

    @Override
    public void register(Account account) {
        accounts.add(account);
    }

    @Override
    public List<Account> list() {
        return accounts;
    }

    @Override
    public Account searchByNumber(Integer accountNumber) {
        return this.accounts
                .stream()
                .filter(account -> account.number().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Account> searchByClientCpf(String cpf) {
        return this.accounts
                .stream()
                .filter(account -> account.client().cpf().equals(cpf))
                .toList();
    }


    @Override
    public void close(Account accountNumber) {
        var account = searchByNumber(accountNumber.number());
        accounts.remove(account);
    }
}
