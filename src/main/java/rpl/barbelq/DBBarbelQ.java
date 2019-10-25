/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author ALDO
 */
public class DBBarbelQ {
    private Connection conn = null;
    public  void connect(String email, String password){
        try{
            String url = "jdbc:sqlite:BarbelQ.db";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            System.out.println(email);
            System.out.println(password);
            String query = "SELECT email,password FROM DataPengguna where email ='" +email+"' AND password = '" +password+"'";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                System.out.println("ada");
            }else{
                System.out.println("tidak ada");
            }
            conn.close();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            
        }
        
    }
}
