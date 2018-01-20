package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * class that produces reusable dialog windows for displaying information to the player and receiving input from them
 */
public class DialogFactory {

    private static Skin skin; // skin shared by all dialog windows for a uniform aesthetic

    public DialogFactory() {
        skin = new Skin(Gdx.files.internal("dialogBox/skin/uiskin.json"));
    }

    /**
     * Creates a dialog modal in the given stage with an ok button and the given title and message
     *
     * @param title String to be used at the top of the dialog box
     * @param message String to be used as the content of the dialog
     * @param stage to draw the box onto
     */
    public static void basicDialogBox(String title, String message, Stage stage) {
        Dialog dialog = new Dialog(title, DialogFactory.skin);
        dialog.text(message);
        dialog.button("Ok", "0");
        dialog.show(stage);
    }

    /**
     * Generates a dialog box displaying which players turn it is next and how many troops they have to allocate
     *
     * @param nextPlayer String to be used to display the name of the next player
     * @param troopsToAllocate Integer to be used to display number of troops
     * @param stage to draw the box onto
     */
    public static void nextTurnDialogBox(String nextPlayer, Integer troopsToAllocate, Stage stage) {
        basicDialogBox("Next Turn", "Next Player: " + nextPlayer + "\nTroops to Allocate: " + troopsToAllocate, stage);
    }

    /**
     * Creates a dialog where the player can confirm if they want to exit the game
     *
     * @param stage The stage to draw the box onto
     */
    public static void exitDialogBox(Stage stage) {
        Dialog dialog = new Dialog("Quit", DialogFactory.skin) {
            protected void result(Object object) {
                if (object.toString().equals("1")){ // yes pressed : quit the game
                    Gdx.app.exit();
                }
            }
        };
        dialog.text("Are you sure you want to exit the game?");
        dialog.button("Yes", "1");
        dialog.button("No", "0");
        dialog.show(stage);
    }

    /**
     * Creates a dialog modal which says which player took control of a sector from which other player
     *
     * @param prevOwner name of the player who used to own the sector
     * @param newOwner name of the player who now controls the sector
     * @param sectorName name of the sector being taken
     * @param stage to draw the box onto
     */
    public static void sectorOwnerChangeDialog(String prevOwner, String newOwner, String sectorName, Stage stage) {
        basicDialogBox("Sector Owner Change", newOwner + " gained " + sectorName + " from " + prevOwner, stage);
    }

