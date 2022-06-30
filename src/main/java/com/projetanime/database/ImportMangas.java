package com.projetanime.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class ImportMangas {
    private static Connection con = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;

    public ImportMangas(){
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
        String queryForAnime = "CREATE TABLE IF NOT EXISTS mangas (" +
                "manga_id INT AUTO_INCREMENT PRIMARY KEY," +
                "nom_manga VARCHAR(250)," +
                "pays VARCHAR(250)," +
                "nb_tomes INT," +
                "annee_edition DATE," +
                "sens_lecture VARCHAR(250)," +
                "magazine_id INT," +
                "editor_id INT," +
                "FOREIGN KEY (magazine_id) REFERENCES magazines(magazine_id)," +
                "FOREIGN KEY (editor_id) REFERENCES editor(editor_id))";

        String queryForMagazines = "CREATE TABLE IF NOT EXISTS magazines (" +
                "magazine_id INT AUTO_INCREMENT PRIMARY KEY," +
                "magazine VARCHAR(150))";

        String queryForTablesCommunes = "CREATE TABLE IF NOT EXISTS mangas_themes_genres (" +
                "manga_id INT," +
                "theme_id INT," +
                "genre_id INT," +
                "FOREIGN KEY (manga_id) REFERENCES mangas(manga_id)," +
                "FOREIGN KEY (theme_id) REFERENCES themes(theme_id)," +
                "FOREIGN KEY (genre_id) REFERENCES genres(genre_id))";

        String queryForEditor = "CREATE TABLE IF NOT EXISTS editor (" +
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
            statement.execute(queryForMagazines);
            statement.execute(queryForThemes);
            statement.execute(queryForGenres);
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

    public static void importerMagazine() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvGenre = "magazines.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO magazines(magazine_id, magazine) VALUES(?,?)";
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

    public static void importerManga() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvAnime = "mangas.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO animes(manga_id, nom_manga, pays, nb_tomes, annee_edition, sens_lecture, magazine_id, editor_id) VALUES(?,?,?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvAnime));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";;");
                String manga_id = data[0];
                String nom_manga = data[1];
                String pays = data[2];
                String nb_tomes = data[3];
                String annee_edition = data[4];
                String sens_lecture = data[5];
                String magazine_id = data[6];
                String editor_id = data[7];



                if (manga_id.equals("NULL")){
                    preparedStatement.setNull(1,Types.NULL);
                }else {
                    preparedStatement.setInt(1, Integer.parseInt(manga_id));
                }

                preparedStatement.setString(2,nom_manga);
                if (pays.equals("NULL")){
                    preparedStatement.setNull(3,Types.NULL);
                }else {
                    preparedStatement.setString(3, pays);
                }
                if (nb_tomes.equals("NULL")){
                    preparedStatement.setNull(4,Types.NULL);
                }else {
                    preparedStatement.setInt(4, Integer.parseInt(nb_tomes));
                }
                if (annee_edition.equals("NULL")){
                    preparedStatement.setNull(5,Types.NULL);
                }else {
                    Date date = new Date(Long.parseLong(annee_edition));
                    preparedStatement.setDate(5, date);
                }

                if (sens_lecture.equals("NULL")){
                    preparedStatement.setNull(6,Types.NULL);
                }else {
                    preparedStatement.setString(6, sens_lecture);
                }
                if (magazine_id.equals("NULL")){
                    preparedStatement.setNull(7,Types.NULL);
                }else {
                    preparedStatement.setInt(7, Integer.parseInt(magazine_id));
                }
                if (editor_id.equals("NULL")){
                    preparedStatement.setNull(8,Types.NULL);
                }else {
                    preparedStatement.setInt(8, Integer.parseInt(editor_id));
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
        String csvGenre = "mangasThemesGenres.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO mangas_themes_genres (manga_id, theme_id, genre_id) VALUES(?,?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvGenre));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";;");
                String manga_id = data[0];
                String theme = data[1];
                String genre = data[2];

                if (manga_id.equals("NULL")){
                    preparedStatement.setNull(1, Types.NULL);
                }
                else {
                    preparedStatement.setInt(1, Integer.parseInt(manga_id));
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
            String interrogation = "Select * From mangas";
            ResultSet rs;

            rs = statement.executeQuery(interrogation);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
