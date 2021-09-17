package com.spring.kodo.service.impl;

import com.spring.kodo.service.inter.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Service
public class DataSourceServiceImpl implements DataSourceService
{
    private Connection connection;

    @Autowired
    public DataSourceServiceImpl(DataSource dataSource) throws SQLException
    {
        this.connection = dataSource.getConnection();
    }

    /*
        Function should only be used if user is looking to empty existing data records in ALL tables within the database
        Otherwise, it could potentially break data integrity of the database
     */
    @Override
    public void truncateAllTables() throws SQLException
    {
        ArrayList<String> truncateCommands = getTruncateAllTablesCommands();

        PreparedStatement preparedStatement = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
        preparedStatement.execute();

        for (String truncateCommand : truncateCommands)
        {
            preparedStatement = connection.prepareStatement(truncateCommand);
            preparedStatement.execute();
        }

        preparedStatement = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
        preparedStatement.execute();
    }

    /*
        Function to create SQL statements to truncate all tables within the database
     */
    public ArrayList<String> getTruncateAllTablesCommands() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT CONCAT('TRUNCATE TABLE ', TABLE_NAME) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'Kodo'");
        ResultSet resultSet = preparedStatement.executeQuery();

        ResultSetMetaData metadata = resultSet.getMetaData();
        int numberOfColumns = metadata.getColumnCount();

        ArrayList<String> truncateCommands = new ArrayList<String>();

        while (resultSet.next()) {
            int i = 1;
            while(i <= numberOfColumns) {
                truncateCommands.add(resultSet.getString(i++));
            }
        }
        return truncateCommands;
    }

    @Override
    public void createDatabase() throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS Kodo");
        preparedStatement.execute();
    }
}
