package sepr.game.saveandload;

public class GameStateJSON {
    public String currentPhase;
    public MapStateJSON map;
    public PlayerHashMapJSON[] players;
    public boolean turnTimerEnabled;
    public int maxTurnTime;
    public long turnTimeStart;
    public Integer[] turnOrder;
    public int currentPlayerPointer;

    public class MapStateJSON {
        SectorHashMapJSON[] sectors;
    }

    public class PlayerStateJSON {
        public int id;
        public String collegeName;
        public String playerName;
        public int troopsToAllocate;
        public ColorJSON sectorColour;
        public String playerType;
    }

    public class SectorHashMapJSON {
        public int id;
        public SectorStateJSON sector;
    }

    public class PlayerHashMapJSON {
        public int id;
        public PlayerStateJSON player;
    }

    public class SectorStateJSON {
        public int id;
        public int ownerId;
        public String displayName;
        public int unitsInSector;
        public int reinforcementsProvided;
        public String college; // name of the college this sector belongs to
        public boolean neutral; // is this sector a default neutral sector
        public int[] adjacentSectorIds; // ids of sectors adjacent to this one
        public String sectorTexture;
        public int sectorCentreX; // the centre x coordinate of this sector, relative to the sectorTexture
        public int sectorCentreY; //the centre y coordinate of this sector, relative to the sectorTexture
        public boolean decor; // is this sector for visual purposes only, i.e. lakes are decor
        public String fileName;
        public boolean allocated; // becomes true once the sector has been allocated
    }

    public class ColorJSON {
        
    }
}