package sepr.game;

/**
 * Created by Dom's Surface Mark 2 on 09/12/2017.
 */
public class AudioHandler {
    private static float MUSIC_PERCENTAGE = 1; // float value between 0 and 1 representing percentage amount of music volume
    private static float FX_PERCENTAGE = 1; // float value between 0 and 1 representing percentage amount of music volume


    public static void setMusicPercentage(float musicPercentage) {
        MUSIC_PERCENTAGE = musicPercentage;
    }

    public static void setFxPercentage(float fxPercentage) {
        FX_PERCENTAGE = fxPercentage;
    }
}
