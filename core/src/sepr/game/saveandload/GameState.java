package sepr.game.saveandload;

import com.badlogic.gdx.graphics.Color;
import sepr.game.*;
import sepr.game.utils.PlayerType;
import sepr.game.utils.TurnPhaseType;

import java.util.HashMap;
import java.util.List;

public class GameState {
    public TurnPhaseType currentPhase;
    public Map map;
    public MapState mapState;
    public HashMap<Integer, Player> players;
    public PlayerState[] playerStates;
    public boolean turnTimerEnabled;
    public int maxTurnTime;
    public long turnTimeStart;
    public List<Integer> turnOrder;
    public int currentPlayerPointer;

    public class MapState {
        public HashMap<Integer, Sector> sectors;
        public SectorState[] sectorStates;
    }

    public class PlayerState {
        public int hashMapPosition;
        public int id;
        public GameSetupScreen.CollegeName collegeName;
        public String playerName;
        public int troopsToAllocate;
        public Color sectorColour;
        public PlayerType playerType;
    }

    public class SectorState {
        public int hashMapPosition;
        public int id;
        public int ownerId;
        public String displayName;
        public int unitsInSector;
        public int reinforcementsProvided;
        public String college; // name of the college this sector belongs to
        public boolean neutral; // is this sector a default neutral sector
        public int[] adjacentSectorIds; // ids of sectors adjacent to this one
        public int sectorCentreX; // the centre x coordinate of this sector, relative to the sectorTexture
        public int sectorCentreY; //the centre y coordinate of this sector, relative to the sectorTexture
        public boolean decor; // is this sector for visual purposes only, i.e. lakes are decor
        public String fileName;
        public boolean allocated; // becomes true once the sector has been allocated
    }
}
