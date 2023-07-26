package br.com.luizvictor.bytebank.cli.client;

import br.com.luizvictor.bytebank.app.client.RemoveClient;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
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

        System.out.println("Client removed");
        System.out.println("Press ENTER to return to the menu");
    }
}
