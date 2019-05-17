package utills;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class SceneManager {
    private Scene mainScene;
    private static SceneManager link;
    public static SceneManager getInstance(){
        if(link == null){
            link = new SceneManager();
        }
        return link;
    }
    public void setUpMainSccene(Scene mainScene){
        this.mainScene = mainScene;
    }
    private HashMap<String, Pane> scenes;
    private SceneManager(){
        scenes = new HashMap<String, Pane>();
    }
    public void addScreen(String name, Pane pane){
        scenes.put(name, pane);
    }
    public void removeScreen(String name){
        scenes.remove(name);
    }
    public void activateScreen(String name){
        mainScene.setRoot(scenes.get(name));
    }

}
