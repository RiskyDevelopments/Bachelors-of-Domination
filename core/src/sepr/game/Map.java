package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.plaf.synth.SynthStyle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.badlogic.gdx.graphics.g3d.particles.ParticleChannels.Color;

/**
 * stores the game map and the sectors within it
 */
public class Map{
    private HashMap<Integer, Sector> sectors; // mapping of sector ID to the sector object
    private List<UnitChangeParticle> particles; // list of active particle effects displaying the changes to the amount of units on a sector
    private PVC proViceChancellor = new PVC((float)0.5,3);

    private BitmapFont font; // font for rendering sector unit data
    private GlyphLayout layout = new GlyphLayout();

    private Texture troopCountOverlay = new Texture("uiComponents/troopCountOverlay.png");

    private int[] unitsToMove; // units to move from an attacking to conquered sector, 3 index array : [0] amount to move; [1] source sector id ; [2] target sector id

    private Random random;

    /**
     * Performs the maps initial setup
     * loads the sector data from the sectorProperties.csv file
     * allocates each sector to the players in the passed players hashmap
     *
     * @param players hashmap of players who are in the game
     * @param allocateNeutralPlayer if true then the neutral player should be allocated the default neutral sectors else they should be allocated no sectors
     */
    public Map(HashMap<Integer, Player> players, boolean allocateNeutralPlayer) {
        random = new Random();

        this.loadSectors();
        font = WidgetFactory.getFontSmall();

        particles = new ArrayList<UnitChangeParticle>();
        this.allocateSectors2(players, allocateNeutralPlayer);
    }

    /**
     * converts a space seperated string of integers to an integer array
     *
     * @param stringData space separated integers e.g. '1 2 3 4 5'
     * @return the integers in the data in an array
     */
    private int[] strToIntArray(String stringData) {
        String[] strArray = stringData.split(" ");
        int[] intArray = new int[strArray.length];
        for (int i = 0; i < intArray.length; i++) {
            if (strArray[i].equals("")) {
                continue; // skip if string is empty
            }
            intArray[i] = Integer.parseInt(strArray[i]);
        }
        return intArray;
    }

    /**
     * load the sector properties from the sectorProperties.csv file into the sectors hashmap
     */
    private void loadSectors() {
        this.sectors = new HashMap<Integer, Sector>();

        String csvFile = "mapData/sectorProperties.csv";
        String line = "";
        Integer ID = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                Sector temp = sectorDataToSector(line.split(","));
                this.sectors.put(temp.getId(), temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // csv file no present
        } catch (IOException e) {
            e.printStackTrace(); // error occurred whilst reading the file
        }
    }

    /**
     * converts a String array of sector data to a sector object
     *
     * @param sectorData sector data taken from the sectorProperties csv file
     * @return a sector with the properties fo the supplied data
     */
    private Sector sectorDataToSector(String[] sectorData) {
        int sectorId = Integer.parseInt(sectorData[0]);
        int ownerId = -1;
        String filename = "mapData/" + sectorData[1];
        Texture sectorTexture = new Texture("mapData/" + sectorData[1]);
        Pixmap sectorPixmap = new Pixmap(Gdx.files.internal("mapData/" + sectorData[1]));
        String displayName = sectorData[2];
        int unitsInSector = 0;
        int reinforcementsProvided = Integer.parseInt(sectorData[4]);
        String college = sectorData[5];
        boolean neutral = Boolean.parseBoolean(sectorData[6]);
        int[] adjacentSectors = strToIntArray(sectorData[7]);
        int sectorX = Integer.parseInt(sectorData[8]);
        int sectorY = Integer.parseInt(sectorData[9]);
        boolean decor = Boolean.parseBoolean(sectorData[10]);

        return new Sector(sectorId, ownerId, filename, sectorTexture, sectorPixmap, displayName, unitsInSector, reinforcementsProvided, college, neutral, adjacentSectors, sectorX, sectorY, decor);
    }

