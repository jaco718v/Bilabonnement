package com.example.bilabonnement.model;

public class Lejebil {
  private int vognnummer;
  private int stelnummer;
  private String fabrikant;
  private String model;
  private String udstyrspakke;
  private double købspris;
  private double lejepris;
  private double stålpris;
  private double co2Niveau;
  private double regAfgift;
  private String status;

  public Lejebil(int vognnummer, int stelnummer, String fabrikant, String model, String udstyrspakke, double købspris, double lejepris, double stålpris, double co2Niveau, double regAfgift, String status) {
    this.vognnummer = vognnummer;
    this.stelnummer = stelnummer;
    this.fabrikant = fabrikant;
    this.model = model;
    this.udstyrspakke = udstyrspakke;
    this.købspris = købspris;
    this.lejepris = lejepris;
    this.stålpris = stålpris;
    this.co2Niveau = co2Niveau;
    this.regAfgift = regAfgift;
    this.status = status;
  }

  public int getVognnummer() {
    return vognnummer;
  }

  public int getStelnummer() {
    return stelnummer;
  }

  public String getFabrikant() {
    return fabrikant;
  }

  public String getModel() {
    return model;
  }

  public String getUdstyrspakke() {
    return udstyrspakke;
  }

  public double getKøbspris() {
    return købspris;
  }

  public double getLejepris() {
    return lejepris;
  }

  public double getStålpris() {
    return stålpris;
  }

  public double getCo2Niveau() {
    return co2Niveau;
  }

  public double getRegAfgift() {
    return regAfgift;
  }

  public String getStatus() {
    return status;
  }
}
