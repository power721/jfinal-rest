package com.jfinal.rest;

import com.google.gson.Gson;
import com.jfinal.core.Controller;

import java.io.IOException;
import java.io.InputStreamReader;

public class RestController extends Controller {

    private Gson gson = new Gson();

    public <T> T getBean(Class<T> beanClass) {
        try {
            return gson.fromJson(new InputStreamReader(getRequest().getInputStream()), beanClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
