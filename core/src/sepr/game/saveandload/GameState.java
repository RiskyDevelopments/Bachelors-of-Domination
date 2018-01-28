package sepr.game.saveandload;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import sepr.game.*;

import java.util.HashMap;
import java.util.List;

public class GameState {
    private TurnPhaseType currentPhase;
    private HashMap<TurnPhaseType, Phase> phases;
    private SpriteBatch gameplayBatch;
    private OrthographicCamera gameplayCamera;
    private Viewport gameplayViewport;
    private Map map;
    private HashMap<Integer, Player> players;
    private boolean turnTimerEnabled;
    private int maxTurnTime;
    private long turnTimeStart;
    private List<Integer> turnOrder;
    private int currentPlayerPointer;
    private Texture mapBackground;
    private boolean gameSetup = false;

    public class MapState {
        public HashMap<Integer, Sector> sectors;
        public BitmapFont font;
        public GlyphLayout layout;
    }

    public class OptionsState {

    }

    public class PlayerState {
        public int id;
        public GameSetupScreen.CollegeName collegeName;
        public String playerName;
        public int troopsToAllocate;
        public Color sectorColour;
        public PlayerType playerType;
    }

    public class Sector {
        public int id;
        public int ownerId;
        public String displayName;
        public int unitsInSector;
        public int reinforcementsProvided;
        public String college; // name of the college this sector belongs to
        public boolean neutral; // is this sector a default neutral sector
        public int[] adjacentSectorIds; // ids of sectors adjacent to this one
        public Texture sectorTexture;
        public Pixmap sectorPixmap; // the pixel data of this sectors texture
        public int sectorCentreX; // the centre x coordinate of this sector, relative to the sectorTexture
        public int sectorCentreY; //the centre y coordinate of this sector, relative to the sectorTexture
        public boolean decor; // is this sector for visual purposes only, i.e. lakes are decor
        public String fileName;
        public boolean allocated; // becomes true once the sector has been allocated
    }
}
