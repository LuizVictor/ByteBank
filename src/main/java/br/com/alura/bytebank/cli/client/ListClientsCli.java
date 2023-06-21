package br.com.alura.bytebank.cli.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;

import java.util.List;
import java.util.Scanner;

public final class ListClientsCli {
    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public static List<ClientDto> all(ClientRepository repository) {
        ListClient listClient = new ListClient(repository);
        return listClient.list();
    }

    public static ClientDto byCpf(ClientRepository repository) {
        System.out.println("Enter CPF:");
        String cpf = scanner.next();

        ListClient listClient = new ListClient(repository);
        return listClient.searchByCpf(cpf);
    }

    public static ClientDto byEmail(ClientRepository repository) {
        System.out.println("Enter email:");
        String email = scanner.next();

        ListClient listClient = new ListClient(repository);
        return listClient.searchByEmail(email);
    }
}
