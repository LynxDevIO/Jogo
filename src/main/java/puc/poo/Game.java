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

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String command;
        int flag = 0;

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