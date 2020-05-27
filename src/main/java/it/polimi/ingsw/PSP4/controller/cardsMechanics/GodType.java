package it.polimi.ingsw.PSP4.controller.cardsMechanics;

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
    HERA("Hera"),
    HESTIA("Hestia"),
    MINOTAUR ("Minotaur"),
    PAN ("Pan"),
    PROMETHEUS ("Prometheus"),
    TRITON("Triton"),
    ZEUS("Zeus");

    public String getName() {
        return name;
    }

    private final String name;

    GodType(String name) {
        this.name = name;
    }

    public GameMechanics getGameMechanics() {
        switch (this) {
            case APOLLO:
                return new ApolloGameMechanics(new DefaultGameMechanics());
            case ARTEMIS:
                return new ArtemisGameMechanics(new DefaultGameMechanics());
            case ATHENA:
                return new AthenaGameMechanics(new DefaultGameMechanics());
            case ATLAS:
                return new AtlasGameMechanics(new DefaultGameMechanics());
            case DEMETER:
                return new DemeterGameMechanics(new DefaultGameMechanics());
            case HEPHAESTUS:
                return new HephaestusGameMechanics(new DefaultGameMechanics());
            case HERA:
                return new HeraGameMechanics(new DefaultGameMechanics());
            case HESTIA:
                return new HestiaGameMechanics(new DefaultGameMechanics());
            case MINOTAUR:
                return new MinotaurGameMechanics(new DefaultGameMechanics());
            case PAN:
                return new PanGameMechanics(new DefaultGameMechanics());
            case PROMETHEUS:
                return new PrometheusGameMechanics(new DefaultGameMechanics());
            case TRITON:
                return new TritonGameMechanics(new DefaultGameMechanics());
            case ZEUS:
                return new ZeusGameMechanics(new DefaultGameMechanics());
            default:
                return new DefaultGameMechanics();
        }
    }

    public PathType getPathType() {
        switch (this) {
            case ARTEMIS:
                return PathType.DOUBLE_MOVE;
            case DEMETER:
            case HESTIA:
                return PathType.DOUBLE_BUILD;
            case PROMETHEUS:
                return PathType.EARLY_BUILD;
            default:
                return PathType.DEFAULT;
        }
    }

    public static List<String> getImplementedGodsList() {
        List<String> godNames = Stream.of(GodType.values()).map(GodType::toString).collect(Collectors.toList());
        godNames.remove("DEFAULT");
        return godNames;
    }
}
