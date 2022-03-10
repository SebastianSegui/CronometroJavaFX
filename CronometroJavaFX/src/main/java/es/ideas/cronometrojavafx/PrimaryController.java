package es.ideas.cronometrojavafx;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.io.File;
import javafx.beans.property.BooleanProperty;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Clase controladora de la aplicacion.
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
 * Clase App: {@link es.ideas.cronometrojavafx.App}.
 */
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
    @FXML
    private Button btnSubirHoras;
    @FXML
    private Button btnBajarHoras;
    @FXML
    private Button btnSubirMinutos;
    @FXML
    private Button btnBajarMinutos;
    @FXML
    private Button btnSubirSegundos;
    @FXML
    private Button btnBajarSegundos;

    private Timeline cronometro;
    private Timeline reloj;
    
    private int segundos;
    private int minutos;
    private int horas;
    
    private final SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
    
  /* Utilizamos una SimpleBooleanProperty para controlar los botones de una
    forma más comoda */
    private BooleanProperty booleanIniciar = 
            new SimpleBooleanProperty(false);
    private BooleanProperty booleanParar = 
            new SimpleBooleanProperty(false);
    private BooleanProperty booleanReiniciar = 
            new SimpleBooleanProperty(false);
    
    /**
     * Encargado de cargar todos los recursos y funcionalidades de la app.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Timeline del cronómetro
        cronometro = new Timeline();
        cronometro.setCycleCount(Animation.INDEFINITE);
        cronometro.getKeyFrames().add(new KeyFrame(Duration.millis(1000), 
                (ActionEvent evento) -> {
            segundos--;
            comprobarFormato();
            actualizarCronometro();
        }));
        
        //Timeline del reloj (hora actual)
        reloj = new Timeline();
        reloj.setCycleCount(Animation.INDEFINITE);
        reloj.getKeyFrames().add(new KeyFrame(Duration.millis(1000), 
                (ActionEvent evento) -> {
            lbReloj.setText(formato.format(new Date()));
        }));
        reloj.play();
              
        
        /*Bind para habilitar/deshabilitar el botón Iniciar */
        btnIniciar.disableProperty().
                bind(booleanIniciar.not());
        
        btnParar.disableProperty().
                bind(booleanParar.not());
        
        btnReiniciar.disableProperty().
                bind(booleanReiniciar.not());
    }
      
    /**
     * Método para comprobar el formato del cronómetro.
     * Tiene en cuenta que si el siguiente segundo es el -1, los segundos pasan
     * a ser de nuevo 59 y actualiza los minutos restándole 1.
     * Hace lo mismo con los minutos y las horas, si el siguiente minuto es -1,
     * los minutos pasan a ser de nuevo 59 y se resta 1 hora.
     * También reproduce un sonido y finaliza el cronómetro cuando llega a 0.
     */
    public void comprobarFormato(){
        if(segundos==-1){
            segundos=59;
            minutos--;
        }
        
        if(minutos==-1){
            minutos=59;
            horas--;
        }
        
        if(horas == 0 && minutos == 0 && segundos == 0){
            reproducirSonido();
            segundos = 0;
            minutos = 0;
            horas = 0;
               
            cronometro.stop();
            actualizarCronometro();
            activarBotones();
            
            booleanIniciar.set(true);
            booleanParar.set(false);
            booleanReiniciar.set(false);
        }
    }
   
    /**
     * Método para actualizar la label del crónometro.
     * Tiene en cuenta que si las horas, minutos o segundos son inferiores a 10,
     * nos añade un 0 delante del número para que de un aspecto más real 
     * NOTA: Los 0 delante del numero se consiguen mediante el uso de un 
     * operador ternario, que vendria a ser como un if else
     */
    public void actualizarCronometro(){
        String texto = (horas<=9?"0":"")+horas+":"+
                (minutos<=9?"0":"")+minutos+":"+
                (segundos <= 9?"0":"")+segundos;
        lbCronometro.setText(texto);
        
        if(segundos == 0 && minutos == 0 && horas == 0){
            booleanIniciar.set(false);
        } else if(cronometro.getStatus().toString().equals("STOPPED")) {
            booleanIniciar.set(true);
        }
    }
        
    @FXML
    private void iniciarCrono(ActionEvent event) {
        cronometro.play();
        booleanIniciar.set(false);
        booleanParar.set(true);
        booleanReiniciar.set(true);
        desactivarBotones();
    }

    //Método para pausar el cronómetro
    @FXML
    private void pararCrono(ActionEvent event) {
        booleanParar.set(false);
        booleanIniciar.set(true);
        cronometro.pause();
    }

    //Método para resetear el cronómetro y poner todo a 0 como en un inicio.
    @FXML
    private void reiniciarCrono(ActionEvent event) {
        segundos = 0;
        minutos = 0;
        horas = 0;
               
        cronometro.stop();
        actualizarCronometro();
        activarBotones();
        
        booleanIniciar.set(true);
        booleanParar.set(false);
        booleanReiniciar.set(false);
    }
    
    private void activarBotones(){
            btnSubirHoras.setDisable(false);
            btnSubirMinutos.setDisable(false);
            btnSubirSegundos.setDisable(false);
            btnBajarHoras.setDisable(false);
            btnBajarMinutos.setDisable(false);
            btnBajarSegundos.setDisable(false); 
    }
    
    private void desactivarBotones(){
            btnSubirHoras.setDisable(true);
            btnSubirMinutos.setDisable(true);
            btnSubirSegundos.setDisable(true);
            btnBajarHoras.setDisable(true);
            btnBajarMinutos.setDisable(true);
            btnBajarSegundos.setDisable(true); 
    }
    
    private void reproducirSonido(){
        try {
            
        File archivo = new File("src/main/resources/es/ideas/sounds/alarm.wav");
        
        AudioInputStream sonido = AudioSystem.getAudioInputStream(archivo);
        
        Clip clip;
        clip = AudioSystem.getClip();
        clip.open(sonido);
        clip.setFramePosition(0);
        clip.start();
        }catch (Exception ex){ex.printStackTrace();}
    }

    @FXML
    private void subirHoras(ActionEvent event) {  
        if (horas <= 24) {
            if (horas == 24){
                horas = 0;
            } else {
                horas++;
            }          
            actualizarCronometro();
        } 
        if (horas == 24){
            minutos = 0;
            segundos = 0;
            actualizarCronometro();
        }
    }

    @FXML
    private void bajarHoras(ActionEvent event) {
        if (horas <= 24) {
            if (horas == 0){
                horas = 24;
            } else {
                horas--;
            }
            actualizarCronometro();
        }
        if (horas == 24){
            minutos = 0;
            segundos = 0;
            actualizarCronometro();
        }
    }

    @FXML
    private void subirMinutos(ActionEvent event) {
        if (horas != 24 && minutos < 60 && minutos >= 0) {
            if (minutos == 59){
                minutos = 0;
            } else {
                minutos++;
            }
            actualizarCronometro();
        } 
    }

    @FXML
    private void bajarMinutos(ActionEvent event) {
        if (horas != 24 && minutos < 60 && minutos >= 0) {
            if (minutos == 0){
                minutos = 59;
            } else {
                minutos--;
            }
            actualizarCronometro();
        } 
    }

    @FXML
    private void subirSegundos(ActionEvent event) {
        if (horas != 24 && segundos < 60 && segundos >= 0) {
            if (segundos == 59){
                segundos = 0;
            } else {
                segundos++;
            }
            actualizarCronometro();
        } 
    }

    @FXML
    private void bajarSegundos(ActionEvent event) {
        if (horas != 24 && segundos < 60 && segundos >= 0) { 
            if (segundos == 0){
                segundos = 59;
            } else {
                segundos--;
            }
            actualizarCronometro();
        }   
    }
}
