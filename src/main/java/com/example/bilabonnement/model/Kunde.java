package com.example.bilabonnement.model;

public class Kunde {
  private int kundeID;
  private String fornavn;
  private String efternavn;
  private int kontaktnummer;
  private String email;

  public Kunde(){}

  public Kunde(int kundeID, String fornavn, String efternavn, int kontaktnummer, String email) {
    this.kundeID = kundeID;
    this.fornavn = fornavn;
    this.efternavn = efternavn;
    this.kontaktnummer = kontaktnummer;
    this.email = email;
  }

  public int getKundeID() {
    return kundeID;
  }

  public String getFornavn() {
    return fornavn;
  }

  public String getEfternavn() {
    return efternavn;
  }

  public int getKontaktnummer() {
    return kontaktnummer;
  }

  public String getEmail() {
    return email;
  }

  public void setKundeID(int kundeID) {
    this.kundeID = kundeID;
  }

  public void setFornavn(String fornavn) {
    this.fornavn = fornavn;
  }

  public void setEfternavn(String efternavn) {
    this.efternavn = efternavn;
  }

  public void setKontaktnummer(int kontaktnummer) {
    this.kontaktnummer = kontaktnummer;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
