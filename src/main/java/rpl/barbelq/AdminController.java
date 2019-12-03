/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Krisna
 */
public class AdminController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    
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
    private Group tampilTambah;
    @FXML
    private TextArea txtaktivitas,txtmakanan,txtubahAktivitas,txtubahMakanan;
    @FXML
    private TextField namaAdmin,usiaAdmin,emailAdmin;
    @FXML
    private Label namaUser;
    @FXML
    private Button btnLogout;
    @FXML
    private AnchorPane Pendaftaran,crudAdmin;
    @FXML
    private PasswordField passwordAdmin;
    @FXML
    private ComboBox<String> cbKategori, cbKategoriUbah;
    ObservableList<String> options = FXCollections.observableArrayList("Berlebihan","Kekurangan");
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbKategori.setItems(options);
        cbKategori.setValue("Berlebihan");
        cbKategoriUbah.setItems(options);
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
                a.setAlertType(Alert.AlertType.CONFIRMATION);
                a.setTitle("BarbelQ");
                a.setHeaderText(null);
                a.setContentText("Apa Anda Yakin Akan Menghapus Saran Ini?");
                Optional<ButtonType> result = a.showAndWait();
                if(result.get() == ButtonType.OK){
                    dbModel.InsertOrUpdate("DELETE FROM Saran WHERE id_saran ='" + srn.getId_saran() + "'");
                    tabelSaran.getItems().remove(index);
                    System.out.println(srn.getId_saran());
                }
                
            }
        });
        
        cxMenuItemEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tampilTambah.visibleProperty().set(false);
                tabelSaran.disableProperty().set(true);
                cxMenu.hide();
                final Saran srn = tabelSaran.getSelectionModel().getSelectedItem();
                try {
                    dbModel.rs = dbModel.resultset("Select * from Saran where id_saran = "+srn.getId_saran()+" ");
                    if(dbModel.rs.next()){
                        txtubahAktivitas.setText(dbModel.rs.getString("aktivitas"));
                        txtubahMakanan.setText(dbModel.rs.getString("makanan"));
                        cbKategoriUbah.setValue(dbModel.rs.getString("kategori"));
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
        if(txtubahAktivitas.getText().equals("") || txtubahMakanan.getText().equals("") || cbKategoriUbah.getValue().isEmpty()){
            bantuAlert(null,"Field Tidak Boleh Kosong");
        }else{
            if(cekData(txtubahAktivitas,txtubahMakanan)){
                bantuAlert(null, "Data Tidak Boleh Sama");
            }else{
                final Saran srn1 = tabelSaran.getSelectionModel().getSelectedItem();
                int index = tabelSaran.getSelectionModel().getSelectedIndex();
                ObservableList<Saran> saran = tabelSaran.getItems();
                dbModel.InsertOrUpdate("update Saran set makanan= '"+txtubahMakanan.getText()+"',aktivitas= '"+txtubahAktivitas.getText()+"',kategori= '"+cbKategoriUbah.getValue()+"' where id_saran ="+srn1.getId_saran()+" ");
                saran.set(index, updateORinsert(srn1.getId_saran(),txtubahAktivitas,txtubahMakanan,cbKategoriUbah));
                bantuAlert(null, "Data Berhasil Di Update");
                tabelSaran.disableProperty().set(false);
                tampilTambah.visibleProperty().set(true);
            }
        }     
    }
    
    @FXML
    void handleTambah(ActionEvent event) throws SQLException {
        if(txtaktivitas.getText().equals("") || txtmakanan.getText().equals("") || cbKategori.getValue().isEmpty()){
            bantuAlert(null, "Field Tidak Boleh Kosong");
        }else{
            if(cekData(txtaktivitas,txtmakanan)){
                bantuAlert(null, "Data Tidak Boleh Sama");
            }else{
                dbModel.InsertOrUpdate("insert into Saran(makanan,aktivitas,kategori) values('"+txtmakanan.getText()+"','"+txtaktivitas.getText()+"','"+cbKategori.getValue()+"')");
                int hsl = 0;
                dbModel.resultset("select MAX(id_saran) as hasil from Saran ");
                if(dbModel.rs.next()){
                    hsl = dbModel.rs.getInt("hasil");
                }
                dbModel.rs.close();
                tabelSaran.getItems().add(updateORinsert(hsl, txtaktivitas, txtmakanan, cbKategori));
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setTitle("BarbelQ");
                a.setHeaderText(null);
                a.setContentText("Data Berhasil Di Tambah");
                a.show();
            }
        } 
    }
     
    @FXML
    void handleBatal(ActionEvent event) {
        tampilTambah.visibleProperty().set(true);
        tabelSaran.disableProperty().set(false);
    }

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
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
    
    @FXML
    void btnSignupAdmin(ActionEvent event) {
        dbModel.InsertOrUpdate("insert into DataPengguna(nama,email,password,usiaTahun,level) values('"+namaAdmin.getText()+"','"+emailAdmin.getText()+"','"+passwordAdmin.getText()+"',"+usiaAdmin.getText()+",2)");
        bantuAlert(null,"Admin Berhasil Ditambah");
        Pendaftaran.visibleProperty().set(false);
        crudAdmin.visibleProperty().set(true);
        
    }

    @FXML
    void btnCancelAdmin(ActionEvent event) {
        namaAdmin.setText("");
        usiaAdmin.setText("");
        emailAdmin.setText("");
        passwordAdmin.setText("");
        crudAdmin.visibleProperty().set(true);
        Pendaftaran.visibleProperty().set(false);
    }

    @FXML
    void btnDaftarAdmin(ActionEvent event) {
        crudAdmin.visibleProperty().set(false);
        Pendaftaran.visibleProperty().set(true);
    }
////////////////////////////////////////////////////////////////////////////////
    public void GetUser(String nama) {
        // TODO Auto-generated method stub
        namaUser.setText(nama);
    }
    
    private Saran updateORinsert(int id, TextArea aktivitas, TextArea makanan, ComboBox<String> Kategori){
        Saran srn = new Saran();
        srn.setId_saran(new SimpleIntegerProperty(id));
        srn.setAktivitas(aktivitas.getText());
        srn.setMakanan(makanan.getText());
        srn.setKategori(Kategori.getValue());
        aktivitas.setText("");
        makanan.setText("");
        Kategori.setValue("Berlebihan");
        return srn;
    }
    private void bantuAlert(String header, String isi){
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(header);
        a.setContentText(isi);
        a.showAndWait();
    }
        
    private boolean cekData(TextArea aktivitas, TextArea makanan) throws SQLException{
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
            
}
