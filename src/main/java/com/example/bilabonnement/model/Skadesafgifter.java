package com.example.bilabonnement.model;

public class Skadesafgifter {
  private int rapportID;
  private double afgiftOverkørteKilometer=0;
  private double afgiftManglendeService=0;
  private double afgiftManglendeRengøring=0;
  private double afgiftManglendeDækskifte=0;
  private double afgiftLakfeltSkade=0;
  private double afgiftAlufælgSkade=0;
  private double afgiftStenslagSkade=0;



  public Skadesafgifter(){}

  public Skadesafgifter(Skadesrapport skadesrapport){
    rapportID = skadesrapport.getRapportID();
    double kilometerPris = 1.5;
    afgiftOverkørteKilometer = skadesrapport.getOverkørteKilometer()*kilometerPris;

    double servicePris = 2500;
    if(skadesrapport.isManglendeService()){
      afgiftManglendeService=servicePris;}

    double rengøringPris = 999;
    if(skadesrapport.isManglendeRengøring()){
      afgiftManglendeRengøring=rengøringPris;
    }
    double dækPris = 999;
    if(skadesrapport.isManglendeDækskifte()){
      afgiftManglendeDækskifte=dækPris;
    }
    double lakfeltPris = 1500;
    afgiftLakfeltSkade = skadesrapport.getLakfeltSkade()*lakfeltPris;
    double alufælgPris = 400;
    afgiftAlufælgSkade = skadesrapport.getAlufælgSkade()*alufælgPris;
    double stenslagAfgiftInitialExtra = 350;
    double stenslagAfgiftEfterfølgende = 300;
    if(skadesrapport.getStenslagSkade()>0){
    afgiftStenslagSkade = skadesrapport.getStenslagSkade()*stenslagAfgiftEfterfølgende+stenslagAfgiftInitialExtra;}
  }

  public void setRapportID(int rapportID) {
    this.rapportID = rapportID;
  }

  public void setAfgiftOverkørteKilometer(double afgiftOverkørteKilometer) {
    this.afgiftOverkørteKilometer = afgiftOverkørteKilometer;
  }

  public void setAfgiftManglendeService(double afgiftManglendeService) {
    this.afgiftManglendeService = afgiftManglendeService;
  }

  public void setAfgiftManglendeRengøring(double afgiftManglendeRengøring) {
    this.afgiftManglendeRengøring = afgiftManglendeRengøring;
  }

  public void setAfgiftManglendeDækskifte(double afgiftManglendeDækskifte) {
    this.afgiftManglendeDækskifte = afgiftManglendeDækskifte;
  }

  public void setAfgiftLakfeltSkade(double afgiftLakfeltSkade) {
    this.afgiftLakfeltSkade = afgiftLakfeltSkade;
  }

  public void setAfgiftAlufælgSkade(double afgiftAlufælgSkade) {
    this.afgiftAlufælgSkade = afgiftAlufælgSkade;
  }

  public void setAfgiftStenslagSkade(double afgiftStenslagSkade) {
    this.afgiftStenslagSkade = afgiftStenslagSkade;
  }

  public int getRapportID() {
    return rapportID;
  }

  public double getAfgiftOverkørteKilometer() {
    return afgiftOverkørteKilometer;
  }

  public double getAfgiftManglendeService() {
    return afgiftManglendeService;
  }

  public double getAfgiftManglendeRengøring() {
    return afgiftManglendeRengøring;
  }

  public double getAfgiftManglendeDækskifte() {
    return afgiftManglendeDækskifte;
  }

  public double getAfgiftLakfeltSkade() {
    return afgiftLakfeltSkade;
  }

  public double getAfgiftAlufælgSkade() {
    return afgiftAlufælgSkade;
  }

  public double getAfgiftStenslagSkade() {
    return afgiftStenslagSkade;
  }
}
