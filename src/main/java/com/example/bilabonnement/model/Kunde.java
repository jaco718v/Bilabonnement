package com.example.bilabonnement.model;

public class Kunde {
  private String navn;
  private int kontaktnummer;
  private String email;

  public Kunde(String navn, int kontaktnummer, String email) {
    this.navn = navn;
    this.kontaktnummer = kontaktnummer;
    this.email = email;
  }
}
