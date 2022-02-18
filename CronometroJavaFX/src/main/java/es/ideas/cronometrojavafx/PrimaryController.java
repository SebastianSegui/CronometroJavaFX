package es.ideas.cronometrojavafx;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.File;
import javafx.scene.layout.AnchorPane;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PrimaryController implements Initializable{

    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnParar;
    @FXML
    private Button btnReiniciar;
    @FXML
    private Button btnReanudar;
    @FXML
    private Label lbCronometro;
    @FXML
    private Label lbReloj;
    @FXML
    private TextField tfHoras;
    @FXML
    private TextField tfMinutos;
    @FXML
    private TextField tfSegundos;
    
    private Timeline cronometro;
    private Timeline reloj;
    
    private int segundos;
    private int minutos;
    private int horas;
    
    private final SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
    
  /* Utilizamos una SimpleBooleanProperty para controlar los botones de una
    forma más comoda */
    private SimpleBooleanProperty booleanIniciar = 
            new SimpleBooleanProperty(true);
    private SimpleBooleanProperty booleanParar = 
            new SimpleBooleanProperty(false);
    private SimpleBooleanProperty booleanReanudar = 
            new SimpleBooleanProperty(false);
    private SimpleBooleanProperty booleanReiniciar = 
            new SimpleBooleanProperty(false);
    
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
        
        //Controlamos que los TextField admitan solo números
        tfSegundos.textProperty().addListener((ObservableValue<? extends 
                String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfSegundos.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        tfMinutos.textProperty().addListener((ObservableValue<? extends 
                String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfMinutos.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }); 
        tfHoras.textProperty().addListener((ObservableValue<? extends 
                String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfHoras.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });        
        
        /* Deshabilita el botón de Añadir cuando los parámetros
           horas, minutos y segundos no se han introducido aún*/
        btnIniciar.disableProperty().
                bind(tfSegundos.textProperty().isEmpty().
                or(tfMinutos.textProperty().isEmpty().
                or(tfHoras.textProperty().isEmpty()    
                )));
        
        btnParar.disableProperty().
                bind(tfSegundos.textProperty().isEmpty().
                or(tfMinutos.textProperty().isEmpty().
                or(tfHoras.textProperty().isEmpty().    
                or(booleanParar.not()))));
                
        btnReanudar.disableProperty().
                bind(tfSegundos.textProperty().isEmpty().
                or(tfMinutos.textProperty().isEmpty().
                or(tfHoras.textProperty().isEmpty().    
                or(booleanReanudar.not()))));
        
        btnReiniciar.disableProperty().
                bind(tfSegundos.textProperty().isEmpty().
                or(tfMinutos.textProperty().isEmpty().
                or(tfHoras.textProperty().isEmpty().    
                or(booleanReiniciar.not()))));
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
        
        if(horas == 0 && minutos == 0 && segundos == 0){
            reproducirSonido();
            booleanParar.set(false);
            cronometro.stop();
        }
    }
   
  /*Método para actualizar la label del crónometro.
    Tiene en cuenta que si las horas, minutos o segundos son inferiores a 10,
    nos añade un 0 delante del número para que de un aspecto más real 
    NOTA: Los 0 delante del numero se consiguen mediante el uso de un 
    operador ternario, que vendria a ser como un if else */
    public void actualizarCronometro(){
        String texto = (horas<=9?"0":"")+horas+":"+(minutos<=9?"0":"")+minutos+":"+(segundos <= 9?"0":"")+segundos;
        lbCronometro.setText(texto);
    }
       
    //OnAction para los botones de iniciar, parar y reiniciar cronómetro.
    @FXML
    private void iniciarCrono(ActionEvent event) {
        //Convierte los números introducidos en los textfield a "int"
        segundos = Integer.parseInt(tfSegundos.getText());
        minutos = Integer.parseInt(tfMinutos.getText());
        horas = Integer.parseInt(tfHoras.getText());
        
        /*Bind para habilitar/deshabilitar el botón Iniciar teniendo en cuenta
          si los textfield están rellenados o no */
        booleanIniciar.set(false);
        btnIniciar.disableProperty().bind((
                tfSegundos.textProperty().isEmpty()).
                or(tfMinutos.textProperty().isEmpty()).
                or(tfHoras.textProperty().isEmpty()).
                or(booleanIniciar.not()));
        
        cronometro.play();
        booleanParar.set(true);
        booleanReiniciar.set(true);
        tfSegundos.setDisable(true);
        tfMinutos.setDisable(true);
        tfHoras.setDisable(true);
    }

    //Método para pausar el cronómetro
    @FXML
    private void pararCrono(ActionEvent event) {
        booleanParar.set(false);
        booleanReanudar.set(true);
        cronometro.pause();
    }
    
    //Método para reanudar el cronómetro
    @FXML
    private void reanudarCrono(ActionEvent event) {
        booleanReanudar.set(false);
        booleanParar.set(true);
        cronometro.play();
    }

    //Método para resetear el cronómetro y poner todo a 0 como en un inicio.
    @FXML
    private void reiniciarCrono(ActionEvent event) {
        segundos = 0; tfSegundos.setText(""); tfSegundos.setDisable(false);
        minutos = 0; tfMinutos.setText(""); tfMinutos.setDisable(false);
        horas = 0; tfHoras.setText(""); tfHoras.setDisable(false);
               
        cronometro.stop();
        comprobarFormato();
        actualizarCronometro();
        
        booleanIniciar.set(true);
        booleanParar.set(false);
        booleanReanudar.set(false);
        booleanReiniciar.set(false);
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
}
