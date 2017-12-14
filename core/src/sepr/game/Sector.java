package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Dom's Surface Mark 2 on 16/11/2017.
 */
public class Sector {
    private int id;
    private int ownerId;
    private int prevOwnerId;
    private String displayName;
    private int unitsInSector;
    private int reinforcementsProvided;
    private String adjacentSectorIds; // <-- May want to reconsider structure
    private Texture sectorTexture;
    private Pixmap sectorPixmap;
    private int sectorCentreX;
    private int sectorCentreY;
    private boolean decor; // is this sector for visual purposes only, i.e. lakes are decor
    private String fileName;

    public Sector(int id, int ownerId, String displayName, int unitsInSector, int reinforcementsProvided, String adjacentSectorIds, Texture sectorTexture, Pixmap sectorPixmap, String fileName, int sectorCentreX, int sectorCentreY, boolean decor) {
        this.id = id;
        this.ownerId = ownerId;
        this.prevOwnerId = ownerId;
        this.displayName = displayName;
        this.unitsInSector = unitsInSector;
        this.reinforcementsProvided = reinforcementsProvided;
        this.adjacentSectorIds = adjacentSectorIds;
        this.sectorTexture = sectorTexture;
        this.sectorPixmap = sectorPixmap;
        this.sectorCentreX = sectorCentreX;
        this.sectorCentreY = sectorCentreY;
        this.decor = decor;
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public int getPrevOwnerId() { return prevOwnerId; }

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

    public String getAdjacentSectorIds() {
        return adjacentSectorIds;
    }

    public Texture getSectorTexture() {
        return sectorTexture;
    }

    public void setNewSectorTexture(Pixmap newPixmap) {
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

    public boolean isDecor() {
        return decor;
    }

    public String getFileName() {
        return fileName;
    }

    public void updateOwnerId() { prevOwnerId = ownerId; }

    /**
     * Function to check if a given sector is adjacent
     * @param toCheck The sector object to check
     * @return True/False
     */
   /* public boolean isAdjacentTo(Sector toCheck) {
        for (int adjacent : this.adjacentSectorIds) {
            if (adjacent == toCheck.getId()) {
                return true;
            }
        }
        return false;
    }*/

    /**
     * Changes the number of units in this sector
     * If there are 0 units in sector then ownerId should be -1 (neutral)
     * @throws IllegalArgumentException if units in sector is below 0
     * @param amount number of units to change by, (can be negative to subtract units
     */
    public void addUnits(int amount) {
        this.unitsInSector += amount;
    }


    /**
     * The method takes a sectorId and recolors it to the specified color
     * @param newColor what color the sector be changed to
     * @throws RuntimeException if attempt to recolor a decor sector
     */
    public void changeSectorColor(Color newColor){
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
        //Texture t = new Texture(sector.getSectorPixmap().getWidth(), sector.getSectorPixmap().getHeight(), Pixmap.Format.RGBA8888); // create new texture to represent the sector
        this.setNewSectorTexture(newPix); // draw the generated pixmap to the new texture
        newPix.dispose();
    }
}