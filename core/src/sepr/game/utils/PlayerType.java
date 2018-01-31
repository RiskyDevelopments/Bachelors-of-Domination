package sepr.game.utils;

/**
 * Possible types of player
 */
public enum PlayerType {
    NONE("NONE"),
    HUMAN("HUMAN PLAYER"),
    NEUTRAL_AI("NEUTAL A.I.");

    private final String shortCode;

    PlayerType(String code){
        this.shortCode = code;
    }

    public String getPlayerType(){
        return this.shortCode;
    }

    /**
     * converts the string representation of the enum to the enum value
     * @throws IllegalArgumentException if the text does not match any of the enum's string values
     * @param text string representation of the enum
     * @return the enum value of the provided text
     */
    public static PlayerType fromString(String text) throws IllegalArgumentException {
        for (PlayerType playerType : PlayerType.values()) {
            if (playerType.getPlayerType().equals(text)) return playerType;
        }
        throw new IllegalArgumentException("Text parameter must match one of the enums");
    }

    @Override
    public String toString() {
        return this.shortCode;
    }
}