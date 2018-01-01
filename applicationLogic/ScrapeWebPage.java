/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeWebPage {

    public String scrapedText = " ";
    public String link;
    public String category;
    public String subject;  //the subject about whom we are searching the web

    public ScrapeWebPage(String link, String category, String subject) {
        this.link = link;
        this.category = category;
        this.subject = subject;
    }

    public String scrapeLinkAndGetText() throws Exception {
        String url = link;
        Document doc = Jsoup.connect(url).followRedirects(true).get();
        for (Element refresh : doc.select("html head meta[http-equiv=refresh]")) {
            Matcher m = Pattern.compile("(?si)\\d+;\\s*url=(.+)|\\d+")
                    .matcher(refresh.attr("content"));
            if (m.matches()) {
                if (m.group(1) != null) {
                    String u = m.group(1).toString();
                    System.out.println(m.group(1).toString());
                    u = u.substring(1, u.length() - 1);
                    System.out.println("Meta tag redirected url: " + u);
                    doc = Jsoup.connect(u).get();
                }
                break;
            }
        }

        System.out.println("Checking URL: " + doc.location());
        String subject = this.subject;

        try {
            System.out.println("$$$$$$$$$$      Printing data from body tag     $$$$$$$$$$$$$$");
            Element body = doc.body();
            doc.setBaseUri(url);
            Elements ptags = body.getElementsByTag("p");

            for (String s : ptags.eachText()) {
                if (s.contains(subject) || s.contains(" He ") || s.contains(" She ") || s.contains(" his ") || s.contains(" her ") || s.contains(" him ") || s.contains(" I ")) {

                    StringBuilder sb = new StringBuilder(s);
                    int i = 0;
                    while ((i = sb.indexOf(" ", i + 120)) != -1) {
                        sb.replace(i, i + 1, "\n");
                    }
                    s = sb.toString() + "\n";

                    scrapedText = scrapedText + "* " + s + "\n";
                }
                //System.out.println("#   "+s);
            }
            scrapedText = scrapedText + "#[source]: " + link + "\n";
            scrapedText = scrapedText + "#[category]: " + category + "\n";
            return scrapedText;
        } catch (Exception e) {
            System.out.println(e);
            return scrapedText;
        }
    }

}
