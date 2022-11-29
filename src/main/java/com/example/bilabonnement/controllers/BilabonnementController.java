package com.example.bilabonnement.controllers;


import com.example.bilabonnement.model.Kunde;
import com.example.bilabonnement.model.Lejebil;
import com.example.bilabonnement.repositories.BilabonnementRepository;
import com.example.bilabonnement.services.BilabonnementServices;
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
  BilabonnementServices bilabonnementServices;

  public BilabonnementController(BilabonnementRepository r, BilabonnementServices s){
    bilabonnementRepository = r;
    bilabonnementServices = s;
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
    model.addAttribute("datoNu",bilabonnementServices.getDato());
    model.addAttribute("datoFemMåneder",bilabonnementServices.getDatoOmFemMåneder());
    return "opretaftale";
  }

  @PostMapping("/opretaftale")
  public String opretAftale( @RequestParam("kundeID") int kundeID, @RequestParam("vognnummer") int vognnummer,
                            @RequestParam("aftaletype") String aftaletype, @RequestParam("kilometerpakke") int kilometerpakke,
                            @RequestParam("startdato") String startdato, @RequestParam("slutdato") String slutdato){
    bilabonnementRepository.opretLejeaftaleDB(kundeID, vognnummer, aftaletype, kilometerpakke, startdato, slutdato);
    return "forside";
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
    bilabonnementRepository.meldBilTjekketDB(vognnummer);
    bilabonnementRepository.setAftaleBetaling(kontraktID);
    return "/";
  }

  @GetMapping("/hvisbillager")
  public String hvisBilLager(Model model){
    String status = "ledig";
    ArrayList<Lejebil> bilListe  = bilabonnementRepository.getBilListeViaStatus(status);
    int antalBiler = bilListe.size();
    model.addAttribute("bilListe",bilListe);
    model.addAttribute("antalBiler",antalBiler);

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

  @GetMapping("/meldafleveret/{id}")
  public String meldBilAfleveret(@PathVariable("id") int vognnummer){
    bilabonnementRepository.meldBilAfleveretDB(vognnummer);
    // <td><a th:href="@{'/meldafleveret/'+${bil.vognnummer}}">Meld bil afleveret</a></td> //Indsæt i hvislejeaftaler
    return "redirect:/hvislejeaftaler";
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
}
