package com.example.bilabonnement.model;

public class Lejeaftale {
  private int kontraktNr;
  private int kundeID;
  private int vognnummer;
  private String aftaleType;
  private String startDato;
  private String slutDato;
  private String kontraktStatus;


  public Lejeaftale(){}
  public Lejeaftale(int kontraktNr, int kundeID, int vognnummer, String aftaleType, String startDato, String slutDato, String kontraktStatus) {
    this.kontraktNr = kontraktNr;
    this.kundeID = kundeID;
    this.vognnummer = vognnummer;
    this.aftaleType = aftaleType;
    this.startDato = startDato;
    this.slutDato = slutDato;
    this.kontraktStatus = kontraktStatus;
  }

  public int getKontraktNr() {
    return kontraktNr;
  }

  public void setKontraktNr(int kontraktNr) {
    this.kontraktNr = kontraktNr;
  }

  public int getKundeID() {
    return kundeID;
  }

  public void setKundeID(int kundeID) {
    this.kundeID = kundeID;
  }

  public int getVognnummer() {
    return vognnummer;
  }

  public void setVognnummer(int vognnummer) {
    this.vognnummer = vognnummer;
  }

  public String getAftaleType() {
    return aftaleType;
  }

  public void setAftaleType(String aftaleType) {
    this.aftaleType = aftaleType;
  }


  public String getStartDato() {
    return startDato;
  }

  public void setStartDato(String startDato) {
    this.startDato = startDato;
  }

  public String getSlutDato() {
    return slutDato;
  }

  public void setSlutDato(String slutDato) {
    this.slutDato = slutDato;
  }

  public String getKontraktStatus() {
    return kontraktStatus;
  }

  public void setKontraktStatus(String kontraktStatus) {
    this.kontraktStatus = kontraktStatus;
  }
}
