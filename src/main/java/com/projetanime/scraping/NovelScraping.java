package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.projetanime.readFile.ReadFiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class NovelScraping {
    public static void main(String[] args) throws IOException {
        List<String> allNovels = new ArrayList<>();
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        String searchUrlForNovels = "https://novel.icotaku.com/novels.html?filter=all";

        novels_parser(client, searchUrlForNovels, allNovels);
        scrapAllPages(client,allNovels);
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
        file.write("novel_id;;nom_novel;;nom_novel_alternatif;;pays;;nb_tomes;;annee_publication;;editeur_id\n");
        StringBuilder stringBuilder = new StringBuilder();

        int id = 1;
        for (String link: listLinks){
            stringBuilder.delete(0,stringBuilder.length());
            HtmlPage pageActuel = client.getPage(link);
            List<HtmlElement> titre = pageActuel.getByXPath("//*[@id=\"fiche_entete\"]/div/h1");
            List<HtmlElement> informations;

            if (!pageActuel.getByXPath("//*[@id=\"divFicheHentai\"]").isEmpty() && !pageActuel.getByXPath("//*[@id=\"divFicheError\"]").isEmpty()){
                informations = pageActuel.getByXPath("//*[@id=\"page\"]/div[6]/div[2]/div/div");
            }
            else if (!pageActuel.getByXPath("//*[@id=\"divFicheHentai\"]").isEmpty()){
                informations = pageActuel.getByXPath("//*[@id=\"page\"]/div[5]/div[2]/div/div");
            } else if (!pageActuel.getByXPath("//*[@id=\"divFicheError\"]").isEmpty()) {
                informations = pageActuel.getByXPath("//*[@id=\"page\"]/div[5]/div[2]/div/div");
            } else {
                informations = pageActuel.getByXPath("//*[@id=\"page\"]/div[4]/div[2]/div");
            }

            stringBuilder.append(id + ";;");
            for (HtmlElement item : titre){
                String titreItem = item.getVisibleText();
                System.out.println("nom_novel " + titreItem);
                stringBuilder.append(titreItem+";;");

            }

            for (HtmlElement item : informations){
                HashMap<String,String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++){
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }

                if (!stringList.containsKey("Titre alternatif") || stringList.get("Titre alternatif").equals("?")){
                    stringBuilder.append("NULL;;");
                    stringList.clear();
                    break;
                }
                else {
                    stringBuilder.append(stringList.get("Titre alternatif") + ";;");
                    stringList.clear();
                    break;
                }

            }

            for (HtmlElement item : informations){
                HashMap<String,String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++){
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }

                if (!stringList.containsKey("Pays") || stringList.get("Pays").equals("?")){
                    stringBuilder.append("NULL;;");
                    stringList.clear();
                    break;
                }
                else {
                    stringBuilder.append(stringList.get("Pays") + ";;");
                    stringList.clear();
                    break;
                }
            }

            for (HtmlElement item : informations){
                HashMap<String,String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++){
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }

                if (!stringList.containsKey("Nombre de tomes") || stringList.get("Nombre de tomes").equals("?")){
                    stringBuilder.append("NULL;;");
                    stringList.clear();
                    break;
                }
                else {
                    stringBuilder.append(stringList.get("Nombre de tomes") + ";;");
                    stringList.clear();
                    break;
                }
            }


            for (HtmlElement item : informations){
                HashMap<String,String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++){
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }

                if (!stringList.containsKey("Année de publication") || stringList.get("Année de publication").equals("?")){
                    stringBuilder.append("NULL;;");
                    stringList.clear();
                    break;
                }
                else {
                    stringBuilder.append(stringList.get("Année de publication") + ";;");
                    stringList.clear();
                    break;
                }
            }

            ReadFiles readEditeur = new ReadFiles();
            Map<String, String> editeurs = readEditeur.byBufferedReader("studio.csv");

            List<String> editeurslistes = new ArrayList<>();
            for (HtmlElement item : informations){
                HashMap<String,String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++){
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }

                if (!stringList.containsKey("Editeur original") || stringList.get("Editeur original").equals("?")){
                    stringBuilder.append("NULL\n");
                    stringList.clear();
                    break;
                }
                else {
                    String[] listValue = stringList.get("Editeur original").split(", ");
                    List<String> listediteur = new LinkedList<>(Arrays.asList(listValue));
                    for (int i = 0; i < listediteur.size(); i++) {
                        if (i != 0 && listediteur.get(i).equals(listediteur.get(i - 1))) {
                            listediteur.remove(i);
                            continue;
                        }
                        if (editeurs.containsValue(listediteur.get(i))) {
                            String tempEditeur = String.valueOf(readEditeur.getKeys(editeurs, listediteur.get(i)));
                            tempEditeur = tempEditeur.replaceAll("\\[", "");
                            tempEditeur = tempEditeur.replaceAll("\\]", "");
                            stringBuilder.append(tempEditeur + "\n");

                            break;

                        }
                    }
                }
            }

            id++;
            file.write(String.valueOf(stringBuilder));
        }
        file.close();

    }
}
