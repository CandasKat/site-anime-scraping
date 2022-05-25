package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scraping {
    public static void main(String[] args) throws IOException {
        List<String> allAnimes = new ArrayList<>();
        List<String> allMangas = new ArrayList<>();
        List<String> allNovels = new ArrayList<>();
        List<String> allDramas = new ArrayList<>();

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        String searchUrlForAnimes = "https://anime.icotaku.com/animes.html?filter=all";
        //String searchUrlForMangas = "https://manga.icotaku.com/mangas.html?filter=all";
        //String searchUrlForNovels = "https://novel.icotaku.com/novels.html?filter=all";
        //String searchUrlForDramas = "https://drama.icotaku.com/dramas.html?filter=all";

        anime_parser(client, searchUrlForAnimes, allAnimes);
        scrapAllPages(client,allAnimes);
        //mangas_parser(client, searchUrlForMangas, allMangas);
        //novels_parser(client, searchUrlForNovels, allNovels);
        //dramas_parser(client, searchUrlForDramas, allDramas);
    }

    private static void dramas_parser(WebClient client, String searchUrl, List<String> allDramas) throws IOException {
        int numeroLastPage = 0;
        HtmlPage page = client.getPage(searchUrl);

        List<HtmlElement> dernierPage = page.getByXPath("//*[@id=\"page\"]/div[4]/div/a[8]");
        for (HtmlElement dernier : dernierPage){
            HtmlAnchor dernierAnchor = (HtmlAnchor) dernier;
            String[] numero = String.valueOf(dernierAnchor.getHrefAttribute()).split("page=");
            numero = numero[1].split("\"");
            numeroLastPage = Integer.parseInt(numero[0]);
        }

        for (int i = 1;i <= numeroLastPage;i++){
            String searchAllUrls = searchUrl + "&page=" + i;
            HtmlPage allPages = client.getPage(searchAllUrls);

            List<HtmlElement> items = allPages.getByXPath("//*[@id=\"page\"]/table/tbody/tr/td/div/a[1]");
            for (HtmlElement item : items){
                HtmlAnchor itemAnchor = (HtmlAnchor) item;
                String itemUrl = "https://drama.icotaku.com/" + itemAnchor.getHrefAttribute();
                allDramas.add(itemUrl);
            }

        }
    }


    public static void anime_parser(WebClient client, String searchUrl, List<String> allAnimes) throws IOException {
        int numeroLastPage = 0;
        HtmlPage page = client.getPage(searchUrl);

        List<HtmlElement> dernierPage = page.getByXPath("//*[@id=\"page\"]/div[4]/div/a[8]");
        for (HtmlElement dernier : dernierPage){
            HtmlAnchor dernierAnchor = (HtmlAnchor) dernier;
            String[] numero = String.valueOf(dernierAnchor.getHrefAttribute()).split("page=");
            numero = numero[1].split("\"");
            numeroLastPage = Integer.parseInt(numero[0]);
        }

        for (int i = 1;i <= numeroLastPage;i++){
            String searchAllUrls = searchUrl + "&page=" + i;
            HtmlPage allPages = client.getPage(searchAllUrls);

            List<HtmlElement> items = allPages.getByXPath("//*[@id=\"page\"]/table/tbody/tr/td/div/a[1]");
            for (HtmlElement item : items){
                HtmlAnchor itemAnchor = (HtmlAnchor) item;
                String itemUrl = "https://anime.icotaku.com/" + itemAnchor.getHrefAttribute();
                allAnimes.add(itemUrl);
            }

        }
    }

    public static void mangas_parser(WebClient client, String searchUrl, List<String> allMangas) throws IOException {
        int numeroLastPage = 0;
        HtmlPage page = client.getPage(searchUrl);

        List<HtmlElement> dernierPage = page.getByXPath("//*[@id=\"page\"]/div[4]/div/a[8]");
        for (HtmlElement dernier : dernierPage){
            HtmlAnchor dernierAnchor = (HtmlAnchor) dernier;
            String[] numero = String.valueOf(dernierAnchor.getHrefAttribute()).split("page=");
            numero = numero[1].split("\"");
            numeroLastPage = Integer.parseInt(numero[0]);
        }

        for (int i = 1;i <= numeroLastPage;i++){
            String searchAllUrls = searchUrl + "&page=" + i;
            HtmlPage allPages = client.getPage(searchAllUrls);

            List<HtmlElement> items = allPages.getByXPath("//*[@id=\"page\"]/table/tbody/tr/td/div/a[1]");
            for (HtmlElement item : items){
                HtmlAnchor itemAnchor = (HtmlAnchor) item;
                String itemUrl = "https://manga.icotaku.com/" + itemAnchor.getHrefAttribute();
                allMangas.add(itemUrl);
            }
        }
    }

    public static void novels_parser(WebClient client, String searchUrl, List<String> allNovels) throws IOException {
        int numeroLastPage = 0;
        HtmlPage page = client.getPage(searchUrl);

        List<HtmlElement> dernierPage = page.getByXPath("//*[@id=\"page\"]/div[4]/div/a[8]");
        for (HtmlElement dernier : dernierPage){
            HtmlAnchor dernierAnchor = (HtmlAnchor) dernier;
            String[] numero = String.valueOf(dernierAnchor.getHrefAttribute()).split("page=");
            numero = numero[1].split("\"");
            numeroLastPage = Integer.parseInt(numero[0]);
        }

        for (int i = 1;i <= numeroLastPage;i++){
            String searchAllUrls = searchUrl + "&page=" + i;
            HtmlPage allPages = client.getPage(searchAllUrls);

            List<HtmlElement> items = allPages.getByXPath("//*[@id=\"page\"]/table/tbody/tr/td/div/a[1]");
            for (HtmlElement item : items){
                HtmlAnchor itemAnchor = (HtmlAnchor) item;
                String itemUrl = "https://novel.icotaku.com/" + itemAnchor.getHrefAttribute();
                allNovels.add(itemUrl);
            }
        }
    }

    public static void scrapAllPages(WebClient client, List<String> listLinks) throws IOException {
        FileWriter file = new FileWriter("animes.csv", true);
        file.write("titre;categorie;genre;theme\n");
        for (String link: listLinks){
            HtmlPage pageActuel = client.getPage(link);

            List<HtmlElement> titre = pageActuel.getByXPath("//*[@id=\"fiche_entete\"]/div/h1");
            List<HtmlElement> informations =  pageActuel.getByXPath("//*[@id=\"page\"]/div[4]/div[2]/div");


            for (HtmlElement item : titre){
                String titreItem = item.getVisibleText();
                System.out.println("Titre " + titreItem);
                file.write(titreItem + ";");
            }

            for (HtmlElement item : informations){

                String titreItem = item.getVisibleText();
                String[] listItem = titreItem.split("\n");

                for (int i = 0; i < listItem.length; i++) {
                    if (listItem[i].startsWith("Catégorie")){
                        String[] itemforFile = listItem[i].split(": ");
                        System.out.println(itemforFile[0] + " " + itemforFile[1]);
                        if (itemforFile[1].equals("?")){
                            file.write("null;");
                            break;
                        }
                        file.write(itemforFile[1] + ";");
                        break;
                    } else if (!listItem[i].startsWith("Catégorie")) {
                        continue;
                    } else {
                        file.write("null;");
                    }
                }


            }

            for (HtmlElement item : informations){
                String titreItem = item.getVisibleText();
                String[] listItem = titreItem.split("\n");

                for (int i = 0; i < listItem.length; i++) {
                    if (listItem[i].startsWith("Genre(s)")){
                        String[] itemforFile = listItem[i].split(": ");
                        System.out.println(itemforFile[0] + " " + itemforFile[1]);
                        if (itemforFile[1].equals("?")){
                            file.write("null;");
                            break;
                        }
                        file.write(itemforFile[1] + ";");
                        break;
                    }else if (!listItem[i].startsWith("Catégorie")) {
                        continue;
                    } else {
                        file.write("null;");
                    }
                }

            }

            for (HtmlElement item : informations){
                String titreItem = item.getVisibleText();
                String[] listItem = titreItem.split("\n");

                for (int i = 0; i < listItem.length; i++) {
                    if (listItem[i].startsWith("Thème(s)")){
                        String[] itemforFile = listItem[i].split(": ");
                        System.out.println(itemforFile[0] + " " + itemforFile[1]);
                        if (itemforFile[1].equals("?")){
                            file.write("null\n");
                            break;
                        }
                        file.write(itemforFile[1] + "\n");
                        break;
                    }
                    else if (!listItem[i].startsWith("Catégorie")) {
                        continue;

                    }
                    else {
                        file.write("null\n");
                    }
                }

            }
        }
        file.close();
    }
}
