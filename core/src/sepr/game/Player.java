package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;

/**
 * Created by Dom's Surface Mark 2 on 16/11/2017.
 */
public abstract class Player {
    private int id;
    private String collegeName;
    private int troopsToAllocate; // how many troops the player has to allocate at the start of their next reinforcement phase
    private Color sectorColour; // what colour to shade sectors owned by the player

    /**
     *
     * @param id player's unique identifier
     * @param collegeName display name for this player
     */
    public Player(int id, String collegeName, Color sectorColour) {
        this.id = id;
        this.collegeName = collegeName;
        this.troopsToAllocate = 0;
        this.sectorColour = sectorColour;
    }

    public int getId() {
        return id;
    }

    public Color getSectorColour() {
        return sectorColour;
    }

    public void setTroopsToAllocate(int troopsToAllocate) {
        this.troopsToAllocate = troopsToAllocate;
    }

    /**
     * Let player attack enemy territories adjacent to its own
     */
    protected abstract void processAttackPhase();

    /**
     * Let player move troops between adjacent friendly territories
     */
    protected abstract void processMovePhase();

    /**
     * Let the player choose where to allocate their additional reinforcements
     * @param amount thr amount of toops available to allocate
     * @return how many troops remaining they may allocate in a future turn
     */
    protected abstract int processAllocationPhase(int amount);

    /**
     * resolves a conflict between two territories
     * @throws IllegalArgumentException if a player is attacking a sector it owns
     * @throws IllegalArgumentException if sectors are not connected
     * @param attackingSectorId id of sector carrying out the attack
     * @param defendingSectorId id of sector being attacked
     * @param amountOfTroopsAttacking number of troops that are being used to attack with
     */
    private static void resolveCombat(int attackingSectorId, int defendingSectorId, int amountOfTroopsAttacking) {

    }
}

