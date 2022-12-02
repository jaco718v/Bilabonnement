package com.example.bilabonnement.model;

public class Skadesrapport {
  private int kontraktID;
  private int rapportID;
  private int overkoerteKilometer;
  private boolean manglendeService;
  private boolean manglendeRengoering;
  private boolean manglendeDaekskifte;
  private int lakfeltSkade;
  private int alufaelgSkade;
  private int stenslagSkade;

  public Skadesrapport(){}

  public Skadesrapport(int kontraktID){
    this.kontraktID=kontraktID;
  }

  public Skadesrapport(int kontraktID, int rapportID, int overkoerteKilometer, boolean manglendeService, boolean manglendeRengoering, boolean manglendeDaekskifte, int lakfeltSkade, int alufaelgSkade, int stenslagSkade) {
    this.kontraktID = kontraktID;
    this.rapportID = rapportID;
    this.overkoerteKilometer = overkoerteKilometer;
    this.manglendeService = manglendeService;
    this.manglendeRengoering = manglendeRengoering;
    this.manglendeDaekskifte = manglendeDaekskifte;
    this.lakfeltSkade = lakfeltSkade;
    this.alufaelgSkade = alufaelgSkade;
    this.stenslagSkade = stenslagSkade;
  }


  public int getOverkoerteKilometer() {
    return overkoerteKilometer;
  }

  public void setOverkoerteKilometer(int overkoerteKilometer) {
    this.overkoerteKilometer = overkoerteKilometer;
  }

  public void setKontraktID(int kontraktID) {
    this.kontraktID = kontraktID;
  }

  public void setRapportID(int rapportID) {
    this.rapportID = rapportID;
  }

  public boolean isManglendeService() {
    return manglendeService;
  }

  public void setManglendeService(boolean manglendeService) {
    this.manglendeService = manglendeService;
  }

  public boolean isManglendeRengoering() {
    return manglendeRengoering;
  }

  public void setManglendeRengoering(boolean manglendeRengoering) {
    this.manglendeRengoering = manglendeRengoering;
  }

  public boolean isManglendeDaekskifte() {
    return manglendeDaekskifte;
  }

  public void setManglendeDaekskifte(boolean manglendeDaekskifte) {
    this.manglendeDaekskifte = manglendeDaekskifte;
  }

  public int getLakfeltSkade() {
    return lakfeltSkade;
  }

  public void setLakfeltSkade(int lakfeltSkade) {
    this.lakfeltSkade = lakfeltSkade;
  }

  public int getAlufaelgSkade() {
    return alufaelgSkade;
  }

  public void setAlufaelgSkade(int alufaelgSkade) {
    this.alufaelgSkade = alufaelgSkade;
  }

  public int getStenslagSkade() {
    return stenslagSkade;
  }

  public void setStenslagSkade(int stenslagSkade) {
    this.stenslagSkade = stenslagSkade;
  }

  public int getKontraktID() {
    return kontraktID;
  }

  public int getRapportID() {
    return rapportID;
  }
}
