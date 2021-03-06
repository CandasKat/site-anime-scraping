package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.projetanime.readFile.ReadFiles;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class NovelScraping {
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
        FileWriter file = new FileWriter("novels.csv", true);
        file.write("novel_id;;nom_novel;;pays;;nb_tomes;;annee_publication;;editeur_id\n");
        File file1 = new File("novelsThemesGenres.csv");
        FileWriter genreTheme = new FileWriter(file1);
        genreTheme.write("novel_id;;theme;;genre\n");
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


            for (HtmlElement item : titre){
                String titreItem = item.getVisibleText();
                System.out.println("nom_novel " + titreItem);
                stringBuilder.append(id).append(";;").append(titreItem).append(";;");

            }

            for (HtmlElement item : informations){
                HashMap<String,String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++){
                    if (listItems[i].startsWith("Pays")){
                        ArrayUtils.remove(listItems, i);
                        continue;
                    }
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }


                List<HtmlImage> pays = pageActuel.getByXPath("//*[@id=\"page\"]/div[4]/div[2]/div/div[3]/img");
                for (HtmlImage image: pays) {
                    stringList.put("Pays", "https://novel.icotaku.com" + image.getSrcAttribute()) ;
                }



                if (!stringList.containsKey("Pays") || stringList.get("Pays").equals("?")){
                    stringBuilder.append("NULL;;");

                }
                else {
                    stringBuilder.append(stringList.get("Pays") + ";;");

                }
                if (!stringList.containsKey("Nombre de tomes") || stringList.get("Nombre de tomes").equals("?")){
                    stringBuilder.append("NULL;;");

                }
                else {
                    stringBuilder.append(stringList.get("Nombre de tomes") + ";;");

                }
                if (!stringList.containsKey("Ann??e de publication") || stringList.get("Ann??e de publication").equals("?")){
                    stringBuilder.append("NULL;;");

                }
                else {
                    stringBuilder.append(stringList.get("Ann??e de publication") + ";;");

                }



                ReadFiles readEditeur = new ReadFiles();
                Map<String, String> editeurs = readEditeur.byBufferedReader("editorNovels.csv");

                if (!stringList.containsKey("Editeur original") || stringList.get("Editeur original").equals("?")){
                    stringBuilder.append("NULL\n");
                    break;
                }
                else {
                    String[] listValue = stringList.get("Editeur original").split(", ");
                    List<String> listediteur = new LinkedList<>(Arrays.asList(listValue));
                    for (int i = 0; i < listediteur.size(); i++) {
                        if (editeurs.containsValue(listediteur.get(i))) {
                            String tempEditeur = String.valueOf(readEditeur.getKeys(editeurs, listediteur.get(i)));
                            tempEditeur = tempEditeur.replaceAll("\\[", "");
                            tempEditeur = tempEditeur.replaceAll("\\]", "");
                            stringBuilder.append(tempEditeur + "\n");
                        }
                        if (i != 0 && listediteur.get(i).equals(listediteur.get(i - 1))) {
                            listediteur.remove(i);
                            continue;
                        }

                    }
                }
            }


            ReadFiles readEditeur = new ReadFiles();
            Map<String, String> themes = readEditeur.byBufferedReader("themes.csv");
            Map<String, String> genres = readEditeur.byBufferedReader("genres.csv");
            List<String> themeslistes = new ArrayList<>();
            List<String> genreslistes = new ArrayList<>();
            for (HtmlElement item : informations) {
                HashMap<String, String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++) {
                    if (listItems[i].startsWith("Pays")) {
                        ArrayUtils.remove(listItems, i);
                        continue;
                    }
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }


                if (!stringList.containsKey("Th??mes") || stringList.get("Th??mes").equals("?")) {
                    themeslistes.add("NULL");
                    break;
                } else {
                    String[] listValue = stringList.get("Th??mes").split(", ");
                    List<String> listtheme = new LinkedList<>(Arrays.asList(listValue));
                    for (int i = 0; i < listtheme.size(); i++) {
                        if (i != 0 && listtheme.get(i).equals(listtheme.get(i - 1))) {
                            listtheme.remove(i);
                            continue;
                        }
                        if (themes.containsValue(listtheme.get(i))) {
                            String tempTheme = String.valueOf(readEditeur.getKeys(themes, listtheme.get(i)));
                            tempTheme = tempTheme.replaceAll("\\[", "");
                            tempTheme = tempTheme.replaceAll("\\]", "");
                            if (listtheme.size() <= 1){

                                themeslistes.add(tempTheme);
                                break;
                            }
                            else {
                                themeslistes.add(tempTheme);

                            }
                        }
                    }
                }

                if (!stringList.containsKey("Genres") || stringList.get("Genres").equals("?")) {
                    genreslistes.add("NULL");
                    break;
                } else {
                    String[] listValue = stringList.get("Genres").split(", ");
                    List<String> listgenre = new LinkedList<>(Arrays.asList(listValue));
                    for (int i = 0; i < listgenre.size(); i++) {
                        if (i != 0 && listgenre.get(i).equals(listgenre.get(i - 1))) {
                            listgenre.remove(i);
                            continue;
                        }
                        if (genres.containsValue(listgenre.get(i))) {
                            String tempGenre = String.valueOf(readEditeur.getKeys(genres, listgenre.get(i)));
                            tempGenre = tempGenre.replaceAll("\\[", "");
                            tempGenre = tempGenre.replaceAll("\\]", "");
                            if (listgenre.size() <= 1){
                                genreslistes.add(tempGenre);
                                break;
                            }
                            else {
                                genreslistes.add(tempGenre);
                            }
                        }
                    }
                }
            }
            StringBuilder themesGenres = new StringBuilder();
            StringBuilder finaux = new StringBuilder();
            for (int i = 0 ; i < themeslistes.size(); i++){
                for (int j = 0; j < genreslistes.size(); j++){
                    themesGenres.append(themeslistes.get(i) + ";;" + genreslistes.get(j));
                    finaux.append(id + ";;");
                    finaux.append(themesGenres  + "\n");
                    genreTheme.write(String.valueOf(finaux));
                    themesGenres.delete(0,themesGenres.length());
                    finaux.delete(0, finaux.length());
                }

            }
            id++;
            file.write(String.valueOf(stringBuilder));
        }
        genreTheme.close();
        file.close();

    }
}
