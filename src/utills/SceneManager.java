package utills;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class SceneManager {
    private Scene mainScene;
    private HashMap<String, Pane> scenes;
    public SceneManager(Scene mainScene) {
        this.mainScene = mainScene;
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
