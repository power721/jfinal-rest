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

    private final int errorCode;

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
