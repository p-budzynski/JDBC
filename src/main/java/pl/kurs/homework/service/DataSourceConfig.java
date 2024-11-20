package pl.kurs.homework.service;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/homework_jdbc";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static BasicDataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(100);

        return dataSource;
    }
}
