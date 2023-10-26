package com.example.demo.Generator;
import java.io.File;

import java.io.PrintWriter;
import java.util.*;




public class OrdBehandler {
    protected URLReader urlReader;
    public static String text;

    public static String startKombinasjon;

    /**
     * Oppretter en ny OrdBehandler med en spesifisert startkombinasjon.
     *
     * @param urlReader En URLReader-instans som inneholder teksten som skal behandles.
     * @param ord1 Det første ordet i startkombinasjonen.
     * @param ord2 Det andre ordet i startkombinasjonen.
     * @throws Exception Hvis det oppstår unntak under konstruksjonen.
     */

    public OrdBehandler(URLReader urlReader, String ord1, String ord2) throws Exception {
        startKombinasjon = ord1 + "+" + ord2;
        this.urlReader = urlReader;
        List<String> ord = new ArrayList<>(Arrays.asList(urlReader.getUt().split("\\s+|(?<=[.!? ,])|(?=[.!? ,])")));
        ord.removeIf(word -> word.equals("") || word.equals("–"));
        HashMap<String, Integer> kombinasjoner = new HashMap<>();

        // Går gjennom ordene og teller ordkombinasjonene
        for (int i = 0; i < ord.size()-2; i++) {
            String kombinasjon = ord.get(i) + "+" + ord.get(i+1) + "+" + ord.get(i+2);
            // Legg til eller oppdater telleren for ordkombinasjonen
            kombinasjoner.put(kombinasjon, kombinasjoner.getOrDefault(kombinasjon, 0) + 1);
        }
        text = genererTekst(kombinasjoner);
    }

    /**
     * Genererer tekst basert på en gitt kartlegging av kombinasjoner og sannsynligheter.
     *
     * Denne metoden genererer tekst ved å velge ord fra den gitte kartleggingen av kombinasjoner
     * og sannsynlighetene Sannsynlighetsberegningen fungerer ikke helt optimalt, men beregner sannsynlighet
     * på antall kombinasjoner, ikke antall hver kombinasjon forekommer
     * Metoden håndterer spesialtegn som komma, punktum, utropstegn og spørsmålstegn.
     *
     * @param kombinasjoner En kartlegging av kombinasjoner og tilhørende sannsynligheter.
     * @return Den genererte teksten basert på de gitte kombinasjonene.
     */
    String genererTekst(HashMap<String, Integer> kombinasjoner) {
        Random random = new Random();
        boolean spesialtegn = false;
        boolean komma = false;
        StringBuilder generertTekst = new StringBuilder();
        generertTekst.append(startKombinasjon.split("\\+")[0] + " " + startKombinasjon.split("\\+")[1] + " ");
        while (kombinasjoner.size() != 0) {
            ArrayList<String> muligeNesteOrd = new ArrayList<>();
            HashMap<String, Integer> muligSannsynelighet =new HashMap<>();
            for (String kombinasjon : kombinasjoner.keySet()) {
                String[] kombiData = kombinasjon.split("\\+");
                String[] startData = startKombinasjon.split("\\+");
                if (kombiData[0].toLowerCase().equals(startData[0].toLowerCase()) && kombiData[1].toLowerCase().equals(startData[1].toLowerCase())) {
                    muligeNesteOrd.add(kombiData[2]);
                }
            }
            for (String s : muligeNesteOrd) {
                muligSannsynelighet.put(s, muligSannsynelighet.getOrDefault(s, 0) + 1);
            }


            if (!muligeNesteOrd.isEmpty()) {
                // Velger et ord basert på sannsynlighet.
                String nesteOrd = velgOrdBasertPåSannsynlighet(muligSannsynelighet, random);
                // Sjekker etter "spesialtegn", fjerner mellomrom etter ord foran disse og starter neste ord med stor forbokstav
                if (nesteOrd.equals(",")) {
                    if (komma || spesialtegn) {
                        generertTekst.append("");
                        komma = false;
                        spesialtegn = false;
                    } else {
                        generertTekst.deleteCharAt(generertTekst.length()-1);
                        generertTekst.append(nesteOrd + " ");
                        komma = true;
                    }
                }
                else if (nesteOrd.equals(".") || nesteOrd.equals("!") || nesteOrd.equals("?")) {
                    if (komma || spesialtegn) {
                        generertTekst.append("");
                        komma = false;
                        spesialtegn = false;
                    } else {
                        generertTekst.deleteCharAt(generertTekst.length()-1);
                        generertTekst.append(nesteOrd + " ");
                        spesialtegn = true;
                    }
                }
                else if (spesialtegn) {
                    generertTekst.append(nesteOrd.substring(0, 1).toUpperCase() + nesteOrd.substring(1)).append(" ");
                    spesialtegn = false;
                    komma = false;
                }
                else {
                    generertTekst.append(nesteOrd).append(" ");
                    komma = false;
                    spesialtegn = false;
                }
                String kombinasjonKey = startKombinasjon.split("\\+")[0] + "+" + startKombinasjon.split("\\+")[1] + "+" + nesteOrd;
                if (kombinasjoner.containsKey(kombinasjonKey)) {
                    // Hent nåværende verdi (antall forekomster) og reduser den med 1.
                    int nyVerdi = kombinasjoner.get(kombinasjonKey) - 1; // Oppdater verdien som du trenger
                    kombinasjoner.put(kombinasjonKey, nyVerdi);
                    // Hvis verdien er 0 eller mindre etter oppdateringen, fjern nøkkelen fra kartet.
                    if (kombinasjoner.get(kombinasjonKey) <= 0)
                        kombinasjoner.remove(kombinasjonKey); // Fjern nøkkelen hvis verdien er 0 eller mindre etter oppdatering.
                }
                // Oppdater startKombinasjon for å utforske neste mulige ordkombinasjon.
                startKombinasjon = startKombinasjon.split("\\+")[1] + "+" + nesteOrd;
            } else {
                // Ingen flere mulige ord, finner ny random startkombinsjon generering.
                Map.Entry<String, Integer> først = kombinasjoner.entrySet().iterator().next();
                while (true) {
                    for (int i = 0; i < (int) (Math.random() * kombinasjoner.size() - 1); i++) {
                        først = kombinasjoner.entrySet().iterator().next();
                    }
                    String førsteNøkkel = først.getKey();
                    String[] nøkkelDeler = førsteNøkkel.split("\\+");
                    if (!nøkkelDeler[0].equals(".") && !nøkkelDeler[0].equals("!") && !nøkkelDeler[0].equals("?") && !nøkkelDeler[0].equals(",")) {
                        startKombinasjon = nøkkelDeler[0] + "+" + nøkkelDeler[1];
                        break;
                    } else {
                        kombinasjoner.remove(førsteNøkkel);
                    }
                }
            }
        }
        return generertTekst.toString();
    }

