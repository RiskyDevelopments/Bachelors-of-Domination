package sepr.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sepr.game.utils.TurnPhaseType;

/**
 * handles input, updating and rendering for the movement phase
 * not implemented
 */
public class PhaseMovement extends Phase {

    public PhaseMovement(GameScreen gameScreen) {
        super(gameScreen, TurnPhaseType.MOVEMENT);
    }

    @Override
    public void phaseAct() {

    }

    @Override
    protected void visualisePhase(SpriteBatch batch) {

    }
}
