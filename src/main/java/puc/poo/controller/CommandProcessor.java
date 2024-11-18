package puc.poo.controller;

import puc.poo.model.Stag;
import puc.poo.model.GameObject;
import puc.poo.model.Player;
import puc.poo.model.Scenario;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.io.IO.readln;

/**
 * A classe `CommandProcessor` lida com comandos textuais para interagir com o ambiente do jogo.
 * Ela conecta a entrada do usuário com as ações do jogo, permitindo que os jogadores naveguem
 * por cenários, manipulem objetos e executem tarefas no jogo usando comandos textuais simples.
 */
public class CommandProcessor {
    private Player player;
    private Stag stag;
    private Map<String, Scenario> scenarios;

    /**
     * Construtor da classe `CommandProcessor`.
     *
     * @param player    O jogador que está interagindo com o ambiente do jogo.
     * @param scenarios Um mapa de cenários, onde a chave é o nome do cenário e o valor é o objeto `Scenario`.
     */
    public CommandProcessor(Player player, Map<String, Scenario> scenarios) {
        this.player = player;
        this.scenarios = scenarios;
    }

    /**
     * Processa um comando textual fornecido pelo usuário.
     *
     * @param command O comando textual a ser processado.
     */
    public void processCommand(String command) {
        String[] parts = command.split(" ");
        String action = parts[0];
        String subject;
        if (parts.length > 1) {
            if (parts[1].contains(":")) {
                subject = parts[1].split(":")[1];
            } else {
                subject = parts[1];
            }
        } else {
            subject = null;
        }

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
                System.out.println("Comando de sistema: sair");
                break;
            default:
                System.out.println("Não entendo isso.");
        }
    }

    /**
     * Move o jogador para um cenário específico com base na entrada fornecida.
     *
     * @param input A entrada que especifica para qual cenário o jogador deve se mover.
     */
    private void goToScenario(String input) {
        Scenario currentScenario = player.getCurrentScenario();
        Set<String> cardinals = Set.of("norte", "sul", "leste", "oeste");

        if (input != null) {
            // Verificar se a entrada é uma direção cardinal
            if (cardinals.contains(input)) {
                Scenario nextScenario = currentScenario.getExit(input);

                // Verificar se existe um cenário nessa direção
                if (nextScenario != null) {
                    player.setCurrentScenario(nextScenario);
                    System.out.println(player.getCurrentScenario().getImagePath());
                    System.out.println("Você chegou em \"" + nextScenario.getName().toUpperCase() + "\".");
                    try {
                        Thread.sleep(1000);
                        look(null);
                    } catch (Exception e) {}
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
                            System.out.println(player.getCurrentScenario().getImagePath());
                            System.out.println("Você chegou em \"" + nextScenario.getName() + "\".");
                            try {
                                Thread.sleep(1000);
                                look(null);
                            } catch (Exception e) {}
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
        } else {
            System.out.println("Preciso de um lugar para entrar...");
        }
    }

    /**
     * Descreve o assunto especificado, permitindo que o jogador "olhe" em volta.
     *
     * @param subject O assunto a ser descrito.
     */
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

    /**
     * Abre um objeto específico no jogo.
     *
     * @param subject O objeto a ser aberto.
     */
    private void open(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null) {
            obj.unlock(player.getInventory());
        } else {
            System.out.println("Não entendo \"%s\".".formatted(subject.toUpperCase()));
        }
    }

    /**
     * Fecha um objeto específico no jogo.
     *
     * @param subject O objeto a ser fechado.
     */
    private void close(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null) {
            obj.lock(player.getInventory());
        } else {
            System.out.println("Não entendo \"%s\".".formatted(subject.toUpperCase()));
        }
    }

    /**
     * Permite que o jogador pegue um objeto específico no jogo.
     *
     * @param subject O objeto a ser pego.
     */
    private void take(String subject) {
        GameObject obj = player.getCurrentScenario().getObject(subject);
        if (obj != null) {
            if (obj.isStorable()) {
                player.addToInventory(obj);
                player.getCurrentScenario().removeObject(obj);
                System.out.println("Você pegou \"" + obj.getName().toUpperCase() + "\".");
            } else {
                System.out.println("Você não pode pegar isso.");
            }
        } else {
            System.out.println("Não entendo \"%s\".".formatted(subject).toUpperCase());
        }
    }

    /**
     * Permite que o jogador largue um objeto específico no jogo.
     *
     * @param subject O objeto a ser largado.
     */
    private void drop(String subject) {
        GameObject obj = player.getFromInventory(subject);
        if (obj != null) {
            player.removeFromInventory(obj);
            player.getCurrentScenario().addObject(obj);
            System.out.println("Você soltou \"%s\".".formatted(obj.getName()));

            // Se o objeto "Chifres do Veado" for solto no cenário "Interior da Cabana"
            // o jogador completa o jogo e ele cecha.
            if (obj.getName().equals("chifres do veado")) {
                if (player.getCurrentScenario().getName().equals("Interior da Cabana")) {
                    System.out.println("Parabéns! Você completou o jogo!");
                    System.exit(0);
                } else {
                    System.out.println("Não devo soltar isso aqui.");
                }
            }
        } else {
            System.out.println("Você não possui \"%s\" no inventário.".formatted(subject));
        }
    }

    /**
     * Usa um objeto específico no jogo. Se o objeto tiver uma ação, esta será executada.
     * Caso contrário, se ele armazena objetos, a ação será de obter o item.
     *
     * @param subject O objeto a ser usado.
     */
    private void use(String subject) {
        // Primeiro, tenta encontrar o objeto no cenário atual
        GameObject obj = player.getCurrentScenario().getObject(subject);
        
        // Se não encontrar no cenário, tenta no inventário
        if (obj == null) {
            obj = player.getFromInventory(subject);
        }

        // Se o objeto não estiver vazio
        if (obj != null) {
            // Se o objeto tiver ação
            if (obj.hasAction()) {
                obj.getAction().execute();
                if (!obj.getAction().isRepeatable) {
                    obj.getAction().setActive(false);
                }
            } else {
                // Se o objeto não tiver ação
                // mas for do tipo armazenamento
                if (obj.isStorage()) {
                    if (!obj.getContents().isEmpty()) {
                        System.out.println("Há alguma coisa dentro desse objeto...");
                        System.out.println("Você colocou no seu inventário:");
                        obj.getContents().forEach(e -> {
                            player.addToInventory(e);
                            System.out.println("- \"%s\"".formatted(e.getName().toUpperCase()));
                        });
                        obj.getContents().clear();
                    } else {
                        System.out.println("Este objeto está vazio.");
                    }
                } else {
                    // Caso contrário...
                    System.out.println("Esse item não pode ser usado.");
                }
            }
        } else {
            System.out.println("Parece não haver \"%s\" ao redor.".formatted(subject.toUpperCase()));
        }
    }

    /**
     * Permite que o jogador volte para o cenário anterior.
     */
    private void back() {
        if (player.hasPreviousScenario()) {
            player.setCurrentScenario(player.getPreviousScenario());
            System.out.println(player.getCurrentScenario().getImagePath());
            System.out.println("Você voltou a \"%s\".".formatted(player.getCurrentScenario().getName().toUpperCase()));
        } else {
            System.out.println("Não há lugar para retornar.");
        }
    }

    /**
     * Mostra o inventário atual do jogador.
     */
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

    /**
     * Retorna o mapa de cenários.
     *
     * @return O mapa de cenários.
     */
    public Map<String, Scenario> getScenarios() {
        return scenarios;
    }

    /**
     * Define o mapa de cenários.
     *
     * @param scenarios O novo mapa de cenários a ser definido.
     */
    public void setScenarios(Map<String, Scenario> scenarios) {
        this.scenarios = scenarios;
    }
}
