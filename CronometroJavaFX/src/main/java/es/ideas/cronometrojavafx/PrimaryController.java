package es.ideas.cronometrojavafx;

import java.net.URL;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PrimaryController implements Initializable{

    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnParar;
    @FXML
    private Button btnReiniciar;
    @FXML
    private Label lbCronometro;
    @FXML
    private Label lbReloj;
    
    private Timeline cronometro;
    private int segundos = 0;
    private int minutos = 0;
    private int horas = 0;
    
    private Timeline reloj;
    private final SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Necesario para que el cronometro empiece en 0:0:0
        actualizarCronometro();
        
        //Timeline del reloj (hora actual)
        reloj = new Timeline();
        reloj.setCycleCount(Animation.INDEFINITE);
        reloj.getKeyFrames().add(new KeyFrame(Duration.millis(1000), (ActionEvent evento) -> {
            lbReloj.setText(formato.format(new Date()));
        }));
        reloj.play();
        
        //Timeline del cronometro
        cronometro = new Timeline();
        cronometro.setCycleCount(Animation.INDEFINITE);
        cronometro.getKeyFrames().add(new KeyFrame(Duration.millis(1000), (ActionEvent evento) -> {
            segundos--;
            comprobarFormato();
            actualizarCronometro();
        }));
        
    }
    
    //Método para actualizar el label del crónometro.
    public void actualizarCronometro(){
        lbCronometro.setText(horas + ":" + minutos + ":" + segundos);
    }
    
    /* Método para comprobar el formato del cronómetro.
       Tiene en cuenta que si el siguiente segundo es el -1, los segundos pasan
       a ser de nuevo 59 y actualiza los minutos restándole 1.
       Hace lo mismo con los minutos y las horas, si el siguiente minuto es -1,
       los minutos pasan a ser de nuevo 59 y se resta 1 hora.
    */
    public void comprobarFormato(){
        if (segundos==-1){
            segundos=59;
            minutos--;
        }
        if (minutos==-1){
            minutos=59;
            horas--;
        }
    }

    //OnAction para los botones de iniciar, parar y reiniciar cronómetro.
    @FXML
    private void iniciarCrono(ActionEvent event) {
        cronometro.play();
    }

    @FXML
    private void pararCrono(ActionEvent event) {
        cronometro.pause();
    }

    @FXML
    private void reiniciarCrono(ActionEvent event) {
        actualizarCronometro();
    }
}
