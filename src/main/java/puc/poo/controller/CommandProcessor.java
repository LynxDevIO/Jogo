package puc.poo.controller;

import puc.poo.Game;
import puc.poo.model.GameObject;
import puc.poo.model.Player;
import puc.poo.model.Scenario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.io.IO.readln;

/// Classe responsável pelo gerenciamento dos comandos que o jogador vai usar.
public class CommandProcessor {
    private Player player;
    private Map<String, Scenario> scenarios;

    public CommandProcessor(Player player, Map<String, Scenario> scenarios) {
        this.player = player;
        this.scenarios = scenarios;
    }

    public void processCommand(String command) {
        String[] parts = command.split(" ");
        String action = parts[0];
        String subject = parts.length > 1 ? parts[1] : null;

        switch (action) {
            case "olhar":
            case "observar":
            case "ver":
                look(subject);
                break;
            case "entrar":
            case "ir":
                goToScenario(subject);
                break;
            case "abrir":
                open(subject);
                break;
            case "fechar":
                close(subject);
                break;
            case "pegar":
                take(subject);
                break;
            case "largar":
            case "soltar":
                drop(subject);
                break;
            case "usar":
                use(subject);
                break;
            case "voltar":
                back();
                break;
            case "salvar": // Estou reconsiderando a opção de salvar e carregar um estado pelo terminal.
                saveGame();
                break;
            case "carregar":
                loadGame();
                break;
            case "sair":
                String resposta = readln("Deseja fechar o jogo? ").toLowerCase();
                if (resposta.equals("sim")) {
                    System.out.println("Fechando jogo...");
                    System.exit(0);
                } else {
                    System.out.println("Ok. Continue a jogar!");
                }
                break;
            case "i": // Comando de inventário
            case "inventário":
                showInventory();
                break;
            case "ajuda":
                System.out.println("Lista de comandos: observar/ver, entrar/ir, abrir, fechar, pegar, largar/soltar," +
                        " usar, voltar, i/inventário");
                System.out.println("Comando de sistema: salvar, carregar, sair");
            default:
                System.out.println("Não entendo isso.");
        }
    }

    private void goToScenario(String input) {
        Scenario currentScenario = player.getCurrentScenario();
        Set<String> cardinals = Set.of("norte", "sul", "leste", "oeste");

        // Verificar se a entrada é uma direção cardinal
        if (cardinals.contains(input)) {
            Scenario nextScenario = currentScenario.getExit(input);

            // Verificar se existe um cenário nessa direção
            if (nextScenario != null) {
                player.setCurrentScenario(nextScenario);
                System.out.println("Você chegou em \"" + nextScenario.getName() + "\".");
            } else {
                System.out.println("Não há caminho para essa direção.");
            }
            return;
        }

        // Caso contrário, verificar se a entrada é um objeto (como uma porta)
        GameObject blockingObject = currentScenario.getObject(input);
        if (blockingObject != null) {
            // Verificar se o objeto é abrível e está aberto
            if (blockingObject.isOpenable()) {
                if (blockingObject.isOpen()) {
                    Scenario nextScenario = currentScenario.getExit(input); // Obter o cenário associado a esse objeto
                    if (nextScenario != null) {
                        player.setCurrentScenario(nextScenario);
                        System.out.println("Você chegou em \"" + nextScenario.getName() + "\".");
                    } else {
                        System.out.println("Esse caminho não leva a lugar nenhum.");
                    }
                } else {
                    System.out.println("Esse caminho está fechado.");
                }
            } else {
                System.out.println("Esse objeto não pode ser usado para viajar.");
            }
        } else {
            System.out.println("Direção ou objeto inválido.");
        }
    }

    private void look(String subject) {
        if (subject == null) {
            System.out.println(player.getCurrentScenario().getDescription());
        } else {
            GameObject obj = player.getCurrentScenario().getObject(subject);
            if (obj != null) {
                System.out.println(obj.getDescription());
            } else {
                System.out.println("Não entendo \"%s\".".formatted(subject.toUpperCase()));
            }
        }
    }

    private void open(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null) {
            obj.unlock(player.getInventory());
        } else {
            System.out.println("Não entendo \"%s\".".formatted(subject).toUpperCase());
        }
    }

    private void close(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null) {
            obj.lock(player.getInventory());
        } else {
            System.out.println("Não entendo \"%s\".".formatted(subject).toUpperCase());
        }
    }

    private void take(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null) {
            if (obj.isStorable()) {
                player.addToInventory(obj);
                player.getCurrentScenario().removeObjectByName(subject);
                System.out.println("Você pegou \"" + subject.toUpperCase() + "\".");
            } else {
                System.out.println("Você não pode pegar isso.");
            }
        } else {
            System.out.println("Não entendo \"%s\".".formatted(subject).toUpperCase());
        }
    }

    private void drop(String subject) {
        GameObject obj = player.getFromInventory(subject);
        if (obj != null) {
            player.removeFromInventory(subject);
            player.getCurrentScenario().addObject(obj);
            System.out.println("Você soltou \"" + subject.toUpperCase() + "\".");
        } else {
            System.out.println("Você não tem \"%s.\"".formatted(subject).toUpperCase());
        }
    }

    /// Se o objeto tiver uma ação, ela será executada. Caso contrário, se ele armazena objetos, a ação será de obter o item.
    private void use(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null) {
            if (obj.hasAction() ) {
                obj.getAction().execute();
            } else {
                if (obj.isStorage()) {
                    System.out.println("Há alguma coisa dentro desse objeto...");
                    System.out.println("Você colocou no seu inventário:");
                    obj.getContents().forEach(e -> {
                        player.addToInventory(e);
                        System.out.println("- \"%s\"".formatted(e.getName().toUpperCase()));
                    });
                } else {
                    System.out.println("Esse item não pode ser usado.");
                }
            }
        } else {
            System.out.println("Parece não haver \"%s\" ao redor.".formatted(subject).toUpperCase());
        }
    }

    private void back() {
        if (player.hasPreviousScenario()) {
            player.setCurrentScenario(player.getPreviousScenario());
            System.out.println("Você voltou a \"%s\".".formatted(player.getCurrentScenario().getName().toUpperCase()));
        } else {
            System.out.println("Não há lugar para retornar.");
        }
    }

    private void showInventory() {
        List<GameObject> inventory = player.getInventory();

        if (inventory.isEmpty()) {
            System.out.println("Seu inventário está vazio.");
        } else {
            System.out.println("Itens no inventário:");
            for (GameObject obj : inventory) {
                System.out.println("- " + obj.getName());
            }
        }
    }

    private void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("savegame.dat"))) {
            oos.writeObject(this);
            System.out.println("Jogo salvo com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o jogo: " + e.getMessage());
        }
    }

    private void loadGame() {
        File saveFile = new File("savegame.dat");
        if (!saveFile.exists()) {
            System.out.println("Não há jogo salvo. Por favor, salve um jogo primeiro.");
            return; // Finaliza o metodo se nenhum arquivo existe.
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            Game loadedGame = (Game) ois.readObject();
            this.player = loadedGame.player;
            this.scenarios = loadedGame.scenarios;
            System.out.println("O jogo carregou com sucesso.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar o jogo: " + e.getMessage());
        }
    }

    public Map<String, Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(Map<String, Scenario> scenarios) {
        this.scenarios = scenarios;
    }
}
