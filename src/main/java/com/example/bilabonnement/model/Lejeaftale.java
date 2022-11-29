package com.example.bilabonnement.model;

public class Lejeaftale {
  private int kontraktNr;
  private int kilometerpakke;
  private String startdato;
  private String slutdato;
  private int kundeID;
  private int vognnummer;
  private String kontraktstatus;

  public Lejeaftale(int kontraktNr, int kilometerpakke, String startdato, String slutdato, int kundeID, int vognnummer) {
    this.kontraktNr = kontraktNr;
    this.kilometerpakke = kilometerpakke;
    this.startdato = startdato;
    this.slutdato = slutdato;
    this.kundeID = kundeID;
    this.vognnummer = vognnummer;
  }
}
