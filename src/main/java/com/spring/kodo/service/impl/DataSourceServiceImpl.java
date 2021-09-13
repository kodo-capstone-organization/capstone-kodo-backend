package com.spring.kodo.service.impl;

import com.spring.kodo.service.inter.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class DataSourceServiceImpl implements DataSourceService
{
    private Connection connection;

    @Autowired
    public DataSourceServiceImpl(DataSource dataSource) throws SQLException
    {
        this.connection = dataSource.getConnection();
    }

    public void dropDatabase() throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement("DROP DATABASE Kodo");
        preparedStatement.execute();
    }

    public void createDatabase() throws SQLException
    {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE DATABASE Kodo");
        preparedStatement.execute();
    }
}
