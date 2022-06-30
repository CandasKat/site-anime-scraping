package com.projetanime.database;

import java.io.*;
import java.sql.*;
import java.sql.Date;

public class ImportDramas {
    private static Connection con = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;

    public ImportDramas(){
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
        String queryForAnime = "CREATE TABLE IF NOT EXISTS dramas (" +
                "drama_id INT AUTO_INCREMENT PRIMARY KEY," +
                "nom_drama VARCHAR(250)," +
                "pays VARCHAR(250)," +
                "nb_film INT," +
                "duree_film INT," +
                "annee_diffusion DATE," +
                "studio_id INT," +
                "FOREIGN KEY (studio_id) REFERENCES studiosdramas(studio_id))";

        String queryForStudios = "CREATE TABLE IF NOT EXISTS studiosdramas (" +
                "studio_id INT AUTO_INCREMENT PRIMARY KEY," +
                "studio VARCHAR(150))";

        String queryForTablesCommunes = "CREATE TABLE IF NOT EXISTS dramas_themes_genres (" +
                "drama_id INT," +
                "theme INT," +
                "genre INT," +
                "FOREIGN KEY (drama_id) REFERENCES dramas(drama_id)," +
                "FOREIGN KEY (theme) REFERENCES themes(theme_id)," +
                "FOREIGN KEY (genre) REFERENCES genres(genre_id))";

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

    public static void importerDrama() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvAnime = "dramas.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO dramas(drama_id, nom_drama, pays, nb_film, duree_film, annee_diffusion, studio_id) VALUES(?,?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvAnime));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";;");
                String drama_id = data[0];
                String nom_drama = data[1];
                String pays = data[2];
                String nb_film = data[3];
                String duree_film = data[4];
                String annee_diffusion = data[5];
                String studio_animation = data[6];



                if (drama_id.equals("NULL")){
                    preparedStatement.setNull(1,Types.NULL);
                }else {
                    preparedStatement.setInt(1, Integer.parseInt(drama_id));
                }

                preparedStatement.setString(2,nom_drama);
                if (pays.equals("NULL")){
                    preparedStatement.setNull(3,Types.NULL);
                }else {
                    preparedStatement.setString(3, pays);
                }
                if (nb_film.equals("NULL")){
                    preparedStatement.setNull(4,Types.NULL);
                }else {
                    preparedStatement.setInt(4, Integer.parseInt(nb_film));
                }
                if (duree_film.equals("NULL")){
                    preparedStatement.setNull(5,Types.NULL);
                }else {
                    preparedStatement.setString(5, duree_film);
                }


                if (annee_diffusion.equals("NULL")){
                    preparedStatement.setNull(6,Types.NULL);
                }else {
                    java.sql.Date date = new Date(Long.parseLong(annee_diffusion));
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
        String csvGenre = "dramasThemesGenres.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO dramas_themes_genres (drama_id, theme, genre) VALUES(?,?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvGenre));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";;");
                String drama_id = data[0];
                String theme = data[1];
                String genre = data[2];

                if (drama_id.equals("NULL")){
                    preparedStatement.setNull(1, Types.NULL);
                }
                else {
                    preparedStatement.setInt(1, Integer.parseInt(drama_id));
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
            String interrogation = "Select * From dramas";
            ResultSet rs;

            rs = statement.executeQuery(interrogation);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}


