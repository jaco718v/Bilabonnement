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
  public String showOpretKunde(){
    return "opretkunde";
  }

  @PostMapping("/opretkunde")
  public String opretKunde(@RequestParam("fornavn") String fornavn, @RequestParam("efternavn") String efternavn, @RequestParam("kontaktnummer") int kontaktnummer,
                           @RequestParam("email") String email){
    bilabonnementRepository.opretKundeDB(fornavn, efternavn, kontaktnummer, email);
    return "redirect:/";
  }

  @GetMapping("/findkundehistorik")
  public String showFindKundeHistorik(HttpSession session){
    ArrayList<Kunde> kundeListe = (ArrayList<Kunde>)session.getAttribute("kundeListe");
    if(kundeListe==null) kundeListe = new ArrayList<>();
    return "findkundetilaftale";
  }

  @PostMapping("/findkundehistorik")
  public String findKundeHistorik(@RequestParam("fornavn") String fornavn, HttpSession session){
    ArrayList<Kunde> kundeListe = bilabonnementRepository.findKunderMedFornavn(fornavn);
    session.setAttribute("kundeListe",kundeListe);
    return "redirect:/findkundehistorik";
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
    model.addAttribute("datoNu",bilabonnementServices.getDato());
    model.addAttribute("datoFemMåneder",bilabonnementServices.getDatoOmFemMåneder());
    return "opretaftale";
  }

  @PostMapping("/opretaftale")
  public String opretAftale( @RequestParam("kundeID") int kundeID, @RequestParam("vognnummer") int vognnummer,
                            @RequestParam("aftaletype") String aftaletype,
                            @RequestParam("startdato") String startdato, @RequestParam("slutdato") String slutdato){
    bilabonnementRepository.opretLejeaftaleDB(kundeID, vognnummer, aftaletype, startdato, slutdato);
    bilabonnementRepository.setBilUdlejetDB(vognnummer);
    return "forside";
  }

  @GetMapping("/updateaftale/{id}")
  public String showUpdateAftale(@RequestParam("id") int kontraktID, Model model){
    Lejeaftale lejeaftale = bilabonnementRepository.getLejeaftaleViaKontraktID(kontraktID);
    model.addAttribute("lejeaftale", lejeaftale);
    return "updateaftale";
  }

  @PostMapping("/updateaftale")
  public String updateAftale(@ModelAttribute Lejeaftale lejeaftale){
    bilabonnementRepository.updateLejeaftale(lejeaftale);
  return "redirect:/"; //Tilbage placeholder
  }

  @GetMapping("/sletaftale{id}")
  public String sletLejeaftale(@PathVariable("id") int kontraktID){
    int vognnummer = bilabonnementRepository.getLejeaftaleViaKontraktID(kontraktID).getVognnummer();
    bilabonnementRepository.sletLejeaftaleOgRelateret(kontraktID);
    bilabonnementRepository.setBilLedigDB(vognnummer);
    return "redirect:/"; //Tilbage placeholder
  }

  @GetMapping("/opretskadesrapport/{id}")
  public String showOpretSkadesrapport(@PathVariable("id") int vognnummer, Model model){
    int kontraktID = bilabonnementRepository.findKontraktIDMedVognnummer(vognnummer);
    model.addAttribute("vognnummer", vognnummer);
    model.addAttribute("kontraktID",kontraktID);
    return "opretskadesrapport";
  }

  @PostMapping("/opretskadesrapport")
  public String opretSkadesrapport(@RequestParam("kontraktID") int kontraktID, @RequestParam("vognnummer") int vognnummer, @RequestParam("kilometer") int overkørteKilometer,
                                   @RequestParam("service") boolean manglendeService, @RequestParam("rengoering") boolean manglendeRengøring,
                                   @RequestParam("daekskifte") boolean manglendeDækskifte, @RequestParam("lakfelt") int lakfeltSkade,
                                   @RequestParam("alufaelg") int alufælgSkade, @RequestParam("stenslag") int stenslagSkade){
    bilabonnementRepository.opretSkadesrapportDB(kontraktID, overkørteKilometer, manglendeService, manglendeRengøring, manglendeDækskifte, lakfeltSkade, alufælgSkade, stenslagSkade);
    bilabonnementRepository.setBilTjekketDB(vognnummer);
    bilabonnementRepository.setAftaleBetaling(kontraktID);
    return "/";
  }

  @GetMapping("/hvisbillager")
  public String hvisBilLager(Model model){
    String status = "ledig";
    ArrayList<Lejebil> bilListe  = bilabonnementRepository.getBilListeViaStatus(status);
    ArrayList<String> lavBestand = bilabonnementRepository.findLavFabrikantBestand();
    int antalBiler = bilListe.size();
    model.addAttribute("bilListe",bilListe);
    model.addAttribute("antalBiler",antalBiler);
    model.addAttribute("lavbestand",lavBestand);

    return "lager";
  }

  @GetMapping("/hvisudlejedebiler")
  public String hvisUdlejedeBiler(Model model){
  String status = "udlejet";
  ArrayList<Lejebil> bilListe  = bilabonnementRepository.getBilListeViaStatus(status);
  int antalBiler = bilListe.size();
  double samletIndtægt = bilabonnementServices.udregnAbonnementIndtægt(bilListe);
  model.addAttribute("bilListe",bilListe);
  model.addAttribute("antalBiler",antalBiler);
  model.addAttribute("samletIndtægt",samletIndtægt);

    return "udlejedebiler";
  }

  @GetMapping("/meldafleveret/{id}/{kundeID}")
  public String meldBilAfleveret(@PathVariable("id") int vognnummer,@PathVariable("kundeID") int kundeID){
    bilabonnementRepository.setBilAfleveret(vognnummer);
    bilabonnementRepository.setAftaleVenter(vognnummer);
    return "redirect:/viskundehistorik/{kundeID}"; //Tilbage placeholder
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

  @GetMapping("/visskadesrapport/{id}")
  public String visSkaderapport(@PathVariable("id") int kontraktID, Model model){
    Skadesrapport skadesrapport = bilabonnementRepository.getSkadesrapportViaKontraktID(kontraktID);
    model.addAttribute("skadesrapport",skadesrapport);
    int rapportID = skadesrapport.getRapportID();
    Skadesafgifter skadesafgifter = bilabonnementRepository.getSkadesafgifterViaRapportID(rapportID);
    model.addAttribute("skadesafgifter",skadesafgifter);

    return "visskadesrapport";
  }

  @GetMapping("/viskundehistorik/{id}")
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
    model.addAttribute("lejeaftalerIgangværende", lejeaftalerIgangværende);

    String afsluttetStatus = "afsluttet";
    ArrayList<Lejeaftale> lejeaftalerAfsluttet =
        bilabonnementRepository.getLejeaftalerViaKundeIDOgStatus(kundeID, afsluttetStatus);
    model.addAttribute("lejeaftalerAfsluttet",lejeaftalerAfsluttet);

    return "viskundehistorik";
  }
}
