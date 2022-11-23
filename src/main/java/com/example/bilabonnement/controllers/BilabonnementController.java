package com.example.bilabonnement.controllers;


import com.example.bilabonnement.repositories.BilabonnementRepository;
import org.springframework.stereotype.Controller;

@Controller
public class BilabonnementController {

  BilabonnementRepository bilabonnementRepository;

  public BilabonnementController(BilabonnementRepository b){
    bilabonnementRepository = b;
  }

}
