package br.com.alura.bytebank.infra.account.repository;

import br.com.alura.bytebank.domain.account.Account;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.infra.orm.AccountModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRepositoryDb implements AccountRepository {

    private final EntityManager entityManager;

    public AccountRepositoryDb(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void register(Account account) {
        AccountDto accountDto = new AccountDto(account);
        entityManager.persist(new AccountModel(accountDto));
    }

    @Override
    public List<Account> list() {
        String jpql = "select accounts from AccountModel accounts";
        TypedQuery<AccountModel> query = entityManager.createQuery(jpql, AccountModel.class);
        return query.getResultList().stream().map(accountModel -> {
            ClientDto clientDto = new ClientDto(
                    accountModel.client().name(),
                    accountModel.client().cpf(),
                    accountModel.client().email()
            );
            AccountDto accountDto = new AccountDto(accountModel.number(), clientDto);
            Account account = new Account(accountDto);

            if (accountModel.balance().compareTo(BigDecimal.ZERO) > 0) {
                account.setBalance(accountModel.balance());
            }

            return account;
        }).collect(Collectors.toList());
    }

    @Override
    public Account searchByNumber(Integer accountNumber) {
        try {
            AccountModel accountModel = entityManager.find(AccountModel.class, accountNumber);
            ClientDto clientDto = new ClientDto(
                    accountModel.client().name(),
                    accountModel.client().cpf(),
                    accountModel.client().email()
            );

            AccountDto accountDto = new AccountDto(accountNumber, clientDto);
            Account account = new Account(accountDto);

            if (accountModel.balance().compareTo(BigDecimal.ZERO) > 0) {
                account.setBalance(accountModel.balance());
            }

            return account;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Account> searchByClientCpf(String cpf) {
        String jpql = "select accounts from AccountModel accounts where accounts.client.cpf = :cpf";
        TypedQuery<AccountModel> query = entityManager.createQuery(jpql, AccountModel.class);
        return query.setParameter("cpf", cpf).getResultList().stream().map(accountModel -> {
            ClientDto clientDto = new ClientDto(
                    accountModel.client().name(),
                    accountModel.client().cpf(),
                    accountModel.client().email()
            );
            AccountDto accountDto = new AccountDto(accountModel.number(), clientDto);
            Account account = new Account(accountDto);

            if (accountModel.balance().compareTo(BigDecimal.ZERO) > 0) {
                account.setBalance(accountModel.balance());
            }

            return account;
        }).collect(Collectors.toList());
    }

    @Override
    public void close(Account account) {
        AccountModel accountModel = entityManager.find(AccountModel.class, account.number());
        entityManager.remove(accountModel);
    }
}
