package com.projetanime.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectionDB {
    public static void main(String[] args) throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        connectionDB.importerAnime();

    }
    private static Connection con = null;

    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;

    public ConnectionDB(){
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

    public void createTables(){
        String queryForAnime = "CREATE TABLE IF NOT EXISTS animes (" +
                "id_anime INT AUTO_INCREMENT PRIMARY KEY," +
                "nom_anime VARCHAR(150)," +
                "nom_anime_alternatif VARCHAR(150)," +
                "nb_episode INT," +
                "duree_episode INT," +
                "annee_diffusion DATE," +
                "theme_id INT," +
                "genre_id INT," +
                "FOREIGN KEY (theme_id) REFERENCES themes(theme_id)," +
                "FOREIGN KEY (genre_id) REFERENCES genres(genre_id))";

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
            statement.execute(queryForEditor);
            statement.execute(queryForAnime);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void importerTheme() throws SQLException {
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

    public void importerGenre() throws SQLException {
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

    public void importerAnime() throws SQLException {
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
            String sql = "INSERT INTO animes(nom_anime, nom_anime_alternatif, nb_episode, duree_episode, annee_diffusion, theme_id, genre_id) VALUES(?,?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvAnime));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";;");
                String nom_anime = data[0];
                String nom_anime_alternatif = data[1];
                String nb_episode = data[2];
                String duree_episode = data[3];
                String annee_diffusion = data[4];
                String theme_id = data[5];
                String genre_id = data[6];




                preparedStatement.setString(1,nom_anime);
                if (nom_anime_alternatif.equals("NULL")){
                    preparedStatement.setNull(2,Types.NULL);
                }else {
                    preparedStatement.setString(2, nom_anime_alternatif);
                }
                if (nb_episode.equals("NULL")){
                    preparedStatement.setNull(3,Types.NULL);
                }else {
                    preparedStatement.setInt(3, Integer.parseInt(nb_episode));
                }
                if (duree_episode.equals("NULL")){
                    preparedStatement.setNull(4,Types.NULL);
                }else {
                    preparedStatement.setInt(4, Integer.parseInt(duree_episode));
                }
                if (annee_diffusion.equals("NULL")){
                    preparedStatement.setNull(5,Types.NULL);
                }else {
                    preparedStatement.setObject(5,annee_diffusion);                }


                if (theme_id.equals("NULL")){
                    preparedStatement.setNull(6,Types.NULL);
                }else {
                    preparedStatement.setInt(6, Integer.parseInt(theme_id));
                }


                if (genre_id.equals("NULL")){
                    preparedStatement.setNull(7, Types.NULL);
                } else {
                    preparedStatement.setInt(7, Integer.parseInt(genre_id));
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
