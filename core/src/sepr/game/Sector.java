package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * Created by Dom's Surface Mark 2 on 16/11/2017
 */
public class Sector {
    private int id;
    private int ownerId;
    private String displayName;
    private int unitsInSector;
    private int reinforcementsProvided;
    private String college;
    private boolean neutral;
    private int[] adjacentSectorIds; // <-- May want to reconsider structure
    private Texture sectorTexture;
    private Pixmap sectorPixmap;
    private int sectorCentreX;
    private int sectorCentreY;
    private boolean decor; // is this sector for visual purposes only, i.e. lakes are decor
    private String fileName;
    private boolean allocated; // becomes true once the sector has been allocated

    /**
     * @param id sector id
     * @param ownerId id of player who owns sector
     * @param displayName sector display name
     * @param unitsInSector number of units in sector
     * @param reinforcementsProvided number of reinforcements the sector provides
     * @param college unique id of the college this sector belongs to
     * @param adjacentSectorIds ids of adjacent sectors
     * @param sectorTexture sector texture from assets
     * @param sectorPixmap pixmap of sector texture
     * @param fileName sector filename
     * @param sectorCentreX xcoord of sector centre
     * @param sectorCentreY ycoord of sector centre
     * @param decor false if a sector is accessible to a player and true if sector is decorative
     */
    public Sector(int id, int ownerId, String fileName, Texture sectorTexture, Pixmap sectorPixmap, String displayName, int unitsInSector, int reinforcementsProvided, String college, boolean neutral, int[] adjacentSectorIds, int sectorCentreX, int sectorCentreY, boolean decor) {
        this.id = id;
        this.ownerId = ownerId;
        this.displayName = displayName;
        this.unitsInSector = unitsInSector;
        this.reinforcementsProvided = reinforcementsProvided;
        this.college = college;
        this.neutral = neutral;
        this.adjacentSectorIds = adjacentSectorIds;
        this.sectorTexture = sectorTexture;
        this.sectorPixmap = sectorPixmap;
        this.sectorCentreX = sectorCentreX;
        this.sectorCentreY = 1080 - sectorCentreY;
        this.decor = decor;
        this.fileName = fileName;
        this.allocated = false;
    }

    public int getId() { return id; }

    public int getOwnerId() {
        return ownerId;
    }

    /**
     * sets the owner id and colour of this sector
     * @param player the player object that owns this sector
     */
    public void setOwner(Player player) {
        this.ownerId = player.getId();
        this.changeSectorColor(player.getSectorColour());
        this.allocated = true;
    }

    public void setOwner(int playerId){
        this.ownerId = playerId;
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

    public Texture getSectorTexture() {
        return sectorTexture;
    }

    /**
     * Sets the new texture for a sector
     * @param newPixmap the memory representation of the textures pixels
     */
    private void setNewSectorTexture(Pixmap newPixmap) {
        this.sectorTexture.dispose();
        Texture temp = new Texture(newPixmap);
        this.sectorTexture = temp;
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
     * @return boolean value to check whether sector is decorative
     */
    public boolean isDecor() {
        return decor;
    }

    public boolean isNeutral() { return neutral; }

    public boolean isAllocated() {
        return allocated;
    }

    public String getCollege() { return college; }

    public String getFileName() {
        return fileName;
    }

    /**
     * Function to check if a given sector is adjacent
     * @param toCheck The sector object to check
     * @return True/False
     */
    public boolean isAdjacentTo(Sector toCheck) {
        for (int adjacent : this.adjacentSectorIds) {
            if (adjacent == toCheck.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the number of units in this sector
     * If there are 0 units in sector then ownerId should be -1 (neutral)
     * @throws IllegalArgumentException if units in sector is below 0
     * @param amount number of units to change by, (can be negative to subtract units
     */
    public void addUnits(int amount) throws IllegalArgumentException {
        this.unitsInSector += amount;
        if (this.unitsInSector < 0) {
            this.unitsInSector = 0;
            throw new IllegalArgumentException("Cannot have less than 0 units on a sector");
        }
    }

    /**
     * The method takes a sectorId and recolors it to the specified color
     * @param newColor what color the sector be changed to
     * @throws RuntimeException if attempt to recolor a decor sector
     */
    private void changeSectorColor(Color newColor){
        if (this.isDecor()) {
            throw new RuntimeException("Should not recolour decor sector");
        }

        Pixmap newPix = new Pixmap(Gdx.files.internal(this.getFileName())); // pixmap for drawing updated sector texture to
        for (int x = 0; x < this.getSectorPixmap().getWidth(); x++){
            for (int y = 0; y < this.getSectorPixmap().getHeight(); y++){
                if(newPix.getPixel(x, y) != -256){
                    Color tempColor = new Color(0,0,0,0);
                    Color.rgba8888ToColor(tempColor, newPix.getPixel(x, y)); // get the pixels current color
                    tempColor.sub(new Color(Color.WHITE).sub(newColor)); // calculate the new color of the pixel
                    newPix.drawPixel(x, y, Color.rgba8888(tempColor));  // draw the modified pixel value to the new pixmap
                }
            }
        }
        this.setNewSectorTexture(newPix); // draw the generated pixmap to the new texture
        newPix.dispose();
    }
}