    /**
     * Creates a dialog box with a slider and okay box allowing a player who has conquered a sector to select how many troops to move onto it
     *
     * @param bonusTroops Integer used to provide the bonus troops provided by conquered tile
     * @param maxTroops the amount of troops on the attacking tile
     * @param troopsMoved 3 index final array for setting value of slider to representing how many troops to move: [0] amount of units to move; [1] id of source sector; [2] id of target sector
     * @param prevOwner name of the player who used to own the sector
     * @param newOwner name of the player who now controls the sector
     * @param sectorName name of the sector being taken
     * @param stage The stage to draw the box onto
     */
    public static void attackSuccessDialogBox(Integer bonusTroops, Integer maxTroops, final int[] troopsMoved, String prevOwner, String newOwner, String sectorName, Stage stage) {
        final Slider slider = new Slider(1, (maxTroops - 1), 1, false, DialogFactory.skin); // slider max value is maxTroops -1 as must leave at least one troop on attacking sector
        slider.setValue(1); // must move at least one troop so set initial value to 1
        final Label sliderValue = new Label("1", DialogFactory.skin); // label to display the slider value

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sliderValue.setText(new StringBuilder((int)slider.getValue() + "")); // update slider value label when slider moved
            }
        });

        Dialog dialog = new Dialog("Success!", DialogFactory.skin) {
            protected void result(Object object) {
                // set number of troops to move to the value of the slider when the dialog is closed
                troopsMoved[0] = (int)slider.getValue();
            }
        };

        dialog.text(newOwner + " gained " + sectorName + " from " + prevOwner + "\nYou have earned " + bonusTroops + " bonus troops!\nHow many troops would you like to move to the new sector?");
        dialog.getContentTable().row();

        dialog.getContentTable().add(slider).padLeft(20).padRight(20).align(Align.left).expandX();
        dialog.getContentTable().add(sliderValue).padLeft(20).padRight(20).align(Align.right);

        dialog.getContentTable().row();

        dialog.button("Ok", "0");
        dialog.show(stage);
    }

    /**
     * Creates a dialog modal allowing the user to select how many units they want to allocate to a sector
     *
     * @param maxAllocation maximum amount of troops that can be assigned to this tile
     * @param allocation 2 index array storing : [0] number of troops to allocate ; [1] id of sector to allocate to
     * @param sectorName name of sector being allocated to
     * @param stage The stage to draw the box onto
     */
    public static void allocateUnitsDialog(Integer maxAllocation, final int[] allocation, String sectorName, Stage stage) {
        final Slider slider = new Slider(1, maxAllocation, 1, false, DialogFactory.skin);
        final Label sliderValue = new Label("1", DialogFactory.skin);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sliderValue.setText(new StringBuilder((int)slider.getValue() + ""));
            }
        });

        Dialog dialog = new Dialog("Select amount of troops to allocate", DialogFactory.skin) {
            protected void result(Object object) {
                if (object.equals("0")) {
                    allocation[0] = -1;
                    allocation[1] = -1;
                } else if (object.equals("1")) {
                    allocation[0] = (int)slider.getValue();
                }
            }
        };
        dialog.text("You can allocate up to " + maxAllocation + " troops to " + sectorName);
        dialog.getContentTable().row();

        dialog.getContentTable().add(slider).padLeft(20).padRight(20).align(Align.left).expandX();
        dialog.getContentTable().add(sliderValue).padLeft(20).padRight(20).align(Align.right);

        dialog.getContentTable().row();

        dialog.button("Cancel", "0");
        dialog.button("Ok", "1");
        dialog.show(stage);
    }

    /**
     * Dialog box for the player to select how many troops they want to attack with
     *
     * @param stage to display the dialog on
     * @param maxAttackers max number of attackers the player chooses to attack with
     * @param defenders how many units are defending
     * @return the number of troops chosen to attack with or 0 if the attack is canceled
     */
    public static void attackDialog(Stage stage, int maxAttackers, int defenders, final int[] attackers) {
        final Slider slider = new Slider(1, maxAttackers, 1, false, DialogFactory.skin);
        slider.setValue(maxAttackers);
        final Label sliderValue = new Label(maxAttackers + "", DialogFactory.skin); // label showing the value of the slider
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sliderValue.setText(new StringBuilder((int)slider.getValue() + "")); // update slider value label when the slider is moved
            }
        });

        Dialog dialog = new Dialog("Select number of troops to attack with", DialogFactory.skin) {
            protected void result(Object object) {
                // object is the button pressed
                if (object.equals("0")) {
                    attackers[0] = 0; // cancel pressed: set number of attacker to 0, i.e. no attack
                } else {
                    attackers[0] = (int)slider.getValue(); // set numvber of attackers to the value of the slider
                }
            }
        };

        // add labels saying the max number of attackers and how many defenders there are
        dialog.text(new Label("Max attackers: " + maxAttackers, DialogFactory.skin)).padLeft(20).padRight(20).align(Align.left);
        dialog.text(new Label("Defenders: " + defenders, DialogFactory.skin)).padLeft(20).padRight(20).align(Align.right);

        dialog.getContentTable().row();

        // add slider and label showing number of units selected
        dialog.getContentTable().add(slider).padLeft(20).padRight(20).align(Align.left).expandX();
        dialog.getContentTable().add(sliderValue).padLeft(20).padRight(20).align(Align.right);

        dialog.getContentTable().row();

        // add buttons for accepting or canceling the selection
        dialog.button("Cancel", "0").padLeft(20).padRight(40).align(Align.center);
        dialog.button("Ok", "1").padLeft(40).padRight(20).align(Align.center);

        dialog.show(stage);
    }

    /**
     * dialog that displays a list of players that have been eliminated
     * @param playerNames players that have been eliminated
     * @param stage to draw the box to
     */
    public static void playersOutDialog(String[] playerNames, Stage stage) {
        Dialog dialog = new Dialog("Elimination!", DialogFactory.skin);
        String message = "The following player has been eliminated:";
        if (playerNames.length > 1) {
            message = "The following players have been eliminated:";
        }
        for (String s : playerNames) {
            message += "\n    " + s;
        }
        dialog.text(message);
        dialog.button("Ok", "0");
        dialog.show(stage);
    }

    /**
     * dialog displayed when a player has won the game
     *
     * @param playerName name of player who has won
     * @param collegeName name of the winning player's college
     * @param main for changing back to the menu screen
     * @param stage to draw the dialog to
     */
    public static void gameOverDialog(String playerName, String collegeName, final Main main, Stage stage) {
        Dialog dialog = new Dialog("Game Over!", DialogFactory.skin) {
            protected void result(Object object) {
                main.setMenuScreen();
            }
        };
        dialog.text("Game Over!\n" + playerName + " of College " + collegeName + " has conquered the University of York!");
        dialog.button("Ok", "0");
        dialog.show(stage);
    }
}
