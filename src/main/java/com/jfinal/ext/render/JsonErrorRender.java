/**
 * Copyright 2015 power721 (power0721@gmail.com)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.render;

import com.jfinal.render.JsonRender;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Render error to json response.
 */
public class JsonErrorRender extends JsonRender {

    private static final Map<Integer, String> ERROR_CODES = new HashMap<Integer, String>();

    static {
        ERROR_CODES.put(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
        ERROR_CODES.put(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        ERROR_CODES.put(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        ERROR_CODES.put(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        ERROR_CODES.put(HttpServletResponse.SC_NOT_ACCEPTABLE, "Not Acceptable");
        ERROR_CODES.put(HttpServletResponse.SC_GONE, "Gone");
        ERROR_CODES.put(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        ERROR_CODES.put(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Service Unavailable");
    }

    private final int errorCode;

    /**
     * construct the error render by the error code
     *
     * @param errorCode the error code
     */
    public JsonErrorRender(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    /**
     * render the HTTP response
     */
    @Override
    public void render() {
        String error = ERROR_CODES.get(errorCode);
        request.setAttribute("error", error != null ? error :
                "Internal Server Error(invalid error code" + errorCode + ")");
        response.setStatus(errorCode);
        super.render();
    }

}
