package com.example.bilabonnement.repositories;

import com.example.bilabonnement.model.*;
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
      String sqlQuery = "SELECT * FROM kunder WHERE kunde_fornavn=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setString(1, fornavn);
      ResultSet resultSet = pstm.executeQuery();
      while (resultSet.next()) {
        int kundeID = resultSet.getInt(1);
        String efternavn = resultSet.getString(3);
        int kontaktnummer = resultSet.getInt(4);
        String email = resultSet.getString(5);
        kundeliste.add(new Kunde(kundeID, fornavn, efternavn, kontaktnummer, email));
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

  public ArrayList<Lejebil> lavBilListe(ResultSet resultSet) {
    ArrayList<Lejebil> bilListe = new ArrayList<>();
    try {
      while (resultSet.next()) {
        int vognnummer = resultSet.getInt(1);
        int stelnummer = resultSet.getInt(2);
        String fabrikant = resultSet.getString(3);
        String model = resultSet.getString(4);
        String udstyrspakke = resultSet.getString(5);
        double købspris = resultSet.getDouble(6);
        double lejepris = resultSet.getDouble(7);
        double stålpris = resultSet.getDouble(8);
        double co2Niveau = resultSet.getDouble(9);
        double regAfgift = resultSet.getDouble(10);
        String status = resultSet.getString(11);
        String farve = resultSet.getString(12);
        bilListe.add(new Lejebil(vognnummer, stelnummer, fabrikant, model,
            udstyrspakke, købspris, lejepris, stålpris, co2Niveau, regAfgift, status, farve));
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return bilListe;
  }

  public Lejebil findBilMedVognnummer(int vognnummer) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM lejebiler WHERE vognummer=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, vognnummer);
      ResultSet resultSet = pstm.executeQuery();
      ArrayList<Lejebil> bilListe = lavBilListe(resultSet);
      if (!bilListe.isEmpty()) {
        return bilListe.get(1);
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Lejebil> findBilerFraFabrikant(String fabrikant) {
    ArrayList<Lejebil> bilListe = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM lejebiler WHERE fabrikant=? and lejebil_status='ledig'";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setString(1, fabrikant);
      ResultSet resultSet = pstm.executeQuery();
      bilListe = lavBilListe(resultSet);
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return bilListe;
  }

  public void opretLejeaftaleDB(int kundeID, int vognnummer, String aftaletype, int kilometerpakke,
                                String startdato, String slutdato) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO lejeaftaler(kunde_id, vognnummer,aftaletype," +
          " kilometerpakke, startdato, slutdato)" +
          "VALUES(?,?,?,?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);

      psmt.setInt(1, kundeID);
      psmt.setInt(2, vognnummer);
      psmt.setString(3, aftaletype);
      psmt.setInt(4, kilometerpakke);
      psmt.setString(5, startdato);
      psmt.setString(6, slutdato);

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void opretSkadesrapportDB(int kontraktID, int overkørteKilometer,
                                   boolean manglendeService, boolean manglendeRengøring,
                                   boolean manglendeDækskifte, int lakfeltSkade,
                                   int alufælgSkade, int stenslagSkade) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO skadesrapporter(kontrakt_id, overkørte_kilometer, manglende_service, manglende_rengøring, manglende_dækskifte, lakfelt_skade, alufælg_skade, stenslag_skade)" +
          "VALUES(?,?,?,?,?,?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);
      psmt.setInt(1, kontraktID);
      psmt.setInt(2, overkørteKilometer);
      psmt.setBoolean(3, manglendeService);
      psmt.setBoolean(4, manglendeRengøring);
      psmt.setBoolean(5, manglendeDækskifte);
      psmt.setInt(6, lakfeltSkade);
      psmt.setInt(7, alufælgSkade);
      psmt.setInt(8, stenslagSkade);

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    int rapportID = getRapportIDViaKontraktID(kontraktID);
    if (rapportID > 0) {
      Skadesrapport skadesrapport = new Skadesrapport(kontraktID, rapportID, overkørteKilometer, manglendeService,
          manglendeRengøring, manglendeDækskifte, lakfeltSkade, alufælgSkade, stenslagSkade);

      opretSkadeafgifterDB(new Skadesafgifter(skadesrapport));
    }
  }

  public void opretSkadeafgifterDB(Skadesafgifter skadesafgifter) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO skadesrapporter(rapport_id, afgift_overkørte_kilometer, afgift_manglende_service, " +
          "afgift_manglende_rengøring, afgift_manglende_dækskifte, afgift_lakfelt_skade, " +
          "afgift_alufælg_skade, afgift_stenslag_skade)" +
          "VALUES(?,?,?,?,?,?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);
      psmt.setInt(1, skadesafgifter.getRapportID());
      psmt.setDouble(2, skadesafgifter.getAfgiftOverkørteKilometer());
      psmt.setDouble(3, skadesafgifter.getAfgiftManglendeService());
      psmt.setDouble(4, skadesafgifter.getAfgiftManglendeRengøring());
      psmt.setDouble(5, skadesafgifter.getAfgiftManglendeDækskifte());
      psmt.setDouble(6, skadesafgifter.getAfgiftLakfeltSkade());
      psmt.setDouble(7, skadesafgifter.getAfgiftAlufælgSkade());
      psmt.setDouble(8, skadesafgifter.getAfgiftStenslagSkade());

      psmt.executeUpdate();

    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public ArrayList<Lejebil> getBilListeViaStatus(String status) {
    ArrayList<Lejebil> afleveretBilListe = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM lejebiler WHERE lejebil_status=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setString(1, status);
      ResultSet resultSet = pstm.executeQuery();
      afleveretBilListe = lavBilListe(resultSet);
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return afleveretBilListe;
  }

  public int getRapportIDViaKontraktID(int kontraktID) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT rapport_id FROM skadesrapporter WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, kontraktID);
      ResultSet resultSet = pstm.executeQuery();
      int rapportID = resultSet.getInt(1);
      return rapportID;
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return -1;
  }


  public void setAftaleVenter(int kontraktID) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejeaftaler SET kontrakt_status = 'venter' Where koontrakt_id = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, kontraktID);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void setAftaleBetaling(int kontraktID) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlUpdate = "UPDATE lejeaftaler SET kontrakt_status = 'betaling' Where koontrakt_id = ?";
      PreparedStatement pstm = conn.prepareStatement(sqlUpdate);
      pstm.setInt(1, kontraktID);
      pstm.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }

  public void meldBilAfleveretDB(int vognnummer) {
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

  public void meldBilTjekketDB(int vognnummer) {
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

  public ArrayList<Lejeaftale> lavLejeaftaleListe(ResultSet resultSet) {
    ArrayList<Lejeaftale> aftaleListe = new ArrayList<>();
    try {
      while (resultSet.next()) {
        int kontraktNr = resultSet.getInt(1);
        int kilometerpakke = resultSet.getInt(2);
        String startDato = resultSet.getString(3);
        String slutDato = resultSet.getString(4);
        int kundeID = resultSet.getInt(5);
        int vognnummer = resultSet.getInt(6);
        aftaleListe.add(new Lejeaftale(kontraktNr, kilometerpakke, startDato, slutDato, kundeID, vognnummer));
      }
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return aftaleListe;
  }

  public Skadesrapport getSkadesrapportViaKundeID(int kundeID) {
    Skadesrapport skadesrapport = new Skadesrapport();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM skadesrapporter WHERE kunde_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, kundeID);
      ResultSet resultSet = pstm.executeQuery();
      skadesrapport.setOverkørteKilometer(resultSet.getInt(3));
      skadesrapport.setManglendeService(resultSet.getBoolean(4));
      skadesrapport.setManglendeRengøring(resultSet.getBoolean(5));
      skadesrapport.setManglendeDækskifte(resultSet.getBoolean(6));
      skadesrapport.setLakfeltSkade(resultSet.getInt(7));
      skadesrapport.setAlufælgSkade(resultSet.getInt(8));
      skadesrapport.setStenslagSkade(resultSet.getInt(9));
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return skadesrapport;
  }

  public int findKontraktIDMedVognnummer(int vognnummer) {
    int kontraktID = 0;
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT kontrakt_id FROM lejeaftaler WHERE vognnummer=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, vognnummer);
      ResultSet resultSet = pstm.executeQuery();
      kontraktID = resultSet.getInt(1);

    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return kontraktID;
  }

}