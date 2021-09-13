package com.spring.kodo.service.inter;

import java.sql.SQLException;

public interface DataSourceService
{
    void dropDatabase() throws SQLException;

    void createDatabase() throws SQLException;
}
