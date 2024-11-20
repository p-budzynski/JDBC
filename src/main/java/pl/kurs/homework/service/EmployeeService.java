package pl.kurs.homework.service;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeService {
    private static final BasicDataSource dataSource = DataSourceConfig.getDataSource();

    public void hireEmployee(String firstName, String lastName, Position position, double salary) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO public.employee " +
                    "(first_name, last_name, job_position, salary) " +
                    "VALUES(?, ?, ?::job_position, ?);");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, position.getDbValue());
            preparedStatement.setDouble(4, salary);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void fireEmployee(int employeeId) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE employee SET fired = TRUE WHERE id = ?;");
            preparedStatement.setInt(1, employeeId);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void increaseSalaryById(int employeeId) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE employee SET salary = salary * 1.2 WHERE id = ?;");
            preparedStatement.setInt(1, employeeId);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void promoteEmployee(int employeeId, String newPosition) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE employee SET job_position = ?::job_position WHERE id = ?;");
            preparedStatement.setString(1, newPosition);
            preparedStatement.setInt(2, employeeId);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void findEmployeeById(int employeeId) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id, first_name, last_name, job_position, salary, fired FROM employee WHERE id = ?;");
        preparedStatement.setInt(1, employeeId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String jobPosition = resultSet.getString("job_position");
            Double salary = resultSet.getDouble("salary");
            boolean isFired = resultSet.getBoolean("fired");
            System.out.println("Employee ID: " + id + ", first name: " + firstName + ", last name: " +
                    lastName + ", position: " + jobPosition + ", salary: " + salary + " PLN, fired: " + isFired);
        } else {
            System.out.println("The employee with ID: " + employeeId + " does not exist.");
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
