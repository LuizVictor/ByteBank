package br.com.alura.bytebank.cli.account;

import br.com.alura.bytebank.app.account.RegisterAccount;
import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import jakarta.persistence.EntityManager;

import java.util.Scanner;

public class RegisterAccountCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public RegisterAccountCli(AccountRepository accountRepository, ClientRepository clientRepository, EntityManager entityManager) {
        System.out.println("Enter client cpf:");
        String cpf = scanner.next();

        System.out.println("Enter account number:");
        Integer number = scanner.nextInt();

        ListClient listClient = new ListClient(clientRepository);
        ClientDto client = listClient.searchByCpf(cpf);

        RegisterAccount register = new RegisterAccount(accountRepository, clientRepository);
        AccountDto dto = new AccountDto(number, client);
        register.execute(dto);
        entityManager.flush();

        System.out.println("Account opened successfully");
        System.out.println("Press ENTER to return to the menu");
    }
}
