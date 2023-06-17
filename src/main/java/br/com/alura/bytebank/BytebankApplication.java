/*
package br.com.alura.bytebank;

import java.util.Scanner;

import br.com.alura.bytebank.app.account.*;
import br.com.alura.bytebank.app.client.*;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.alura.bytebank.infra.client.ClientRepositoryMemory;

public class BytebankApplication {

    private static final AccountRepository accountRepository = new AccountRepositoryMemory();
    private static final ClientRepository clientRepository = new ClientRepositoryMemory();
    private static final ListAccount listAccount = new ListAccount(accountRepository);
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public static void main(String[] args) {
        var option = showMenu();
        while (option != 11) {
            try {
                switch (option) {
                    case 1 -> registerClient();
                    case 2 -> listClients();
                    case 3 -> listAccounts();
                    case 4 -> openAccount();
                    case 5 -> checkBalance();
                    case 6 -> deposit();
                    case 7 -> withdraw();
                    case 8 -> transfer();
                    case 9 -> listAccountsByClientCpf();
                    case 10 -> close();
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Press ENTER to return to the menu");
                scanner.next();
            }
            option = showMenu();
        }

        System.out.println("Finalizing the application.");
    }

    private static int showMenu() {
        System.out.println("""
                BYTEBANK - CHOOSE AN OPTION:
                1 - New client
                2 - List client
                3 - List accounts
                4 - Open account
                5 - Check balance
                6 - Deposit
                7 - Withdraw
                8 - Transfer
                9 - List accounts by client cpf
                10 - Close an account
                11 - Exit
                """);
        return scanner.nextInt();
    }

    private static void openAccount() {
        System.out.println("Enter account number:");
        var accountNumber = scanner.nextInt();

        System.out.println("Enter the client CPF:");
        var cpf = scanner.next();

        OpenAccount openAccount = new OpenAccount(accountRepository, clientRepository);
        AccountDto accountDto = new AccountDto(accountNumber, cpf);
        openAccount.execute(accountDto);

        System.out.println("Account successfully opened!");
        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }

    private static void registerClient() {
        System.out.println("Enter name:");
        var name = scanner.next();

        System.out.println("Enter CPF:");
        var cpf = scanner.next();

        System.out.println("Enter email:");
        var email = scanner.next();

        RegisterClient registerClient = new RegisterClient(clientRepository);
        ClientDto clientDto = new ClientDto(cpf, name, email);
        registerClient.execute(clientDto);

        System.out.println("Account successfully opened!");
        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }

    private static void listClients() {
        System.out.println("Registered Clients:");

        ListClient listClient = new ListClient(clientRepository);
        var clients = listClient.list();
        clients.forEach(System.out::println);

        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }

    private static void listAccounts() {
        System.out.println("Registered Accounts:");

        var accounts = listAccount.list();
        accounts.forEach(System.out::println);

        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }

    private static void listAccountsByClientCpf() {
        System.out.println("Enter client CPF:");
        var cpf = scanner.next();


        System.out.println("Registered Accounts:");
        var accounts = listAccount.searchByCpf(cpf);
        accounts.forEach(System.out::println);

        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }

    private static void checkBalance() {
        System.out.println("Enter account number:");
        var accountNumber = scanner.nextInt();
        CheckBalance checkBalance = new CheckBalance(accountRepository);

        var balance = checkBalance.execute(accountNumber);
        System.out.println("Account balance: " + balance);

        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }

    private static void deposit() {
        System.out.println("Enter account number:");
        var accountNumber = scanner.nextInt();

        System.out.println("Enter the deposit amount:");
        var amount = scanner.nextBigDecimal();

        Deposit deposit = new Deposit(accountRepository);
        deposit.execute(accountNumber, amount);

        System.out.println("Deposit made successfully!");
        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }

    private static void withdraw() {
        System.out.println("Enter account number:");
        var accountNumber = scanner.nextInt();

        System.out.println("Enter the withdrawal amount:");
        var amount = scanner.nextBigDecimal();

        Withdraw withdraw = new Withdraw(accountRepository);
        withdraw.execute(accountNumber, amount);

        System.out.println("Withdrawal successful!");
        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }

    public static void transfer() {
        System.out.println("Account number that will transfer");
        var accountNumberFrom = scanner.nextInt();

        System.out.println("Account number that will receive");
        var accountNumberTo = scanner.nextInt();

        System.out.println("Amount");
        var amount = scanner.nextBigDecimal();

        TransferService transferService = new TransferService(accountRepository);
        transferService.execute(accountNumberFrom, accountNumberTo, amount);
        scanner.next();
    }

    private static void close() {
        System.out.println("Enter account number:");
        var accountNumber = scanner.nextInt();

        CloseAccount closeAccount = new CloseAccount(accountRepository);
        closeAccount.execute(accountNumber);

        System.out.println("Account closed successfully!");
        System.out.println("Press ENTER to return to the menu");
        scanner.next();
    }
}
*/
