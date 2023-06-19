package br.com.alura.bytebank.tests.persistance.util;

import br.com.alura.bytebank.app.account.RegisterAccount;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.client.ClientRepository;
import jakarta.persistence.EntityManager;

public class AccountUtil {
    public static void create(EntityManager entityManager, AccountRepository accountRepository) {
        ClientRepository clientRepository = RepositoryUtil.clientRepository();
        ClientUtil.create(entityManager, clientRepository);
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);
        AccountDto accountDto1 = new AccountDto(1234, ClientUtil.list(clientRepository, "123.123.123-12"));
        AccountDto accountDto2 = new AccountDto(4321, ClientUtil.list(clientRepository, "123.123.123-21"));
        registerAccount.execute(accountDto1);
        registerAccount.execute(accountDto2);
        entityManager.flush();
    }
}