    /**
     * Velger et ord basert på sannsynligheten gitt i en kartlegging av mulige ord og deres sannsynligheter.
     *
     * Denne metoden tar en kartlegging av mulige ord og deres sannsynligheter som inndata, og basert på disse
     * sannsynlighetene, velger den et ord. Hvis ingen gyldige sannsynligheter er tilgjengelige, velger metoden
     * et vilkårlig ord fra de tilgjengelige alternativene. Metoden håndterer også tilfeldig valg i tråd med
     * sannsynlighetene.
     *
     * @param muligSannsynlighet En kartlegging av mulige ord og deres tilhørende sannsynligheter.
     * @param random En instans av Random for tilfeldig tallgenerering.
     * @return Ordet valgt basert på sannsynlighetene eller et vilkårlig ord hvis ingen gyldige sannsynligheter finnes.
     */
    public static String velgOrdBasertPåSannsynlighet(HashMap<String, Integer> muligSannsynlighet, Random random) {
        int totalSannsynlighet = 0;

        for (int sannsynlighet : muligSannsynlighet.values()) {
            totalSannsynlighet += sannsynlighet;
        }

        if (totalSannsynlighet == 0) {
            // Ingen gyldige sannsynligheter, returner et vilkårlig ord.
            String[] muligeNesteOrd = muligSannsynlighet.keySet().toArray(new String[0]);
            String valgtOrd = muligeNesteOrd[random.nextInt(muligeNesteOrd.length)];
            System.out.println("Valgt ord (" + (1.0 / muligeNesteOrd.length * 100) + "%): " + valgtOrd);
            return valgtOrd;
        }

        int tilfeldigTall = random.nextInt(totalSannsynlighet);
        int akkumulertSannsynlighet = 0;
        for (String ord : muligSannsynlighet.keySet()) {
            int sannsynlighet = muligSannsynlighet.get(ord);
            akkumulertSannsynlighet += sannsynlighet;
            //double sannsynlighetsprosent = 100 / muligSannsynlighet.size();
            //System.out.println("Sannsynlighet for " + ord + ": " + sannsynlighetsprosent + "%");
            if (akkumulertSannsynlighet > tilfeldigTall) {

                return ord;
            }
        }
        return null; // Dette bør aldri nås, men om det gjør det, returnerer vi null.}
    }

    /**
     * Skriver teksten som er generert til en fil.
     *
     * @throws Exception
     */
    public static void printTilTxt() throws Exception {
        PrintWriter pw = new PrintWriter("src/txt.txt");
        pw.println(text);
        pw.close();
    }
}
