package scraping;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraping {
    public static void main(String[] args) throws IOException {
        anime_parser();
        //mangas_parser();
        //novels_parser();
        //dramas_parser();

    }

    public static void anime_parser() throws IOException {
        String linkSite = "https://anime.icotaku.com/animes.html?filter=all";
        Document html = Jsoup.connect(linkSite).get();

        Elements pages = html.getElementsByClass("anime_pager").select("a");
        String lastPage = String.valueOf(pages.last());
        String[] numeroLastPage = lastPage.split("page=");
        numeroLastPage = numeroLastPage[1].split("\"");
        int numero = Integer.parseInt(numeroLastPage[0]);

        List<String> allPages = new ArrayList<>();
        for (int i = 1; i <= numero; i++){
            allPages.add(linkSite + "&page=" + i);
        }

        for (String page: allPages){
            Document htmlForPage = Jsoup.connect(page).get();
            Elements div = htmlForPage.getElementsByClass("td_apercufiche");
            Elements link = div.select("a");
            List<String> linkHref = link.eachAttr("href");

            System.out.println(linkHref);

            for (String element : linkHref) {
                System.out.println("https://anime.icotaku.com/" + element);
            }

        }
    }

    public static void mangas_parser() throws IOException {
        String linkSite = "https://manga.icotaku.com/mangas.html?filter=all";
        Document html = Jsoup.connect(linkSite).get();

        Elements pages = html.getElementsByClass("manga_pager").select("a");
        String lastPage = String.valueOf(pages.last());
        String[] numeroLastPage = lastPage.split("page=");
        numeroLastPage = numeroLastPage[1].split("\"");
        int numero = Integer.parseInt(numeroLastPage[0]);

        List<String> allPages = new ArrayList<>();
        for (int i = 1; i <= numero; i++){
            allPages.add(linkSite + "&page=" + i);
        }

        for (String page: allPages){
            Document htmlForPage = Jsoup.connect(page).get();
            Elements div = htmlForPage.getElementsByClass("td_apercufiche");
            Elements link = div.select("a");
            List<String> linkHref = link.eachAttr("href");

            for (String element : linkHref) {
                System.out.println("https://manga.icotaku.com/" + element);
            }

        }
    }

    public static void novels_parser() throws IOException {
        String linkSite = "https://novel.icotaku.com/novels.html?filter=all";
        Document html = Jsoup.connect(linkSite).get();

        Elements pages = html.getElementsByClass("novel_pager").select("a");
        String lastPage = String.valueOf(pages.last());
        String[] numeroLastPage = lastPage.split("page=");
        numeroLastPage = numeroLastPage[1].split("\"");
        int numero = Integer.parseInt(numeroLastPage[0]);

        List<String> allPages = new ArrayList<>();
        for (int i = 1; i <= numero; i++){
            allPages.add(linkSite + "&page=" + i);
        }

        for (String page: allPages){
            Document htmlForPage = Jsoup.connect(page).get();
            Elements div = htmlForPage.getElementsByClass("td_apercufiche");
            Elements link = div.select("a");
            List<String> linkHref = link.eachAttr("href");

            for (String element : linkHref) {
                System.out.println("https://novel.icotaku.com/" + element);
            }

        }
    }

    public static void dramas_parser() throws IOException {
        String linkSite = "https://drama.icotaku.com/dramas.html?filter=all";
        Document html = Jsoup.connect(linkSite).get();

        Elements pages = html.getElementsByClass("drama_pager").select("a");
        String lastPage = String.valueOf(pages.last());
        String[] numeroLastPage = lastPage.split("page=");
        numeroLastPage = numeroLastPage[1].split("\"");
        int numero = Integer.parseInt(numeroLastPage[0]);

        List<String> allPages = new ArrayList<>();
        for (int i = 1; i <= numero; i++){
            allPages.add(linkSite + "&page=" + i);
        }

        for (String page: allPages){
            Document htmlForPage = Jsoup.connect(page).get();
            Elements div = htmlForPage.getElementsByClass("td_apercufiche");
            Elements link = div.select("a");
            List<String> linkHref = link.eachAttr("href");

            for (String element : linkHref) {
                System.out.println("https://drama.icotaku.com/" + element);
            }

        }
    }
}
