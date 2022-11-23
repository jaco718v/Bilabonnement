package com.example.bilabonnement.controllers;


import com.example.bilabonnement.model.Kunde;
import com.example.bilabonnement.model.Lejebil;
import com.example.bilabonnement.repositories.BilabonnementRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

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
  public String opretKunde(@RequestParam("fornavn") String fornavn, @RequestParam("efternavn") String efternavn, @RequestParam("kontaktnummer") int kontaktnummer,
                           @RequestParam("email") String email){
    bilabonnementRepository.opretKundeDB(fornavn, efternavn, kontaktnummer, email);
    return "redirect:/";
  }

  @GetMapping("/findkundetilaftale")
  public String showFindKundeTilAftale(HttpSession session){
    ArrayList<Kunde> kundeListe = (ArrayList<Kunde>)session.getAttribute("kundeListe");
    if(kundeListe==null) kundeListe = new ArrayList<>();
    return "findkundetilaftale";
  }

  @PostMapping("/findkundetilaftale")
  public String findKundeTilAftale(@RequestParam("fornavn") String fornavn, HttpSession session){
    ArrayList<Kunde> kundeListe = bilabonnementRepository.findKunderMedFornavn(fornavn);
    session.setAttribute("kundeListe",kundeListe);
    return "redirect:/findkundetilaftale";
  }

  @GetMapping("/saveid/{id}")
  public String saveID(@PathVariable("id") int kundeID, HttpSession session){
    session.setAttribute("kundeID",kundeID);
    return "redirect:/findbiltilaftale";
  }

  @GetMapping("/findbiltilaftale")
  public String showFindBilTilAftale(HttpSession session, Model model){
    ArrayList<Lejebil> bilListe = (ArrayList<Lejebil>)session.getAttribute("bilListe");
    if(bilListe==null) bilListe = new ArrayList<>();
    return "findbiltilaftale";
  }

  @PostMapping("/findbiltilaftale")
  public String findBilTilAftale(@RequestParam("fabrikant") String fabrikant,
                                 HttpSession session, RedirectAttributes redirectAttributes){
    ArrayList<Lejebil> bilListe = bilabonnementRepository.findBilerFraFabrikant(fabrikant);
    session.setAttribute("bilListe",bilListe);
    return "redirect:/findbiltilaftale";
  }

  @GetMapping("/savevognnummer/{id}")
  public String saveVognnummer(@PathVariable("id") int vognnummer, HttpSession session){
    session.setAttribute("vognnummer", vognnummer);
    return "redirect:/opretaftale";
  }


  @GetMapping("/opretaftale")
  public String showOpretAftale(HttpSession session ,Model model){
    model.addAttribute("kunde",bilabonnementRepository.findKundeMedID((int)session.getAttribute("kundeID")));
    model.addAttribute("bil", bilabonnementRepository.findBilMedVognnummer((int)session.getAttribute("vognnummer")));
    return "opretaftale";
  }

  @PostMapping("/opretaftale")
  public String opretAftale( @RequestParam("kundeID") int kundeID, @RequestParam("vognnummer") int vognnummer,
                            @RequestParam("aftaletype") boolean aftaletype, @RequestParam("kilometerpakke") int kilometerpakke,
                            @RequestParam("startdato") String startdato, @RequestParam("slutdato") String slutdato){
    bilabonnementRepository.opretLejeaftaleDB(kundeID, vognnummer, aftaletype, kilometerpakke, startdato, slutdato);
    return "forside";
  }

}
