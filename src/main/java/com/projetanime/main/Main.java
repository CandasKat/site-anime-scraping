package com.projetanime.main;

import com.gargoylesoftware.htmlunit.WebClient;
import com.projetanime.database.ImportAnimes;
import com.projetanime.database.ImportDramas;
import com.projetanime.database.ImportMangas;
import com.projetanime.database.ImportNovels;
import com.projetanime.scraping.AnimeScraping;
import com.projetanime.scraping.DramaScraping;
import com.projetanime.scraping.MangaScraping;
import com.projetanime.scraping.NovelScraping;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        List<String> allAnimes = new ArrayList<>();
        List<String> allMangas = new ArrayList<>();
        List<String> allNovels = new ArrayList<>();
        List<String> allDramas = new ArrayList<>();


        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        String searchUrlForAnimes = "https://anime.icotaku.com/animes.html?filter=all";
        String searchUrlForMangas = "https://manga.icotaku.com/mangas.html?filter=all";
        String searchUrlForNovels = "https://novel.icotaku.com/novels.html?filter=all";
        String searchUrlForDramas = "https://drama.icotaku.com/dramas.html?filter=all";




        boolean isFalse = true;
        while (isFalse){
            System.out.println("================================================================================");
            System.out.println("Voulez vous faire quoi?\n" +
                    "1 - Scrap Anime\n" +
                    "2 - Scrap Manga\n" +
                    "3 - Scrap Novels\n" +
                    "4 - Scrap Dramas\n" +
                    "5 - Importer les animes à base de donnée\n" +
                    "6 - Importer les mangas à base de donnée\n" +
                    "7 - Importer les novels à base de donnée\n" +
                    "8 - Importer les dramas à base de donnée\n" +
                    "9 - Exit");
            System.out.println("================================================================================");
            Scanner scanner = new Scanner(System.in);
            int choix = scanner.nextInt();
            if (choix > 8 || choix < 1 || !Character.isDigit(choix)){
                System.out.println("Veuillez entrer un choix valide.");
                continue;
            } else if (choix == 9) {
                System.exit(0);
            } else if (choix == 1) {
                AnimeScraping.anime_parser(client,searchUrlForAnimes,allAnimes);
                AnimeScraping.scrapAllPages(client,allAnimes);
            }
            else if (choix == 2) {
                MangaScraping.mangas_parser(client,searchUrlForAnimes,allMangas);
                MangaScraping.scrapAllPages(client,allAnimes);
            }
            else if (choix == 3) {
                NovelScraping.novels_parser(client,searchUrlForAnimes,allNovels);
                NovelScraping.scrapAllPages(client,allAnimes);
            }
            else if (choix == 4) {

            }
            else if (choix == 5) {
                ImportAnimes.importerGenre();
                ImportAnimes.importerTheme();
                ImportAnimes.importerAnime();
                ImportAnimes.importerCommune();
            }
            else if (choix == 6) {

            }
            else if (choix == 7) {

            }
            else if (choix == 8) {

            }


        }

    }
}
