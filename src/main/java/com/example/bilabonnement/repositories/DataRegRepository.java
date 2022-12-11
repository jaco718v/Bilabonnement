package com.example.bilabonnement.repositories;

import com.example.bilabonnement.model.*;
import com.example.bilabonnement.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class DataRegRepository {

  @Value("${spring.datasource.url}")
  private String db_url;

  @Value("${spring.datasource.username}")
  private String uid;

  @Value("${spring.datasource.password}")
  private String pwd;


  public void opretKundeDB(String fornavn, String efternavn, int kontaktnummer, String email) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO kunder(kunde_fornavn, kunde_efternavn, kunde_kontaktnummer, kunde_email)" +
          "VALUES (?,?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);
      psmt.setString(1, fornavn);
      psmt.setString(2, efternavn);
      psmt.setInt(3, kontaktnummer);
      psmt.setString(4, email);

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public ArrayList<Kunde> findKunderMedFornavn(String fornavn) {
    ArrayList<Kunde> kundeliste = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM kunder WHERE kunde_fornavn=? ORDER BY kunde_fornavn";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setString(1, fornavn);
      ResultSet resultSet = pstm.executeQuery();
      while (resultSet.next()) {
        int kundeID = resultSet.getInt(1);
        String fornavnDB = resultSet.getString(2);
        String efternavn = resultSet.getString(3);
        int kontaktnummer = resultSet.getInt(4);
        String email = resultSet.getString(5);
        kundeliste.add(new Kunde(kundeID, fornavnDB, efternavn, kontaktnummer, email));
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return kundeliste;
  }

  public Kunde findKundeMedID(int kundeID) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM kunder WHERE kunde_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, kundeID);
      ResultSet resultSet = pstm.executeQuery();
      if (resultSet.next()) {
        String fornavn = resultSet.getString(2);
        String efternavn = resultSet.getString(3);
        int kontaktnummer = resultSet.getInt(4);
        String email = resultSet.getString(5);
        return (new Kunde(kundeID, fornavn, efternavn, kontaktnummer, email));
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return null;
  }

  public int findKundeIDMedKontraktID(int kontraktID){
    int kundeID = 0;
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT kunde_id FROM lejeaftaler WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, kontraktID);
      ResultSet resultSet = pstm.executeQuery();
      resultSet.next();
      kundeID = resultSet.getInt(1);

    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return kundeID;
  }

  public ArrayList<Kunde> findAlleKunder(){
    ArrayList<Kunde> kunder = new ArrayList<>();
    try{
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM kunder ORDER BY kunde_fornavn";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      ResultSet resultSet = pstm.executeQuery();
      while (resultSet.next()) {
        int kundeID = resultSet.getInt(1);
        String fornavn = resultSet.getString(2);
        String efternavn = resultSet.getString(3);
        int kontaktnummer = resultSet.getInt(4);
        String email = resultSet.getString(5);
        kunder.add(new Kunde(kundeID, fornavn, efternavn, kontaktnummer, email));
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return kunder;
  }



  public void opretLejeaftaleDB(int kundeID, int vognnummer, String aftaletype,
                                String startdato, String slutdato) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO lejeaftaler(kunde_id, vognnummer, aftaletype," +
          " startdato, slutdato)" +
          "VALUES(?,?,?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);

      psmt.setInt(1, kundeID);
      psmt.setInt(2, vognnummer);
      psmt.setString(3, aftaletype);
      psmt.setString(4, startdato);
      psmt.setString(5, slutdato);

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void setAftaleVenter(int vognnummer) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejeaftaler SET kontrakt_status = 'venter' Where vognnummer = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, vognnummer);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void setAftaleBetaling(int kontraktID) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejeaftaler SET kontrakt_status = 'betaling' Where kontrakt_id = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, kontraktID);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void setAftaleAfsluttet(int vognnummer) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejeaftaler SET kontrakt_status = 'Afsluttet' Where vognnummer = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, vognnummer);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public ArrayList<Lejeaftale> findLejeaftalerViaKundeIDOgStatus(int kundeID, String status){
    ArrayList<Lejeaftale> aftaleListe = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url,uid,pwd);
      String sqlQuery = "SELECT * FROM lejeaftaler WHERE kunde_id=? AND kontrakt_status=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, kundeID);
      pstm.setString(2,status);
      ResultSet resultSet = pstm.executeQuery();
      aftaleListe = lavLejeaftaleListe(resultSet);
    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return  aftaleListe;
  }

  public ArrayList<Lejeaftale> lavLejeaftaleListe(ResultSet resultSet) {
    ArrayList<Lejeaftale> aftaleListe = new ArrayList<>();
    try {
      while (resultSet.next()) {
        int kontraktNr = resultSet.getInt(1);
        int kundeID = resultSet.getInt(2);
        int vognnummer = resultSet.getInt(3);
        String aftaleType = resultSet.getString(4);
        String startDato = resultSet.getString(5);
        String slutDato = resultSet.getString(6);
        String kontrakt_status = resultSet.getString(7);

        aftaleListe.add(new Lejeaftale(kontraktNr, kundeID, vognnummer, aftaleType,
            startDato, slutDato, kontrakt_status));
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return aftaleListe;
  }

  public Lejeaftale findLejeaftaleViaKontraktID(int kontraktID){
    Lejeaftale lejeaftale = new Lejeaftale();
    try {
      Connection conn = ConnectionManager.getConnection(db_url,uid,pwd);
      String sqlQuery = "SELECT * FROM lejeaftaler WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, kontraktID);
      ResultSet resultSet = pstm.executeQuery();
      lejeaftale = lavLejeaftaleListe(resultSet).get(0);
    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return  lejeaftale;
  }

  public void updaterLejeaftaleDB(Lejeaftale lejeaftale){
    try {
      Connection conn = ConnectionManager.getConnection(db_url,uid,pwd);
      String sqlUpdate = "UPDATE lejeaftaler SET aftaletype=?," +
          "startdato=?, slutdato=? WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setString(1,lejeaftale.getAftaleType());
      pstm.setString(2,lejeaftale.getStartDato());
      pstm.setString(3,lejeaftale.getSlutDato());
      pstm.setInt(4,lejeaftale.getKontraktID());

      pstm.executeUpdate();

    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void sletLejeaftaleOgRelateret(int kontrakt_id){
    try {
      sletSkadesrapportDB(kontrakt_id);
      Connection conn = ConnectionManager.getConnection(db_url,uid,pwd);
      String sqlDelete = "DELETE FROM lejeaftaler WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlDelete);
      pstm.setInt(1,kontrakt_id);
      pstm.executeUpdate();

    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void sletSkadesrapportDB(int kontraktID){
    try {
      Connection conn = ConnectionManager.getConnection(db_url,uid,pwd);
      String sqlDelete = "DELETE FROM skadesrapporter WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlDelete);
      pstm.setInt(1,kontraktID);

      pstm.executeUpdate();

    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public int findKontraktIDMedVognnummer(int vognnummer) {
    int kontraktID = 0;
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT kontrakt_id FROM lejeaftaler WHERE vognnummer=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, vognnummer);
      ResultSet resultSet = pstm.executeQuery();
      resultSet.next();
      kontraktID = resultSet.getInt(1);

    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return kontraktID;
  }




  public Kunde newKunde(){
    return new Kunde();
  }

  public Lejeaftale newLejeaftale(){
    return new Lejeaftale();
  }

}