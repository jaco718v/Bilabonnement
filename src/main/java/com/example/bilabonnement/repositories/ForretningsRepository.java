package com.example.bilabonnement.repositories;

import com.example.bilabonnement.model.Lejebil;
import com.example.bilabonnement.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class ForretningsRepository {

  @Value("${spring.datasource.url}")
  private String db_url;

  @Value("${spring.datasource.username}")
  private String uid;

  @Value("${spring.datasource.password}")
  private String pwd;



  public ArrayList<Lejebil> lavBilListe(ResultSet resultSet) {
    ArrayList<Lejebil> bilListe = new ArrayList<>();
    try {
      while (resultSet.next()) {
        int vognnummer = resultSet.getInt(1);
        int stelnummer = resultSet.getInt(2);
        String fabrikant = resultSet.getString(3);
        String model = resultSet.getString(4);
        String udstyrspakke = resultSet.getString(5);
        double lejepris = resultSet.getDouble(6);
        double købspris = resultSet.getDouble(7);
        double stålpris = resultSet.getDouble(8);
        double co2Niveau = resultSet.getDouble(9);
        double regAfgift = resultSet.getDouble(10);
        String status = resultSet.getString(11);
        String farve = resultSet.getString(12);
        int kilometerpakke = resultSet.getInt(13);
        bilListe.add(new Lejebil(vognnummer, stelnummer, fabrikant, model,
            udstyrspakke, lejepris, købspris, stålpris, co2Niveau, regAfgift, status, farve, kilometerpakke));
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return bilListe;
  }

  public Lejebil findBilMedVognnummer(int vognnummer) {
    ArrayList<Lejebil> bilListe = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM lejebiler WHERE vognnummer=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, vognnummer);
      ResultSet resultSet = pstm.executeQuery();
      bilListe = lavBilListe(resultSet);

    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return bilListe.get(0);
  }

  public ArrayList<Lejebil> findBilListeViaStatus(String status) {
    ArrayList<Lejebil> BilListeAfStatus = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM lejebiler WHERE lejebil_status=? ORDER BY model";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setString(1, status);
      ResultSet resultSet = pstm.executeQuery();
      BilListeAfStatus = lavBilListe(resultSet);
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return BilListeAfStatus;
  }

  public void setBilAfleveret(int vognnummer) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejebiler SET lejebil_status = 'Afleveret' Where vognnummer = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, vognnummer);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void setBilUdlejet(int vognnummer) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejebiler SET lejebil_status = 'Udlejet' Where vognnummer = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, vognnummer);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void setBilTjekket(int vognnummer) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejebiler SET lejebil_status = 'Tjekket' Where vognnummer = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, vognnummer);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void setBilLedig(int vognnummer) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejebiler SET lejebil_status = 'Ledig' Where vognnummer = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, vognnummer);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public ArrayList<String> findLavFabrikantBestand(){
    ArrayList<String> manglendeFabrikanter = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT COUNT(vognnummer), fabrikant FROM lejebiler " +
          "WHERE lejebil_status='Ledig' GROUP BY fabrikant HAVING COUNT(vognnummer) < 3";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      ResultSet resultSet = pstm.executeQuery();
      while(resultSet.next()){
        manglendeFabrikanter.add(resultSet.getString(2));
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return manglendeFabrikanter;
  }

}
