package es.ideas.cronometrojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.text.Font;

/**
 * Clase principal de la aplicación encargada de crear el escenario, la escena 
 * y de cargar el contenido.
 * 
 * @author Alex
 * @see <a href="https://github.com/iAleZz"> Repositorio
 *      de Alex</a>
 * @author Sebastián
 * @see <a href="https://github.com/SebastianSegui"> Repositorio
 *      de Sebastián</a>
 * @see <a href="https://github.com/SebastianSegui/ColoresJavaFX"> Repositorio
 *      del proyecto</a>
 * 
 * Clase Controlador: {@link es.ideas.cronometrojavafx.PrimaryController}.
 */
public class App extends Application {

    private static Scene scene;
    
    /**
     * Recibe el stage y le carga la vista mediante el método loadFXML.
     * 
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {       
        scene = new Scene(loadFXML("primary"), 600, 400);
        stage.setTitle("Temporizador");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
  /*Métodos para obtener de los recursos el fxml y cargarlos en la escena 
    en el metodo start. */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(
                fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static void main(String[] args) {
        launch();
    }
}