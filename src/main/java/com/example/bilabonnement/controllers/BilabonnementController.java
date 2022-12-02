package com.example.bilabonnement.controllers;


import com.example.bilabonnement.model.*;
import com.example.bilabonnement.repositories.BilabonnementRepository;
import com.example.bilabonnement.services.BilabonnementServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class BilabonnementController {

  BilabonnementRepository bilabonnementRepository;
  BilabonnementServices bilabonnementServices;

  public BilabonnementController(BilabonnementRepository r, BilabonnementServices s){
    bilabonnementRepository = r;
    bilabonnementServices = s;
  }




  @GetMapping("/")
  public String frontpage(HttpSession session){
    session.invalidate();
    return "forside";
  }

  @GetMapping("/opretkunde")
  public String showOpretKunde(Model model){
    Kunde kunde = new Kunde();
    model.addAttribute("kunde",kunde);
    return "opretkunde";
  }

  @PostMapping("/opretkunde")
  public String opretKunde(@ModelAttribute Kunde kunde){
    bilabonnementRepository.opretKundeDB(kunde.getFornavn(), kunde.getEfternavn(), kunde.getKontaktnummer(), kunde.getEmail());
    return "redirect:/";
  }

  @GetMapping("/findkundeoverblik")
  public String showFindKundeOverblik(HttpSession session){
    ArrayList<Kunde> kundeListe = (ArrayList<Kunde>)session.getAttribute("kundeListe");
    if(kundeListe==null)
    {kundeListe = bilabonnementRepository.getAlleKunder();
      session.setAttribute("kundeListe",kundeListe);
    }
    return "findkundeoverblik";
  }

  @PostMapping("/findkundeoverblik")
  public String findKundeOverblik(@RequestParam("fornavn") String fornavn, HttpSession session){
    ArrayList<Kunde> kundeListe = bilabonnementRepository.findKunderMedFornavn(fornavn);
    session.setAttribute("kundeListe",kundeListe);
    return "redirect:/findkundeoverblik";
  }


  @GetMapping("/findkundetilaftale")
  public String showFindKundeTilAftale(HttpSession session){
    ArrayList<Kunde> kundeListe = (ArrayList<Kunde>)session.getAttribute("kundeListe");
    if(kundeListe==null) {
      kundeListe = bilabonnementRepository.getAlleKunder();
      session.setAttribute("kundeListe",kundeListe);
    }
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
    if(bilListe==null) {
      bilListe = bilabonnementRepository.getAlleLedigeLejebiler();
      session.setAttribute("bilListe",bilListe);
    }
    return "findbiltilaftale";
  }

  @PostMapping("/findbiltilaftale")
  public String findBilTilAftale(@RequestParam("fabrikant") String fabrikant,
                                 HttpSession session){
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
    Lejeaftale lejeaftale = new Lejeaftale();
    model.addAttribute("lejeaftale",lejeaftale);
    model.addAttribute("kunde",bilabonnementRepository.findKundeMedID((int)session.getAttribute("kundeID")));
    model.addAttribute("bil", bilabonnementRepository.findBilMedVognnummer((int)session.getAttribute("vognnummer")));
    model.addAttribute("datoNu",bilabonnementServices.getDato());
    model.addAttribute("datoFireMaaneder",bilabonnementServices.getDatoOmFireMåneder());
    model.addAttribute("datoFemMaaneder",bilabonnementServices.getDatoOmFemMåneder());
    return "opretaftale";
  }

  @PostMapping("/opretaftale")
  public String opretAftale(@ModelAttribute Lejeaftale lejeaftale){   //Test om virker
    String formateretSlutdato = bilabonnementServices.formaterDato(lejeaftale.getSlutDato());
    bilabonnementRepository.opretLejeaftaleDB(lejeaftale.getKundeID(), lejeaftale.getVognnummer(), lejeaftale.getAftaleType(), lejeaftale.getStartDato(), formateretSlutdato);
    bilabonnementRepository.setBilUdlejetDB(lejeaftale.getVognnummer());
    return "redirect:/";
  }

  @GetMapping("/updateaftale/{id}")
  public String showUpdateAftale(@PathVariable("id") int kontraktID, Model model){
    Lejeaftale lejeaftale = bilabonnementRepository.getLejeaftaleViaKontraktID(kontraktID);
    model.addAttribute("lejeaftale", lejeaftale);
    return "updateaftale";
  }

  @PostMapping("/updateaftale")
  public String updateAftale(@ModelAttribute Lejeaftale lejeaftale, RedirectAttributes redirectAttributes){
    bilabonnementRepository.updateLejeaftale(lejeaftale);
    redirectAttributes.addAttribute("id",lejeaftale.getKundeID());
  return "redirect:/redirectoverblik";
  }

  @GetMapping("/redirectoverblik")
  public String redirectOverblik(@RequestParam int id){
    return "redirect:/kundeoverblik/{id}";
  }

  @GetMapping("/sletaftale/{id}")
  public String sletLejeaftale(@PathVariable("id") int kontraktID){
    int vognnummer = bilabonnementRepository.getLejeaftaleViaKontraktID(kontraktID).getVognnummer();
    bilabonnementRepository.sletLejeaftaleOgRelateret(kontraktID);
    bilabonnementRepository.setBilLedigDB(vognnummer);
    return "redirect:/kundeoverblik/{id}"; //Tilbage placeholder
  }



  @GetMapping("/opretskadesrapport/{id}")
  public String showOpretSkadesrapport(@PathVariable("id") int vognnummer, Model model){
    int kontraktID = bilabonnementRepository.findKontraktIDMedVognnummer(vognnummer);
    Skadesrapport skadesrapport = new Skadesrapport(kontraktID);
    model.addAttribute("vognnummer", vognnummer);
    model.addAttribute("skadesrapport",skadesrapport);

    return "opretskadesrapport";
  }

  @PostMapping("/opretskadesrapport")
  public String opretSkadesrapport(@ModelAttribute Skadesrapport skadesrapport, @RequestParam("vognnummer") int vognnummer){
    bilabonnementRepository.opretSkadesrapportDB(skadesrapport);
    bilabonnementRepository.setBilTjekketDB(vognnummer);
    bilabonnementRepository.setAftaleBetaling(skadesrapport.getKontraktID());
    return "redirect:/";
  }

  @GetMapping("/visbillager")
  public String visBilLager(Model model){
    String status = "ledig";
    ArrayList<Lejebil> bilListe  = bilabonnementRepository.getBilListeViaStatus(status);
    ArrayList<String> lavBestand = bilabonnementRepository.findLavFabrikantBestand();
    int antalBiler = bilListe.size();
    model.addAttribute("bilListe",bilListe);
    model.addAttribute("antalBiler",antalBiler);
    model.addAttribute("lavbestand",lavBestand);

    return "lager";
  }

  @GetMapping("/visudlejedebiler")
  public String visUdlejedeBiler(Model model){
  String status = "udlejet";
  ArrayList<Lejebil> bilListe  = bilabonnementRepository.getBilListeViaStatus(status);
  int antalBiler = bilListe.size();
  double samletIndtægt = bilabonnementServices.udregnAbonnementIndtægt(bilListe);
  model.addAttribute("bilListe",bilListe);
  model.addAttribute("antalBiler",antalBiler);
  model.addAttribute("samletIndtaegt",samletIndtægt);

    return "udlejedebiler";
  }

  @GetMapping("/meldafleveret/{vognnummer}/{kundeID}")
  public String meldBilAfleveret(@PathVariable("vognnummer") int vognnummer, @PathVariable("kundeID") int kundeID){
    bilabonnementRepository.setBilAfleveret(vognnummer);
    bilabonnementRepository.setAftaleVenter(vognnummer);
    return "redirect:/kundeoverblik/{kundeID}";
  }

  @GetMapping("/meldaftaleafsluttet/{vognnummer}/{kundeID}")
  public String meldAftaleAfsluttet(@PathVariable("vognnummer") int vognnummer, @PathVariable("kundeID") int kundeID){
    bilabonnementRepository.setAftaleAfsluttet(vognnummer);
    bilabonnementRepository.setBilLedigDB(vognnummer);
    return "redirect:/kundeoverblik/{kundeID}";
  }

  @GetMapping("/bilertilskadesrapport")
  public String bilerTilSkadesrapport(Model model){
    String status = "afleveret";
    ArrayList<Lejebil> bilListe  = bilabonnementRepository.getBilListeViaStatus(status);
    int antalBiler = bilListe.size();
    model.addAttribute("bilListe",bilListe);
    model.addAttribute("antalBiler",antalBiler);

    return "bilertilskadesrapport";
  }

  @GetMapping("/skadesrapport/{id}")
  public String visSkaderapport(@PathVariable("id") int kontraktID, Model model){
    Skadesrapport skadesrapport = bilabonnementRepository.getSkadesrapportViaKontraktID(kontraktID);
    model.addAttribute("skadesrapport",skadesrapport);
    int rapportID = skadesrapport.getRapportID();
    Skadesafgifter skadesafgifter = bilabonnementRepository.getSkadesafgifterViaRapportID(rapportID);
    model.addAttribute("skadesafgifter",skadesafgifter);

    return "skadesrapport";
  }

  @GetMapping("/kundeoverblik/{id}")
  public String visLejeAftaler(@PathVariable("id") int kundeID, Model model){
    String betalingStatus ="betaling";
    ArrayList<Lejeaftale> lejeaftalerBetaling =
        bilabonnementRepository.getLejeaftalerViaKundeIDOgStatus(kundeID,betalingStatus);
    model.addAttribute("lejeaftalerBetaling",lejeaftalerBetaling);

    String venterStatus = "venter";
    ArrayList<Lejeaftale> lejeaftalerVenter =
        bilabonnementRepository.getLejeaftalerViaKundeIDOgStatus(kundeID,venterStatus);
    model.addAttribute("lejeaftalerVenter",lejeaftalerVenter);

    String igangværendeStatus = "igangværende";
    ArrayList<Lejeaftale> lejeaftalerIgangværende =
        bilabonnementRepository.getLejeaftalerViaKundeIDOgStatus(kundeID, igangværendeStatus);
    model.addAttribute("lejeaftalerIgangvaerende", lejeaftalerIgangværende);

    String afsluttetStatus = "afsluttet";
    ArrayList<Lejeaftale> lejeaftalerAfsluttet =
        bilabonnementRepository.getLejeaftalerViaKundeIDOgStatus(kundeID, afsluttetStatus);
    model.addAttribute("lejeaftalerAfsluttet",lejeaftalerAfsluttet);

    return "kundeoverblik";
  }
}
