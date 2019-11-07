package rpl.barbelq;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ALDO
 */
public class PrimaryHomeController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    
    private int session;
    
    @FXML
    private TextField Changename;
    @FXML
    private TextField Changeusia;
    @FXML
    private TextField Changeemail;
    @FXML
    private PasswordField Changepassword;
    @FXML
    private Button btnLogout;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleUpdate(ActionEvent event) throws SQLException {
        int id = 0;
        if(!Changename.getText().isEmpty()) {
            dbModel.InsertOrUpdate("update DataPengguna set nama = '" +Changename.getText()+ "' where id_pengguna =  "+session+"");
            Changename.setText("");
        }
        if(!Changeusia.getText().isEmpty()) {
            dbModel.InsertOrUpdate("update DataPengguna set usia = '" +Changeusia.getText()+ "' where id_pengguna =  "+session+"");
            Changeusia.setText("");
        }
        if(!Changeemail.getText().isEmpty()) {
            dbModel.rs = dbModel.resultset("select * from DataPengguna where email ='"+Changeemail.getText()+"'");
            while(dbModel.rs.next()){
                id = dbModel.rs.getInt("id_pengguna");
            }
            dbModel.rs.close();
            if(id == 0){
               dbModel.InsertOrUpdate("update DataPengguna set email = '" +Changeemail.getText()+ "' where id_pengguna =  "+session+""); 
            }
            Changeemail.setText("");
        }
        if(!Changepassword.getText().isEmpty()) {
            dbModel.InsertOrUpdate("update DataPengguna set password = '" +Changepassword.getText()+ "' where id_pengguna =  "+session+"");
            Changepassword.setText("");
        }
        if(id!=0){
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("BarbelQ");
            a.setHeaderText(null);
            a.setContentText("Email telah Digunakan");
            // show the dialog 
            a.show(); 
        }else{
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("BarbelQ");
            a.setHeaderText(null);
            a.setContentText("Update Berhasil");
            // show the dialog 
            a.show(); 
        }
        
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        try{
            Stage stage1 = (Stage) btnLogout.getScene().getWindow();
            stage1.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void GetUser(int user) {
        // TODO Auto-generated method stub
        session = user;
    }
}

