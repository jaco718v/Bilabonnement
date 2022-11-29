package com.example.bilabonnement.model;

public class Lejeaftale {
  private int kontraktNr;
  private int kundeID;
  private int vognnummer;
  private String aftaleType;
  private int kilometerpakke;
  private String startDato;
  private String slutDato;
  private String kontraktStatus;

  public Lejeaftale(int kontraktNr, int kundeID, int vognnummer, String aftaleType, int kilometerpakke, String startDato, String slutDato, String kontraktStatus) {
    this.kontraktNr = kontraktNr;
    this.kundeID = kundeID;
    this.vognnummer = vognnummer;
    this.aftaleType = aftaleType;
    this.kilometerpakke = kilometerpakke;
    this.startDato = startDato;
    this.slutDato = slutDato;
    this.kontraktStatus = kontraktStatus;
  }
}
