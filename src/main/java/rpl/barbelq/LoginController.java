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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    
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
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @FXML
    private TextField txtEmail;
   
    @FXML
    private PasswordField txtPassword;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (dbModel.isDbConnected()) {
            System.out.println("Connected");
        } else {
            System.out.println("Not Connected");
        }
    } 
   
     
    public void Login (ActionEvent event) {
        try {
            if (dbModel.isLogin(txtEmail.getText(), txtPassword.getText())) {
                int id = 0;
                dbModel.rs = dbModel.resultset("select id_pengguna from DataPengguna where email ='" +txtEmail.getText()+"'");
                while (dbModel.rs.next()) {
                   id = dbModel.rs.getInt("id_pengguna");
                }
                dbModel.rs.close();
                Stage stage1 = (Stage) btnDaftar.getScene().getWindow();
                stage1.close();
                Stage primaryStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PrimaryHome.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                PrimaryHomeController primaryHome = (PrimaryHomeController)fxmlLoader.getController();
                primaryHome.GetUser(id  );
                Scene scene = new Scene(root1);
                scene.getStylesheets().add("/styles/Styles.css");
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                // set alert type 
                a.setAlertType(AlertType.INFORMATION);
                a.setTitle("Login Gagal");
                a.setHeaderText(null);
                a.setContentText("Email atau Password Salah");
                // show the dialog 
                a.showAndWait();
            }
        } catch (SQLException | IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        // TODO Auto-generated catch block
        
    }   
}