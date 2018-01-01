/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author nandan
 */
public class GoogleSearchResults {

    public String title;
    public String snippet;
    public String link;

    public GoogleSearchResults() {
    }

    public GoogleSearchResults(String title, String snippet, String link) {
        this.title = title;
        this.snippet = snippet;
        this.link = link;
    }

    public ArrayList<GoogleSearchResults> getGoogleSearchResults(String searchName) throws Exception {
        searchName = searchName.replace(" ", "%20");
        ArrayList<GoogleSearchResults> results = new ArrayList<GoogleSearchResults>();
        System.out.println("Searching Google::::::::::::::::::::::::::::(((((((()))))))");
        URL url = new URL(
                "https://www.googleapis.com/customsearch/v1?key=" + "ENTER YOUR KEY HERE" + "&cx=013036536707430787589:_pqjad5hr1a&q=" + searchName + "&alt=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        StringBuffer json = new StringBuffer();
        //System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            json.append(output);
            //System.out.println(output);
        }
        //System.out.println("Keys:");
        JSONObject obj = new JSONObject(json.toString());

        JSONArray arr = obj.getJSONArray("items");
        //System.out.println("Name is:");
        for (int i = 0; i < arr.length(); i++) {

            try {
                String link = arr.getJSONObject(i).getString("displayLink");
                String titles = arr.getJSONObject(i).getString("title");
                if (link.contains("linkedin.com")) {
                    continue;
                    //do nothing
                } else {
                    String snippet = arr.getJSONObject(i).getString("snippet");
                    String urlLink = arr.getJSONObject(i).getString("link");
                    results.add(new GoogleSearchResults(titles, snippet, urlLink));
                }

                //System.out.println("Data from LinkedIn");
            } catch (Exception e) {
                System.out.println(e);
            }

        }

        conn.disconnect();
        return results;
    }

    public ArrayList<LinkedInData> getLinkedInData(String searchName) throws Exception {
        searchName = searchName.replace(" ", "%20");
        ArrayList<LinkedInData> results = new ArrayList<LinkedInData>();
        System.out.println("Searching Google for linkedin::::::::::::::::::::::::::::(((((((()))))))");
        URL url = new URL(
                "https://www.googleapis.com/customsearch/v1?key=" + "ENTER YOUR KEY HERE" + "&cx=013036536707430787589:_pqjad5hr1a&q=" + searchName + "&alt=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        StringBuffer json = new StringBuffer();
        //System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            json.append(output);
            //System.out.println(output);
        }
        //System.out.println("Keys:");
        JSONObject obj = new JSONObject(json.toString());

        JSONArray arr = obj.getJSONArray("items");
        //System.out.println("Name is:");
        for (int i = 0; i < arr.length(); i++) {

            try {
                String link = arr.getJSONObject(i).getString("displayLink");
                String titles = arr.getJSONObject(i).getString("title");
                System.out.println("****Here****");
                System.out.println(link);
                if (link.contains("linkedin.com")) {
                    System.out.println("Data from LinkedIn:");
                    JSONObject jobj = arr.getJSONObject(i);
                    JSONObject mj = jobj.getJSONObject("pagemap");

                    JSONArray jarr = mj.getJSONArray("hcard");
                    String name = jarr.getJSONObject(0).getString("fn");
                    String title = jarr.getJSONObject(0).getString("title");
                    System.out.println("Name: " + name);
                    System.out.println("Description: " + title);

                    jarr = mj.getJSONArray("person");
                    String loc = jarr.getJSONObject(0).getString("location");
                    String org = jarr.getJSONObject(0).getString("org");
                    String role = jarr.getJSONObject(0).getString("role");
                    System.out.println("Location: " + loc);
                    System.out.println("Organization: " + org);
                    System.out.println("Role: " + role);
                    results.add(new LinkedInData(name, title, loc, org, role));
                } else {

                    //do nothing
                }

                //System.out.println("Data from LinkedIn");
            } catch (Exception e) {
                System.out.println(e);
            }

        }

        conn.disconnect();
        return results;
    }

}
