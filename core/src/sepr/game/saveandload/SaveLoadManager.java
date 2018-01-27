package sepr.game.saveandload;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class SaveLoadManager {

    private static String SAVE_FILE_PATH = "";

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

    public SaveLoadManager() {
        String home = System.getProperty("user.home");

        String path = home + File.separator + "Bachelors-of-Domination" + File.separator + "saves" + File.separator + "saves.json";

        boolean directoryExists = new File(path).exists();

        if(directoryExists){
            SAVE_FILE_PATH = path;
        }else{
            File file = new File(path);
            try {
                file.createNewFile();
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
        return false;
    }

}
