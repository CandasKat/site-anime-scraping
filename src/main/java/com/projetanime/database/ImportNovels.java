package com.projetanime.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class ImportNovels {
    private static Connection con = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;

    public ImportNovels(){
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
        String queryForAnime = "CREATE TABLE IF NOT EXISTS novels (" +
                "novel_id INT AUTO_INCREMENT PRIMARY KEY," +
                "nom_novel VARCHAR(250)," +
                "pays VARCHAR(250)," +
                "nb_tomes INT," +
                "annee_publication DATE," +
                "sens_lecture VARCHAR(250)," +
                "editor_id INT," +
                "FOREIGN KEY (editor_id) REFERENCES editorNovels(editor_id))";

        String queryForTablesCommunes = "CREATE TABLE IF NOT EXISTS novels_themes_genres (" +
                "novel_id INT," +
                "theme INT," +
                "genre INT," +
                "FOREIGN KEY (novel_id) REFERENCES novels(novel_id)," +
                "FOREIGN KEY (theme) REFERENCES themes(theme_id)," +
                "FOREIGN KEY (genre) REFERENCES genres(genre_id))";

        String queryForEditor = "CREATE TABLE IF NOT EXISTS editorNovels (" +
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

    public static void importerEditor() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvGenre = "editorNovels.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO editorNovels(editor_id, editor) VALUES(?,?)";
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

    public static void importerNovels() throws SQLException {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            System.out.println("Pilote introuvable...");
        }
        con = DriverManager.getConnection(url, Database.user, Database.password);
        con.setAutoCommit(false);
        String csvAnime = "novels.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO novels(novel_id, nom_novel, pays, nb_tomes, annee_publication, editor_id) VALUES(?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvAnime));
            String lineText = null;
            int count = 0;
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(";;");
                String novel_id = data[0];
                String nom_novel = data[1];
                String pays = data[2];
                String nb_tomes = data[3];
                String annee_publication = data[4];
                String editor_id = data[5];



                if (novel_id.equals("NULL")){
                    preparedStatement.setNull(1,Types.NULL);
                }else {
                    preparedStatement.setInt(1, Integer.parseInt(novel_id));
                }

                preparedStatement.setString(2,nom_novel);
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
                if (annee_publication.equals("NULL")){
                    preparedStatement.setNull(5,Types.NULL);
                }else {
                    Date date = new Date(Long.parseLong(annee_publication));
                    preparedStatement.setDate(5, date);
                }

                if (editor_id.equals("NULL")){
                    preparedStatement.setNull(6,Types.NULL);
                }else {
                    preparedStatement.setInt(6, Integer.parseInt(editor_id));
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
        String csvGenre = "novelsThemesGenres.csv";
        int batchSize = 20;

        try {
            String sql = "INSERT INTO novels_themes_genres (novel_id, theme, genre) VALUES(?,?,?)";
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
            String interrogation = "Select * From novels";
            ResultSet rs;

            rs = statement.executeQuery(interrogation);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
