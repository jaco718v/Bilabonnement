package com.example.bilabonnement.services;

import com.example.bilabonnement.model.Lejebil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

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
    Date dato = new Date();
    SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
    String strDato = formater.format(dato);
    return strDato;
  }

  public String getDatoOmFemMåneder(){
    LocalDate dato = LocalDate.now();
    dato = dato.plusMonths(5);
    int dag = dato.getDayOfMonth();
    int måned = dato.getMonthValue();
    int år = dato.getYear();
    String datoOmFemMåneder = dag+"/"+måned+"/"+år;
    return datoOmFemMåneder;
  }
}
