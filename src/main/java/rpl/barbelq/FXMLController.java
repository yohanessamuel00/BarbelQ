package rpl.barbelq;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FXMLController implements Initializable {
    
    DBBarbelQ dbModel = new DBBarbelQ();
    
    @FXML
    private Hyperlink btnDaftar;
    
    Alert a = new Alert(AlertType.NONE);
    
    @FXML
    private void buttoncomOnAction(ActionEvent event) throws IOException{
        try {
            Stage stage1 = (Stage) btnDaftar.getScene().getWindow();
            stage1.close();
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
        if (dbModel.isDbConnected()) {
            label.setText("Connected");
        } else {
            label.setText("Not Connected");
        }
    } 
   
     
    public void Login (ActionEvent event) {
        try {
            if (dbModel.isLogin(txtEmail.getText(), txtPassword.getText())) {
                int id = 0;
                Stage stage1 = (Stage) btnDaftar.getScene().getWindow();
                stage1.close();
                Stage primaryStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/User.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Statement statement = dbModel.conection.createStatement();
                ResultSet rs = statement.executeQuery("select id_pengguna from DataPengguna where email ='" +txtEmail.getText()+"'");
                while (rs.next()) {
                    id = rs.getInt("id_pengguna");
                }
                rs.close();
                UserController userController = (UserController)fxmlLoader.getController();
                userController.GetUser(id);
                Scene scene = new Scene(root1);
                scene.getStylesheets().add("/styles/Styles.css");
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                // set alert type 
                a.setAlertType(AlertType.INFORMATION);
                a.setHeaderText("Login Gagal");
                a.setContentText("Email atau Password Salah");
                // show the dialog 
                txtEmail.clear();
                txtPassword.clear();
                a.show(); 
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
