package sepr.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import sepr.game.utils.TurnPhaseType;

/**
 * handles input, updating and rendering for the reinforcement phase
 */
public class PhaseReinforce extends Phase {
    private int[] allocateUnits; // 2 index array storing : [0] number of troops to allocate ; [1] id of sector to allocate to

    public PhaseReinforce(GameScreen gameScreen) {
        super(gameScreen, TurnPhaseType.REINFORCEMENT);
    }

    @Override
    void enterPhase(Player player) {
        super.enterPhase(player);

        currentPlayer.addTroopsToAllocate(5); // players get a basic reinforcement of 5 troops every turn
        if (player.getOwnsPVC())  // assigns a bonus of two troops if they own the PVC tile
        {
            currentPlayer.addTroopsToAllocate(2);

        }

        updateTroopReinforcementLabel();
        DialogFactory.nextTurnDialogBox(currentPlayer.getPlayerName(), currentPlayer.getTroopsToAllocate(), this);
    }

    @Override
    public void endPhase() {
        currentPlayer.setTroopsToAllocate(0); // any unallocated units are removed
        super.endPhase();
    }

    /**
     * checks if the user has completed the unit allocation dialog
     */
    private void detectUnitAllocation() {
        if (allocateUnits != null) { // check that an allocation has been initiated
            if (allocateUnits[1] == -1 || allocateUnits[0] == 0) { // cancel allocation if sector id set to -1 or 0 units are allocated
                allocateUnits = null;
            } else if (allocateUnits[0] != -1) { // dialog complete : perform the allocation
                gameScreen.getMap().addUnitsToSectorAnimated(allocateUnits[1], allocateUnits[0]);
                currentPlayer.addTroopsToAllocate(-allocateUnits[0]);
                allocateUnits = null;
                updateTroopReinforcementLabel();
            }
        }
    }

    @Override
    public void phaseAct() {
        detectUnitAllocation();
    }

    @Override
    public void visualisePhase(SpriteBatch batch) {

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (super.touchUp(screenX, screenY, pointer, button)) {
            return true;
        }

        Vector2 worldCoord = gameScreen.screenToWorldCoords(screenX, screenY);

        int sectorId = gameScreen.getMap().detectSectorContainsPoint((int)worldCoord.x, (int)worldCoord.y);
        if (sectorId != -1) { // If selected a sector
            if (currentPlayer.getTroopsToAllocate() <= 0) { // check the player still has units to allocate
                DialogFactory.basicDialogBox("Allocation Problem", "You have no more troops to allocate", this);
            } else if (gameScreen.getMap().getSectorById(sectorId).getOwnerId() != currentPlayer.getId()) { // check the player has chosen to add units to their own sector
                DialogFactory.basicDialogBox("Allocation Problem", "Cannot allocate units to a sector you do not own", this);
            } else {
                // setup allocation form
                allocateUnits = new int[2];
                allocateUnits[0] = -1;
                allocateUnits[1] = sectorId;
                DialogFactory.allocateUnitsDialog(currentPlayer.getTroopsToAllocate(), allocateUnits, gameScreen.getMap().getSectorById(sectorId).getDisplayName(), this);
            }
        }
        return false;
    }
}
