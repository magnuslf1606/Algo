import java.util.*;

public class OrdBehandler {
    protected URLReader urlReader;
    String startKombinasjon = "til+deg"; // Velg en startkombinasjon.
    String ut;
    OrdBehandler(URLReader urlReader) {
        this.urlReader = urlReader;
        List<String> ord = new ArrayList<>(Arrays.asList(urlReader.getUt().split(" ")));

        ord.removeIf(word -> word.equals("") || word.equals(","));

        HashMap<String, Integer> kombinasjoner = new HashMap<>();

        // Gå gjennom ordene og generer og tell ordkombinasjoner
        for (int i = 0; i < ord.size() - 2; i++) {
            String kombinasjon = ord.get(i) + "+" + ord.get(i+1) + "+" + ord.get(i+2);

            // Ignorer spesialtegn og konverter til små bokstaver (avhengig av kravene)
            kombinasjon = kombinasjon.replaceAll("[.,;?!«»]", "").toLowerCase();
            //kombinasjon = kombinasjon.toLowerCase(); //Bare lowercase

            // Legg til eller oppdater telleren for ordkombinasjonen
            kombinasjoner.put(kombinasjon, kombinasjoner.getOrDefault(kombinasjon, 0) + 1);
        }
        // Skriv ut resultatet
        for (String kombinasjon : kombinasjoner.keySet()) {
            int antall = kombinasjoner.get(kombinasjon);
            //System.out.println(kombinasjon + ": " + antall);
        }
        System.out.println("UT: " +genererTekst(kombinasjoner));
    }
    String genererTekst(HashMap<String, Integer> kombinasjoner) {
        StringBuilder generertTekst = new StringBuilder();
        generertTekst.append(startKombinasjon.split("\\+")[0] + " " + startKombinasjon.split("\\+")[1] + " ");
        for (int antallOrd = 0; antallOrd < kombinasjoner.size(); antallOrd++) {
            ArrayList<String> muligeNesteOrd = new ArrayList<>();
            for (String kombinasjon : kombinasjoner.keySet()) {
                String[] kombiData = kombinasjon.split("\\+");
                String[] startData = startKombinasjon.split("\\+");
                if (kombiData[0].equals(startData[0]) && kombiData[1].equals(startData[1])) {
                    String[] deler = kombinasjon.split("\\+");
                    muligeNesteOrd.add(deler[2]);
                }
            }
           // System.out.println("ARR: " + muligeNesteOrd);
            if (!muligeNesteOrd.isEmpty()) {
                // Velg et ord basert på sannsynlighet.
                String nesteOrd = velgOrdBasertPåSannsynlighet(muligeNesteOrd, kombinasjoner);
                generertTekst.append(nesteOrd).append(" ");
                //System.out.println(startKombinasjon.split("\\+")[0] + "+" + startKombinasjon.split("\\+")[1] + "+" + nesteOrd);
                kombinasjoner.remove(startKombinasjon.split("\\+")[0] + "+" + startKombinasjon.split("\\+")[1] + "+" + nesteOrd);
                startKombinasjon = startKombinasjon.split("\\+")[1] + "+" + nesteOrd;
                //System.out.println(kombinasjoner.size());

            } else {
                // Ingen flere mulige ord, avslutt generering.
                break;
            }
        }
        return generertTekst.toString();
    }
    public static String velgOrdBasertPåSannsynlighet(ArrayList<String> muligeNesteOrd, HashMap<String, Integer> kombinasjoner) {
        Random random = new Random();
        // Summer opp sannsynligheter for hvert mulig neste ord.
        int totalSannsynlighet = 0;
        for (String ord : muligeNesteOrd) {
            if (kombinasjoner.containsKey(ord)) {
                totalSannsynlighet += kombinasjoner.get(ord);
            }
        }
        if (totalSannsynlighet == 0) {
            // Ingen gyldige sannsynligheter, returner et vilkårlig ord.
            return muligeNesteOrd.get(random.nextInt(muligeNesteOrd.size()));
        }
        // Generer et tilfeldig tall mellom 0 og totalSannsynlighet.
        int tilfeldigTall = random.nextInt(totalSannsynlighet);

        // Finn det ordet som samsvarer med det tilfeldige tallet.
        int akkumulertSannsynlighet = 0;
        for (String ord : muligeNesteOrd) {
            if (kombinasjoner.containsKey(ord)) {
                akkumulertSannsynlighet += kombinasjoner.get(ord);
                if (akkumulertSannsynlighet > tilfeldigTall) {
                    return ord; // Dette ordet er valgt.
                }
            }
        }
        // Dette bør aldri nås, men om det gjør det, returnerer vi et vilkårlig ord.
        return muligeNesteOrd.get(random.nextInt(muligeNesteOrd.size()));
    }
}