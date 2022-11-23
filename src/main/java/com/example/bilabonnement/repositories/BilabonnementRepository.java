package com.example.bilabonnement.repositories;

import com.example.bilabonnement.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class BilabonnementRepository {

  @Value("${spring.datasource.url}")
  private String db_url;

  @Value("${spring.datasource.username}")
  private String uid;

  @Value("${spring.datasource.password}")
  private String pwd;

  public void opretKundeDB(String navn, int kontaktnummer, String email) {
    try {
      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
      String sqlInsert = "INSERT INTO kunder(kunde_navn, kunde_kontaktnummer, kunde_email)" +
          "VALUES (?,?,?)";
      PreparedStatement psmt = conn.prepareStatement(sqlInsert);
      psmt.setString(1, navn);
      psmt.setInt(2, kontaktnummer);
      psmt.setString(3, email);

      psmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Couldn't connect to db");
      e.printStackTrace();
    }
  }
}
