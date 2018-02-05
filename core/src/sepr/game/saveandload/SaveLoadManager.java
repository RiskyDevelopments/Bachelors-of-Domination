package sepr.game.saveandload;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.Sys;
import sepr.game.GameScreen;
import sepr.game.Main;
import sepr.game.Player;
import sepr.game.Sector;

import java.io.*;
import java.util.Map;

public class SaveLoadManager {
    private Main main;
    private GameScreen gameScreen;

    private static String SAVE_FILE_PATH = "";
    private static int currentSaveID = -1;
    private static int numberOfSaves = 0;

    private Boolean loadedSave = false;

    /*
        saves: [
            {
                id: 0,
                players: [
                    {
                        ...
                    }
                ],
                map: [
                    {
                        ...
                    }
                ]
            }
        ]
     */

    public SaveLoadManager(final Main main, GameScreen gameScreen) {
        this.main = main;
        this.gameScreen = gameScreen;

        String home = System.getProperty("user.home");

        String path = home + File.separator + "Bachelors-of-Domination" + File.separator + "saves" + File.separator + "saves.json";
        boolean directoryExists = new File(path).exists();

        this.SAVE_FILE_PATH = path;

        if(!directoryExists){
            File file = new File(path);
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

                JSONObject savesTemplate = new JSONObject();
                savesTemplate.put("Saves", this.numberOfSaves);
                savesTemplate.put("CurrentSaveID", this.currentSaveID);

                try {
                    FileWriter fileWriter = new FileWriter(this.SAVE_FILE_PATH);
                    fileWriter.write(savesTemplate.toJSONString());
                    fileWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean LoadFromFile(){

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(SAVE_FILE_PATH));
            JSONObject jsonObject = (JSONObject)obj;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean LoadSaveByID(int id){
        return false; // TODO this should return a save configuration type
    }

    public boolean SaveToFile(){
        return false;
    }

    public boolean SaveByID(int id){
        GameState gameState = new GameState();
        gameState.currentPhase = this.gameScreen.getCurrentPhase();
        gameState.map = this.gameScreen.getMap();
        gameState.players = this.gameScreen.getPlayers();
        gameState.turnTimerEnabled = this.gameScreen.isTurnTimerEnabled();
        gameState.maxTurnTime = this.gameScreen.getMaxTurnTime();
        gameState.turnTimeStart = this.gameScreen.getTurnTimeStart();
        gameState.turnOrder = this.gameScreen.getTurnOrder();
        gameState.currentPlayerPointer = this.gameScreen.getCurrentPlayerPointer();

        GameState.MapState mapState = gameState.new MapState();

        mapState.sectors = gameState.map.getSectors();
        mapState.sectorStates = new GameState.SectorState[mapState.sectors.size()];

        int i = 0;

        for (Map.Entry<Integer, Sector> sector : mapState.sectors.entrySet()){
            Integer key = sector.getKey();
            Sector value = sector.getValue();

            GameState.SectorState sectorState = gameState.new SectorState();
            sectorState.hashMapPosition = key;
            sectorState.id = value.getId();
            sectorState.ownerId = value.getOwnerId();
            sectorState.displayName = value.getDisplayName();
            sectorState.unitsInSector = value.getUnitsInSector();
            sectorState.reinforcementsProvided = value.getReinforcementsProvided();
            sectorState.college = value.getCollege();
            sectorState.neutral = value.isNeutral();
            sectorState.adjacentSectorIds = value.getAdjacentSectorIds();
            sectorState.sectorCentreX = value.getSectorCentreX();
            sectorState.sectorCentreY = value.getSectorCentreY();
            sectorState.decor = value.isDecor();
            sectorState.fileName = value.getFileName();
            sectorState.allocated = value.isAllocated();

            mapState.sectorStates[i] = sectorState;

            i++;
        }

        mapState.sectors = null;
        gameState.map = null;
        gameState.mapState = mapState;

        gameState.playerStates = new GameState.PlayerState[gameState.players.size()];

        i = 0;

        for (Map.Entry<Integer, Player> player : gameState.players.entrySet()) {
            Integer key = player.getKey();
            Player value = player.getValue();

            GameState.PlayerState playerState = gameState.new PlayerState();
            playerState.hashMapPosition = key;
            playerState.id = value.getId();
            playerState.collegeName = value.getCollegeName();
            playerState.playerName = value.getPlayerName();
            playerState.troopsToAllocate = value.getTroopsToAllocate();
            playerState.sectorColour = value.getSectorColour();
            playerState.playerType = value.getPlayerType();

            gameState.playerStates[i] = playerState;
            i++;
        }

        gameState.players = null;

        JSONObject newSave = new JSONObject();
        newSave.put("Saves", this.numberOfSaves);
        newSave.put("CurrentSaveID", this.currentSaveID);

        JSONifier jifier = new JSONifier(gameState);
        newSave.put("GameState", jifier.getJSONGameState());

        try {
            FileWriter fileWriter = new FileWriter(this.SAVE_FILE_PATH);
            fileWriter.write(newSave.toJSONString());
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Returns the ID of the currently loaded save, generates a new ID if no file is loaded
     * @return The current save ID
     */
    public int GetCurrentSaveID(){
        if (!this.loadedSave){
            return GetNextSaveID();
        }else{
            return this.currentSaveID;
        }
    }

    /**
     * Returns the next available save ID
     * @return The nexxt available save ID
     */
    public int GetNextSaveID(){
        this.currentSaveID++;

        return this.currentSaveID;
    }

}
