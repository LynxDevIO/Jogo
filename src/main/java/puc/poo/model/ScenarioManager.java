package puc.poo.model;

import java.util.HashMap;
import java.util.Map;

public class ScenarioManager {
    private final Player player;
    private final Map<String, Scenario> scenarios;

    public ScenarioManager(Player player) {
        this.player = player;
        this.scenarios = new HashMap<>();
    }

    public void initializeScenarios() {
        // CENÁRIO: FLORESTA
        Scenario forest = new Scenario("Floresta", "Você está numa floresta bem fria. Há uma cabine ao norte.", "forest.png");
        scenarios.put(forest.getName(), forest);

        // CENÁRIO: EXTERIOR_CABINE
        Scenario cabinFront = new Scenario("Exterior da Cabine", "Você está em frente à cabine.", "cabin_front.png");
        GameObject pot = new GameObject("pote", "Um pote de planta um pouco inclinado", 0, 0, true);
        cabinFront.addObject(pot);
        GameObject door = new GameObject("porta", "Uma porta de madeira.", 0, 0, false);
        cabinFront.addObject(door);
        scenarios.put(cabinFront.getName(), cabinFront);

        // Cenário: INTERIOR_CABINE
        Scenario insideCabin = new Scenario("Interior da Cabine", "Você está dentro da cabine.", "inside_cabin.png");
        GameObject fireplace = new GameObject("lareira", "Uma lareira acesa.", 0, 0, true);
        GameObject portrait = new GameObject("retrato", "Um retrato na parede.", 0, 0, true);
        GameObject chest = new GameObject("baú", "Um baú de madeira.", 0, 0, true);
        GameObject key = new GameObject("chave", "Uma chave velha.", 0, 0, true);

        // Adicionar os objetos aos cenários
        insideCabin.addObject(fireplace);
        insideCabin.addObject(portrait);
        insideCabin.addObject(chest);
        portrait.addContainedObject(key); // a CHAVE está dentro do RETRATO (inicialmente)

        scenarios.put(insideCabin.getName(), insideCabin);

        // Definir as saídas para cada cenário
        forest.addExit("norte", cabinFront); // da floresta para a entrada da cabine
        cabinFront.addExit("sul", forest); // da entrada da cabine de volta para a floresta
        cabinFront.addExit("porta", insideCabin); // da entrada da cabine para dentro dela
        insideCabin.addExit("porta", cabinFront); // de dentro da cabine para a entrada dela

        // Definir o cenário FLORESTA como cenário atual.
        player.setCurrentScenario(forest);
    }
}
