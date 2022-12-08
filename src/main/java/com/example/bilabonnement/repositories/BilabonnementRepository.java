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

  public ArrayList<Lejebil> findBilerFraFabrikant(String fabrikant) {
    ArrayList<Lejebil> bilListe = new ArrayList<>();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM lejebiler WHERE fabrikant=? and lejebil_status='ledig' ORDER BY model";
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
      sletSkadesafgifterDB(kontrakt_id);
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
    // Get rapportID
    skadesrapport.setRapportID(findRapportIDMedKontraktID(skadesrapport.getKontraktID()));

    opretSkadeafgifterDB(new Skadesafgifter(skadesrapport));

  }

  public void opretSkadeafgifterDB(Skadesafgifter skadesafgifter) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO skadesafgifter(rapport_id, afgift_overkørte_kilometer, afgift_manglende_service, " +
          "afgift_manglende_rengøring, afgift_manglende_dækskifte, afgift_lakfelt_skade, " +
          "afgift_alufælg_skade, afgift_stenslag_skade)" +
          "VALUES(?,?,?,?,?,?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);
      psmt.setInt(1, skadesafgifter.getRapportID());
      psmt.setDouble(2, skadesafgifter.getAfgiftOverkoerteKilometer());
      psmt.setDouble(3, skadesafgifter.getAfgiftManglendeService());
      psmt.setDouble(4, skadesafgifter.getAfgiftManglendeRengoering());
      psmt.setDouble(5, skadesafgifter.getAfgiftManglendeDaekskifte());
      psmt.setDouble(6, skadesafgifter.getAfgiftLakfeltSkade());
      psmt.setDouble(7, skadesafgifter.getAfgiftAlufaelgSkade());
      psmt.setDouble(8, skadesafgifter.getAfgiftStenslagSkade());

      psmt.executeUpdate();

    } catch (SQLException e) {
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

  public void sletSkadesafgifterDB(int kontraktID){
    try {
      Connection conn = ConnectionManager.getConnection(db_url,uid,pwd);
      String sqlQuery ="SELECT rapport_id FROM skadesrapporter WHERE kontrakt_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1,kontraktID);
      ResultSet resultSet = pstm.executeQuery();
      if(resultSet.next()){
      int rapportID = resultSet.getInt(1);

      String sqlDelete = "DELETE FROM skadesafgifter WHERE rapport_id=?";
      pstm = conn.prepareStatement(sqlDelete);
      pstm.setInt(1,rapportID);

      pstm.executeUpdate();
      }

    } catch (SQLException e){
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
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
      Skadesafgifter skadesafgifter = findSkadesafgifterViaRapportID(skadesrapport.getRapportID());
      skadesrapport.setSkadesafgifter(skadesafgifter);
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return skadesrapport;
  }

  public Skadesafgifter findSkadesafgifterViaRapportID(int rapportID){
    Skadesafgifter skadesafgifter = new Skadesafgifter();
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlQuery = "SELECT * FROM skadesafgifter WHERE rapport_id=?";
      PreparedStatement pstm = conn.prepareStatement(sqlQuery);
      pstm.setInt(1, rapportID);
      ResultSet resultSet = pstm.executeQuery();
      resultSet.next();
      skadesafgifter.setRapportID(resultSet.getInt(1));
      skadesafgifter.setAfgiftOverkoerteKilometer(resultSet.getDouble(2));
      skadesafgifter.setAfgiftManglendeService(resultSet.getDouble(3));
      skadesafgifter.setAfgiftManglendeRengoering(resultSet.getDouble(4));
      skadesafgifter.setAfgiftManglendeDaekskifte(resultSet.getDouble(5));
      skadesafgifter.setAfgiftLakfeltSkade(resultSet.getDouble(6));
      skadesafgifter.setAfgiftAlufaelgSkade(resultSet.getDouble(7));
      skadesafgifter.setAfgiftStenslagSkade(resultSet.getDouble(8));

    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
    return skadesafgifter;
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



  public Kunde newKunde(){
    return new Kunde();
  }

  public Lejeaftale newLejeaftale(){
    return new Lejeaftale();
  }

  public Skadesrapport newSkadesrapport(int kontraktID){
    return newSkadesrapport(kontraktID);
  }
}