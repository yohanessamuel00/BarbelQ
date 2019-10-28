/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpl.barbelq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ALDO
 */
class DBBarbelQ {
     Connection conection;
     Statement statement;
     ResultSet rs = null;
     public DBBarbelQ () {
        try{
            Class.forName("org.sqlite.JDBC");
            conection =DriverManager.getConnection("jdbc:sqlite:BarbelQ.db");
            statement = conection.createStatement();
        }catch(ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
            System.out.println("connection not successful");
            System.exit(1);
        }   
    }
  
    public boolean isDbConnected() {
        try {
            return !conection.isClosed();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean isLogin(String user, String pass) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "select * from DataPengguna where email = ? and password = ?";
        try {    
            preparedStatement = conection.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);
    
            rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
            // TODO: handle exception
        } finally {
            preparedStatement.close();
            rs.close();
        }
    }
    
    public int InsertOrUpdate(String query){
        int hasil=0;
        try{
            hasil = statement.executeUpdate(query);           
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return hasil;
    }
    
    public ResultSet resultset(String query){
        try{
            rs = statement.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return rs;
    }
}
