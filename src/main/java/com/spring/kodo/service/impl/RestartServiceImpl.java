package com.spring.kodo.service.impl;

import com.spring.kodo.service.inter.RestartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.stereotype.Service;

@Service
public class RestartServiceImpl implements RestartService
{
    @Autowired
    private RestartEndpoint restartEndpoint;

    public void restartApp()
    {
        restartEndpoint.restart();
    }
}
