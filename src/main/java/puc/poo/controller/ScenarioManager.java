package puc.poo.controller;

import puc.poo.model.GameObject;
import puc.poo.model.ObjectAction;
import puc.poo.model.Player;
import puc.poo.model.Scenario;

import java.util.HashMap;
import java.util.Map;

/// Classe responsável pelo gerencimaneto dos cenários.
// Todo: os cenários precisam ser atualizados com a nova lógica!!
public class ScenarioManager {
    private final Player player;
    private final Map<String, Scenario> scenarios;

    public ScenarioManager(Player player) {
        this.player = player;
        this.scenarios = new HashMap<>();
    }

    /// Convenção para nomear objetos:
    ///  <p><b>* nomeDoCenario_nomeDoObjeto</b></p>
    public void initializeScenarios() {
        // CENÁRIO: FLORESTA
        Scenario forest = new Scenario("Floresta", "Você está numa floresta bem fria. Há uma cabine ao norte.", "forest.png");
        scenarios.put(forest.getName(), forest);

        // CENÁRIO: EXTERIOR CABINE
        Scenario cabinFront = new Scenario("Exterior da Cabine", "Você está em frente à cabine.", "cabin_front.png");
        GameObject cabinFront_pot = new GameObject("pote", "Um pote de planta um pouco inclinado");
        cabinFront_pot.setAsStorage(true);

        GameObject cabinFront_key = new GameObject("chave da cabine", "Esta é a chave da cabine. Muito bem escondida por sinal.");
        cabinFront_key.setAsStorable();
        cabinFront_pot.getContents().add(cabinFront_key);

        GameObject cabinFront_door = new GameObject("porta", "Uma porta de madeira.");
        cabinFront_door.setAsOpenable(true);
        cabinFront_door.setKeyId(cabinFront_key.getId());

        cabinFront.addObject(cabinFront_pot);
        cabinFront.addObject(cabinFront_door);
        scenarios.put(cabinFront.getName(), cabinFront);

        // Cenário: INTERIOR CABINE
        Scenario insideCabin = new Scenario("Interior da Cabine", "Você está dentro da cabine.", "inside_cabin.png");

        GameObject insideCabin_fireplace = new GameObject("lareira", "Uma lareira acesa.");
        insideCabin_fireplace.setAction(new ObjectAction(){{
            setActionDescriptionActive("Você se esquenta à lareira.");
            setActionDescriptionInactive("Você já se esquentou.");
            setActive(true);
        }});

        GameObject insideCabin_portrait = new GameObject("retrato", "Um retrato na parede.");
        insideCabin_portrait.setAsStorage(true);

        GameObject insideCabin_chest = new GameObject("baú", "Um baú de madeira.");
        insideCabin_chest.setAsStorage(true);
        insideCabin_chest.setAsOpenable(true);

        GameObject insideCabin_key = new GameObject("chave", "Uma chave velha.");
        insideCabin_key.setStorable(true);
        insideCabin_chest.getContents().add(cabinFront_key);

        insideCabin.addObject(insideCabin_fireplace);
        insideCabin.addObject(insideCabin_portrait);
        insideCabin.addObject(insideCabin_chest);

        scenarios.put(insideCabin.getName(), insideCabin);

        // Definir as saídas para cada cenário
        forest.addExit("norte", cabinFront); // da floresta para a entrada da cabine
        cabinFront.addExit("sul", forest); // da entrada da cabine de volta para a floresta
        cabinFront.addExit(cabinFront_door.getName(), insideCabin); // da entrada da cabine para dentro dela
        insideCabin.addExit(cabinFront_door.getName(), cabinFront); // de dentro da cabine para a entrada dela

        // Definir o cenário FLORESTA como cenário atual.
        player.setCurrentScenario(forest);
    }
}
