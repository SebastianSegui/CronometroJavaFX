module es.ideas.cronometrojavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens es.ideas.cronometrojavafx to javafx.fxml;
    exports es.ideas.cronometrojavafx;
}
