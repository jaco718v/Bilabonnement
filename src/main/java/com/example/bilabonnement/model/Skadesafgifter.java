package com.example.bilabonnement.model;

public class Skadesafgifter {
  private int rapportID;
  private double afgiftOverkoerteKilometer =0;
  private double afgiftManglendeService=0;
  private double afgiftManglendeRengoering =0;
  private double afgiftManglendeDaekskifte =0;
  private double afgiftLakfeltSkade=0;
  private double afgiftAlufaelgSkade =0;
  private double afgiftStenslagSkade=0;



  public Skadesafgifter(){}

  public Skadesafgifter(Skadesrapport skadesrapport){
    rapportID = skadesrapport.getRapportID();
    double kilometerPris = 1.5;
    afgiftOverkoerteKilometer = skadesrapport.getOverkoerteKilometer()*kilometerPris;

    double servicePris = 2500;
    if(skadesrapport.isManglendeService()){
      afgiftManglendeService=servicePris;}

    double rengøringPris = 999;
    if(skadesrapport.isManglendeRengoering()){
      afgiftManglendeRengoering =rengøringPris;
    }
    double dækPris = 999;
    if(skadesrapport.isManglendeDaekskifte()){
      afgiftManglendeDaekskifte =dækPris;
    }
    double lakfeltPris = 1500;
    afgiftLakfeltSkade = skadesrapport.getLakfeltSkade()*lakfeltPris;
    double alufælgPris = 400;
    afgiftAlufaelgSkade = skadesrapport.getAlufaelgSkade()*alufælgPris;
    double stenslagAfgiftInitialExtra = 350;
    double stenslagAfgiftEfterfølgende = 300;
    if(skadesrapport.getStenslagSkade()>0){
    afgiftStenslagSkade = skadesrapport.getStenslagSkade()*stenslagAfgiftEfterfølgende+stenslagAfgiftInitialExtra;}
  }

  public void setRapportID(int rapportID) {
    this.rapportID = rapportID;
  }

  public void setAfgiftOverkoerteKilometer(double afgiftOverkoerteKilometer) {
    this.afgiftOverkoerteKilometer = afgiftOverkoerteKilometer;
  }

  public void setAfgiftManglendeService(double afgiftManglendeService) {
    this.afgiftManglendeService = afgiftManglendeService;
  }

  public void setAfgiftManglendeRengoering(double afgiftManglendeRengoering) {
    this.afgiftManglendeRengoering = afgiftManglendeRengoering;
  }

  public void setAfgiftManglendeDaekskifte(double afgiftManglendeDaekskifte) {
    this.afgiftManglendeDaekskifte = afgiftManglendeDaekskifte;
  }

  public void setAfgiftLakfeltSkade(double afgiftLakfeltSkade) {
    this.afgiftLakfeltSkade = afgiftLakfeltSkade;
  }

  public void setAfgiftAlufaelgSkade(double afgiftAlufaelgSkade) {
    this.afgiftAlufaelgSkade = afgiftAlufaelgSkade;
  }

  public void setAfgiftStenslagSkade(double afgiftStenslagSkade) {
    this.afgiftStenslagSkade = afgiftStenslagSkade;
  }

  public int getRapportID() {
    return rapportID;
  }

  public double getAfgiftOverkoerteKilometer() {
    return afgiftOverkoerteKilometer;
  }

  public double getAfgiftManglendeService() {
    return afgiftManglendeService;
  }

  public double getAfgiftManglendeRengoering() {
    return afgiftManglendeRengoering;
  }

  public double getAfgiftManglendeDaekskifte() {
    return afgiftManglendeDaekskifte;
  }

  public double getAfgiftLakfeltSkade() {
    return afgiftLakfeltSkade;
  }

  public double getAfgiftAlufaelgSkade() {
    return afgiftAlufaelgSkade;
  }

  public double getAfgiftStenslagSkade() {
    return afgiftStenslagSkade;
  }
}
