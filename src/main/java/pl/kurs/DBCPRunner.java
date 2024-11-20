package pl.kurs;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;

public class DBCPRunner {
    public static void main(String[] args) throws SQLException {

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(100);

        Connection connection = dataSource.getConnection();

//        executeSelectPreparedStatement(connection, "ABCD");
        executeInTransaction2(connection);

        if (connection != null) {
            connection.close();
        }
    }

    private static void executeInTransaction(Connection connection) throws SQLException {
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;

        try {

            try {
                preparedStatement = connection.prepareStatement("DELETE FROM person WHERE imie = ?;");
                preparedStatement.setString(1, "Adam");
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }

            try {
                preparedStatement2 = connection.prepareStatement("INSERT INTO public.person " +
                        "(imie, nazwisko, data_urodzenia, pesel, nr_tel) " +
                        "VALUES(?, ?, ?, ?, ?);");

                preparedStatement2.setString(1, "Karol");
                preparedStatement2.setString(2, "Kowalski");
                preparedStatement2.setDate(3, new Date(98, 2, 22));
                preparedStatement2.setString(4, "98032202579");
                preparedStatement2.setString(5, "416234876");

                int rowsAffected2 = preparedStatement2.executeUpdate();
                System.out.println("Rows affected2: " + rowsAffected2);
            } finally {
                if (preparedStatement2 != null) {
                    preparedStatement2.close();
                }
            }

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        }


    }

    private static void executeInTransaction2(Connection connection) throws SQLException {
        connection.setAutoCommit(false); // Wyłączenie automatycznej transakcji, musimy ręcznie obsługiwac commit oraz rollback!!!!!!!!!!!

        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;

        try {
            preparedStatement = connection.prepareStatement("DELETE FROM person WHERE id = ?;"); // Utworzenie preparedStatement z query, z dynamicznym parametrem "?"
            preparedStatement.setInt(1, 10); // Ustawienie dynamicznego parametru "?" pod pierwszym indeksem na wartość 10
            int rowsAffected = preparedStatement.executeUpdate(); // Wywołanie query, executeUpdate zwraca nam inta ktory mówi ile wpisow zostało zmodyfikowanych
            System.out.println("Rows affected: " + rowsAffected);


            preparedStatement2 = connection.prepareStatement("INSERT INTO public.person " +
                    "(imie, nazwisko, data_urodzenia, pesel, nr_tel) " +
                    "VALUES(?, ?, ?, ?, ?);");

            preparedStatement2.setString(1, "Karol");
            preparedStatement2.setString(2, "Kowalski");
            preparedStatement2.setDate(3, new Date(98, 2, 22));
            preparedStatement2.setString(4, "98032202579");
            preparedStatement2.setString(5, "416234876");

            int rowsAffected2 = preparedStatement2.executeUpdate();
            System.out.println("Rows affected2: " + rowsAffected2);

            connection.commit(); // Zatwierdzenie wszystkich operacji, jezeli doszlismy do tego miejsca, to nie polecial wyjatek i wszystkie operacje mozna uznac za sukces
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback(); // Jezeli poleciał wyjątek, to znaczy ze któras z operacji sie nie powiodła i nalezy wykonać rollback(wycofac pozostałe zmiany)
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close(); // Zamkniecie zasobow
            }
            if (preparedStatement2 != null) {
                preparedStatement2.close();
            }
        }


    }

    private static void executeSelectPreparedStatement(Connection connection, String searchName) throws SQLException {
        String query = "SELECT id, imie, nazwisko FROM person WHERE imie = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, searchName);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String imie = resultSet.getString("imie");
            String nazwisko = resultSet.getString("nazwisko");

            System.out.println(id + " " + imie + " " + nazwisko);
        }

        resultSet.close();
        preparedStatement.close();
    }

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

        resultSet.close();
        statement.close();
    }

    private static void executeInsert(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO public.person " +
                "(imie, nazwisko, data_urodzenia, pesel, nr_tel) " +
                "VALUES('Adam', 'Nowak', '1999-05-13', '95110502579', '500789456');";

        Statement statement = connection.createStatement();

        int rowsAffected = statement.executeUpdate(insertQuery);

        System.out.println(rowsAffected);

        statement.close();
    }

    private static void executeUpdate(Connection connection) throws SQLException {
        String updateQuery = "UPDATE person SET imie = 'Karol' WHERE id = 2;";

        Statement statement = connection.createStatement();

        int rowsAffected = statement.executeUpdate(updateQuery);

        System.out.println(rowsAffected);

        statement.close();
    }

    private static void executeDelete(Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM person WHERE id = 2;";

        Statement statement = connection.createStatement();

        int rowsAffected = statement.executeUpdate(deleteQuery);

        System.out.println(rowsAffected);

        statement.close();
    }
}
