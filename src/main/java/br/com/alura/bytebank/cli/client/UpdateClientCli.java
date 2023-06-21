package br.com.alura.bytebank.cli.client;

import br.com.alura.bytebank.app.client.UpdateClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import jakarta.persistence.EntityManager;

import java.util.Scanner;

public class UpdateClientCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public UpdateClientCli(ClientRepository repository, EntityManager entityManager) {
        System.out.println("Enter cpf: ");
        String cpf = scanner.next();

        System.out.println("Enter name:");
        String name = scanner.next();

        System.out.println("Enter email:");
        String email = scanner.next();

        UpdateClient update = new UpdateClient(repository);

        update.execute(new ClientDto(name, cpf, email));
        entityManager.flush();
    }
}
