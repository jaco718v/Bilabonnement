package com.example.bilabonnement.services;

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
}
