package com.projetanime.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class ImportAnimes {
    private static Connection con = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;

    public ImportAnimes(){
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        try {
            con = DriverManager.getConnection(url, Database.user, Database.password);
            System.out.println("Connexion réussie");
            createTables();
        } catch (Exception ex){
            System.out.println("La connexion a échoué");
        }
    }

    public static void createTables(){
        String queryForAnime = "CREATE TABLE IF NOT EXISTS animes (" +
                "anime_id INT AUTO_INCREMENT PRIMARY KEY," +
                "nom_anime VARCHAR(250)," +
                "nom_anime_alternatif VARCHAR(250)," +
                "nb_episode INT," +
                "duree_episode INT," +
                "annee_diffusion DATE," +
                "studio_id INT," +
                "FOREIGN KEY (studio_id) REFERENCES studios(studio_id))";

        String queryForStudios = "CREATE TABLE IF NOT EXISTS studios (" +
                "studio_id INT AUTO_INCREMENT PRIMARY KEY," +
                "studio VARCHAR(150))";

        String queryForTablesCommunes = "CREATE TABLE IF NOT EXISTS animes_themes_genres (" +
                "anime_id INT," +
                "theme INT," +
                "genre INT," +
                "FOREIGN KEY (anime_id) REFERENCES animes(anime_id)," +
                "FOREIGN KEY (theme) REFERENCES themes(theme_id)," +
                "FOREIGN KEY (genre) REFERENCES genres(genre_id))";

        String queryForEditor = "CREATE TABLE IF NOT EXISTS editors (" +
                "editor_id INT AUTO_INCREMENT PRIMARY KEY," +
                "editor VARCHAR(150))";

        String queryForGenres = "CREATE TABLE IF NOT EXISTS genres (" +
                "genre_id INT AUTO_INCREMENT PRIMARY KEY," +
                "genre VARCHAR(150))";

        String queryForThemes = "CREATE TABLE IF NOT EXISTS themes (" +
                "theme_id INT AUTO_INCREMENT PRIMARY KEY," +
                "theme VARCHAR(150))";
        try {
            statement = con.createStatement();
            statement.execute(queryForThemes);
            statement.execute(queryForGenres);
            statement.execute(queryForStudios);
            statement.execute(queryForEditor);
            statement.execute(queryForAnime);
            statement.execute(queryForTablesCommunes);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void importerTheme() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvTheme = "themes.csv";
        int batchSize = 20;
        try {
            String sql = "INSERT INTO themes(theme_id, theme) VALUES(?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvTheme));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";");
                String theme_id = data[0];
                String theme = data[1];
                preparedStatement.setString(1,theme_id);
                preparedStatement.setString(2, theme);

                preparedStatement.addBatch();
                if (count % batchSize == 0){
                    preparedStatement.executeBatch();
                }

            }
            lineReader.close();

            preparedStatement.executeBatch();
            con.commit();
            con.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void importerGenre() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvGenre = "genres.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO genres(genre_id, genre) VALUES(?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvGenre));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";");
                String genre_id = data[0];
                String genre = data[1];
                preparedStatement.setString(1,genre_id);
                preparedStatement.setString(2, genre);

                preparedStatement.addBatch();
                if (count % batchSize == 0){
                    preparedStatement.executeBatch();
                }

            }
            lineReader.close();

            preparedStatement.executeBatch();
            con.commit();
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void importerStudio() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvGenre = "studio.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO studios(studio_id, studio) VALUES(?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvGenre));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";");
                String studio_id = data[0];
                String studio = data[1];
                preparedStatement.setString(1,studio_id);
                preparedStatement.setString(2, studio);

                preparedStatement.addBatch();
                if (count % batchSize == 0){
                    preparedStatement.executeBatch();
                }

            }
            lineReader.close();

            preparedStatement.executeBatch();
            con.commit();
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void importerAnime() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvAnime = "animes.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO animes(anime_id, nom_anime, nom_anime_alternatif, nb_episode, duree_episode, annee_diffusion, studio_id) VALUES(?,?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvAnime));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";;");
                String anime_id = data[0];
                String nom_anime = data[1];
                String nom_anime_alternatif = data[2];
                String nb_episode = data[3];
                String duree_episode = data[4];
                String annee_diffusion = data[5];
                String studio_animation = data[6];



                if (duree_episode.equals("NULL")){
                    preparedStatement.setNull(1,Types.NULL);
                }else {
                    preparedStatement.setInt(1, Integer.parseInt(anime_id));
                }

                preparedStatement.setString(2,nom_anime);
                if (nom_anime_alternatif.equals("NULL")){
                    preparedStatement.setNull(3,Types.NULL);
                }else {
                    preparedStatement.setString(3, nom_anime_alternatif);
                }
                if (nb_episode.equals("NULL")){
                    preparedStatement.setNull(4,Types.NULL);
                }else {
                    preparedStatement.setInt(4, Integer.parseInt(nb_episode));
                }
                if (duree_episode.equals("NULL")){
                    preparedStatement.setNull(5,Types.NULL);
                }else {
                    preparedStatement.setInt(5, Integer.parseInt(duree_episode));
                }


                if (annee_diffusion.equals("NULL")){
                    preparedStatement.setNull(6,Types.NULL);
                }else {
                    Date date = new Date(Long.parseLong(annee_diffusion));
                    preparedStatement.setDate(6, date);                }


                if (studio_animation.equals("NULL")){
                    preparedStatement.setNull(7,Types.NULL);
                }else {
                    preparedStatement.setInt(7, Integer.parseInt(studio_animation));
                }



                preparedStatement.addBatch();
                if (count % batchSize == 0){
                    preparedStatement.executeBatch();
                }

            }
            lineReader.close();

            preparedStatement.executeBatch();
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void importerCommune() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvGenre = "animesThemesGenres.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO animes_themes_genres (anime_id, theme, genre) VALUES(?,?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvGenre));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";;");
                String anime_id = data[0];
                String theme = data[1];
                String genre = data[2];

                if (anime_id.equals("NULL")){
                    preparedStatement.setNull(1, Types.NULL);
                }
                else {
                    preparedStatement.setInt(1, Integer.parseInt(anime_id));
                }
                if (theme.equals("NULL")){
                    preparedStatement.setNull(2, Types.NULL);
                }
                else {
                    preparedStatement.setInt(2, Integer.parseInt(theme));
                }
                if (genre.equals("NULL")){
                    preparedStatement.setNull(3, Types.NULL);
                }
                else {
                    preparedStatement.setInt(3, Integer.parseInt(genre));
                }


                preparedStatement.addBatch();
                if (count % batchSize == 0){
                    preparedStatement.executeBatch();
                }

            }
            lineReader.close();

            preparedStatement.executeBatch();
            con.commit();
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    static ResultSet selectAll() {
        try {
            statement = con.createStatement();
            String interrogation = "Select * From animes";
            ResultSet rs;

            rs = statement.executeQuery(interrogation);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
