package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditorScrap {
    public static void main(String[] args) throws IOException {
        List<String> allGenres = new ArrayList<>();
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        String urlThemees = "https://anime.icotaku.com/editeurs.html#";


        scrapAllPages(client, urlThemees);
    }


    public static void scrapAllPages(WebClient client, String urlGenres) throws IOException {
        FileWriter file = new FileWriter("editor.csv", true);
        file.write("editor_id;editor\n");
        int id = 1;

        HtmlPage pageActuel = client.getPage(urlGenres);

        List<HtmlElement> genres1 = pageActuel.getByXPath("//*[@id=\"listecontenu\"]/div/div/a");



        for (HtmlElement item : genres1) {

            String titreItem = item.getVisibleText();
            System.out.println("editor " + titreItem);
            file.write(id + ";" + titreItem + "\n");
            id++;
        }

        file.close();
    }
}
