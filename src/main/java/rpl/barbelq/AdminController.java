/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

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
import rpl.barbelq.Saran;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Krisna
 */
public class AdminController implements Initializable {
    DBBarbelQ dbModel = new DBBarbelQ();
    Alert a = new Alert(Alert.AlertType.NONE);
    

    @FXML
    private TableColumn<Saran, String> colMakanan;
    
    @FXML
    private TableColumn<Saran, Integer> colidSaran;


    @FXML
    private TableColumn<Saran, String> colAktivitas;

    @FXML
    private TableColumn<Saran, String> colKategori;

    @FXML
    private Button btnTambah;
    
    @FXML
    private Group tampilUbah;
    
    @FXML
    private Group tampilTambah;

    @FXML
    private TableView<Saran> tabelSaran;
    
    @FXML
    private TextField txtaktivitas;

    @FXML
    private TextField txtmakanan;

    @FXML
    private TextField txtkategori;
    
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
                String cek = String.valueOf(dbModel.rs.getInt("id_saran"));
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
            }
        });
    }

    @FXML
    void handleUbah(ActionEvent event) {
        tampilTambah.visibleProperty().set(true);
    }
    @FXML
    void handleTambah(ActionEvent event) throws SQLException {
        dbModel.InsertOrUpdate("insert into Saran(makanan,aktivitas,kategori) values('"+txtaktivitas.getText()+"','"+txtmakanan.getText()+"','"+txtkategori.getText()+"')");
        Saran srn = new Saran();
        int hsl = 0;
        dbModel.resultset("select MAX(id_saran) as hasil from Saran ");
        if(dbModel.rs.next()){
            hsl = dbModel.rs.getInt("hasil");
        }
        dbModel.rs.close();
        srn.setId_saran(new SimpleIntegerProperty(hsl));
        srn.setAktivitas(txtaktivitas.getText());
        srn.setMakanan(txtmakanan.getText());
        srn.setKategori(txtkategori.getText());
        tabelSaran.getItems().add(srn);
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setTitle("BarbelQ");
        a.setHeaderText(null);
        a.setContentText("Update Berhasil");
        a.show();
        
    }
}
