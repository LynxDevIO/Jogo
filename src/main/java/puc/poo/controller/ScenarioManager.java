package puc.poo.controller;

import java.util.HashMap;
import java.util.Map;

import puc.poo.model.GameObject;
import puc.poo.model.ObjectAction;
import puc.poo.model.ObjectAction.ActionType;
import puc.poo.model.ObjectCondition;
import puc.poo.model.Player;
import puc.poo.model.Scenario;
import puc.poo.model.Stag;
import puc.poo.view.ScenarioViews;

/**
 * A classe ScenarioManager é responsável por criar, inicializar e gerenciar vários cenários do jogo.
 * Ela mantém referências para as entidades do jogador e do veado, além de uma coleção de cenários.
 * Ela fornece funcionalidades para inicializar esses cenários com objetos e propriedades, definir saídas para
 * navegação entre cenários e definir o cenário atual para o jogador.
 */
public class ScenarioManager {
    private final Player player;
    private final Stag stag;
    private final Map<String, Scenario> scenarios;

    public ScenarioManager(Player player, Stag stag) {
        this.player = player;
        this.stag = stag;
        this.scenarios = new HashMap<>();
    }

    /// Convenção para nomear objetos:
    ///  <p><b>* nomeDoCenario_nomeDoObjeto</b></p>
    public void initializeScenarios() {
        // CENÁRIO: FLORESTA
        Scenario forest = new Scenario("Floresta", "Você está numa floresta. O ar está frio. Há uma cabana ao norte.", ScenarioViews.forest_scenario());
        scenarios.put(forest.getName(), forest);

        // CENÁRIO: EXTERIOR cabana
        Scenario cabinFront = new Scenario("Exterior da Cabana", "Você está em frente à cabana. Ao sul está a floresta.", ScenarioViews.exteriorCabin_scenario());

        GameObject cabinFront_pot = new GameObject("pote", "Um pote de planta um pouco inclinado");
        cabinFront_pot.setAsStorage(true);

        GameObject cabinFront_note = new GameObject("lembrete", "Preciso caçar o veado na floresta e colocar um troféu dentro desta cabana.");
        cabinFront.addObject(cabinFront_note);

        GameObject cabinFront_key = new GameObject("chave da cabana", "Esta é a chave da cabana. Muito bem escondida por sinal.");
        cabinFront_key.setAsStorable();
        cabinFront_pot.getContents().add(cabinFront_key);

        GameObject cabinFront_door = new GameObject("porta", "Uma porta de madeira.");
        cabinFront_door.setAsOpenable(true);
        cabinFront_door.setKeyId(cabinFront_key.getId());

        cabinFront.addObject(cabinFront_pot);
        cabinFront.addObject(cabinFront_door);
        scenarios.put(cabinFront.getName(), cabinFront);

        // Cenário: INTERIOR cabana
        Scenario insideCabin = new Scenario("Interior da Cabana", "Você está dentro da cabana.", ScenarioViews.interiorCabin_scenario());

        GameObject insideCabin_fireplace = new GameObject("lareira", "Uma lareira acesa.");
        insideCabin_fireplace.setAction(new ObjectAction(player, stag, ActionType.GENERIC) {{
            setActionDescriptionActive("Você se esquenta à lareira.");
            setActionDescriptionInactive("Você já se esquentou.");
            setActive(true);
        }});

        GameObject insideCabin_portrait = new GameObject("retrato", "Um retrato na parede.");
        insideCabin_portrait.setAsStorage(true);

        GameObject insideCabin_chest = new GameObject("baú", "Um baú de madeira.");
        insideCabin_chest.setAsStorage(true);
        insideCabin_chest.setAsOpenable(true);

        GameObject insideCabin_huntingRifle = new GameObject("rifle de caça", "Um rifle de caçador em boas condições.");
        insideCabin_huntingRifle.setAction(new ObjectAction(player, stag, ActionType.RIFLE) {{
            setActionDescriptionActive("Você mira e atira com o rifle.");
            setActionDescriptionInactive("A munição acabou.");
            setRepeatable(true);
            setActionCount(0);
            setActive(true);
        }});

        insideCabin_chest.getContents().add(insideCabin_huntingRifle);

        GameObject insideCabin_key = new GameObject("chave do baú", "Uma chave velha.");
        insideCabin_key.setStorable(true);

        insideCabin_portrait.getContents().add(insideCabin_key);
        insideCabin_chest.setKeyId(insideCabin_key.getId());

        GameObject insideCabin_ammo = new GameObject("munição .22", "Munição calibre .22 para o rifle de caçador.");
        insideCabin_ammo.setAction(new ObjectAction(player, stag, ActionType.AMMO){{
            setCondition(new ObjectCondition());
            setActive(true);
        }});

        insideCabin.addObject(insideCabin_fireplace);
        insideCabin.addObject(insideCabin_portrait);
        insideCabin.addObject(insideCabin_chest);
        insideCabin.addObject(insideCabin_ammo);

        scenarios.put(insideCabin.getName(), insideCabin);

        // Definir as saídas para cada cenário
        forest.addExit("norte", cabinFront); // da floresta para a entrada da cabana
        cabinFront.addExit("sul", forest); // da entrada da cabana de volta para a floresta
        cabinFront.addExit(cabinFront_door.getName(), insideCabin); // da entrada da cabana para dentro dela
        insideCabin.addExit(cabinFront_door.getName(), cabinFront); // de dentro da cabana para a entrada dela

        // Definir o cenário FLORESTA como cenário atual.
        player.setCurrentScenario(forest);
    }
}
