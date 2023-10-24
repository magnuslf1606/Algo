import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class OrdBehandler {
    protected URLReader urlReader;
    protected String startKombinasjon = "alladin+skal"; // Velg en startkombinasjon.
    OrdBehandler(URLReader urlReader) throws Exception {
        this.urlReader = urlReader;
        List<String> ord = new ArrayList<>(Arrays.asList(urlReader.getUt().split(" ")));
        ord.removeIf(word -> word.equals("") || word.equals(","));
        HashMap<String, Integer> kombinasjoner = new HashMap<>();
        // Gå gjennom ordene og generer og tell ordkombinasjoner
        for (int i = 0; i < ord.size() - 2; i++) {
            String kombinasjon = ord.get(i) + "+" + ord.get(i+1) + "+" + ord.get(i+2);
            // Ignorer spesialtegn og konverter til små bokstaver (avhengig av kravene)
            kombinasjon = kombinasjon.replaceAll("[.,;?!«»]","").toLowerCase();
            //kombinasjon = kombinasjon.toLowerCase(); //Bare lowercase

            // Legg til eller oppdater telleren for ordkombinasjonen
            kombinasjoner.put(kombinasjon, kombinasjoner.getOrDefault(kombinasjon, 0) + 1);
        }
        printTilTxt(genererTekst(kombinasjoner));
    }
    String genererTekst(HashMap<String, Integer> kombinasjoner) {
        Random random = new Random();
        StringBuilder generertTekst = new StringBuilder();
        generertTekst.append(startKombinasjon.split("\\+")[0] + " " + startKombinasjon.split("\\+")[1] + " ");
        while (kombinasjoner.size() != 0) {
            ArrayList<String> muligeNesteOrd = new ArrayList<>();
            HashMap<String, Integer> muligSannsynelighet =new HashMap<>();
            for (String kombinasjon : kombinasjoner.keySet()) {
                String[] kombiData = kombinasjon.split("\\+");
                String[] startData = startKombinasjon.split("\\+");
                if (kombiData[0].equals(startData[0]) && kombiData[1].equals(startData[1])) {
                    String[] deler = kombinasjon.split("\\+");
                    muligeNesteOrd.add(deler[2]);
                    //muligSannsynelighet.put(muligeNesteOrd.toString(), muligSannsynelighet.getOrDefault(muligeNesteOrd.toString(), 0) + 1);
                }
            }
            for (String s : muligeNesteOrd) {
                muligSannsynelighet.put(s, muligSannsynelighet.getOrDefault(s, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : muligSannsynelighet.entrySet()) {
                System.out.println("HER: " + entry.getKey() + ": " + entry.getValue());
            }


            //System.out.println("ARR: " + muligeNesteOrd);
            if (!muligeNesteOrd.isEmpty()) {
                // Velg et ord basert på sannsynlighet.
                String nesteOrd = velgOrdBasertPåSannsynlighet(muligSannsynelighet, random);
                generertTekst.append(nesteOrd).append(" ");

                // Opprett en nøkkel for den neste ordkombinasjonen som inneholder nåværende og neste ord.
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
                for (int i = 0; i < (int)(Math.random()*kombinasjoner.size()-1); i++) {
                    først = kombinasjoner.entrySet().iterator().next();
                }
                String førsteNøkkel = først.getKey();
                String[] nøkkelDeler = førsteNøkkel.split("\\+");
                startKombinasjon = nøkkelDeler[0] + "+" + nøkkelDeler[1];
            }
            System.out.println(kombinasjoner.size());
        }
        return generertTekst.toString();
    }
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
            double sannsynlighetsprosent = 100 / muligSannsynlighet.size();
            System.out.println("Sannsynlighet for " + ord + ": " + sannsynlighetsprosent + "%");
            if (akkumulertSannsynlighet > tilfeldigTall) {
                System.out.println("Valgt ord (" + sannsynlighetsprosent + "%): " + ord);
                return ord;
            }
        }
        return null; // Dette bør aldri nås, men om det gjør det, returnerer vi null.
    }
    void printTilTxt(String s) throws Exception {
        PrintWriter pw = new PrintWriter("src/txt.txt");
        pw.println(s);
        pw.close();
    }
}