package br.com.luizvictor.bytebank.tests.persistance.util;

import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import br.com.luizvictor.bytebank.domain.account.AccountDetailDto;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AccountUtil {
    public static void create(EntityManager entityManager, AccountRepository accountRepository) {
        ClientRepository clientRepository = RepositoryUtil.clientRepository();
        ClientUtil.create(entityManager, clientRepository);
        ClientDto client1 = ClientUtil.list(clientRepository, "123.123.123-12");
        ClientDto client2 = ClientUtil.list(clientRepository, "123.123.123-21");

        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);
        registerAccount.execute(client1);
        registerAccount.execute(client2);

        entityManager.flush();
    }

    public static AccountDetailDto list(AccountRepository accountRepository, Integer number) {
        ListAccount list = new ListAccount(accountRepository);
        return list.searchByNumber(number);
    }

    public static List<AccountDetailDto> all(AccountRepository accountRepository) {
        ListAccount listAccount = new ListAccount(accountRepository);
        return listAccount.list();
    }
}
