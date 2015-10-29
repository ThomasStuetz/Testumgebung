package at.htl.remotecontrol.gui.controller;

import at.htl.remotecontrol.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Philipp:  18.10.2015  Einfügen eines Login's mit Werteübergabe
 */
public class ControllerStudent implements Initializable {

    @FXML
    TextField tfUsername, tfTeacherIP;

    @FXML
    Button btnLogin;

    private Client client;

    public ControllerStudent() {
    }

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void logIn(ActionEvent event) {
        try {
            client = new Client(tfTeacherIP.getText(), tfUsername.getText());
            client.start();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut(ActionEvent actionEvent) {
        client.stop();
    }

}
