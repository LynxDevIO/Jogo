package puc.poo.model;

import puc.poo.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import static java.io.IO.readln;

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
                String resposta = readln("Deseja fechar o jogo?").toLowerCase();
                if (resposta.equals("sim")) {
                    System.out.println("Fechando jogo...");
                    System.exit(0);
                } else {
                    System.out.println("Ok. Continue a jogar!");
                }
                break;
            case "i": // Inventory command
            case "inventário":
                showInventory();
                break;
            default:
                System.out.println("Não entendo isso.");
        }
    }

    private void goToScenario(String direction) {
        Scenario currentScenario = player.getCurrentScenario();
        Scenario nextScenario = currentScenario.getExit(direction);

        if (nextScenario != null) {
            if (direction.equals("porta") && currentScenario.getObject("porta") != null) {
                GameObject door = currentScenario.getObject("porta");
                if (!door.isOpen()) {
                    System.out.println("A porta está fechada. Você deve destrancá-la primeiro.");
                    return; // Previne passar pela porta ("IR") se ela estiver fechada
                }
            }
            player.setCurrentScenario(nextScenario);
            System.out.println("Você chegou em \"" + nextScenario.getName() + "\".");
        } else {
            System.out.println("Não posso ir ali");
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
                System.out.println("Não entendo isso.");
            }
        }
    }

    private void open(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null) {
            if (obj.getName().equals("porta")) {
                GameObject key = player.getFromInventory("chave");
                    // TODO: Nesses casos, seria melhor obter o tipo do item (por atributo da classe) e seu ID Se o ID da chave for o mesmo que a "fechadura" pede, então a porta abre.
                if (key != null) {
                    obj.setOpen(true); // Fecha e abre a porta
                    System.out.println("Você destrancou e abriu \"" + subject + "\".");
                } else {
                    System.out.println("A porta está fechada. Você precisa de um chava para abri-la.");
                }
            } else if (obj.isOpenable()) {
                obj.setOpen(true);
                System.out.println("Você abriu \"" + subject + "\".");
            } else {
                System.out.println("Não posso abrir isso.");
            }
        } else {
            System.out.println("Não entendo isso.");
        }
    }

    private void close(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null && obj.isOpenable() && obj.isOpen()) {
            obj.setOpen(false);
            System.out.println("Você fechou \"" + subject + "\".");
        } else {
            System.out.println("Você não pode fechar isso.");
        }
    }

    private void take(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null && obj.isStorable()) {
            player.addToInventory(obj);
            player.getCurrentScenario().removeObject(subject);
            System.out.println("Você pegou \"" + subject + "\".");
        } else {
            System.out.println("Você não pode pegar isso.");
        }
    }

    private void drop(String subject) {
        GameObject obj = player.getFromInventory(subject);
        if (obj != null) {
            player.removeFromInventory(subject);
            player.getCurrentScenario().addObject(obj);
            // TODO: Precisamos adicionar uma condição aqui e talvez modificar a classe do objeto para guardar o cenário original de um item, de modo que, quando ele for solto (DROP) pelo jogador, voltará ao cenário original OU não poderá ser solto no cenário diferente
            System.out.println("Você soltou \"" + subject + "\".");
        } else {
            System.out.println("Você não tem isso.");
        }
    }

    private void use(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        // Esse metodo é específico para um cenário.
        // TODO: Precisamos refatorá-lo pra permitir mais interações com itens e facilidade para configurar isso
        if (obj != null) {
            switch (obj.getName()) {
                case "pote" -> {
                    System.out.println("Você encontrou uma chave de baixo do pote!");
                    GameObject key = new GameObject("chave", "Uma chave enferrujada.", 0, 0, true);
                    player.addToInventory(key);
                    player.getCurrentScenario().removeObject("pote"); // Remove o pote após o uso
                }

                // TODO: Talvez devessemos adicionar uma lista de status do jogador como faminto, com frio etc
                case "fireplace" -> System.out.println("Você usou a lareira para se esquentar.");

                case "portrait" -> {
                    System.out.println("Você encontrou uma chave atrás do retrato!");
                    GameObject key = new GameObject("chave", "Uma chave enferrujada..", 0, 0, true);
                    player.addToInventory(key);
                    player.getCurrentScenario().removeObject("retrato");
                }

                default -> System.out.println("Você não pode usar isso.");
            }
        } else {
            System.out.println("Você não tem isso.");
        }
    }

    private void back() {
        if (player.hasPreviousScenario()) {
            player.setCurrentScenario(player.getPreviousScenario());
            System.out.println("Você voltou ao cenário anterior.");
        } else {
            System.out.println("Você não pode voltar mais.");
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
