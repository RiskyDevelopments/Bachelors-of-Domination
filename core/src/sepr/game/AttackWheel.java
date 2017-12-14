package sepr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;

public class AttackWheel {
    /**
     * Function to calculate the sector percentages
     *
     * @return hashMap of each sector and its percentage
     */
    private static HashMap<String, Double> sectorCalc() {
        int attackingTroops = 6; // Replace with getters once made
        int defendingTroops = 4;
        double strength = ((double)attackingTroops - (double)defendingTroops) / 100;
        double normal = 0.25 + (strength);
        double critical = 0.1 + (strength);
        double failed = 0.15 + (strength);

        // Ensures that inverse is not created
        double normalMin = 0.1;
        double normalMax = 0.4;
        double criticalMin = 0.01;
        double criticalMax = 0.2;
        double failedMin = 0.1;
        double failedMax = 0.3;

        if (normal < normalMin) {
            normal = normalMin;
        } else if (normal > normalMax) {
            normal = normalMax;
        }

        if (critical < criticalMin) {
            critical = criticalMin;
        } else if (critical > criticalMax) {
            critical = criticalMax;
        }

        if (failed < failedMin) {
            failed = failedMin;
        } else if (failed > failedMax) {
            failed = failedMax;
        }

        HashMap<String, Double> sectors = new HashMap<String, Double>();
        sectors.put("normal", normal);
        sectors.put("critical", critical);
        sectors.put("failed", failed);

        return sectors;
    }

    /**
     * Helper function to dynamically create a sector
     *
     * @param shapeRenderer The shapeRenderer object
     * @param color Color of the sector
     * @param lineWidth Line width of the sector
     * @param x origin coordinate
     * @param y origin coordinate
     * @param radius sector radius
     * @param start start degree of sector
     * @param degrees end degree of sector
     *
     * @return shapeRenderer
     */
    private static ShapeRenderer genSector(
            ShapeRenderer shapeRenderer,
            Color color,
            float lineWidth,
            float x,
            float y,
            float radius,
            float start,
            float degrees
    ) {
        // Draw the lines
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        Gdx.gl20.glLineWidth(lineWidth);
        shapeRenderer.arc(x, y, radius, start, degrees);
        shapeRenderer.end();

        // Draw the sector
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.arc(x, y, radius, start, degrees);
        shapeRenderer.end();
        return shapeRenderer;
    }

    /**
     * Generates the attack wheel with sectors in the percent of the circle
     *
     * @return returns a 500x500 texture of the attack wheel
     */
    public static Texture genAttackWheelTexture() {
        HashMap<String, Double> sectors = sectorCalc();
        float normalDegrees = (float)(sectors.get("normal") * 360);
        float criticalDegrees = (float)(sectors.get("critical") * 360);
        float failedDegrees = (float)(sectors.get("failed") * 360);
        float lineWidth = 3.0f;
        float x = 500;
        float y = 500;
        float radius = 200;

        FrameBuffer wheel = new FrameBuffer(Pixmap.Format.RGBA8888, 1000, 500, false);
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        wheel.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        Gdx.gl20.glLineWidth(6.0f);
        shapeRenderer.circle(x, y, radius);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(x, y, radius);
        shapeRenderer.end();

        shapeRenderer = genSector(shapeRenderer, Color.GREEN, lineWidth, x, y, radius, 270, normalDegrees);
        shapeRenderer = genSector(shapeRenderer, Color.GOLD, lineWidth, x, y, radius, normalDegrees + 270, criticalDegrees);
        genSector(shapeRenderer, Color.RED, lineWidth, x, y, radius, normalDegrees + criticalDegrees + 270, failedDegrees);
        wheel.end();

        return wheel.getColorBufferTexture();
    }
}
