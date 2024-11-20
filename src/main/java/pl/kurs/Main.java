package pl.kurs;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        final String driver = "org.postgresql.Driver";

        Class.forName(driver);

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        executeSelect(connection);
//        executeInsert(connection);
//        executeSelect(connection);



//        executeUpdate(connection);

        executeDelete(connection);
        executeSelect(connection);


        connection.close();

    }

    // CREATE TABLE IF NOT EXIST
    // IF NOT EXIST CREATE TABLE
    private static void executeSelect(Connection connection) throws SQLException {
        String query = "SELECT id, imie, nazwisko FROM person;";

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String imie = resultSet.getString("imie");
            String nazwisko = resultSet.getString("nazwisko");

            System.out.println(id + " " + imie + " " + nazwisko);

        }
    }

    private static void executeInsert(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO public.person " +
                "(imie, nazwisko, data_urodzenia, pesel, nr_tel) " +
                "VALUES('Adam', 'Nowak', '1999-05-13', '95110502579', '500789456');";

        Statement statement = connection.createStatement();

        int rowsAffected = statement.executeUpdate(insertQuery);

        System.out.println(rowsAffected);


    }

    private static void executeUpdate(Connection connection) throws SQLException {
        String updateQuery = "UPDATE person SET imie = 'Karol' WHERE id = 2;";

        Statement statement = connection.createStatement();

        int rowsAffected = statement.executeUpdate(updateQuery);

        System.out.println(rowsAffected);


    }

    private static void executeDelete(Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM person WHERE id = 2;";

        Statement statement = connection.createStatement();

        int rowsAffected = statement.executeUpdate(deleteQuery);

        System.out.println(rowsAffected);


    }

}