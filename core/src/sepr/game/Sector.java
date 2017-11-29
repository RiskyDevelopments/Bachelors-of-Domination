package sepr.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Dom's Surface Mark 2 on 16/11/2017.
 */
public class Sector {
    private int id;
    private int ownerId;
    private String displayName;
    private int unitsInSector;
    private int reinforcementsProvided;
    private int[] adjacentSectorIds; // <-- May want to reconsider structure
    private Texture sectorTexture;
    private Pixmap sectorPixmap;
    private int sectorCentreX;
    private int sectorCentreY;
    private boolean decor; // is this sector for visual purposes only, i.e. lakes are decor

    public Sector(int id, int ownerId, String displayName, int unitsInSector, int reinforcementsProvided, int[] adjacentSectorIds, Texture sectorTexture, Pixmap sectorPixmap, int sectorCentreX, int sectorCentreY, boolean decor) {
        this.id = id;
        this.ownerId = ownerId;
        this.displayName = displayName;
        this.unitsInSector = unitsInSector;
        this.reinforcementsProvided = reinforcementsProvided;
        this.adjacentSectorIds = adjacentSectorIds;
        this.sectorTexture = sectorTexture;
        this.sectorPixmap = sectorPixmap;
        this.sectorCentreX = sectorCentreX;
        this.sectorCentreY = sectorCentreY;
        this.decor = decor;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getReinforcementsProvided() {
        return reinforcementsProvided;
    }

    public int getUnitsInSector() {
        return unitsInSector;
    }

    public int[] getAdjacentSectorIds() {
        return adjacentSectorIds;
    }

    public Texture getSectorTexture() {
        return sectorTexture;
    }

    public void setSectorTexture(Texture sectorTexture) {
        this.sectorTexture = sectorTexture;
    }

    public Pixmap getSectorPixmap() {
        return sectorPixmap;
    }

    public int getSectorCentreX() {
        return sectorCentreX;
    }

    public int getSectorCentreY() {
        return sectorCentreY;
    }

    /**
     * Changes the number of units in this sector
     * If there are 0 units in sector then ownerId should be -1 (neutral)
     * @throws IllegalArgumentException if units in sector is below 0
     * @param amount number of units to change by, (can be negative to subtract units
     */
    public void addUnits(int amount) {
        this.unitsInSector += amount;
    }
}
