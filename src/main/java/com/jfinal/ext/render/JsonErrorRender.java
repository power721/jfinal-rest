package com.jfinal.ext.render;

import com.jfinal.render.JsonRender;

import java.util.HashMap;
import java.util.Map;

public class JsonErrorRender extends JsonRender {

    private static final Map<Integer, String> errorCodes = new HashMap<Integer, String>();
    static {
        errorCodes.put(400, "Bad Request");
        errorCodes.put(401, "Unauthorized");
        errorCodes.put(403, "Forbidden");
        errorCodes.put(404, "Not Found");
        errorCodes.put(406, "Not Acceptable");
        errorCodes.put(410, "Gone");
        errorCodes.put(429, "Too Many Requests");
        errorCodes.put(500, "Internal Server Error");
        errorCodes.put(503, "Service Unavailable");
    }

    protected int errorCode;

    public JsonErrorRender(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    @Override
    public void render() {
        request.setAttribute("error", errorCodes.get(errorCode));
        response.setStatus(errorCode);
        super.render();
    }

}
