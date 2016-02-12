package at.htl.remotecontrol.client.view;

import at.htl.remotecontrol.client.Client;
import at.htl.remotecontrol.client.Exam;
import at.htl.remotecontrol.common.MyUtils;
import at.htl.remotecontrol.common.Pupil;
import at.htl.remotecontrol.common.fx.FxUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @timeline .
 * 15.10.2015: PON 001  created class
 * 18.10.2015: PHI 030  created login with text boxes
 * 14.10.2015: MET 030  new GUI design and created function setControls(value)
 * 24.10.2015: PON 005  added logout method
 * 30.10.2015: MET 010  expanded to login and logout
 * 31.10.2015: MET 020  creating Client packages and submitting them to client
 * 06.11.2015: PON 010  expansion to the password field
 * 19.11.2015: PON 015  GUI extended by the TextField "Port" and implements corresponding functions
 * 29.11.2015: PHI 040  display of messages in the GUI with setMsg()
 * 03.01.2016: MET 005  setMsg() improved by using FxUtils
 * 12.02.2016: MET 005  activation and deactivation of controls depending on login and logout
 * 12.02.2016: MET 030  display of messages in the GUI with setMsg() enormously improved
 */
public class Controller implements Initializable {

    //region Controls
    @FXML
    TextField tfServerIP;
    @FXML
    TextField tfPort;
    @FXML
    TextField tfEnrolmentID;
    @FXML
    TextField tfCatalogNumber;
    @FXML
    TextField tfFirstName;
    @FXML
    TextField tfLastName;
    @FXML
    TextField tfPathOfProject;
    @FXML
    Button btnTestConnection;
    @FXML
    Button btnChooseDirectory;
    @FXML
    Button btnLogin;
    @FXML
    Button btnLogout;
    @FXML
    Label lbAlert;
    //endregion

    private Client client;
    private boolean loggedIn;

    public void initialize(URL location, ResourceBundle resources) {
        //this.loggedIn = false;
        setControls(true);
    }

    @FXML
    public void testConnection() {

    }

    /**
     * Shows a dialog-screen to choose the working-directory where
     * the project will be and saves the path of it.
     */
    @FXML
    public void chooseProjectDirectory() {
        tfPathOfProject.setText(
                FxUtils.chooseDirectory("Select Project Directory", null).getPath());
    }

    /**
     * Connects the client with the server.
     */
    @FXML
    public void login() {
        if (setExam()) {
            setControls(false);
            //client.connectToServer(Exam.getInstance().getServerAddress());
            //Packet packet = new Packet(
            //      Packet.Action.GET_PUPIL,
            //    "Sign in: " + Exam.getInstance().getPupil().toString());
            //packet.put(Packet.Resource.PUPIL, Exam.getInstance().getPupil());
            //client.sendPacket(packet);
            /*try {
                if (!loggedIn) {
                    btnLogin.setDisable(true);
                    btnLogOut.setDisable(false);
                    client = new Client(new LoginPackage(
                            tfUsername.getText(),
                            pfPassword.getText(),
                            tfTeacherIP.getText(),
                            tfPath.getText(),
                            port
                    ));
                    client.start();
                    loggedIn = true;
                }
            } catch (Exception e) {

            }*/
            setMsg("Signed in!", false);
        }
    }

    /**
     * Disconnects from the server.
     */
    @FXML
    public void logout() {
        setControls(true);
        setMsg("Test successfully submitted", false);
    }

    /**
     * Enables and disables controls depending on login and logout
     *
     * @param value enable controls?
     */
    public void setControls(boolean value) {
        tfServerIP.setEditable(value);
        tfPort.setEditable(value);
        tfEnrolmentID.setEditable(value);
        tfCatalogNumber.setEditable(value);
        tfFirstName.setEditable(value);
        tfLastName.setEditable(value);
        tfPathOfProject.setEditable(value);
        btnChooseDirectory.setDisable(!value);
        btnLogin.setDisable(!value);
        btnLogout.setDisable(value);
    }

    /**
     * Saves the valid values in the repository "Exam"
     *
     * @return successful
     */
    public boolean setExam() {
        if (validForm()) {
            Exam.getInstance().setServerIP(tfServerIP.getText());
            Exam.getInstance().setPort(Integer.valueOf(tfPort.getText()));
            Exam.getInstance().setPupil(new Pupil(
                    Integer.valueOf(tfCatalogNumber.getText()),
                    tfEnrolmentID.getText(),
                    tfFirstName.getText(),
                    tfLastName.getText(),
                    tfPathOfProject.getText())
            );
            return true;
        }
        return false;
    }

    /**
     * Checks whether the user has entered valid values
     *
     * @return is form valid?
     */
    public boolean validForm() {
        String serverIP = tfServerIP.getText();
        int port = MyUtils.strToInt(tfPort.getText());
        String enrolmentID = tfEnrolmentID.getText();
        int catalogNumber = MyUtils.strToInt(tfCatalogNumber.getText());
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        String pathOfProject = tfPathOfProject.getText();
        boolean validity = false;
        if (serverIP.isEmpty()) {
            setMsg("Specify the IP-Address of the server!", true);
        } else if ((serverIP.split(".").length != 4 && !serverIP.equals("localhost"))
                || serverIP.length() > 15) {
            setMsg("Invalid IP-Address!", true);
        } else if (port < 1) {
            setMsg("Invalid Port!", true);
        } else if (enrolmentID.isEmpty()) {
            setMsg("Enter your enrolment id", true);
        } else if (enrolmentID.length() >= 10) {
            setMsg("The enrolment id is too long!", true);
        } else if (catalogNumber < 1) {
            setMsg("Invalid catalog number!", true);
        } else if (firstName.isEmpty() || firstName.length() > 20) {
            setMsg("Enter your correct first name", true);
        } else if (lastName.isEmpty() || lastName.length() > 20) {
            setMsg("Enter your correct last name", true);
        } else if (pathOfProject.isEmpty()) {
            setMsg("Specify the path of project!", true);
        } else {
            validity = true;
        }
        return validity;
    }

    /**
     * colors the logout-button to see easier if the finished test will
     * be sent on logout.
     *
     * @param event Information from the click on the ToggleButton.
     */
    /*public void handleSelect(ActionEvent event) {
        if (((ToggleButton) event.getSource()).isSelected()) {
            btnLogOut.setStyle("-fx-background-color: lawngreen");
        } else {
            btnLogOut.setStyle("-fx-background-color: crimson");
        }
    }
    */

    /**
     * Sets an message on the screen of the student.
     *
     * @param text  specifies the message to show
     * @param error TRUE   if it is an error-message
     *              FALSE  if it is a success-message
     */
    private void setMsg(String text, boolean error) {
        FxUtils.setMsg(lbAlert, text, error);
    }

}
