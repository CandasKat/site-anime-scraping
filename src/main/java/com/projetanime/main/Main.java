package com.projetanime.main;

import com.gargoylesoftware.htmlunit.WebClient;
import com.projetanime.database.ImportAnimes;
import com.projetanime.database.ImportDramas;
import com.projetanime.database.ImportMangas;
import com.projetanime.database.ImportNovels;
import com.projetanime.scraping.*;

import java.io.File;
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


        if(! new File("editor.csv").exists()){
            EditorScrap.scrapAllPages(client);
        }
        if(! new File("editorNovels.csv").exists()){
            EditorNovels.scrapAllPages(client);
        }
        if(! new File("genres.csv").exists()){
            GenresScrap.scrapAllPages(client);
        }
        if(! new File("magazines.csv").exists()){
            MagazineScrap.scrapAllPages(client);
        }
        if(! new File("studio.csv").exists()){
            StudioScrap.scrapAllPages(client);
        }
        if(! new File("themes.csv").exists()){
            ThemeScrap.scrapAllPages(client);
        }
        if(! new File("studiosDramas.csv").exists()){
            StudioDramas.scrapAllPages(client);
        }

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
            scanner.nextLine();
            if (choix == 9) {
                System.exit(0);
            } else if (choix == 1) {

                AnimeScraping.anime_parser(client,searchUrlForAnimes,allAnimes);
                AnimeScraping.scrapAllPages(client,allAnimes);
            }
            else if (choix == 2) {
                MangaScraping.mangas_parser(client,searchUrlForMangas,allMangas);
                MangaScraping.scrapAllPages(client,allMangas);
            }
            else if (choix == 3) {
                NovelScraping.novels_parser(client,searchUrlForNovels,allNovels);
                NovelScraping.scrapAllPages(client,allNovels);
            }
            else if (choix == 4) {
                DramaScraping.drama_parser(client,searchUrlForDramas,allDramas);
                DramaScraping.scrapAllPages(client,allDramas);
            }
            else if (choix == 5) {
                //ImportAnimes.createTables();
                ImportAnimes.importerGenre();
                ImportAnimes.importerTheme();
                ImportAnimes.importerAnime();
                ImportAnimes.importerCommune();
            }
            else if (choix == 6) {
                ImportMangas importMangas = new ImportMangas();
                importMangas.createTables();
                //ImportMangas.importerMagazine();
               // ImportMangas.importerTheme();
                //ImportMangas.importerGenre();
                //importMangas.importerManga();
                //importMangas.importerCommune();
            }
            else if (choix == 7) {
                ImportNovels.createTables();
                //ImportNovels.importerEditor();
                //ImportNovels.importerGenre();
                //ImportNovels.importerTheme();
                ImportNovels.importerNovels();
            }
            else if (choix == 8) {
                ImportDramas importDramas = new ImportDramas();
                importDramas.createTables();
                //ImportDramas.importerStudio();
                importDramas.importerDrama();
                importDramas.importerCommune();
            }


        }

    }
}
