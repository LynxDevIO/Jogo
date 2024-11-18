package puc.poo;

import puc.poo.controller.CommandProcessor;
import puc.poo.controller.StagSpotter;
import puc.poo.model.Stag;
import puc.poo.model.Player;
import puc.poo.model.Scenario;
import puc.poo.controller.ScenarioManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A classe Game inicializa e gerencia o fluxo e a lógica do jogo.
 * Configura o player, os cenários e o processamento de comandos.
 * O jogo começa com uma mensagem inicial (breve tutorial) e entra no loop principal para aguardar os comandos do jogador.
 */
public class Game {

    public Player player; // Representa o jogador do jogo
    public Stag stag = new Stag(); // Representa um elemento ou entidade do jogo
    public Map<String, Scenario> scenarios; // Mapa que mapeia nomes de cenários para objetos de cenário
    private final CommandProcessor commandProcessor; // Processador de comandos do jogador

    // Construtor da classe Game
    public Game() {
        player = new Player(); // Inicializa o jogador
        ScenarioManager scenarioManager = new ScenarioManager(player, stag); // Gerenciador de cenários
        scenarios = new HashMap<>(); // Inicializa o mapa de cenários
        scenarioManager.initializeScenarios(); // Inicializa os cenários
        commandProcessor = new CommandProcessor(player, scenarios); // Inicializa o processador de comandos com o jogador e os cenários
    }

    // Mét. que exibe uma mensagem de boas-vindas e pequenas instruções para o jogador
    private void showWelcomeMessage() {
        Scanner sc = new Scanner(System.in);
        String answer;
        System.out.println("BEM-VINDO AO JOGO DO CAÇADOR!");
        System.out.print("Pressione ENTER para continuar.");
        sc.nextLine(); // Aguarda o jogador pressionar ENTER
        System.out.print("A interação deste jogo é feita por meio de comandos no terminal.");
        sc.nextLine(); // Aguarda o jogador pressionar ENTER
        System.out.print("    Exemplo: ver objeto\n     ou ver :objeto específico (para especificar o nome maior do objeto)");
        sc.nextLine(); // Aguarda o jogador pressionar ENTER
        System.out.print("Alguns comandos, como no exemplo, dependem de um objeto para interagir.");
        sc.nextLine(); // Aguarda o jogador pressionar ENTER
        System.out.print("Mas você pode usar \"ver\", \"observar\" ou \"olhar\" para obter informações do cenário em que você se encontra.");
        sc.nextLine(); // Aguarda o jogador pressionar ENTER
        System.out.print("Use o comando \"ajuda\" para listar os comandos ou \"sair\" para encerrar o jogo.");
        sc.nextLine(); // Aguarda o jogador pressionar ENTER
        System.out.println("Vamos começar? Digite \"NÃO\" para sair ou qualquer entrada para continuar.");
        answer = sc.nextLine().toLowerCase(); // Lê a resposta do jogador
        if (answer.equals("não") || answer.equals("nao") || answer.equals("n")) {
            System.out.println("Saindo do jogo...");
            System.exit(0); // Sai do jogo se o jogador não quiser continuar
        }
    }

    // Mét. que inicia o jogo
    public void start() {
        Scanner scanner = new Scanner(System.in);
        String command;
        int flag = 0;

        showWelcomeMessage(); // Exibe mensagem de boas-vindas e instruções
        StagSpotter stagSpotter = new StagSpotter(stag, player); // Inicializa o StagSpotter com o jogador e a entidade stag
        Thread doveSpotterThread = new Thread(stagSpotter); // Cria uma nova thread para o StagSpotter
        doveSpotterThread.start(); // Inicia a thread do StagSpotter

        // Loop principal do jogo
        while (true) {
            if (flag == 0) {
                System.out.println(player.getCurrentScenario().getImagePath());
                System.out.println(player.getCurrentScenario().getDescription()); // Exibe a descrição do cenário atual do jogador
                flag = 1; // Marca que a descrição inicial do cenário já foi exibida
            }
            command = scanner.nextLine(); // Lê o comando do jogador
            commandProcessor.processCommand(command); // Processa o comando do jogador
        }
    }

    // Mét. principal que inicia o jogo
    public static void main(String[] args) {
        Game game = new Game(); // Cria um novo jogo
        game.start(); // Inicia o jogo
    }
}