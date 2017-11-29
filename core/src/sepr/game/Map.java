package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

/**
 * Created by Dom's Surface Mark 2 on 16/11/2017.
 */
public class Map{
    private HashMap<Integer, Sector> sectors; // mapping of sector ID to the sector object
    private HashMap<String, Color> colors; // mapping of color name to color ***NOT QUITE TRUE***

    Color changeGreen = new Color(0.5f, 0, 1f, 0f);
    Color changeBlue = new Color(0.8f, 0.5f, 0f, 0f);
    Color changeWhite =  new Color(0,0,0,0);

    public Map() {
        this.sectors = new HashMap<Integer, Sector>();

        // hes east
        this.sectors.put(0, new Sector(0, -1, "Hes East 1", 0, 2, new int[]{}, new Texture("hesEast1.png"), new Pixmap(Gdx.files.internal("hesEast1.png")), 0, 0, false));
        this.sectors.put(1, new Sector(1, -1, "Hes East 2", 0, 2, new int[]{}, new Texture("hesEast2.png"), new Pixmap(Gdx.files.internal("hesEast2.png")), 0, 0, false));
        this.sectors.put(2, new Sector(2, -1, "Hes East 3", 0, 2, new int[]{}, new Texture("hesEast3.png"), new Pixmap(Gdx.files.internal("hesEast3.png")), 0, 0, false));
        this.sectors.put(3, new Sector(3, -1, "Hes East 4", 0, 2, new int[]{}, new Texture("hesEast4.png"), new Pixmap(Gdx.files.internal("hesEast4.png")), 0, 0, false));

        // halifax
        this.sectors.put(4, new Sector(4, -1, "Halifax 1", 0, 2, new int[]{}, new Texture("halifax1.png"), new Pixmap(Gdx.files.internal("halifax1.png")), 0, 0, false));
        this.sectors.put(5, new Sector(5, -1, "Halifax 2", 0, 2, new int[]{}, new Texture("halifax2.png"), new Pixmap(Gdx.files.internal("halifax2.png")), 0, 0, false));
        this.sectors.put(6, new Sector(6, -1, "Halifax 3", 0, 2, new int[]{}, new Texture("halifax3.png"), new Pixmap(Gdx.files.internal("halifax3.png")), 0, 0, false));
        this.sectors.put(7, new Sector(7, -1, "Halifax 4", 0, 2, new int[]{}, new Texture("halifax4.png"), new Pixmap(Gdx.files.internal("halifax4.png")), 0, 0, false));

        // derwent
        this.sectors.put(8, new Sector(8, -1, "Derwent 1", 0, 2, new int[]{}, new Texture("derwent1.png"), new Pixmap(Gdx.files.internal("derwent1.png")), 0, 0, false));
        this.sectors.put(9, new Sector(9, -1, "Derwent 2", 0, 2, new int[]{}, new Texture("derwent2.png"), new Pixmap(Gdx.files.internal("derwent2.png")), 0, 0, false));
        this.sectors.put(10, new Sector(10, -1, "Derwent 3", 0, 2, new int[]{}, new Texture("derwent3.png"), new Pixmap(Gdx.files.internal("derwent3.png")), 0, 0, false));
        this.sectors.put(11, new Sector(11, -1, "Derwent 4", 0, 2, new int[]{}, new Texture("derwent4.png"), new Pixmap(Gdx.files.internal("derwent4.png")), 0, 0, false));

        // alcuin
        this.sectors.put(12, new Sector(12, -1, "Alcuin 1", 0, 2, new int[]{}, new Texture("alcuin1.png"), new Pixmap(Gdx.files.internal("alcuin1.png")), 0, 0, false));
        this.sectors.put(13, new Sector(13, -1, "Alcuin 2", 0, 2, new int[]{}, new Texture("alcuin2.png"), new Pixmap(Gdx.files.internal("alcuin2.png")), 0, 0, false));
        this.sectors.put(14, new Sector(14, -1, "Alcuin 3", 0, 2, new int[]{}, new Texture("alcuin3.png"), new Pixmap(Gdx.files.internal("alcuin3.png")), 0, 0, false));

        // vanburgh
        this.sectors.put(15, new Sector(15, -1, "Vanburgh 1", 0, 2, new int[]{}, new Texture("vanbrugh1.png"), new Pixmap(Gdx.files.internal("vanbrugh1.png")), 0, 0, false));
        this.sectors.put(16, new Sector(16, -1, "Vanburgh 2", 0, 2, new int[]{}, new Texture("vanbrugh2.png"), new Pixmap(Gdx.files.internal("vanbrugh2.png")), 0, 0, false));
        this.sectors.put(17, new Sector(17, -1, "Vanburgh 3", 0, 2, new int[]{}, new Texture("vanbrugh3.png"), new Pixmap(Gdx.files.internal("vanbrugh3.png")), 0, 0, false));
        this.sectors.put(18, new Sector(18, -1, "Vanburgh 4", 0, 2, new int[]{}, new Texture("vanbrugh4.png"), new Pixmap(Gdx.files.internal("vanbrugh4.png")), 0, 0, false));

        // wentworth
        this.sectors.put(19, new Sector(19, -1, "Wentworth 1", 0, 2, new int[]{}, new Texture("wentworth1.png"), new Pixmap(Gdx.files.internal("wentworth1.png")), 0, 0, false));
        this.sectors.put(20, new Sector(20, -1, "Wentworth 2", 0, 2, new int[]{}, new Texture("wentworth2.png"), new Pixmap(Gdx.files.internal("wentworth2.png")), 0, 0, false));

        // james
        this.sectors.put(21, new Sector(21, -1, "James 1", 0, 2, new int[]{}, new Texture("james1.png"), new Pixmap(Gdx.files.internal("james1.png")), 0, 0, false));
        this.sectors.put(22, new Sector(22, -1, "James 2", 0, 2, new int[]{}, new Texture("james2.png"), new Pixmap(Gdx.files.internal("james2.png")), 0, 0, false));
        this.sectors.put(23, new Sector(23, -1, "James 3", 0, 2, new int[]{}, new Texture("james3.png"), new Pixmap(Gdx.files.internal("james3.png")), 0, 0, false));
        this.sectors.put(24, new Sector(24, -1, "James 4", 0, 2, new int[]{}, new Texture("james4.png"), new Pixmap(Gdx.files.internal("james4.png")), 0, 0, false));

        //neutral
        this.sectors.put(25, new Sector(25, -1, "Neutral 1", 0, 2, new int[]{}, new Texture("neutral1.png"), new Pixmap(Gdx.files.internal("neutral1.png")), 0, 0, false));
        this.sectors.put(26, new Sector(26, -1, "Neutral 2", 0, 2, new int[]{}, new Texture("neutral2.png"), new Pixmap(Gdx.files.internal("neutral2.png")), 0, 0, false));
        this.sectors.put(27, new Sector(27, -1, "Neutral 3", 0, 2, new int[]{}, new Texture("neutral3.png"), new Pixmap(Gdx.files.internal("neutral3.png")), 0, 0, false));
        this.sectors.put(28, new Sector(28, -1, "Neutral 4", 0, 2, new int[]{}, new Texture("neutral4.png"), new Pixmap(Gdx.files.internal("neutral4.png")), 0, 0, false));
        this.sectors.put(29, new Sector(29, -1, "Neutral 5", 0, 2, new int[]{}, new Texture("neutral5.png"), new Pixmap(Gdx.files.internal("neutral5.png")), 0, 0, false));
        this.sectors.put(30, new Sector(30, -1, "Neutral 6", 0, 2, new int[]{}, new Texture("neutral6.png"), new Pixmap(Gdx.files.internal("neutral6.png")), 0, 0, false));

        // lakes - *SPECIAL CASE*
        this.sectors.put(31, new Sector(31, -1, "Lake 1", 0, 2, new int[]{}, new Texture("lake1.png"), new Pixmap(Gdx.files.internal("lake1.png")), 0, 0, true));
        this.sectors.put(32, new Sector(32, -1, "Lake 2", 0, 2, new int[]{}, new Texture("lake2.png"), new Pixmap(Gdx.files.internal("lake2.png")), 0, 0, true));

        this.colors = new HashMap<String, Color>();
        this.colors.put("green", changeGreen);
        this.colors.put("blue", changeBlue);
        this.colors.put("white", changeWhite);
    }

