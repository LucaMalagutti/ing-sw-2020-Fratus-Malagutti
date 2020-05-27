package it.polimi.ingsw.PSP4.client.gui;

import java.net.URL;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains all the information about the graphic related to a god
 */
public enum GodGraphics {
    DEFAULT(""),
    APOLLO("Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated."),
    ARTEMIS("Your Move: Your Worker may move one additional time, but not back to its initial space.", "move"),
    ATHENA("Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."),
    ATLAS("Your Build: Your Worker may build a dome at any level."),
    DEMETER("Your Build: Your Worker may build one additional time, but not on the same space.", "build"),
    HEPHAESTUS("Your Build: Your Worker may build one additional block (not dome) on top of your first block."),
    HERA("Opponent’s Turn: An opponent cannot win by moving into a perimeter space."),
    HESTIA("Your Build: Your Worker may build one additional time, but this cannot be on a perimeter space.", "build"),
    MINOTAUR("Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space to an unoccupied space at any level."),
    PAN("Win Condition: You also win if your Worker moves down two or more levels."),
    PROMETHEUS("Your Turn: If your Worker does not move up, it may build both before and after moving.", "build"),
    TRITON("Your Move: Each time your Worker moves into a perimeter space, it may immediately move again.", "move"),
    ZEUS("Your Build: Your Worker may build a block under itself.");

    private static final String imagesPath = "/images/";
    private static final String godCardPath = imagesPath + "godCards/fullGodCards/full_{0}.png";
    private static final String selectablePath = imagesPath + "godCards/god_card_{0}.png";
    private static final String podiumPath = imagesPath + "godCards/godPodium/podium-{0}.png";
    private static final String godPowerPath = imagesPath + "godPowers/{0}_power.png";
    private static final String buttonPath = imagesPath + "buttons/";
    private static final String skipPath = buttonPath + "skip-{0}.png";
    private static final String wrapperPath = buttonPath + "{0}_wrapper.png";
    private static final String confirmPath = buttonPath + "{0}_confirm_{1}.png";

    private static final String bgImageProperty = "-fx-background-image: url(\"{0}\")";

    private final String description;
    private final String skipType;

    GodGraphics(String description) {
        this.description = description;
        this.skipType = "";
    }

    GodGraphics(String description, String skipType) {
        this.description = description;
        this.skipType = skipType;
    }

    public String getDescription() { return description; }

    public static GodGraphics getDescriptionFromGod(String god) {
        return GodGraphics.valueOf(god.toUpperCase());
    }

    private static GodGraphics getGraphicsFromGod(String god) { return valueOf(god.toUpperCase()); }

    private static String getImageUrl(String path) {
        URL imageUrl = GodGraphics.class.getResource(path);
        if(imageUrl != null)
            return imageUrl.toString();
        return "";
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
        return getBgImageProperty(MessageFormat.format(skipPath, self.skipType.toLowerCase()));
    }
    public static String getWrapperButtonBG(String god) {
        return getBgImageProperty(MessageFormat.format(wrapperPath, god.toLowerCase()));
    }
    public static Map<Boolean, String> getConfirmButtonsBG(String god) {
        Map<Boolean, String> buttons = new LinkedHashMap<>();
        buttons.put(true, getBgImageProperty(MessageFormat.format(confirmPath, god.toLowerCase(), "true")));
        buttons.put(false, getBgImageProperty(MessageFormat.format(confirmPath, god.toLowerCase(), "false")));
        return buttons;
    }
}
