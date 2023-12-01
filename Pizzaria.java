/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.pizzaria;

/**
 *
 * @author Alunos
 */
public class Cliente {
    private int id;
    private String nome;
    private String email;
    private String senha;

    // Construtores, getters e setters
}
import java.sql.*;

import java.sql.*;

public class ClienteDAO {
    private Connection connection;

    public ClienteDAO() {
        // Inicialize a conexão aqui (você deve substituir a URL e as credenciais)
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:pizzaria.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cadastrarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, email, senha) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getEmail());
            statement.setString(3, cliente.getSenha());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente fazerLogin(String email, String senha) {
        String sql = "SELECT * FROM clientes WHERE email = ? AND senha = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, senha);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getInt("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setSenha(resultSet.getString("senha"));
                return cliente;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Implemente os métodos CRUD restantes conforme necessário
}
public class Pedido {
    private int id;
    private String descricao;
    private int idCliente;

    // Construtores, getters e setters
}
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private Connection connection;

    public PedidoDAO() {
        // Inicialize a conexão aqui (você deve substituir a URL e as credenciais)
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:pizzaria.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cadastrarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (descricao, id_cliente) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pedido.getDescricao());
            statement.setInt(2, pedido.getIdCliente());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listarPedidos(int idCliente) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE id_cliente = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCliente);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(resultSet.getInt("id"));
                pedido.setDescricao(resultSet.getString("descricao"));
                pedido.setIdCliente(resultSet.getInt("id_cliente"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    // Implemente os métodos CRUD restantes conforme necessário
}
import java.util.Scanner;

import java.util.List;
import java.util.Scanner;

public class PizzariaApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ClienteDAO clienteDAO = new ClienteDAO();
        PedidoDAO pedidoDAO = new PedidoDAO();
        Cliente clienteLogado = null;

        while (true) {
            System.out.println("\n1. Cadastrar Cliente");
            System.out.println("2. Fazer Login");
            System.out.println("3. Fazer Pedido");
            System.out.println("4. Listar Pedidos");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    cadastrarCliente(scanner, clienteDAO);
                    break;
                case 2:
                    clienteLogado = fazerLogin(scanner, clienteDAO);
                    break;
                case 3:
                    fazerPedido(scanner, pedidoDAO, clienteLogado);
                    break;
                case 4:
                    listarPedidos(pedidoDAO, clienteLogado);
                    break;
                case 5:
                    System.out.println("Saindo...");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void cadastrarCliente(Scanner scanner, ClienteDAO clienteDAO) {
        System.out.println("\nCadastro de Cliente");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setSenha(senha);

        clienteDAO.cadastrarCliente(cliente);

        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static Cliente fazerLogin(Scanner scanner, ClienteDAO clienteDAO) {
        System.out.println("\nLogin");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Cliente cliente = clienteDAO.fazerLogin(email, senha);

        if (cliente != null) {
            System.out.println("Login bem-sucedido!");
            return cliente;
        } else {
            System.out.println("Login falhou. Verifique seu email e senha.");
            return null;
        }
    }

    private static void fazerPedido(Scanner scanner, PedidoDAO pedidoDAO, Cliente cliente) {
        if (cliente == null) {
            System.out.println("Você precisa fazer login primeiro.");
            return;
        }

        System.out.println("\nFazer Pedido");
        System.out.print("Descrição do Pedido: ");
        String descricao = scanner.nextLine();

        Pedido pedido = new Pedido();
        pedido.setDescricao(descricao);
        pedido.setIdCliente(cliente.getId());

        pedidoDAO.cadastrarPedido(pedido);

        System.out.println("Pedido realizado com sucesso!");
    }

    private static void listarPedidos(PedidoDAO pedidoDAO, Cliente cliente) {
        if (cliente == null) {
            System.out.println("Você precisa fazer login primeiro.");
            return;
        }

        System.out.println("\nListar Pedidos");

        List<Pedido> pedidos = pedidoDAO.listarPedidos(cliente.getId());

        for (Pedido pedido : pedidos) {
            System.out.println("ID: " + pedido.getId() + ", Descrição: " + pedido.getDescricao());
        }
    }
}