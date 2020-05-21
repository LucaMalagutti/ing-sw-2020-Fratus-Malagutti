package it.polimi.ingsw.PSP4.client.gui;

public enum GodPower {
    DEFAULT(""),
    APOLLO(""),
    ARTEMIS(""),
    ATHENA(""),
    ATLAS(""),
    DEMETER(""),
    HEPHAESTUS(""),
    MINOTAUR(""),
    PAN(""),
    PROMETHEUS("");

    private final String description;

    GodPower(String description) { this.description = description; }

    public String getDescription() { return description; }

    public static GodPower getPowerFromGod(String god) {
        return GodPower.valueOf(god.toUpperCase());
    }
}
