package com.projetanime.database;

import java.sql.*;

public class ConnectionDB {
    public static void main(String[] args) {
        new ConnectionDB();

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
        String query = "CREATE TABLE IF NOT EXISTS animes (" +
                "id_anime INT AUTO_INCREMENT PRIMARY KEY," +
                "nom_anime VARCHAR(150)," +
                "nom_anime_alternatif VARCHAR(150)," +
                "nb_episode INT," +
                "duree_episode INT," +
                "annee_diffusion DATE," +
                "theme VARCHAR(150)," +
                "genre VARCHAR(150))";
        try {
            statement = con.createStatement();
            statement.execute(query);
        } catch (SQLException e){
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
