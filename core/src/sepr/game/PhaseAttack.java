package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class PhaseAttack extends Phase{

    private TextureRegion arrow; // TextureRegion for rendering attack visualisation
    private Sector attackingSector; // Stores the sector being used to attack in the attack phase (could store as ID and lookup object each time to save memory)
    private Sector defendingSector; // Stores the sector being attacked in the attack phase (could store as ID and lookup object each time to save memory)
    private int[] numOfAttackers;

    private Vector2 mousePos;
    private Vector2 arrowTailPosition; // Vector x,y for the base of the arrow
    private Vector2 arrowHeadPosition; // Vector x,y for the point of the arrow

    private Random random; // random object for adding some unpredictability to the outcome of attacks

    public PhaseAttack(GameScreen gameScreen, Map map) {
        super(gameScreen, map, TurnPhaseType.ATTACK);

        this.arrow = new TextureRegion(new Texture(Gdx.files.internal("arrow.png")));
        this.attackingSector = null;
        this.defendingSector = null;

        this.mousePos = new Vector2();
        this.arrowHeadPosition = new Vector2();
        this.arrowTailPosition = new Vector2();

        this.random = new Random();
    }

    /**
     * Creates an arrow between coordinates
     * @param gameplayBatch The main sprite batch
     * @param startX Base of the arrow x
     * @param startY Base of the arrow y
     * @param endX Tip of the arrow x
     * @param endY Tip of the arrow y
     */
    private void generateArrow(SpriteBatch gameplayBatch, float startX, float startY, float endX, float endY) {
        int thickness = 30;
        double angle = Math.toDegrees(Math.atan((endY - startY) / (endX - startX)));
        double height = (endY - startY) /  Math.sin(Math.toRadians(angle));
        gameplayBatch.draw(arrow, startX, (startY - thickness/2), 0, thickness/2, (float)height, thickness,1, 1, (float)angle);
    }

    private void getNumberOfAttackers() throws RuntimeException{
        if (attackingSector == null || defendingSector == null) {
            throw new RuntimeException("Cannot execute attack unless both an attacking and defending sector have been selected");
        }
        numOfAttackers = new int[1];
        numOfAttackers[0] = -1;
        gameScreen.getCurrentPlayer().processAttackPhase(this, attackingSector.getUnitsInSector(), defendingSector.getUnitsInSector(), numOfAttackers);
    }

    private void executeAttack() {
        int attackers = numOfAttackers[0];
        int defenders = defendingSector.getUnitsInSector();

        float attackerDefenderDiff = Math.abs(defenders - attackers);

        int attackersLost = (int)Math.min(attackers, attackerDefenderDiff - 2 + random.nextInt(5));
        int defendersLost = (int)Math.min(defenders, attackerDefenderDiff - 2 + random.nextInt(5));

        map.addUnitsToSectorAnimated(attackingSector.getId(), -attackersLost);
        map.addUnitsToSectorAnimated(defendingSector.getId(), -defendersLost);
    }

    @Override
    public void phaseAct() {
        if (attackingSector != null && defendingSector != null && numOfAttackers[0] != -1) {
            if (numOfAttackers[0] == 0) {
                // cancel attack
            } else {
                executeAttack();
            }
            // reset attack
            attackingSector = null;
            defendingSector = null;
            numOfAttackers = null;
        }
    }

    @Override
    public void visualisePhase(SpriteBatch batch) {
        if (this.attackingSector != null) { // If attacking
            if (this.defendingSector == null) { // In mid attack
                generateArrow(batch, this.arrowTailPosition.x, this.arrowTailPosition.y, mousePos.x, mousePos.y);
            } else if (this.defendingSector != null) { // Attack confirmed
                generateArrow(batch, this.arrowTailPosition.x, this.arrowTailPosition.y, this.arrowHeadPosition.x, this.arrowHeadPosition.y);
            }
        }
    }

    @Override
    public void endPhase() {
        super.endPhase();
        attackingSector = null;
        defendingSector = null;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (super.keyDown(keycode)) {
            return true;
        }



        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (super.keyUp(keycode)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (super.keyTyped(character)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (super.touchDown(screenX, screenY, pointer, button)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (super.touchUp(screenX, screenY, pointer, button)) {
            return true;
        }

        Vector2 worldCoord = gameScreen.screenToWorldCoord(screenX, screenY);

        int sectorId = map.detectSectorContainsPoint((int)worldCoord.x, (int)worldCoord.y);
        if (sectorId != -1) { // If selected a sector

            Sector selected = map.getSector(sectorId); // Current sector
            boolean notAlreadySelected = this.attackingSector == null && this.defendingSector == null; // T/F if the attack sequence is complete

            if (this.attackingSector != null && this.defendingSector == null) { // If its the second selection in the attack phase

                if (this.attackingSector.isAdjacentTo(selected) && selected.getOwnerId() != this.currentPlayer.getId()) { // If not own sector and its adjacent
                    this.arrowHeadPosition.set(mousePos.x, mousePos.y); // Finalise the end position of the arrow
                    this.defendingSector = selected;

                    getNumberOfAttackers();
                } else { // Cancel attack as not attackable
                    this.attackingSector = null;
                }

            } else if (selected.getOwnerId() == this.currentPlayer.getId() && selected.getUnitsInSector() > 1 && notAlreadySelected) { // First selection, is owned by the player and has enough troops
                this.attackingSector = selected;
                this.arrowTailPosition.set(mousePos.x, mousePos.y); // Finalise start position of arrow
            } else {
                this.attackingSector = null;
                this.defendingSector = null;
            }

        } else {
            this.attackingSector = null;
            this.defendingSector = null;
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (super.touchDragged(screenX, screenY, pointer)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (super.mouseMoved(screenX, screenY)) {
            return true;
        }
        this.mousePos.x = (gameScreen.gameplayCamera.unproject(new Vector3(screenX, screenY, 0)).x);
        this.mousePos.y = (gameScreen.gameplayCamera.unproject(new Vector3(screenX, screenY, 0)).y);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (super.scrolled(amount)) {
            return true;
        }
        return false;
    }
}
