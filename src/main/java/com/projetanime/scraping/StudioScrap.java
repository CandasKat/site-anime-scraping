package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudioScrap {
    public static void scrapAllPages(WebClient client) throws IOException {
        String urlstudios = "https://anime.icotaku.com/studios.html#";
        FileWriter file = new FileWriter("studio.csv", true);
        file.write("studio_id;studio\n");
        int id = 1;

        HtmlPage pageActuel = client.getPage(urlstudios);

        List<HtmlElement> genres1 = pageActuel.getByXPath("//*[@id=\"listecontenu\"]/div/div/a");



        for (HtmlElement item : genres1) {

            String titreItem = item.getVisibleText();
            System.out.println("studio " + titreItem);
            file.write(id + ";" + titreItem + "\n");
            id++;
        }

        file.close();
    }
}
