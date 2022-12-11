package com.example.bilabonnement.controllers;


import com.example.bilabonnement.model.*;
import com.example.bilabonnement.repositories.DataRegRepository;
import com.example.bilabonnement.repositories.ForretningsRepository;
import com.example.bilabonnement.repositories.SkadeUdbedingRepository;
import com.example.bilabonnement.services.BilabonnementServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class BilabonnementController {

  DataRegRepository dataRegRepository;
  ForretningsRepository forretningsRepository;
  SkadeUdbedingRepository skadeUdbedingRepository;
  BilabonnementServices bilabonnementServices;

  public BilabonnementController(DataRegRepository r, BilabonnementServices s, ForretningsRepository f, SkadeUdbedingRepository su){
    dataRegRepository = r;
    bilabonnementServices = s;
    forretningsRepository = f;
    skadeUdbedingRepository =su;
  }




  @GetMapping("/")
  public String frontpage(HttpSession session){
    session.invalidate();
    return "forside";
  }



  @GetMapping("/opretkunde")
  public String visOpretKunde(Model model){
    Kunde kunde = dataRegRepository.newKunde();
    model.addAttribute("kunde",kunde);
    return "opretkunde";
  }

  @PostMapping("/opretkunde")
  public String opretKunde(@ModelAttribute Kunde kunde){
    dataRegRepository.opretKundeDB(kunde.getFornavn(), kunde.getEfternavn(), kunde.getKontaktnummer(), kunde.getEmail());
    return "redirect:/findkundeoverblik";
  }



  @GetMapping("/findkundeoverblik")
  public String visFindKundeOverblik(HttpSession session){
    ArrayList<Kunde> kundeListe = (ArrayList<Kunde>)session.getAttribute("kundeListe");
    if(kundeListe==null)
    {kundeListe = dataRegRepository.findAlleKunder();
      session.setAttribute("kundeListe",kundeListe);
    }
    return "findkundeoverblik";
  }

  @PostMapping("/findkundeoverblik")
  public String findKundeOverblik(@RequestParam("fornavn") String fornavn, HttpSession session){
    ArrayList<Kunde> kundeListe = dataRegRepository.findAlleKunder();
    kundeListe = bilabonnementServices.søgeFunktionKunder(kundeListe,fornavn);
    session.setAttribute("kundeListe",kundeListe);
    return "redirect:/findkundeoverblik";
  }

  @GetMapping("/kundeoverblik/{id}")
  public String visLejeAftaler(@PathVariable("id") int kundeID, Model model){
    String betalingStatus ="betaling";
    ArrayList<Lejeaftale> lejeaftalerBetaling =
        dataRegRepository.findLejeaftalerViaKundeIDOgStatus(kundeID,betalingStatus);
    model.addAttribute("lejeaftalerBetaling",lejeaftalerBetaling);

    String venterStatus = "venter";
    ArrayList<Lejeaftale> lejeaftalerVenter =
        dataRegRepository.findLejeaftalerViaKundeIDOgStatus(kundeID,venterStatus);
    model.addAttribute("lejeaftalerVenter",lejeaftalerVenter);

    String igangværendeStatus = "igangværende";
    ArrayList<Lejeaftale> lejeaftalerIgangværende =
        dataRegRepository.findLejeaftalerViaKundeIDOgStatus(kundeID, igangværendeStatus);
    model.addAttribute("lejeaftalerIgangvaerende", lejeaftalerIgangværende);

    String afsluttetStatus = "afsluttet";
    ArrayList<Lejeaftale> lejeaftalerAfsluttet =
        dataRegRepository.findLejeaftalerViaKundeIDOgStatus(kundeID, afsluttetStatus);
    model.addAttribute("lejeaftalerAfsluttet",lejeaftalerAfsluttet);

    return "kundeoverblik";
  }

  @GetMapping("/meldafleveret/{vognnummer}/{kundeID}")
  public String meldBilAfleveret(@PathVariable("vognnummer") int vognnummer, @PathVariable("kundeID") int kundeID){
    forretningsRepository.setBilAfleveret(vognnummer);
    dataRegRepository.setAftaleVenter(vognnummer);
    return "redirect:/kundeoverblik/{kundeID}";
  }

  @GetMapping("/meldaftaleafsluttet/{vognnummer}/{kundeID}")
  public String meldAftaleAfsluttet(@PathVariable("vognnummer") int vognnummer, @PathVariable("kundeID") int kundeID){
    dataRegRepository.setAftaleAfsluttet(vognnummer);
    forretningsRepository.setBilLedig(vognnummer);
    return "redirect:/kundeoverblik/{kundeID}";
  }



  @GetMapping("/findkundetilaftale")
  public String visFindKundeTilAftale(HttpSession session){
    ArrayList<Kunde> kundeListe = (ArrayList<Kunde>)session.getAttribute("kundeListe");
    if(kundeListe==null) {
      kundeListe = dataRegRepository.findAlleKunder();
      session.setAttribute("kundeListe",kundeListe);
    }
    return "findkundetilaftale";
  }

  @PostMapping("/findkundetilaftale")
  public String findKundeTilAftale(@RequestParam("fornavn") String fornavn, HttpSession session){
    ArrayList<Kunde> kundeListe = dataRegRepository.findAlleKunder();
    kundeListe = bilabonnementServices.søgeFunktionKunder(kundeListe,fornavn);
    session.setAttribute("kundeListe",kundeListe);
    return "redirect:/findkundetilaftale";
  }

  @GetMapping("/saveid/{id}")
  public String saveID(@PathVariable("id") int kundeID, HttpSession session){
    session.setAttribute("kundeID",kundeID);
    return "redirect:/findbiltilaftale";
  }

  @GetMapping("/findbiltilaftale")
  public String visFindBilTilAftale(HttpSession session, Model model){
    ArrayList<Lejebil> bilListe = (ArrayList<Lejebil>)session.getAttribute("bilListe");
    if(bilListe==null) {
      String statusLedig = "Ledig";
      bilListe = forretningsRepository.findBilListeViaStatus(statusLedig);
      session.setAttribute("bilListe",bilListe);
    }
    return "findbiltilaftale";
  }

  @PostMapping("/findbiltilaftale")
  public String findBilTilAftale(@RequestParam("fabrikant") String fabrikant,
                                 HttpSession session){
    String bilStatus = "Ledig";
    ArrayList<Lejebil> bilListe = forretningsRepository.findBilListeViaStatus(bilStatus);
    bilListe = bilabonnementServices.søgeFunktionBiler(bilListe,fabrikant);
    session.setAttribute("bilListe",bilListe);
    return "redirect:/findbiltilaftale";
  }

  @GetMapping("/savevognnummer/{id}")
  public String saveVognnummer(@PathVariable("id") int vognnummer, HttpSession session){
    session.setAttribute("vognnummer", vognnummer);
    return "redirect:/opretaftale";
  }

  @GetMapping("/opretaftale")
  public String visOpretAftale(HttpSession session ,Model model){
    Lejeaftale lejeaftale = dataRegRepository.newLejeaftale();
    model.addAttribute("lejeaftale",lejeaftale);
    model.addAttribute("kunde", dataRegRepository.findKundeMedID((int)session.getAttribute("kundeID")));
    model.addAttribute("bil", forretningsRepository.findBilMedVognnummer((int)session.getAttribute("vognnummer")));
    model.addAttribute("datoNu",bilabonnementServices.getDato());
    model.addAttribute("datoFireMaaneder",bilabonnementServices.getDatoOmFireMåneder());
    model.addAttribute("datoFemMaaneder",bilabonnementServices.getDatoOmFemMåneder());
    return "opretaftale";
  }

  @PostMapping("/opretaftale")
  public String opretAftale(@ModelAttribute Lejeaftale lejeaftale){   //Test om virker
    String formateretSlutdato = bilabonnementServices.formaterDato(lejeaftale.getSlutDato());
    dataRegRepository.opretLejeaftaleDB(lejeaftale.getKundeID(), lejeaftale.getVognnummer(), lejeaftale.getAftaleType(), lejeaftale.getStartDato(), formateretSlutdato);
    forretningsRepository.setBilUdlejet(lejeaftale.getVognnummer());
    return "redirect:/findkundeoverblik";
  }

  @GetMapping("/updateaftale/{id}")
  public String visUpdaterAftale(@PathVariable("id") int kontraktID, Model model){
    Lejeaftale lejeaftale = dataRegRepository.findLejeaftaleViaKontraktID(kontraktID);
    model.addAttribute("lejeaftale", lejeaftale);
    return "updateaftale";
  }

  @PostMapping("/updateaftale")
  public String updaterAftale(@ModelAttribute Lejeaftale lejeaftale, RedirectAttributes redirectAttributes){
    dataRegRepository.updaterLejeaftaleDB(lejeaftale);
    redirectAttributes.addAttribute("id",lejeaftale.getKundeID());
  return "redirect:/redirectoverblik";
  }

  @GetMapping("/redirectoverblik")
  public String redirectOverblik(@RequestParam int id){
    return "redirect:/kundeoverblik/{id}";
  }

  @GetMapping("/sletaftale/{id}")
  public String sletLejeaftale(@PathVariable("id") int kontraktID){
    int vognnummer = dataRegRepository.findLejeaftaleViaKontraktID(kontraktID).getVognnummer();
    dataRegRepository.sletLejeaftaleOgRelateret(kontraktID);
    forretningsRepository.setBilLedig(vognnummer);
    return "redirect:/kundeoverblik/{id}"; //Tilbage placeholder
  }



  @GetMapping("/bilertilskadesrapport")
  public String bilerTilSkadesrapport(Model model){
    String status = "afleveret";
    ArrayList<Lejebil> bilListe  = forretningsRepository.findBilListeViaStatus(status);
    int antalBiler = bilListe.size();
    model.addAttribute("bilListe",bilListe);
    model.addAttribute("antalBiler",antalBiler);

    return "bilertilskadesrapport";
  }

  @GetMapping("/opretskadesrapport/{id}")
  public String visOpretSkadesrapport(@PathVariable("id") int vognnummer, Model model){
    int kontraktID = dataRegRepository.findKontraktIDMedVognnummer(vognnummer);
    Skadesrapport skadesrapport = skadeUdbedingRepository.newSkadesrapport(kontraktID);
    model.addAttribute("vognnummer", vognnummer);
    model.addAttribute("skadesrapport",skadesrapport);

    return "opretskadesrapport";
  }

  @PostMapping("/opretskadesrapport")
  public String opretSkadesrapport(@ModelAttribute Skadesrapport skadesrapport, @RequestParam("vognnummer") int vognnummer){
    skadeUdbedingRepository.opretSkadesrapportDB(skadesrapport);
    forretningsRepository.setBilTjekket(vognnummer);
    dataRegRepository.setAftaleBetaling(skadesrapport.getKontraktID());
    return "redirect:/bilertilskadesrapport";
  }

  @GetMapping("/skadesrapport/{id}")
  public String visSkaderapport(@PathVariable("id") int kontraktID, Model model){
    int kundeID = dataRegRepository.findKundeIDMedKontraktID(kontraktID);
    model.addAttribute("kundeID",kundeID);
    Skadesrapport skadesrapport = skadeUdbedingRepository.findSkadesrapportViaKontraktID(kontraktID);
    model.addAttribute("skadesrapport",skadesrapport);
    Skadesafgifter skadesafgifter = skadesrapport.getSkadesafgifter();
    model.addAttribute("skadesafgifter",skadesafgifter);

    return "skadesrapport";
  }



  @GetMapping("/visbillager")
  public String visBilLager(Model model){
    String status = "ledig";
    ArrayList<Lejebil> bilListe  = forretningsRepository.findBilListeViaStatus(status);
    ArrayList<String> lavBestand = forretningsRepository.findLavFabrikantBestand();
    int antalBiler = bilListe.size();
    model.addAttribute("bilListe",bilListe);
    model.addAttribute("antalBiler",antalBiler);
    model.addAttribute("lavbestand",lavBestand);

    return "lager";
  }

  @GetMapping("/visudlejedebiler")
  public String visUdlejedeBiler(Model model){
  String status = "udlejet";
  ArrayList<Lejebil> bilListe  = forretningsRepository.findBilListeViaStatus(status);
  int antalBiler = bilListe.size();
  double samletIndtægt = bilabonnementServices.udregnAbonnementIndtægt(bilListe);
  model.addAttribute("bilListe",bilListe);
  model.addAttribute("antalBiler",antalBiler);
  model.addAttribute("samletIndtaegt",samletIndtægt);

    return "udlejedebiler";
  }

}
