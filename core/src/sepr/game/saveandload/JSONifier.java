package sepr.game.saveandload;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lwjgl.Sys;
import sepr.game.Phase;
import sepr.game.utils.TurnPhaseType;

public class JSONifier {

    public GameState state;

    public JSONifier(GameState state){
        this.state = state;
    }

    public JSONObject getJSONGameState(){
        JSONObject gameStateObject = new JSONObject();
        gameStateObject.put("CurrentPhase", this.state.currentPhase.toString());

        JSONObject mapState = new JSONObject();

        JSONArray sectorStates = new JSONArray();

        for (int i = 0; i < this.state.mapState.sectorStates.length; i++){
            JSONObject sectorState = new JSONObject();
            GameState.SectorState sector = this.state.mapState.sectorStates[i];

            sectorState.put("HashMapPosition", sector.hashMapPosition);
            sectorState.put("ID", sector.id);
            sectorState.put("OwnerID", sector.ownerId);
            sectorState.put("DisplayName", sector.displayName);
            sectorState.put("UnitsInSector", sector.unitsInSector);
            sectorState.put("ReinforcementsProvided", sector.reinforcementsProvided);
            sectorState.put("College", sector.college);
            sectorState.put("Neutral", sector.neutral);

            JSONArray adjSectors = new JSONArray();

            for (int j = 0; j < sector.adjacentSectorIds.length; j++){
                adjSectors.add(sector.adjacentSectorIds[j]);
            }

            sectorState.put("AdjacentSectorIDs", adjSectors);

            sectorState.put("SectorCenterX", sector.sectorCentreX);
            sectorState.put("SectorCenterY", sector.sectorCentreY);
            sectorState.put("Decor", sector.decor);
            sectorState.put("FileName", sector.fileName);
            sectorState.put("Allocated", sector.allocated);

            sectorStates.add(sectorState);
        }

        gameStateObject.put("MapState", sectorStates);

        JSONArray playerStates = new JSONArray();

        for (int k = 0; k < this.state.playerStates.length; k++){
            JSONObject playerState = new JSONObject();
            GameState.PlayerState player = this.state.playerStates[k];

            playerState.put("HashMapPosition", player.hashMapPosition);
            playerState.put("ID", player.id);
            playerState.put("CollegeName", player.collegeName.toString());
            playerState.put("PlayerName", player.playerName);
            playerState.put("TroopsToAllocate", player.troopsToAllocate);

            JSONObject colour = new JSONObject();
            colour.put("R", player.sectorColour.r);
            colour.put("G", player.sectorColour.g);
            colour.put("B", player.sectorColour.b);
            colour.put("A", player.sectorColour.a);
            playerState.put("SectorColour", colour);

            playerState.put("PlayerType", player.playerType.toString());

            playerStates.add(playerState);
        }

        gameStateObject.put("PlayerState", playerStates);

        gameStateObject.put("TurnTimerEnabled", this.state.turnTimerEnabled);
        gameStateObject.put("maxTurnTime", this.state.maxTurnTime);
        gameStateObject.put("TurnTimeStart", this.state.turnTimeStart);

        JSONArray turnOrder = new JSONArray();
        for (int i = 0; i < this.state.turnOrder.size(); i++){
            turnOrder.add(this.state.turnOrder.get(i));
        }

        gameStateObject.put("TurnOrder", turnOrder);

        gameStateObject.put("CurrentPlayerPointer", this.state.currentPlayerPointer);

        return gameStateObject;
    }

    public TurnPhaseType StringToPhase(String phase) {
        for (TurnPhaseType type : TurnPhaseType.values()){
            if (type.equalsName(phase)){
                return type;
            }
        }

        return TurnPhaseType.INVALID;
    }

}