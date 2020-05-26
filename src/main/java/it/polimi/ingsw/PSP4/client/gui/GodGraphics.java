package it.polimi.ingsw.PSP4.client.gui;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public enum GodGraphics {
    DEFAULT(""),
    APOLLO("Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."),
    ARTEMIS("Your Move: Your Worker may move one additional time, but not back to its initial space.", "Skip: skip-move.png"),
    ATHENA("Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.", "Wrapper: no-way-up.png"),
    ATLAS("Your Build: Your Worker may build a dome at any level.", "Confirm: build-dome.png, build-block.png"),
    DEMETER("Your Build: Your Worker may build one additional time, but not on the same space.", "Skip: skip-build.png"),
    HEPHAESTUS("Your Build: Your Worker may build one additional block (not dome) on top of your first block.", "Confirm: build-twice.png, build-once.png"),
    HESTIA("Your Build: Your Worker may build one additional time, but this cannot be on a perimeter space.", "Skip: skip-build.png"),
    MINOTAUR("Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space to an unoccupied space at any level."),
    PAN("Win Condition: You also win if your Worker moves down two or more levels."),
    PROMETHEUS("Your Turn: If your Worker does not move up, it may build both before and after moving.", "Skip: skip-build.png"),
    TRITON("Your Move: Each time your Worker moves into a perimeter space, it may immediately move again.", "Skip: skip-move.png"),
    ZEUS("Your Build: Your Worker may build a block under itself.");

    private static final String imagesPath = "/images/";
    private static final String godCardPath = imagesPath + "godCards/fullGodCards/full_{0}.png";
    private static final String selectablePath = imagesPath + "godCards/god_card_{0}.png";
    private static final String podiumPath = imagesPath + "godCards/godPodium/podium-{0}.png";
    private static final String godPowerPath = imagesPath + "godPowers/{0}_power.png";
    private static final String buttonPath = imagesPath + "buttons/{0}";

    private static final String bgImageProperty = "-fx-background-image: url(\"{0}\")";

    private final String description;
    private final String skipButton;
    private final String wrapperButton;
    private final String confirmButtons;

    GodGraphics(String description) {
        this.description = description;
        this.skipButton = "";
        this.wrapperButton = "";
        this.confirmButtons = "";
    }

    GodGraphics(String description, String buttonString) {
        this.description = description;
        String skip = "", wrapper = "", confirm = "";
        String[] buttons = buttonString.split("\\|");
        for(String button : buttons) {
            String[] value = button.split(": ");
            switch(value[0].toLowerCase()) {
                case "skip": skip = value[1]; break;
                case "wrapper": wrapper = value[1]; break;
                case "confirm": confirm = value[1]; break;
            }
        }
        this.skipButton = skip;
        this.wrapperButton = wrapper;
        this.confirmButtons = confirm;
    }

    public String getDescription() { return description; }

    public static GodGraphics getDescriptionFromGod(String god) {
        return GodGraphics.valueOf(god.toUpperCase());
    }

    private static GodGraphics getGraphicsFromGod(String god) { return valueOf(god.toUpperCase()); }

    private static String getImageUrl(String path) {
        return GodGraphics.class.getResource(path).toString();
    }
    private static String getBgImageProperty(String path) {
        return MessageFormat.format(bgImageProperty, getImageUrl(path));
    }

    public static String getGodCardBG(String god) {
        return getBgImageProperty(MessageFormat.format(godCardPath, god.toLowerCase()));
    }
    public static String getSelectableBG(String god) {
        return getBgImageProperty(MessageFormat.format(selectablePath, god.toLowerCase()));
    }
    public static String getPodiumBG(String god) {
        return getBgImageProperty(MessageFormat.format(podiumPath, god.toLowerCase()));
    }
    public static String getGodPowerBG(String god) {
        return getBgImageProperty(MessageFormat.format(godPowerPath, god.toLowerCase()));
    }
    public static String getSkipButtonBG(String god) {
        GodGraphics self = getGraphicsFromGod(god);
        return getBgImageProperty(MessageFormat.format(buttonPath, self.skipButton));
    }
    public static String getWrapperButtonBG(String god) {
        GodGraphics self = getGraphicsFromGod(god);
        return getBgImageProperty(MessageFormat.format(buttonPath, self.wrapperButton));
    }
    public static Map<Boolean, String> getConfirmButtonsBG(String god) {
        GodGraphics self = getGraphicsFromGod(god);
        Map<Boolean, String> buttons = new LinkedHashMap<>();
        String[] images = self.confirmButtons.split(", ");
        buttons.put(true, getBgImageProperty(MessageFormat.format(buttonPath, images[0])));
        buttons.put(false, getBgImageProperty(MessageFormat.format(buttonPath, images[1])));
        return buttons;
    }
}
