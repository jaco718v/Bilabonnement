package com.example.bilabonnement.repositories;

import com.example.bilabonnement.model.Skadesrapport;
import com.example.bilabonnement.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Repository
public class SkadeUdbedringRepository {
  @Value("${spring.datasource.url}")
  private String db_url;

  @Value("${spring.datasource.username}")
  private String uid;

  @Value("${spring.datasource.password}")
  private String pwd;


  public void opretSkadesrapportDB(Skadesrapport skadesrapport) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO skadesrapporter(kontrakt_id, overkørte_kilometer, manglende_service, manglende_rengøring, manglende_dækskifte, lakfelt_skade, alufælg_skade, stenslag_skade)" +
          "VALUES(?,?,?,?,?,?,?,?)";

      PreparedStatement psmt = conn.prepareStatement(sqlInsert);
      psmt.setInt(1, skadesrapport.getKontraktID());
      psmt.setInt(2, skadesrapport.getOverkoerteKilometer());
      psmt.setBoolean(3, skadesrapport.isManglendeService());
      psmt.setBoolean(4, skadesrapport.isManglendeRengoering());
      psmt.setBoolean(5, skadesrapport.isManglendeDaekskifte());
      psmt.setInt(6, skadesrapport.getLakfeltSkade());
      psmt.setInt(7, skadesrapport.getAlufaelgSkade());
      psmt.setInt(8, skadesrapport.getStenslagSkade());

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }

    skadesrapport.setRapportID(findRapportIDMedKontraktID(skadesrapport.getKontraktID()));

    skadesrapport.opretSkadesafgifter();

  }



  public Skadesrapport findSkadesrapportViaKontraktID(int kontraktID) {
    Skadesrapport skadesrapport = new Skadesrapport();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM skadesrapporter WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, kontraktID);
      ResultSet resultSet = pstm.executeQuery();
      resultSet.next();
      skadesrapport.setRapportID(resultSet.getInt(1));
      skadesrapport.setKontraktID(resultSet.getInt(2));
      skadesrapport.setOverkoerteKilometer(resultSet.getInt(3));
      skadesrapport.setManglendeService(resultSet.getBoolean(4));
      skadesrapport.setManglendeRengoering(resultSet.getBoolean(5));
      skadesrapport.setManglendeDaekskifte(resultSet.getBoolean(6));
      skadesrapport.setLakfeltSkade(resultSet.getInt(7));
      skadesrapport.setAlufaelgSkade(resultSet.getInt(8));
      skadesrapport.setStenslagSkade(resultSet.getInt(9));
      skadesrapport.opretSkadesafgifter();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return skadesrapport;
  }

  public int findRapportIDMedKontraktID(int kontraktID){
    int rapportID = 0;
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT rapport_id FROM skadesrapporter WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, kontraktID);
      ResultSet resultSet = pstm.executeQuery();
      resultSet.next();
      rapportID = resultSet.getInt(1);

    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return rapportID;
  }

  public Skadesrapport newSkadesrapport(int kontraktID){
    return new Skadesrapport(kontraktID);
  }
}
