package com.example.bilabonnement.repositories;

import com.example.bilabonnement.model.Kunde;
import com.example.bilabonnement.model.Lejebil;
import com.example.bilabonnement.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class BilabonnementRepository {

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
      psmt.setString(2,efternavn);
      psmt.setInt(3, kontaktnummer);
      psmt.setString(4, email);

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public ArrayList<Kunde> findKunderMedFornavn(String fornavn){
    ArrayList<Kunde> kundeliste = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM kunder WHERE kunde_fornavn=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setString(1,fornavn);
      ResultSet resultSet = pstm.executeQuery();
      while (resultSet.next()){
        int kundeID = resultSet.getInt(1);
        String efternavn=resultSet.getString(3);
        int kontaktnummer=resultSet.getInt(4);
        String email=resultSet.getString(5);
        kundeliste.add(new Kunde(kundeID,fornavn,efternavn,kontaktnummer,email));
      }
    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return kundeliste;
  }

  public Kunde findKundeMedID(int kundeID){
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM kunder WHERE kunde_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1,kundeID);
      ResultSet resultSet = pstm.executeQuery();
      if(resultSet.next()){
        String fornavn = resultSet.getString(2);
        String efternavn=resultSet.getString(3);
        int kontaktnummer=resultSet.getInt(4);
        String email=resultSet.getString(5);
        return (new Kunde(kundeID,fornavn,efternavn,kontaktnummer,email));
      }
    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return null;
  }

  public Lejebil findBilMedVognnummer(int vognnummer){
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM lejebiler WHERE vognummer=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1,vognnummer);
      ResultSet resultSet = pstm.executeQuery();
      if(resultSet.next()){
        int stelnummer= resultSet.getInt(2);
        String fabrikant = resultSet.getString(3);
        String model = resultSet.getString(4);
        String udstyrspakke = resultSet.getString(5);
        double købspris = resultSet.getDouble(6);
        double lejepris = resultSet.getDouble(7);
        double stålpris = resultSet.getDouble(8);
        double co2Niveau = resultSet.getDouble(9);
        double regAfgift = resultSet.getDouble(10);
        String status = resultSet.getString(11);
        return (new Lejebil(vognnummer,stelnummer,fabrikant,model,
            udstyrspakke,købspris,lejepris,stålpris,co2Niveau,regAfgift,status));
      }
    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Lejebil> findBilerFraFabrikant(String fabrikant){
    ArrayList<Lejebil> bilListe = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url,uid,pwd);
      String sqlQuery = "SELECT * FROM lejebiler WHERE fabrikant=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setString(1,fabrikant);
      ResultSet resultSet = pstm.executeQuery();
      while(resultSet.next()){
        int vognnummer = resultSet.getInt(1);
        int stelnummer= resultSet.getInt(2);
        String model = resultSet.getString(4);
        String udstyrspakke = resultSet.getString(5);
        double købspris = resultSet.getDouble(6);
        double lejepris = resultSet.getDouble(7);
        double stålpris = resultSet.getDouble(8);
        double co2Niveau = resultSet.getDouble(9);
        double regAfgift = resultSet.getDouble(10);
        String status = resultSet.getString(11);
        bilListe.add(new Lejebil(vognnummer,stelnummer,fabrikant,model,
            udstyrspakke,købspris,lejepris,stålpris,co2Niveau,regAfgift,status));
      }
    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return bilListe;
  }

  public void opretLejeaftaleDB(int kundeID, int vognnummer, String aftaletype, int kilometerpakke,
                                String startdato, String slutdato){
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO lejeaftaler(kunde_id, vognnummer,aftaletype," +
          " kilometerpakke, startdato, slutdato)" +
          "VALUES(?,?,?,?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);

      psmt.setInt(1,kundeID);
      psmt.setInt(2,vognnummer);
      psmt.setString(3,aftaletype);
      psmt.setInt(4,kilometerpakke);
      psmt.setString(5,startdato);
      psmt.setString(6,slutdato);

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void opretSkadesrapportDB(int kontraktID, int overkørteKilometer,
                                   boolean manglendeService, boolean manglendeRengøring,
                                   boolean manglendeDækskifte, int lakfeltSkade,
                                   int alufælgSkade, int stenslagSkade){
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO skadesrapporter(kontrakt_id, overkørte_kilometer, manglende_service, manglende_rengøring, manglende_dækskifte, lakfelt_skade, alufælg_skade, stenslag_skade)" +
          "VALUES(?,?,?,?,?,?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);
      psmt.setInt(1,kontraktID);
      psmt.setInt(2,overkørteKilometer);
      psmt.setBoolean(3,manglendeService);
      psmt.setBoolean(4,manglendeRengøring);
      psmt.setBoolean(5,manglendeDækskifte);
      psmt.setInt(6,lakfeltSkade);
      psmt.setInt(7,alufælgSkade);
      psmt.setInt(8,stenslagSkade);

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }
  }

}