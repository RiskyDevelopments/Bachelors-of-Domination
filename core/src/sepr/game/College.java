package sepr.game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jackr on 01/12/2017.
 */
public class College {

    private ArrayList<Integer> sectorIds;
    private int reinforcementAmount;
    private String displayName;
    private int id;

    /**
     * Wasn't sure how you'd like to add new colleges, so here's all three options
     */
    public College(int id, String displayName){
        this.id = id;
        this.displayName = displayName;
        this.sectorIds = new ArrayList<Integer>();
        this.reinforcementAmount = 0;
    }

    public College(int id, String displayName, int reinforcementAmount){
        this.id = id;
        this.displayName = displayName;
        this.sectorIds = new ArrayList<Integer>();
        this.reinforcementAmount = reinforcementAmount;
    }

    public College(int id, String displayName, int reinforcementAmount, ArrayList<Integer> sectorIds){
        this.id = id;
        this.displayName = displayName;
        this.sectorIds = sectorIds;
        this.reinforcementAmount = reinforcementAmount;
    }

    public void setReinforcementAmount(int reinforcementAmount) { this.reinforcementAmount = reinforcementAmount; }

    public void addSectorId(int sectorId){
        this.sectorIds.add(sectorId);
    }

    public int getReinforcementAmount() {
        return reinforcementAmount;
    }

    public ArrayList<Integer> getSectorIds(){
        return sectorIds;
    }

    public boolean playerOwnsCollege(int playerId, HashMap<Integer, Sector> sectorsMap){
        for (int sId : sectorIds){
            if (sectorsMap.get(sId).getOwnerId() != playerId)
                return false;
        }
        return true;
    }

}
