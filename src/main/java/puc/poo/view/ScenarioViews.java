package puc.poo.view;

/**
 * A classe ScenarioViews contém representações em arte ASCII de diferentes cenários do jogo.
 */

// Observação: seguir o padrão da documentação de cada cenário para novos cenários.
public class ScenarioViews {

    /**
     * Retorna a representação ASCII do cenário da floresta.
     *
     * @return String representando o cenário da floresta.
     */
    public static String forest_scenario() {
        return "Forest Scenario";
    }

    /**
     * Retorna a representação ASCII do cenário do exterior da cabana.
     *
     * @return String representando o cenário do exterior da cabana.
     */
    public static String exteriorCabin_scenario() {
        return "Exterior Cabin Scenario";
    }

    /**
     * Retorna a representação ASCII do cenário do interior da cabana.
     *
     * @return String representando o cenário do interior da cabana.
     */
    public static String interiorCabin_scenario() {
        return "Interior Cabin Scenario";
    }
}