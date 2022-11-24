package com.example.bilabonnement.model;

public class Skadesrapport {
  private int kontraktID;
  private int overkørteKilometer;
  private boolean manglendeService;
  private boolean manglendeRengøring;
  private boolean manglendeDækskifte;
  private int lakfeltSkade;
  private int alufælgSkade;
  private int stenslagSkade;

  public Skadesrapport() {
  }

  public int getOverkørteKilometer() {
    return overkørteKilometer;
  }

  public void setOverkørteKilometer(int overkørteKilometer) {
    this.overkørteKilometer = overkørteKilometer;
  }

  public boolean isManglendeService() {
    return manglendeService;
  }

  public void setManglendeService(boolean manglendeService) {
    this.manglendeService = manglendeService;
  }

  public boolean isManglendeRengøring() {
    return manglendeRengøring;
  }

  public void setManglendeRengøring(boolean manglendeRengøring) {
    this.manglendeRengøring = manglendeRengøring;
  }

  public boolean isManglendeDækskifte() {
    return manglendeDækskifte;
  }

  public void setManglendeDækskifte(boolean manglendeDækskifte) {
    this.manglendeDækskifte = manglendeDækskifte;
  }

  public int getLakfeltSkade() {
    return lakfeltSkade;
  }

  public void setLakfeltSkade(int lakfeltSkade) {
    this.lakfeltSkade = lakfeltSkade;
  }

  public int getAlufælgSkade() {
    return alufælgSkade;
  }

  public void setAlufælgSkade(int alufælgSkade) {
    this.alufælgSkade = alufælgSkade;
  }

  public int getStenslagSkade() {
    return stenslagSkade;
  }

  public void setStenslagSkade(int stenslagSkade) {
    this.stenslagSkade = stenslagSkade;
  }
}
