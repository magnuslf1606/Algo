import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {
    protected String ut;
    URLReader() {
        try {
            StringBuilder utRaw  = new StringBuilder();
            URL myURL = new URL("https://eventyrforalle.no/tusen-og-en-natt/toen192");
            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()));
            //boolean fortsett = false;
            /*
                TODO:
                    -   Bare ta med selve teksten
                    -   mulig en bool på første og siste linje man ønsker å ha med og sette bool deretter
                    -   Eller en int
             */
            String inputLine;
            while ((inputLine = in.readLine()) != null) {

                utRaw.append(inputLine.toLowerCase());
            }
            in.close();
            ut = utRaw.toString();
            ut = ut.replaceAll("<[^>]*>", "");
        }
        catch (MalformedURLException e) {
            System.out.println("URL feil: " + e);
        }
        catch (IOException e) {
            System.out.println("Tilkobling misslykket: " + e);
        }
    }
    void print() { if (ut != null) System.out.println(ut); }
    public String getUt() { return ut; }
}
