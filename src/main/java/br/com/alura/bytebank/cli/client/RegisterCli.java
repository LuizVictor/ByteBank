package br.com.alura.bytebank.cli.client;

import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import jakarta.persistence.EntityManager;

import java.util.Scanner;

public class RegisterCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public RegisterCli(ClientRepository clientRepository, EntityManager entityManager) {
        System.out.println("Enter name:");
        String name = scanner.next();

        System.out.println("Enter CPF:");
        String cpf = scanner.next();

        System.out.println("Enter email:");
        String email = scanner.next();

        ClientDto client = new ClientDto(name, cpf, email);
        RegisterClient register = new RegisterClient(clientRepository);

        register.execute(client);
        entityManager.flush();

        System.out.println("Account successfully opened!");
        System.out.println("Press ENTER to return to the menu");
        scanner.next();

    }
}