    /**
     * allocates the default neutral sectors to the neutral player
     * @param players hashmap of players containing the Neutral Player at key value GameScreen.NEUTRAL_PLAYER_ID
     */
    private void allocateNeutralSectors(HashMap<Integer, Player> players) {
        for (Sector sector : sectors.values()) {
            if (sector.isNeutral()  && !sector.isDecor()) {
                sector.setOwner(players.get(GameScreen.NEUTRAL_PLAYER_ID));
            }
        }
    }



    /**
     * allocates sectors in the map to the players in a semi-random fashion
     * if there is a neutral player then the default neutral sectors are allocated to them
     * @param players the players the sectors are to be allocated to
     * @param allocateNeutralPlayer should the neutral player be allocated sectors
     * @throws RuntimeException if the players hashmap is empty
     */
    private void allocateSectors(HashMap<Integer, Player> players, boolean allocateNeutralPlayer) {
        if (players.size() == 0) {
            throw new RuntimeException("Cannot allocate sectors to 0 players");
        }

        // set any default neutral sectors to the neutral player
        if (allocateNeutralPlayer) {
            allocateNeutralSectors(players);
        }

        HashMap<Integer, Integer> playerReinforcements = new HashMap<Integer, Integer>(); // mapping of player id to amount of reinforcements they will receive currently
        // set all players to currently be receiving 0 reinforcements, ignoring the neutral player
        for (Integer i : players.keySet()) {
            if (i != GameScreen.NEUTRAL_PLAYER_ID) playerReinforcements.put(i, 0);
        }


        int lowestReinforcementId = players.keySet().iterator().next(); // id of player currently receiving the least reinforcements, any player id is chosen to start as all have 0 reinforcements
        List<Integer> sectorIdsRandOrder = new ArrayList<Integer>(getSectorIds()); // list of sector ids
        Collections.shuffle(sectorIdsRandOrder); // randomise the order sectors ids are stored so allocation order is randomised

        for (Integer i : sectorIdsRandOrder) {
            if (!sectors.get(i).isAllocated()) { // check sector has not already been allocated, may have been allocated to the neutral player
                if (this.getSectorById(i).isDecor()) {
                    continue; // skip allocating sector if it is a decor sector
                }
                this.getSectorById(i).setOwner(players.get(lowestReinforcementId));
                playerReinforcements.put(lowestReinforcementId, playerReinforcements.get(lowestReinforcementId) + this.getSectorById(i).getReinforcementsProvided()); // updates player reinforcements hashmap

                // find the new player with lowest reinforcements
                int minReinforcements = Collections.min(playerReinforcements.values()); // get lowest reinforcement amount
                for (Integer j : playerReinforcements.keySet()) { // find id of player which has the lowest reinforcement amount
                    if (playerReinforcements.get(j) == minReinforcements) { // if this player has the reinforcements matching the min amount set them to the new lowest player
                        lowestReinforcementId = j;
                        break;
                    }
                }
            }
        }
    }

    /**
     * calls the pvc class to see if the PVC tile should spawn
     * @return true if the PVC will spawn on a tile

     */


    public void allocateSectors2(HashMap<Integer, Player> players, boolean allocateNeutralPlayer) {
        if (players.size() == 0) {
            throw new RuntimeException("Cannot allocate sectors to 0 players");
        }

        // set any default neutral sectors to the neutral player
        if (allocateNeutralPlayer) {
            allocateNeutralSectors(players);
        }

        List<Integer> sectorIdsRandOrder = new ArrayList<Integer>(getSectorIds()); // list of sector ids
        Boolean sectorNotAssgined = true;

        for (Player x : players.values()) {
            sectorNotAssgined = true;
            if (!(x.getId() == 4 || x.getId() == 5)) {
                while (sectorNotAssgined) {
                    Collections.shuffle(sectorIdsRandOrder); // randomise the order sectors ids are stored so allocation order is randomised
                    Sector startSector = getSectorById(sectorIdsRandOrder.get(0));
                    if (!startSector.isAllocated()) {
                        if (!startSector.isDecor()) {
                            startSector.setOwner(x);
                            sectorNotAssgined = false;
                            sectorIdsRandOrder.remove(0);
                        }

                    }
                }
            }
        }

        for (Integer i : sectorIdsRandOrder)
        {
            if (!getSectorById(i).isAllocated() && !getSectorById(i).isDecor()) {
                getSectorById(i).setOwner(players.get(GameScreen.UNASSIGNED_ID));
            }
        }
    }


