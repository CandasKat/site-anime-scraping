package com.projetanime.scraping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.projetanime.readFile.ReadFiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AnimeScraping {
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



    public static void scrapAllPages(WebClient client, List<String> listLinks) throws IOException {
        FileWriter file = new FileWriter("animes.csv", true);
        File file1 = new File("animesThemesGenres.csv");
        FileWriter genreTheme = new FileWriter(file1);

        file.write("anime_id;;nom_anime;;nom_anime_alternatif;;nb_episode;;duree_episode;;annee_diffusion;;studio_id\n");
        genreTheme.write("anime_id;;theme;;genre\n");
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
                System.out.println("nom_anime " + titreItem);
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

                }
                else {
                    stringBuilder.append(stringList.get("Titre alternatif") + ";;");
                    stringList.clear();

                }
                if ((!stringList.containsKey("Nombre d'OAV") && !stringList.containsKey("Nombre d'épisode") && !stringList.containsKey("Nombre de film"))){
                    stringBuilder.append("NULL;;");
                    stringList.clear();

                }
                else if (stringList.containsKey("Nombre d'OAV")){
                    if (stringList.get("Nombre d'OAV").equals("?")){
                        stringBuilder.append("NULL;;");
                        stringList.clear();

                    }
                    else {
                        stringBuilder.append(stringList.get("Nombre d'OAV") + ";;");
                        stringList.clear();

                    }
                }
                else if (stringList.containsKey("Nombre d'épisode")) {
                    if (stringList.get("Nombre d'épisode").equals("?")){
                        stringBuilder.append("NULL;;");
                        stringList.clear();

                    }
                    else{
                        stringBuilder.append(stringList.get("Nombre d'épisode") + ";;");
                        stringList.clear();

                    }
                }
                else if (stringList.containsKey("Nombre de film")) {
                    if (stringList.get("Nombre de film").equals("?")) {
                        stringBuilder.append("NULL;;");
                        stringList.clear();

                    } else {
                        stringBuilder.append(stringList.get("Nombre de film") + ";;");
                        stringList.clear();

                    }

                    if ((!stringList.containsKey("Durée d'un OAV") && !stringList.containsKey("Durée d'un épisode") && !stringList.containsKey("Durée d'un film"))) {
                        stringBuilder.append("NULL;;");
                        stringList.clear();

                    } else if (stringList.containsKey("Durée d'un OAV")) {
                        if (stringList.get("Durée d'un OAV").equals("?") || stringList.get("Durée d'un OAV").startsWith("?")) {
                            stringBuilder.append("NULL;;");
                            stringList.clear();

                        } else {
                            if (stringList.get("Durée d'un OAV").endsWith("mins")) {
                                String[] mins = stringList.get("Durée d'un OAV").split(" ");
                                stringBuilder.append(mins[0] + ";;");
                            } else {
                                stringBuilder.append(stringList.get("Durée d'un OAV") + ";;");
                                stringList.clear();

                            }
                        }
                    } else if (stringList.containsKey("Durée d'un épisode")) {
                        if (stringList.get("Durée d'un épisode").equals("?") || stringList.get("Durée d'un épisode").startsWith("?")) {
                            stringBuilder.append("NULL;;");
                            stringList.clear();

                        } else {
                            if (stringList.get("Durée d'un épisode").endsWith("mins")) {
                                String[] mins = stringList.get("Durée d'un épisode").split(" ");
                                stringBuilder.append(mins[0] + ";;");
                            } else {
                                stringBuilder.append(stringList.get("Durée d'un épisode") + ";;");
                                stringList.clear();

                            }
                        }
                    } else if (stringList.containsKey("Durée d'un film")) {
                        if (stringList.get("Durée d'un film").equals("?") || stringList.get("Durée d'un film").startsWith("?")) {
                            stringBuilder.append("NULL;;");
                            stringList.clear();

                        } else {
                            if (stringList.get("Durée d'un film").endsWith("mins")) {
                                String[] mins = stringList.get("Durée d'un film").split(" ");
                                stringBuilder.append(mins[0] + ";;");
                            } else {
                                stringBuilder.append(stringList.get("Durée d'un film") + ";;");
                                stringList.clear();

                            }
                        }
                        if (!stringList.containsKey("Année de diffusion") || stringList.get("Année de diffusion").equals("?")) {
                            stringBuilder.append("NULL;;");
                            stringList.clear();
                            break;
                        } else {
                            stringBuilder.append(stringList.get("Année de diffusion") + ";;");
                            stringList.clear();
                            break;
                        }
                    }
                }

            }





            ReadFiles readstudios = new ReadFiles();
            Map<String, String> studios = readstudios.byBufferedReader("studio.csv");

            for (HtmlElement item : informations){
                HashMap<String,String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++){
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }

                if (!stringList.containsKey("Studio(s) d'animation") || stringList.get("Studio(s) d'animation").equals("?")){
                    stringBuilder.append("NULL\n");
                    stringList.clear();
                    break;
                }
                else {
                    String[] listValue = stringList.get("Studio(s) d'animation").split(", ");
                    List<String> liststudio = new LinkedList<>(Arrays.asList(listValue));
                    for (int i = 0; i < liststudio.size(); i++) {
                        if (i != 0 && liststudio.get(i).equals(liststudio.get(i - 1))) {
                            liststudio.remove(i);
                            continue;
                        }
                        if (studios.containsValue(liststudio.get(i))) {
                            String tempstudio = String.valueOf(readstudios.getKeys(studios, liststudio.get(i)));
                            tempstudio = tempstudio.replaceAll("\\[", "");
                            tempstudio = tempstudio.replaceAll("\\]", "");
                            stringBuilder.append(tempstudio + "\n");

                            break;

                        }
                    }
                }
            }



            ReadFiles readThemes = new ReadFiles();
            Map<String, String> themes = readThemes.byBufferedReader("themes.csv");
            Map<String, String> genres = readThemes.byBufferedReader("genres.csv");

            List<String> themeslistes = new ArrayList<>();
            List<String> genreslistes = new ArrayList<>();
            for (HtmlElement item : informations){
                HashMap<String,String> stringList = new HashMap<>();
                String titreItem = item.getVisibleText();
                String[] listItems = titreItem.split("\n");
                for (int i = 0; i < listItems.length; i++){
                    String[] itemforMap = listItems[i].split(" : ");
                    stringList.put(itemforMap[0], itemforMap[1]);
                }

                if (!stringList.containsKey("Thème(s)") || stringList.get("Thème(s)").equals("?")){
                    themeslistes.add("NULL");
                    stringList.clear();
                    break;
                }
                else {
                    String[] listValue = stringList.get("Thème(s)").split(", ");
                    List<String> listTheme = new LinkedList<>(Arrays.asList(listValue));
                    for (int i = 0; i < listTheme.size(); i++){
                        if (i != 0 && listTheme.get(i).equals(listTheme.get(i-1))){
                            listTheme.remove(i);
                            continue;
                        }
                        if (themes.containsValue(listTheme.get(i))){
                            String temptheme = String.valueOf(readThemes.getKeys(themes,listTheme.get(i)));
                            temptheme = temptheme.replaceAll("\\[", "");
                            temptheme = temptheme.replaceAll("\\]", "");
                            if (listTheme.size() <= 1){

                                themeslistes.add(temptheme);
                                break;
                            }
                            else {
                                themeslistes.add(temptheme);

                            }
                        }
                    }
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

                if (!stringList.containsKey("Genre(s)") || stringList.get("Genre(s)").equals("?")){
                    genreslistes.add("NULL");
                    stringList.clear();
                    break;
                }
                else {
                    String[] listValue = stringList.get("Genre(s)").split(", ");
                    List<String> listgenres = new LinkedList<>(Arrays.asList(listValue));

                    for (int i = 0; i < listgenres.size(); i++){
                        if (i != 0 && listgenres.get(i).equals(listgenres.get(i-1))){
                            listgenres.remove(i);
                            continue;
                        }
                        if (genres.containsValue(listgenres.get(i))){
                            String tempgenre = String.valueOf(readThemes.getKeys(genres,listgenres.get(i)));
                            tempgenre = tempgenre.replaceAll("\\[", "");
                            tempgenre = tempgenre.replaceAll("\\]", "");
                            if (listgenres.size() <= 1){
                                genreslistes.add(tempgenre);
                                break;
                            }
                            else {
                                genreslistes.add(tempgenre);
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
