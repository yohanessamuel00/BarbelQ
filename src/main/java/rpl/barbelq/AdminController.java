/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Krisna
 */
public class AdminController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    
    private int id;
    @FXML
    private TableView<Saran> tabelSaran;
   
    @FXML
    private TableColumn<Saran, Integer> colidSaran;
    
    @FXML
    private TableColumn<Saran, String> colMakanan;
        
    @FXML
    private TableColumn<Saran, String> colAktivitas;
 
    @FXML
    private TableColumn<Saran, String> colKategori;

    
    @FXML
    private Group tampilUbah,tampilTambah;

    @FXML
    private TextField txtaktivitas, txtmakanan,txtkategori;
    @FXML
    private TextField txtubahAktivitas;
    @FXML
    private TextField txtubahMakanan;
    @FXML
    private TextField txtubahKategori;
    @FXML
    private Label namaUser;
    @FXML
    private Label namaUser1;
    @FXML
    private Button btnBatal;
    @FXML
    private Button btnLogout;
    
    
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colidSaran.setCellValueFactory(new PropertyValueFactory("id_saran"));
        colMakanan.setCellValueFactory(new PropertyValueFactory("makanan"));
        colAktivitas.setCellValueFactory(new PropertyValueFactory("aktivitas"));
        colKategori.setCellValueFactory(new PropertyValueFactory("kategori"));

        try {
            dbModel.rs = dbModel.resultset("SELECT * FROM Saran");
            ObservableList<Saran> saran = FXCollections.observableArrayList();
            while (dbModel.rs.next()) {
                Saran srn = new Saran();
                srn.setId_saran(new SimpleIntegerProperty(dbModel.rs.getInt("id_saran")));
                srn.setMakanan(dbModel.rs.getString("makanan"));
                srn.setAktivitas(dbModel.rs.getString("aktivitas"));
                srn.setKategori(dbModel.rs.getString("kategori"));
                saran.add(srn);
            }
            tabelSaran.setItems(saran);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final ContextMenu cxMenu = new ContextMenu();
        MenuItem cxMenuItemEdit = new MenuItem("Ubah Data");
        cxMenu.getItems().add(cxMenuItemEdit);
        MenuItem cxMenuItemDelete = new MenuItem("Hapus Data");
        cxMenu.getItems().add(cxMenuItemDelete);
        
        tabelSaran.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    cxMenu.show(tabelSaran, t.getScreenX(), t.getScreenY());
                }
                if (t.getButton() == MouseButton.PRIMARY) {
                    if (cxMenu.isShowing()) {
                        cxMenu.hide();
                    }
                }
            }
        });

        
        cxMenuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Saran srn = tabelSaran.getSelectionModel().getSelectedItem();
                int index = tabelSaran.getSelectionModel().getSelectedIndex();
                dbModel.InsertOrUpdate("DELETE FROM Saran WHERE id_saran ='" + srn.getId_saran() + "'");
                tabelSaran.getItems().remove(index);
                System.out.println(srn.getId_saran());
            }
        });
        
        cxMenuItemEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tampilTambah.visibleProperty().set(false);
                tabelSaran.disableProperty().set(true);
                cxMenu.hide();
                final Saran srn = tabelSaran.getSelectionModel().getSelectedItem();
                id = srn.getId_saran();
                try {
                    dbModel.rs = dbModel.resultset("Select * from Saran where id_saran = "+srn.getId_saran()+" ");
                    if(dbModel.rs.next()){
                        txtubahAktivitas.promptTextProperty().set(dbModel.rs.getString("aktivitas"));
                        txtubahAktivitas.setText("");
                        txtubahMakanan.promptTextProperty().set(dbModel.rs.getString("makanan"));
                        txtubahMakanan.setText("");
                        txtubahKategori.promptTextProperty().set(dbModel.rs.getString("kategori"));
                        txtubahKategori.setText("");  
                    }
                    dbModel.rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }

    @FXML
    void handleUbah(ActionEvent event) throws SQLException {
        if(txtubahAktivitas.getText().equals("") || txtubahMakanan.getText().equals("") || txtubahKategori.getText().equals("")){
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("BarbelQ");
            a.setHeaderText(null);
            a.setContentText("Field Tidak Boleh Kosong");
            a.show();
        }else{
            if(cekData(txtubahAktivitas,txtubahMakanan)){
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("BarbelQ");
                a.setHeaderText(null);
                a.setContentText("Data Tidak Boleh Sama");
                a.show(); 
            }else{
                int index = tabelSaran.getSelectionModel().getSelectedIndex();
                ObservableList<Saran> saran = tabelSaran.getItems();
                dbModel.InsertOrUpdate("update Saran set makanan= '"+txtubahMakanan.getText()+"',aktivitas= '"+txtubahAktivitas.getText()+"',kategori= '"+txtubahKategori.getText()+"' where id_saran ="+id+" ");

                Saran srn = new Saran();
                srn.setAktivitas(txtubahAktivitas.getText());
                srn.setMakanan(txtubahMakanan.getText());
                srn.setKategori(txtubahKategori.getText());

                saran.set(index, srn);
                tabelSaran.disableProperty().set(false);
                tampilTambah.visibleProperty().set(true);
            }
        }
        
    }
    
    private boolean cekData(TextField aktivitas, TextField makanan) throws SQLException{
        boolean hasil = false;
        dbModel.resultset("select * from Saran ");
        while(dbModel.rs.next()){ 
            hasil = aktivitas.getText().equalsIgnoreCase(dbModel.rs.getString("aktivitas")) && makanan.getText().equalsIgnoreCase(dbModel.rs.getString("makanan"));
            if(hasil){ 
                break;
            }
        }
        dbModel.rs.close();
        return hasil;
    }
    @FXML
    void handleTambah(ActionEvent event) throws SQLException {
        
        if(txtaktivitas.getText().equals("") || txtmakanan.getText().equals("") || txtkategori.getText().equals("")){
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("BarbelQ");
            a.setHeaderText(null);
            a.setContentText("Field Tidak Boleh Kosong");
            a.show();
        }else{
            if(cekData(txtaktivitas,txtmakanan)){
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("BarbelQ");
                a.setHeaderText(null);
                a.setContentText("Data Tidak Boleh Sama");
                a.show(); 
            }else{
                dbModel.InsertOrUpdate("insert into Saran(makanan,aktivitas,kategori) values('"+txtmakanan.getText()+"','"+txtaktivitas.getText()+"','"+txtkategori.getText()+"')");
                int hsl = 0;
                dbModel.resultset("select MAX(id_saran) as hasil from Saran ");
                if(dbModel.rs.next()){
                    hsl = dbModel.rs.getInt("hasil");
                }
                dbModel.rs.close();

                Saran srn = new Saran();
                srn.setId_saran(new SimpleIntegerProperty(hsl));
                srn.setAktivitas(txtaktivitas.getText());
                srn.setMakanan(txtmakanan.getText());
                srn.setKategori(txtkategori.getText());

                tabelSaran.getItems().add(srn);
                txtaktivitas.setText("");
                txtmakanan.setText("");
                txtkategori.setText("");

                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("BarbelQ");
                a.setHeaderText(null);
                a.setContentText("Update Berhasil");
                a.show();
            }
        } 
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        tampilTambah.visibleProperty().set(true);
        tabelSaran.disableProperty().set(false);
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Stage stage1 = (Stage) btnLogout.getScene().getWindow();
        stage1.close();
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("/styles/Styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();  
    }
}
