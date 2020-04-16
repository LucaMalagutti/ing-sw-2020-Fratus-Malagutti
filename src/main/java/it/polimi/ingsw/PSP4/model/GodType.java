package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.*;

import java.util.ArrayList;

public enum GodType {
    APOLLO ("Apollo"),
    ARTEMIS ("Artemis"),
    ATHENA ("Athena"),
    ATLAS ("Atlas"),
    DEMETER ("Demeter"),
    HEPHAESTUS ("Hephaestus"),
    MINOTAUR ("Minotaur"),
    PAN ("Pan"),
    PROMETHEUS ("Prometheus");

    public String getName() {
        return name;
    }

    private final String name;

    GodType(String name) {
        this.name = name;
    }

    public GameMechanics getGameMechanics(GodType god, ArrayList<Player> players) {
        switch (god) {
            case APOLLO:
                return new ApolloGameMechanics(new DefaultGameMechanics());
            case ARTEMIS:
                return new ArtemisGameMechanics(new DefaultGameMechanics());
            case ATHENA:
                return new AthenaGameMechanics(new DefaultGameMechanics(), players);
            case ATLAS:
                return new AtlasGameMechanics(new DefaultGameMechanics());
            case DEMETER:
                return new DemeterGameMechanics(new DefaultGameMechanics());
            case HEPHAESTUS:
                return new HephaestusGameMechanics(new DefaultGameMechanics());
            case MINOTAUR:
                return new MinotaurGameMechanics(new DefaultGameMechanics());
            case PAN:
                return new PanGameMechanics(new DefaultGameMechanics());
            case PROMETHEUS:
                return new PrometheusGameMechanics(new DefaultGameMechanics());
            default:
                System.out.println("Not a valid god");
                return new DefaultGameMechanics();
        }
    }
}
