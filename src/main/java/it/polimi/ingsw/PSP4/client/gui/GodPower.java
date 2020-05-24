package it.polimi.ingsw.PSP4.client.gui;

public enum GodPower {
    DEFAULT(""),
    APOLLO("Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."),
    ARTEMIS("Your Move: Your Worker may move one additional time, but not back to its initial space."),
    ATHENA("Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."),
    ATLAS("Your Build: Your Worker may build a dome at any level."),
    DEMETER("Your Build: Your Worker may build one additional time, but not on the same space."),
    HEPHAESTUS("Your Build: Your Worker may build one additional block (not dome) on top of your first block."),
    MINOTAUR("Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space to an unoccupied space at any level."),
    PAN("Win Condition: You also win if your Worker moves down two or more levels."),
    PROMETHEUS("Your Turn: If your Worker does not move up, it may build both before and after moving.");

    private final String description;

    GodPower(String description) { this.description = description; }

    public String getDescription() { return description; }

    public static GodPower getPowerFromGod(String god) {
        return GodPower.valueOf(god.toUpperCase());
    }
}