    public boolean ShouldPVCSpawn()
    {

        return proViceChancellor.PVCSpawn();
    }

    /**
     * spawns the PVC
     */

    public void spawnPVC(Stage stage) {
        Random rand = new Random();
        ArrayList<Sector> avaliableSectors = new ArrayList<Sector>();
        Sector chosenSector;

        for (Sector x : sectors.values()) {
            if (x.getOwnerId() == 5 && !x.isDecor()) {
                avaliableSectors.add(x);
            }
        }

        if (avaliableSectors.isEmpty()) {
            chosenSector = sectors.get(rand.nextInt(sectors.size())); //if all sectors are occupied then choose a random for sector for the PVC
        } else {
            chosenSector = avaliableSectors.get(rand.nextInt(avaliableSectors.size())); //place PVC on an unallocated sector
        }

        chosenSector.setIsPVCTile(true);
        DialogFactory.PVCSpawnedMessage(stage);
        chosenSector.changeSectorColor(com.badlogic.gdx.graphics.Color.GOLD);

    }

    /**
     * processes an attack from one sector to another
     * triggers specific dialogs dependent on the outcome of the attack
     * controls reassigning owners dependent on the outcome of the attack
     * sets up drawing particle effects showing changes in amount of units in a sector
     * sets up movement of units after conquering a sector
     *
     * @param attackingSectorId id of the sector the attack is coming from
     * @param defendingSectorId id of the defending sector
     * @param attackersLost amount of units lost on the attacking sector
     * @param defendersLost amount of units lost on the defenfing sector
     * @param attacker the player who is carrying out the attack
     * @param defender the player who is being attacked
     * @param unAssigned the neutral player
     * @param stage the stage to draw any dialogs to
     * @return true if attack successful else false
     * @throws IllegalArgumentException if the amount of attackers lost exceeds the amount of attackers
     * @throws IllegalArgumentException if the amount of defenders lost exceeds the amount of attackers
     */


