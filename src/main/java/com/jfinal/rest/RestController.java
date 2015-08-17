package com.jfinal.rest;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class RestController extends Controller {

    protected static final Logger LOGGER = LogManager.getLogger(RestController.class);

    private Gson gson = new Gson();

    public <T> T getJsonData(Class<T> beanClass) {
        try {
            return gson.fromJson(getRequest().getReader(), beanClass);
        } catch (IOException e) {
            LOGGER.error("parse json data failed", e);
        }
        return null;
    }

}
