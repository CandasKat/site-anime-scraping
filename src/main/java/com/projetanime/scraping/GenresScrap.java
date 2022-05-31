package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenresScrap {
    public static void main(String[] args) throws IOException {
        List<String> allGenres = new ArrayList<>();
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        String urlGenres = "https://anime.icotaku.com/genres.html#";


        scrapAllPages(client, urlGenres);
    }


    public static void scrapAllPages(WebClient client, String urlGenres) throws IOException {
        FileWriter file = new FileWriter("genres.csv", true);
        file.write("genre_id;genre\n");
        int id = 1;

        HtmlPage pageActuel = client.getPage(urlGenres);

        List<HtmlElement> genres1 = pageActuel.getByXPath("//*[@id=\"listecontenu\"]/div/div/a");
        //List<HtmlElement> genres2 =  pageActuel.getByXPath("//*[@id=\"listecontenu\"]/div[2]/div/a");


        for (HtmlElement item : genres1) {

            String titreItem = item.getVisibleText();
            System.out.println("genre " + titreItem);
            file.write(id + ";" + titreItem + "\n");
            id++;
        }

        file.close();
    }

}
