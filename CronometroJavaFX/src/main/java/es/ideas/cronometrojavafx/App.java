package es.ideas.cronometrojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    
    //Método principal para cargar la escena en el escenario de la aplicación.
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
  /*Métodos para obtener de los recursos el fxml y cargarlos en la escena 
    en el metodo start. */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(
                fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}