    public boolean attackSector(int attackingSectorId, int defendingSectorId, int attackersLost, int defendersLost, Player attacker, Player defender, Player unAssigned, Stage stage) {
        if (sectors.get(attackingSectorId).getUnitsInSector() < attackersLost) {
            throw new IllegalArgumentException("Cannot loose more attackers than are on the sector: Attackers " + sectors.get(attackingSectorId).getUnitsInSector() + "     Attackers Lost " + attackersLost);
        }
        if (sectors.get(defendingSectorId).getUnitsInSector() < defendersLost) {
            throw new IllegalArgumentException("Cannot loose more defenders than are on the sector: Defenders " + sectors.get(attackingSectorId).getUnitsInSector() + "     Defenders Lost " + attackersLost);
        }

        addUnitsToSectorAnimated(attackingSectorId, -attackersLost); // apply amount of attacking units lost
        addUnitsToSectorAnimated(defendingSectorId, -defendersLost); // apply amount of defending units lost

        /* explain outcome to player using dialog boxes, possible outcomes
         * - All defenders killed, more than one attacker left      -->     successfully conquered sector, player is asked how many units they want to move onto it
         * - All defenders killed, one attacker left                -->     sector attacked becomes neutral as player can't move units onto it
         * - Not all defenders killed, all attackers killed         -->     attacking sector becomes unAssigned
         * - Not all defenders killed, not all attackers killed     -->     both sides loose troops, no dialog to display
         * */
        if (sectors.get(attackingSectorId).getUnitsInSector() == 0) { // attacker lost all troops
            DialogFactory.sectorOwnerChangeDialog(attacker.getPlayerName(), unAssigned.getPlayerName(), sectors.get(attackingSectorId).getDisplayName(), stage);
            sectors.get(attackingSectorId).setOwner(unAssigned);
            if (sectors.get(defendingSectorId).getUnitsInSector() == 0) { // both players wiped each other out
                DialogFactory.sectorOwnerChangeDialog(defender.getPlayerName(), unAssigned.getPlayerName(), sectors.get(attackingSectorId).getDisplayName(), stage);
                sectors.get(defendingSectorId).setOwner(unAssigned);
            }

        } else if (sectors.get(defendingSectorId).getUnitsInSector() == 0 && sectors.get(attackingSectorId).getUnitsInSector() > 1) { // territory conquered
            unitsToMove = new int[3];
            unitsToMove[0] = -1;
            unitsToMove[1] = attackingSectorId;
            unitsToMove[2] = defendingSectorId;



            attacker.addTroopsToAllocate(sectors.get(defendingSectorId).getReinforcementsProvided());
            sectors.get(defendingSectorId).setOwner(attacker);

            if(sectors.get(defendingSectorId).getIsPVCTile()) //if the player takes over PVC tile add PVC bonus
            {
                defender.setOwnsPVC(false);
                attacker.setOwnsPVC(true);
                DialogFactory.TakenOverPVCDialogue(stage);
                sectors.get(defendingSectorId).changeSectorColor(com.badlogic.gdx.graphics.Color.YELLOW);
            }
            DialogFactory.attackSuccessDialogBox(sectors.get(defendingSectorId).getReinforcementsProvided(), sectors.get(attackingSectorId).getUnitsInSector(), unitsToMove, defender.getPlayerName(), attacker.getPlayerName(), sectors.get(defendingSectorId).getDisplayName(), stage);



        } else if (sectors.get(defendingSectorId).getUnitsInSector() == 0 && sectors.get(attackingSectorId).getUnitsInSector() == 1) { // territory conquered but only one attacker remaining so can't move troops onto it
            DialogFactory.sectorOwnerChangeDialog(defender.getPlayerName(), unAssigned.getPlayerName(), sectors.get(defendingSectorId).getDisplayName(), stage);
            sectors.get(defendingSectorId).setOwner(unAssigned);
        }
        return true;
    }


    public Boolean moveTroops(int attackingSectorId, int defendingSectorId, int attackersLost, int defendersLost, Player attacker, Player defender, Player unAssigned, Stage stage){

        addUnitsToSectorAnimated(attackingSectorId, -attackersLost); // apply amount of attacking units lost
        addUnitsToSectorAnimated(defendingSectorId, defendersLost); // apply amount of defending units lost
        sectors.get(defendingSectorId).setOwner(attacker);

        return true;
    }


    /**
     * adds the specified number of units to this sector and sets up drawing a particle effect showing the addition
     *
     * @param sectorId id of sector to add the units to
     * @param amount of units to add
     */
    public void addUnitsToSectorAnimated(int sectorId, int amount) {
        this.sectors.get(sectorId).addUnits(amount);
        this.particles.add(new UnitChangeParticle(amount, new Vector2(sectors.get(sectorId).getSectorCentreX(), sectors.get(sectorId).getSectorCentreY())));
    }

    /**
     * gets the sector that has the corresponding sector id in the sectors hashmap
     *
     * @param sectorId id of the desired sector
     * @return Sector object with the corresponding id in hashmap sectors
     * @throws NullPointerException if the key sectorId does not exist in the sectors hashmap
     */
    public Sector getSectorById(int sectorId) {
        if (sectors.containsKey(sectorId)) {
            return sectors.get(sectorId);
        } else {
            throw new NullPointerException("Cannot get sector as sector id " + sectorId + " does not exist in the sectors hashmap");
        }
    }

    /**
     * @return Set of all SectorIds
     */
    public Set<Integer> getSectorIds() { return sectors.keySet() ; }

