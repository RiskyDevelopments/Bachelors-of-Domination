package sepr.game;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;


/**
 * base class for storing Neutral and Human player data
 */
public class PVC {

    private float spawnChance; //likelihood that the PVC will spawn , between 0 and 1
                                // 0 = 0% ; 1 = 100%
    private float PVCBonus; //the bonus the pvc gives
    private boolean PVCSpawned = false;

    /**
     * creates a player object with the specified properties
     *
     * @param spawnChance       player's unique identifier
     * @param PVCBonus  display name for this player
     */


    public PVC(float spawnChance, int PVCBonus)
    {
        this.spawnChance = spawnChance;
        this.PVCBonus = PVCBonus;
    }

    public boolean PVCSpawn()
    {
        Random rand = new Random();
        Float randomValue = rand.nextFloat();
        if(randomValue <= spawnChance && (PVCSpawned == false) )
        {
            this.PVCSpawned = true;
            return true;
        }


        return false;
    }




    public float getSpawnChance() {
        return spawnChance;
    }

    public void setSpawnChance(float spawnChance) {
        this.spawnChance = spawnChance;
    }

    public float getPVCBonus() {
        return PVCBonus;
    }

    public void setPVCBonus(float PVCBonus) {
        this.PVCBonus = PVCBonus;
    }

}