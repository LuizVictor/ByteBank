package br.com.alura.bytebank.cli.client;

import br.com.alura.bytebank.app.client.RemoveClient;
import br.com.alura.bytebank.domain.client.ClientRepository;
import jakarta.persistence.EntityManager;

import java.util.Scanner;

public class RemoveClientCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public RemoveClientCli(ClientRepository repository, EntityManager entityManager) {
        System.out.println("Enter cpf:");
        String cpf = scanner.next();

        RemoveClient remove = new RemoveClient(repository);
        remove.execute(cpf);
        entityManager.flush();
    }
}
