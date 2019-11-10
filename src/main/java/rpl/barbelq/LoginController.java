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
    Alert a = new Alert(AlertType.NONE);
    
    @FXML
    private Hyperlink btnDaftar;
    
    @FXML
    private Button btnLogin;
    
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
    
    public void Login (ActionEvent event) {
        try {
            if(!"".equals(txtPassword.getText()) && !"".equals(txtEmail.getText())){
                if(txtEmail.getText().equals("admin") && txtPassword.getText().equals("admin")){
                    Stage stage1 = (Stage) btnLogin.getScene().getWindow();
                    stage1.close();
                    Stage primaryStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Admin.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Scene scene = new Scene(root1);
                    scene.getStylesheets().add("/styles/Styles.css");
                    primaryStage.setScene(scene);
                    primaryStage.show();   
                }else{
                    if(dbModel.isLogin(txtEmail.getText(), txtPassword.getText())){
                        int session = 0;
                        String namaUser = "";
                        dbModel.rs = dbModel.resultset("select id_pengguna, nama from DataPengguna where email ='" +txtEmail.getText()+"'");
                        if (dbModel.rs.next()) {
                            session = dbModel.rs.getInt("id_pengguna");
                            namaUser = dbModel.rs.getString("nama");
                        }
                        dbModel.rs.close();
                        Stage stage1 = (Stage) btnLogin.getScene().getWindow();
                        stage1.close();
                        Stage primaryStage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PrimaryHome.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        PrimaryHomeController primaryHome = (PrimaryHomeController)fxmlLoader.getController();
                        primaryHome.GetUser(session, namaUser);
                        Scene scene = new Scene(root1);
                        scene.getStylesheets().add("/styles/Styles.css");
                        primaryStage.setScene(scene);
                        primaryStage.show(); 
                    }else{
                        a.setAlertType(AlertType.INFORMATION);
                        a.setTitle("Login Gagal");
                        a.setHeaderText(null);
                        a.setContentText("Email atau Password Salah");
                        a.showAndWait();
                    }
                }
            }else{
               a.setAlertType(AlertType.INFORMATION);
                a.setTitle("Login Gagal");
                a.setHeaderText(null);
                a.setContentText("Email dan Password Tidak Boleh Kosong");
                // show the dialog 
                a.showAndWait(); 
            }
        } catch (SQLException | IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    } 
    
}