package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

        String csvFile = "sectorProperties.csv";
        String line = "";
        Integer ID = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("sectorProperties.csv"));
            while ((line = br.readLine()) != null){
                String[] temp = line.split(",");
                this.sectors.put(Integer.parseInt(temp[0]), new Sector (Integer.parseInt(temp[0]),-1, temp[2], Integer.parseInt(temp[3]), Integer.parseInt(temp[6]), temp[7], new Texture(temp[1]), new Pixmap(Gdx.files.internal(temp[1])), temp[1], Integer.parseInt(temp[8]), Integer.parseInt(temp[9]), Boolean.parseBoolean(temp[10])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        //Sector sector = sectors.get(sectorId);
        //Pixmap temp = sector.getSectorPixmap();
        Pixmap newPix = new Pixmap(Gdx.files.internal(sectors.get(sectorId).getFileName())); // pixmap for drawing updated sector texture to
        for (int x = 0; x < sectors.get(sectorId).getSectorPixmap().getWidth(); x++){
            for (int y = 0; y < sectors.get(sectorId).getSectorPixmap().getHeight(); y++){
                if(newPix.getPixel(x, y) != -256){
                    Color tempColor = new Color(0,0,0,0);
                    Color.rgba8888ToColor(tempColor, newPix.getPixel(x, y)); // get the pixels current color
                    tempColor.sub(colors.get(newColor)); // calculate the new color of the pixel
                    newPix.drawPixel(x, y, Color.rgba8888(tempColor));  // draw the modified pixel value to the new pixmap
                }
            }
        }
        //Texture t = new Texture(sector.getSectorPixmap().getWidth(), sector.getSectorPixmap().getHeight(), Pixmap.Format.RGBA8888); // create new texture to represent the sector
        sectors.get(sectorId).setNewSectorTexture(newPix); // draw the generated pixmap to the new texture
        newPix.dispose();
        //sector.setSectorTexture(t);
    }

}
