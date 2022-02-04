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
        //necesario para que el cronometro empiece en 0:0:0 NO TOCAR
        actualizarCronometro();
        
        //Esto es el timeline del reloj NO TOCAR
        reloj = new Timeline();
        reloj.setCycleCount(Animation.INDEFINITE);
        reloj.getKeyFrames().add(new KeyFrame(Duration.millis(1000), (ActionEvent evento) -> {
            lbReloj.setText(formato.format(new Date()));
        }));
        reloj.play();
        
        //Esto es el timeline del cronometro HAZ LO QUE QUIERAS xD
        cronometro = new Timeline();
        cronometro.setCycleCount(Animation.INDEFINITE);
        cronometro.getKeyFrames().add(new KeyFrame(Duration.millis(1000), (ActionEvent evento) -> {
            segundos--;
            comprobarFormato();
            actualizarCronometro();
        }));
        cronometro.play();
    }
    
    public void actualizarCronometro(){
        lbCronometro.setText(horas + ":" + minutos + ":" + segundos);
    }
    
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
}
