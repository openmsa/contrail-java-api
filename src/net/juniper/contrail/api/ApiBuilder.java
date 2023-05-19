/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class ApiBuilder {
	private static final String UNABLE_TO_PARSE_RESPONSE = "Unable to parse response";
	private static final Logger LOG = LoggerFactory.getLogger(ApiBuilder.class);

	ApiBuilder() {
	}

	public static String getTypename(final Class<?> cls) {
		String clsname = cls.getName();
		final int loc = clsname.lastIndexOf('.');
		if (loc > 0) {
			clsname = clsname.substring(loc + 1);
		}
		final StringBuilder typename = new StringBuilder();
		for (int i = 0; i < clsname.length(); i++) {
			char ch = clsname.charAt(i);
			if (Character.isUpperCase(ch)) {
				if (i > 0) {
					typename.append("-");
				}
				ch = Character.toLowerCase(ch);
			}
			typename.append(ch);
		}
		return typename.toString();
	}

	public static ApiObjectBase jsonToApiObject(final String data, final Class<? extends ApiObjectBase> cls) {
		if (data == null) {
			LOG.info("Contrail response is null");
			return null;
		}
		final String typename = getTypename(cls);
		final JsonObject jsObj = JsonParser.parseString(data).getAsJsonObject();
		if (jsObj == null) {
			LOG.warn(UNABLE_TO_PARSE_RESPONSE);
			return null;
		}
		JsonElement element = null;
		if (cls.getGenericSuperclass() == VRouterApiObjectBase.class) {
			element = jsObj;
		} else {
			element = jsObj.get(typename);
		}
		if (element == null) {
			LOG.warn("Element {}: not found", typename);
			return null;
		}
		return ApiSerializer.deserialize(element.toString(), cls);
	}

	// body: {"type": class, "fq_name": [parent..., name]}
	public static String buildFqnJsonString(final Class<? extends ApiObjectBase> cls, final List<String> nameList) {
		final Gson json = new Gson();
		final JsonObject jsDict = new JsonObject();
		jsDict.add("type", json.toJsonTree(getTypename(cls)));
		jsDict.add("fq_name", json.toJsonTree(nameList));
		return jsDict.toString();
	}

	public static String getUuid(final String data) {
		if (data == null) {
			return null;
		}
		final JsonObject jsObj = JsonParser.parseString(data).getAsJsonObject();
		if (jsObj == null) {
			LOG.warn(UNABLE_TO_PARSE_RESPONSE);
			return null;
		}
		final JsonElement element = jsObj.get("uuid");
		if (element == null) {
			LOG.warn("Element \"uuid\": not found");
			return null;
		}
		return element.getAsString();
	}

	public static List<? extends ApiObjectBase> jsonToApiObjects(final String data, final Class<? extends ApiObjectBase> cls, final List<String> parent) throws IOException {
		if (data == null) {
			return null;
		}
		final String typename = getTypename(cls);
		final List<ApiObjectBase> list = new ArrayList<>();
		final JsonObject jsObj = JsonParser.parseString(data).getAsJsonObject();
		if (jsObj == null) {
			LOG.warn(UNABLE_TO_PARSE_RESPONSE);
			return null;
		}
		final JsonArray array = jsObj.getAsJsonArray(typename + "s");
		if (array == null) {
			LOG.warn("Element {}: not found", typename);
			return null;
		}
		final Gson json = ApiSerializer.getDeserializer();
		for (final JsonElement element : array) {
			final ApiObjectBase obj = json.fromJson(element.toString(), cls);
			if (obj == null) {
				LOG.warn("Unable to decode list element");
				continue;
			}
			list.add(obj);
		}
		return list;
	}
}
