package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EditorNovels {
    public static void scrapAllPages(WebClient client) throws IOException {
        String urlThemes = "https://novel.icotaku.com/editeurs.html#";
        FileWriter file = new FileWriter("editorNovels.csv", true);
        file.write("editor_id;editor\n");
        int id = 1;

        HtmlPage pageActuel = client.getPage(urlThemes);

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
