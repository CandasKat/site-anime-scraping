package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThemeScrap {
    public static void scrapAllPages(WebClient client) throws IOException {
        String urlThemees = "https://anime.icotaku.com/themes.html#";
        FileWriter file = new FileWriter("themes.csv", true);
        file.write("theme_id;theme\n");
        int id = 1;

        HtmlPage pageActuel = client.getPage(urlThemees);

        List<HtmlElement> genres1 = pageActuel.getByXPath("//*[@id=\"listecontenu\"]/div/div/a");



        for (HtmlElement item : genres1) {

            String titreItem = item.getVisibleText();
            System.out.println("theme " + titreItem);
            file.write(id + ";" + titreItem + "\n");
            id++;
        }

        file.close();
    }
}
