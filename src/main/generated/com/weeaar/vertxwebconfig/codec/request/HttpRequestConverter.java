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

package com.weeaar.vertxwebconfig.codec.request;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link com.weeaar.vertxwebconfig.codec.request.HttpRequest}.
 *
 * NOTE: This class has been automatically generated from the {@link com.weeaar.vertxwebconfig.codec.request.HttpRequest} original class using Vert.x codegen.
 */
public class HttpRequestConverter {

  public static void fromJson(JsonObject json, HttpRequest obj) {
  }

  public static void toJson(HttpRequest obj, JsonObject json) {
    if (obj.getBodyString() != null) {
      json.put("bodyString", obj.getBodyString());
    }
    if (obj.getCookies() != null) {
      json.put("cookies", obj.getCookies());
    }
    json.put("ended", obj.isEnded());
    json.put("ssl", obj.isSSL());
  }
}