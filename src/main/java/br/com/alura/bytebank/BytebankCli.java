package br.com.alura.bytebank;

import br.com.alura.bytebank.cli.client.ListClientsCli;
import br.com.alura.bytebank.cli.client.RegisterClientCli;
import br.com.alura.bytebank.cli.client.UpdateClientCli;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;

public class BytebankCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
    private static EntityManager entityManager = factory.createEntityManager();
    private static ClientRepository clientRepository = new ClientRepositoryDb(entityManager);

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
                    case 6 -> System.out.println("Delete");
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

        while (option != 7) {
            try {
                switch (option) {
                    case 1 -> System.out.println("Register Account");
                    case 2 -> System.out.println("Search by number");
                    case 3 -> System.out.println("Search by client cpf");
                    case 4 -> System.out.println("Deposit");
                    case 5 -> System.out.println("Withdraw");
                    case 6 -> System.out.println("Transfer");
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
                2 - Search by number
                3 - Search by client cpf
                4 - Deposit
                5 - Withdraw
                6 - Transfer
                7 - Back to menu
                """);

        return scanner.nextInt();
    }
}
