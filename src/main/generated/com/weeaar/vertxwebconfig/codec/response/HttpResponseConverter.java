/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.weeaar.vertxwebconfig.codec.response;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link com.weeaar.vertxwebconfig.codec.response.HttpResponse}.
 *
 * NOTE: This class has been automatically generated from the {@link com.weeaar.vertxwebconfig.codec.response.HttpResponse} original class using Vert.x codegen.
 */
public class HttpResponseConverter {

  public static void fromJson(JsonObject json, HttpResponse obj) {
    if (json.getValue("body") instanceof String) {
      obj.setBody((String)json.getValue("body"));
    }
    if (json.getValue("statusCode") instanceof Number) {
      obj.setStatusCode(((Number)json.getValue("statusCode")).intValue());
    }
  }

  public static void toJson(HttpResponse obj, JsonObject json) {
    if (obj.getBody() != null) {
      json.put("body", obj.getBody());
    }
    if (obj.getStatusCode() != null) {
      json.put("statusCode", obj.getStatusCode());
    }
    if (obj.getStatusMessage() != null) {
      json.put("statusMessage", obj.getStatusMessage());
    }
  }
}