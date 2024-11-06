package puc.poo;

import puc.poo.controller.CommandProcessor;
import puc.poo.model.Player;
import puc.poo.model.Scenario;
import puc.poo.controller.ScenarioManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/// Classe que inicia o jogo e suas dependências.
public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Player player;
    public Map<String, Scenario> scenarios;
    private final CommandProcessor commandProcessor;

    public Game() {
        player = new Player();
        ScenarioManager scenarioManager = new ScenarioManager(player);
        scenarios = new HashMap<>();
        scenarioManager.initializeScenarios();
        commandProcessor = new CommandProcessor(player, scenarios);
    }

    private void showWelcomeMessage() {
        Scanner sc = new Scanner(System.in);
        String answer;
        System.out.println("BEM-VINDO AO MISTÉRIO DO CAÇADOR!");
        System.out.print("Pressione ENTER para continuar.");
        sc.nextLine();
        System.out.print("A interação deste jogo é feita por meio de comandos no terminal.");
        sc.nextLine();
        System.out.print("    Exemplo: ver objeto");
        sc.nextLine();
        System.out.print("Alguns comandos, como no exemplo, dependem de um objeto para interagir.");
        sc.nextLine();
        System.out.print("Mas você pode usar \"ver\", \"observar\" ou \"olhar\" para obter informações do cenário em que você se encontra.");
        sc.nextLine();
        System.out.print("Use o comando \"ajuda\" para listar os comandos ou \"sair\" para encerrar o jogo.");
        sc.nextLine();
        System.out.println("Vamos começar? \"Não\" para sair ou qualquer entrada para continuar.");
        answer = sc.nextLine().toLowerCase();
        if (answer.equals("não") || answer.equals("nao") || answer.equals("n")) {
            System.out.println("Saindo do jogo...");
            System.exit(0);
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String command;
        int flag = 0;

        showWelcomeMessage();

        while (true) {
            if (flag == 0) {
                System.out.println(player.getCurrentScenario().getDescription());
                flag = 1;
            }
            command = scanner.nextLine();
            commandProcessor.processCommand(command);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
} 