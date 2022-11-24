package com.example.bilabonnement.services;

import com.example.bilabonnement.model.Lejebil;
import org.springframework.stereotype.Service;

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
}
