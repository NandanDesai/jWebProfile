/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

/**
 *
 * @author nandan
 */
import java.io.BufferedReader;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.JSONObject;

public class WikipediaInfo {

    private String summaryHTML;
    public static String summaryText = "no content found :("; //default string

    public String getWikiSummary(String subject) throws Exception {
        //String subject = "Shah Rukh Khan";
        URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exintro=&titles=" + subject.replace(" ", "%20"));
        String text = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
            String line = null;
            while (null != (line = br.readLine())) {
                line = line.trim();
                if (true) {
                    text += line;
                }
            }
        }

//System.out.println("text = " + text);
        JSONObject json = new JSONObject(text);
        JSONObject query = json.getJSONObject("query");
        JSONObject pages = query.getJSONObject("pages");
        for (String key : pages.keySet()) {
            //System.out.println("key = " + key);
            JSONObject page = pages.getJSONObject(key);
            summaryHTML = page.getString("extract");

        }
        return "<html>" + summaryHTML + "</html>";

    }
}
