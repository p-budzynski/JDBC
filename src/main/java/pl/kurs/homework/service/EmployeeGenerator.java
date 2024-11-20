package pl.kurs.homework.service;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class EmployeeGenerator {
    private static final String[] FIRST_NAME = {"John", "Jane", "Michael", "Emily", "David", "Anna", "Robert", "Laura", "Chris", "Sophia"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Brown", "Williams", "Jones", "Garcia", "Miller", "Davis", "Martinez", "Hernandez"};
    private static final Position[] POSITIONS = Position.values();
    private static final Random RANDOM = new Random();
    private static final BasicDataSource dataSource = DataSourceConfig.getDataSource();

    public void generateAndInsertEmployee(int count) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO public.employee " +
                    "(first_name, last_name, job_position, salary) " +
                    "VALUES(?, ?, ?::job_position, ?);");

            int successfulInserts = 0;
            for (int i = 0; i < count; i++) {
                String firstName = FIRST_NAME[RANDOM.nextInt(FIRST_NAME.length)];
                String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
                Position position = POSITIONS[RANDOM.nextInt(POSITIONS.length)];
                Double salary = generateSalary(position);

                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, position.getDbValue());
                preparedStatement.setDouble(4, salary);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    successfulInserts++;
                }
            }

            System.out.println("Successfully inserted: " + successfulInserts + " employees.");

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

    private Double generateSalary(Position position) {
        return switch (position) {
            case INTERN -> 2000.0 + RANDOM.nextInt(1000);
            case JUNIOR_DEV -> 4000.0 + RANDOM.nextInt(2000);
            case MID_DEV -> 6000.0 + RANDOM.nextInt(2000);
            case LEAD_DEV -> 8000.0 + RANDOM.nextInt(3000);
            case SENIOR_DEV -> 10000.0 + RANDOM.nextInt(5000);
        };
    }
}