    /**
     * Checks to see if there is one player who controls every sector
     * @return -1 if there is no winner or the ID of the player that controlls all the sectors
     */
    private int checkForWinner() {
        return -1;
    }

    /**
     * Tranfers units from one sector to another
     * @throws IllegalArgumentException if the sector are not both owned by the same player
     * @throws IllegalArgumentException if the amount exceeds the number of units on the source sector
     * @throws IllegalArgumentException if the sectors are not connected
     * @param sourceSectorId where to move the units from
     * @param targetSectorId where to move the units to
     * @param amount how many units to move
     */
    private void moveUnits(int sourceSectorId, int targetSectorId, int amount) {

    }

    /**
     * calculates how many reinforcements the given player should receive based on the sectors they control by summing reinforcementsProvided for each Sector they control
     * @param playerId player who calculation is for
     * @return returns the amount of reinforcements the player should be allocated
     */
    private int calculateReinforcementAmount(int playerId) {
        return 0;
    }

    public void detectSectorClick(int screenX, int screenY) {
        for (Sector sector : sectors.values()) {
            if (screenX < 0 || screenY < 0 || screenX > sector.getSectorTexture().getWidth() || screenY > sector.getSectorTexture().getHeight()) {
                continue;
            }
            int pixelValue = sector.getSectorPixmap().getPixel(screenX, screenY);
            if (pixelValue != -256) {
                System.out.println("Hit: " + sector.getDisplayName());
                changeSectorColor(sector.getId(), "green");
                break; // only one sector should be changed at a time so
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Sector sector : sectors.values()) {
            batch.draw(sector.getSectorTexture(), 0, 0);
        }
    }

    /**
     * The method takes a sectorId and recolors it to the specified color
     * @param sectorId id of sector to recolor
     * @param newColor what color the sector be changed to
     */
    public void changeSectorColor(int sectorId, String newColor){
        Sector sector = sectors.get(sectorId);
        Pixmap temp = sector.getSectorPixmap();
        Pixmap newPix = new Pixmap(temp.getWidth(), temp.getHeight(), Pixmap.Format.RGBA8888); // pixmap for drawing updated sector texture to
        for (int x = 0; x < sector.getSectorPixmap().getWidth(); x++){
            for (int y = 0; y < sector.getSectorPixmap().getHeight(); y++){
                if(temp.getPixel(x, y) != -256){
                    Color tempColor = new Color(0,0,0,0);
                    Color.rgba8888ToColor(tempColor, temp.getPixel(x, y)); // get the pixels current color
                    tempColor.sub(colors.get(newColor)); // calculate the new color of the pixel
                    newPix.drawPixel(x, y, Color.rgba8888(tempColor));  // draw the modified pixel value to the new pixmap
                }
            }
        }
        Texture t = new Texture(sector.getSectorPixmap().getWidth(), sector.getSectorPixmap().getHeight(), Pixmap.Format.RGBA8888); // create new texture to represent the sector
        t.draw(newPix, 0, 0); // draw the generated pixmap to the new texture
        sector.setSectorTexture(t);
    }
}