    /**
     * returns the id of the sector that contains the specified point
     * ignores decor sectors
     *
     * @param worldX world x coord
     * @param worldY world y coord
     * @return id of sector that contains point or -1 if no sector contains the point or sector is decor only
     */
    public int detectSectorContainsPoint(int worldX, int worldY) {
        int worldYInverted = 1080 - 1 - worldY; // invert y coordinate for pixmap coordinate system
        for (Sector sector : sectors.values()) {
            if (worldX < 0 || worldYInverted < 0 || worldX > sector.getSectorTexture().getWidth() || worldYInverted > sector.getSectorTexture().getHeight()) {
                return -1; // return no sector contains the point if it outside of the map bounds
            }
            int pixelValue = sector.getSectorPixmap().getPixel(worldX, worldYInverted); // get pixel value of the point in sector image the mouse is over
            if (pixelValue != -256) { // if pixel is not transparent then it is over the sector
              if (sector.isDecor()) {
                    continue; // sector is decor so continue checking to see if a non-decor sector contains point
                } else {
                    return sector.getId(); // return id of sector which is hovered over
                }
            }
        }
        return -1;
    }

    /**
     * carries out the unit movement specified by unitsToMove array
     *  - unitsToMove[0] : number of units to move
     *  - unitsToMove[1] : source sector id
     *  - unitsToMove[2] : target sector id
     * changes in units on sectors are shown on scren using the UnitChangeParticle
     *
     * @throws IllegalArgumentException if the sector are not both owned by the same player
     * @throws IllegalArgumentException if the amount exceeds the (number of units - 1) on the source sector
     * @throws IllegalArgumentException if the sectors are not connected
     */
    private void moveUnits() throws IllegalArgumentException {
        if (sectors.get(unitsToMove[1]).getOwnerId() != sectors.get(unitsToMove[2]).getOwnerId()) {
            throw new IllegalArgumentException("Source and target sectors must have the same owners");
        }
        if (sectors.get(unitsToMove[1]).getUnitsInSector() <= unitsToMove[0]) {
            throw new IllegalArgumentException("Must leave at least one unit on source sector and can't move more units than are on source sector");
        }
        if (!sectors.get(unitsToMove[1]).isAdjacentTo(sectors.get(unitsToMove[2]))) {
            throw new IllegalArgumentException("Sectors must be adjacent in order to move units");
        }
        addUnitsToSectorAnimated(unitsToMove[1], -unitsToMove[0]); // remove units from source
        addUnitsToSectorAnimated(unitsToMove[2], unitsToMove[0]); // add units to target
    }

    /**
     * once unitsToMove has had the amount of units to move and the ids of the source and target sector set, perform the move
     */
    private void detectUnitsMove() {
        if (unitsToMove != null) {
            if (unitsToMove[0] != -1) {
                moveUnits();
                unitsToMove = null;
            }
        }
    }

    /**
     * draws the map and the number of units in each sector and the units change particle effect
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        detectUnitsMove(); // check if units need to be moved, and carry the movement out if required

        for (Sector sector : sectors.values()) {
            String text = sector.getUnitsInSector() + "";
            batch.draw(sector.getSectorTexture(), 0, 0);
            if (!sector.isDecor()) { // don't need to draw the amount of units on a decor sector
                layout.setText(font, text);

                float overlaySize = 40.0f;
                batch.draw(troopCountOverlay, sector.getSectorCentreX() - overlaySize / 2 , sector.getSectorCentreY() - overlaySize / 2, overlaySize, overlaySize);
                font.draw(batch, layout, sector.getSectorCentreX() - layout.width / 2, sector.getSectorCentreY() + layout.height / 2);
            }
        }

        // render particles
        List<UnitChangeParticle> toDelete = new ArrayList<UnitChangeParticle>();
        for (UnitChangeParticle particle : particles) {
            particle.draw(batch);
            if (particle.toDelete()) {
                toDelete.add(particle);
            }
        }
        particles.removeAll(toDelete);
    }

    public HashMap<Integer, Sector> getSectors() {
        return sectors;
    }
}
