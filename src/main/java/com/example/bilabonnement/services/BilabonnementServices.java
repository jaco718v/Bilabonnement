package com.example.bilabonnement.services;

import com.example.bilabonnement.model.Kunde;
import com.example.bilabonnement.model.Lejebil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

@Service
public class BilabonnementServices {

  public double udregnAbonnementIndtægt(ArrayList<Lejebil> bilListe){
    double samletIndtægt = 0;
    for(Lejebil bil: bilListe){
      samletIndtægt+=bil.getLejepris();
    }
    return samletIndtægt;
  }


  public String getDato(){
    LocalDate dato = LocalDate.now();
    DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String strDato = dato.format(formater);
    return strDato;
  }

  public String getDatoOmFireMåneder(){
    LocalDate dato = LocalDate.now();
    dato = dato.plusMonths(4);
    DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String datoOmFireMåneder = dato.format(formater);
    return datoOmFireMåneder;
  }

  public String getDatoOmFemMåneder(){
    LocalDate dato = LocalDate.now();
    dato = dato.plusMonths(5);
    DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String datoOmFemMåneder = dato.format(formater);
    return datoOmFemMåneder;
  }

  public String formaterDato(String dato){
    String formateretDato = null;
    try{
    LocalDate nyDato = LocalDate.parse(dato);
    DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    formateretDato = nyDato.format(formater);}
    catch (DateTimeParseException dtpe){
      return dato;
    }
    return formateretDato;
  }

  public ArrayList<Kunde> søgeFunktionKunder(ArrayList<Kunde> kundeliste, String søgeOrd){
    ArrayList<Kunde> fundneKunder = new ArrayList<>();
    int antalChar = søgeOrd.length();
    for(Kunde kunde: kundeliste){
      boolean found = false;
      if (kunde.getFornavn().length()>=antalChar) {
        String kundeCompare = kunde.getFornavn().substring(0, antalChar);
        if (søgeOrd.equalsIgnoreCase(kundeCompare)){
          fundneKunder.add(kunde);
          found=true;
        }
      }
      if (!found && kunde.getEfternavn().length()>=antalChar) {
        String kundeCompare = kunde.getEfternavn().substring(0, antalChar);
        if (søgeOrd.equalsIgnoreCase(kundeCompare)){
          fundneKunder.add(kunde);
        }
      }
    }
    return  fundneKunder;
  }

  public ArrayList<Lejebil> søgeFunktionBiler(ArrayList<Lejebil> billiste, String søgeOrd){
    ArrayList<Lejebil> fundneBiler = new ArrayList<>();
    int antalChar = søgeOrd.length();
    for(Lejebil bil: billiste){
      if (bil.getFabrikant().length()>=antalChar) {
        String kundeCompare = bil.getFabrikant().substring(0, antalChar);
        if (søgeOrd.equalsIgnoreCase(kundeCompare)){
          fundneBiler.add(bil);
        }
      }
    }
    return  fundneBiler;
  }

}
