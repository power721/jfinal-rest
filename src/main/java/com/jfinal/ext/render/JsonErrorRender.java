package com.jfinal.ext.render;

import com.jfinal.render.JsonRender;

import java.util.HashMap;
import java.util.Map;

public class JsonErrorRender extends JsonRender {

    private static final Map<Integer, String> ERROR_CODES = new HashMap<Integer, String>();
    static {
        ERROR_CODES.put(400, "Bad Request");
        ERROR_CODES.put(401, "Unauthorized");
        ERROR_CODES.put(403, "Forbidden");
        ERROR_CODES.put(404, "Not Found");
        ERROR_CODES.put(406, "Not Acceptable");
        ERROR_CODES.put(410, "Gone");
        ERROR_CODES.put(429, "Too Many Requests");
        ERROR_CODES.put(500, "Internal Server Error");
        ERROR_CODES.put(503, "Service Unavailable");
    }

    protected int errorCode;

    public JsonErrorRender(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    @Override
    public void render() {
        request.setAttribute("error", ERROR_CODES.get(errorCode));
        response.setStatus(errorCode);
        super.render();
    }

}
