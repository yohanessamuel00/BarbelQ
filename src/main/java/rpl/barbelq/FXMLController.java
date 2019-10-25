package rpl.barbelq;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class FXMLController implements Initializable {
    
    public LoginModel loginModel = new LoginModel();
    
    @FXML
    private void buttoncomOnAction(ActionEvent event) throws IOException{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Daftar.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private Label label;
   
    @FXML
    private TextField txtEmail;
   
    @FXML
    private PasswordField txtPassword;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (loginModel.isDbConnected()) {
            label.setText("Connected");
        } else {
            label.setText("Not Connected");
        }
    } 
     
    public void Login (ActionEvent event) {
        try {
            if (loginModel.isLogin(txtEmail.getText(), txtPassword.getText())) {
                Stage primaryStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/User.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Scene scene = new Scene(root1);
                scene.getStylesheets().add("/styles/Styles.css");
                primaryStage.setScene(scene);
                primaryStage.show();
                
                
            } else {
                label.setText("username and password is not correct");
            }
        } catch (SQLException e) {
            label.setText("username and password is not correct");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   
}
