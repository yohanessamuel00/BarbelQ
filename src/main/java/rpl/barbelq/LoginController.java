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
    void buttoncomOnAction(ActionEvent event) throws IOException{
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
    void Login (ActionEvent event) {
        try {
            if(!"".equals(txtPassword.getText()) && !"".equals(txtEmail.getText())){
                if(dbModel.isLogin(txtEmail.getText(), txtPassword.getText())){
                    dbModel.rs = dbModel.resultset("select id_pengguna,nama,level from DataPengguna where email ='" +txtEmail.getText()+"'");
                    int id =0;
                    String nama ="";
                    int level = 0;
                    if(dbModel.rs.next()){
                        id = dbModel.rs.getInt("id_pengguna");
                        nama = dbModel.rs.getString("nama");
                        level = dbModel.rs.getInt("level");
                    }
                    dbModel.rs.close();
                    Stage stage1 = (Stage) btnLogin.getScene().getWindow();
                    stage1.close();
                    if(level == 1){
                        panggilHalaman("PrimaryHome", id, nama);

                    }else{
                        panggilHalaman("Admin", id, nama);
                    }
                }else{
                    bantuAlert(null, "Email atau Password Salah");
                }
            }else{
                bantuAlert(null, "Email dan Password Tidak Boleh Kosong");
            }
        } catch (SQLException | IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }
////////////////////////////////////////////////////////////////////////////////
    private void panggilHalaman(String namaHal, int id, String nama) throws IOException, SQLException{
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/"+namaHal+".fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        if(namaHal.equals("PrimaryHome")){
            PrimaryHomeController primaryHome = (PrimaryHomeController)fxmlLoader.getController();           
            Scene scene = new Scene(root1);
            scene.getStylesheets().add("/styles/Styles.css");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryHome.GetUser(id, nama);  
        }else{
            AdminController admin = (AdminController)fxmlLoader.getController();
            admin.GetUser(nama);
            Scene scene = new Scene(root1);
            scene.getStylesheets().add("/styles/Styles.css");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    private void bantuAlert(String header, String isi){
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(header);
        a.setContentText(isi);
        a.show();
    }
    
}