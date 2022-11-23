package com.example.bilabonnement.controllers;


import com.example.bilabonnement.repositories.BilabonnementRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BilabonnementController {

  BilabonnementRepository bilabonnementRepository;

  public BilabonnementController(BilabonnementRepository b){
    bilabonnementRepository = b;
  }


  @GetMapping("/")
  public String frontpage(){
    return "forside";
  }

  @GetMapping("/opretkunde")
  public String showOpretKunde(){
    return "opretkunde";
  }

  @PostMapping("/opretkunde")
  public String opretKunde(@RequestParam("navn") String navn, @RequestParam("kontaktnummer") int kontaktnummer,
                           @RequestParam("email") String email){
    bilabonnementRepository.opretKundeDB(navn, kontaktnummer, email);
    return "redirect:/";
  }

}
