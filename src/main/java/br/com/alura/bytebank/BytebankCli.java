package br.com.alura.bytebank;

import br.com.alura.bytebank.cli.account.*;
import br.com.alura.bytebank.cli.client.ListClientsCli;
import br.com.alura.bytebank.cli.client.RegisterClientCli;
import br.com.alura.bytebank.cli.client.RemoveClientCli;
import br.com.alura.bytebank.cli.client.UpdateClientCli;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.account.repository.AccountRepositoryDb;
import br.com.alura.bytebank.infra.account.service.AccountServiceDb;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;

public class BytebankCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
    private static final EntityManager entityManager = factory.createEntityManager();
    private static final ClientRepository clientRepository = new ClientRepositoryDb(entityManager);
    private static final AccountRepository accountRepository = new AccountRepositoryDb(entityManager);
    private static final AccountService accountService = new AccountServiceDb(entityManager);

    public static void main(String[] args) {
        int option = showMenu();

        while (option != 3) {
            entityManager.getTransaction().begin();
            try {
                switch (option) {
                    case 1 -> clientMenu();
                    case 2 -> accountMenu();
                }
            } catch (RuntimeException exception) {
                System.out.println("Error: " + exception.getMessage());
                System.out.println("Press ENTER to return to the menu");
                scanner.next();
            }
            option = showMenu();
            entityManager.getTransaction().commit();
        }

        System.out.println("Finalizing the application.");
    }

    private static int showMenu() {
        System.out.println("""
                BYTEBANK - CHOOSE AN OPTION:
                1 - Client Application
                2 - Account Application
                3 - Exit
                """);
        return scanner.nextInt();
    }

    private static void clientMenu() {
        var option = showClientMenu();

        while (option != 7) {
            try {
                switch (option) {
                    case 1 -> new RegisterClientCli(clientRepository, entityManager);
                    case 2 -> System.out.println(ListClientsCli.all(clientRepository));
                    case 3 -> System.out.println(ListClientsCli.byCpf(clientRepository));
                    case 4 -> System.out.println(ListClientsCli.byEmail(clientRepository));
                    case 5 -> new UpdateClientCli(clientRepository, entityManager);
                    case 6 -> new RemoveClientCli(clientRepository, entityManager);
                }
            } catch (RuntimeException exception) {
                System.out.println("Error: " + exception.getMessage());
                System.out.println("Press ENTER to return to the menu");
                scanner.next();
            }
            option = showClientMenu();
        }
    }

    private static int showClientMenu() {
        System.out.println("""
                BYETBANK - Client Application:
                1 - Register new client
                2 - List
                3 - Search by cpf
                4 - Search by email
                5 - Update
                6 - Delete
                7 - Back to menu
                """);

        return scanner.nextInt();
    }

    private static void accountMenu() {
        var option = showAccountMenu();

        while (option != 8) {
            try {
                switch (option) {
                    case 1 -> new RegisterAccountCli(accountRepository, clientRepository, entityManager);
                    case 2 -> System.out.println(ListAccountsCli.all(accountRepository));
                    case 3 -> System.out.println(ListAccountsCli.byNumber(accountRepository));
                    case 4 -> System.out.println(ListAccountsCli.byCpf(accountRepository));
                    case 5 -> new DepositCli(accountRepository, accountService, entityManager);
                    case 6 -> new WithdrawCli(accountRepository, accountService, entityManager);
                    case 7 -> new TransferCli(accountRepository, accountService, entityManager);
                }
            } catch (RuntimeException exception) {
                System.out.println("Error: " + exception.getMessage());
                System.out.println("Press ENTER to return to the menu");
                scanner.next();
            }
            option = showAccountMenu();
        }
    }

    private static int showAccountMenu() {
        System.out.println("""
                BYETBANK - Account Application:
                1 - Open new account
                2 - List Accounts
                3 - Search by number
                4 - Search by client cpf
                5 - Deposit
                6 - Withdraw
                7 - Transfer
                8 - Back to menu
                """);

        return scanner.nextInt();
    }
}
