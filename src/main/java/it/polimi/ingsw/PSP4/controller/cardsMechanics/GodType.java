package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum GodType {
    DEFAULT ("Default"),
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

    public GameMechanics getGameMechanics(ArrayList<Player> players) {
        switch (this) {
            case DEFAULT:
                return new DefaultGameMechanics();
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

    public PathType getPathType() {
        switch (this) {
            case DEFAULT:
            case APOLLO:
            case ATHENA:
            case ATLAS:
            case HEPHAESTUS:
            case MINOTAUR:
            case PAN:
                return PathType.DEFAULT;
            case ARTEMIS:
                return PathType.DOUBLE_MOVE;
            case DEMETER:
                return PathType.DOUBLE_BUILD;
            case PROMETHEUS:
                return PathType.EARLY_BUILD;
            default:
                System.out.println("Not a valid god");
                return PathType.DEFAULT;
        }
    }

    public static List<String> getImplementedGodsList() {
        List<String> godNames = Stream.of(GodType.values()).map(GodType::toString).collect(Collectors.toList());
        godNames.remove("DEFAULT");
        return godNames;
    }
}
