/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.api;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ApiSerializer {
	private static class ReferenceSerializer implements JsonSerializer<ObjectReference<? extends ApiPropertyBase>> {
		@Override
		public JsonElement serialize(final ObjectReference<? extends ApiPropertyBase> objref, final Type type,
				final JsonSerializationContext context) {
			final JsonObject obj = new JsonObject();
			obj.add("to", context.serialize(objref.getReferredName()));
			JsonElement js_attr;
			if (objref.getAttr() == null) {
				js_attr = JsonNull.INSTANCE;
			} else {
				js_attr = context.serialize(objref.getAttr());
			}
			obj.add("attr", js_attr);
			if (objref.getUuid() != null) {
				obj.addProperty("uuid", objref.getUuid());
			}
			return obj;
		}
	}

	private static class NullWritingTypeAdapterFactory implements TypeAdapterFactory {
		@Override
		public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
			if (type.getRawType() != ObjectReference.class) {
				return null;
			}
			final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
			return new TypeAdapter<>() {
				@Override
				public void write(final JsonWriter out, final T value) throws IOException {
					final boolean writeNulls = out.getSerializeNulls();
					out.setSerializeNulls(true);
					delegate.write(out, value);
					out.setSerializeNulls(writeNulls);
				}

				@Override
				public T read(final JsonReader in) throws IOException {
					return delegate.read(in);
				}
			};
		}
	}

	private static final TypeAdapter<Number> UNSIGNED_LONG = new UnsignedLongAdapter();

	private static class UnsignedLongAdapter extends TypeAdapter<Number> {
		private final static BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
		private final static BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
		private final static BigInteger MAX_UNSIGNED = MAX_LONG.subtract(MIN_LONG);

		@Override
		public Number read(final JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}

			final BigInteger value = new BigInteger(in.nextString());

			if ((value.compareTo(MAX_UNSIGNED) > 0) || (value.compareTo(BigInteger.ZERO) < 0)) {
				return value;
			}

			if (value.compareTo(MAX_LONG) > 0) {
				final BigInteger overflow = value.subtract(MAX_LONG);
				return MIN_LONG.add(overflow.subtract(BigInteger.ONE)).longValue();
			}
			return value.longValue();
		}

		@Override
		public void write(final JsonWriter out, final Number value) throws IOException {
			if (value instanceof Long) {
				final long l = value.longValue();
				if (l >= 0L) {
					out.value(value);
				} else {
					final BigInteger bi = BigInteger.valueOf(l);
					final BigInteger overflow = bi.subtract(MIN_LONG);
					final BigInteger unsigned = MAX_LONG.add(overflow).add(BigInteger.ONE);
					out.value(unsigned);
				}
			} else {
				out.value(value);
			}
		}
	}

	static Gson getDeserializer() {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, UNSIGNED_LONG));
		// Do not attempt to deserialize ApiObjectBase.parent
		return builder.excludeFieldsWithModifiers(Modifier.VOLATILE).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
	}

	static Gson getSerializer() {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ObjectReference.class, new ReferenceSerializer());
		builder.registerTypeAdapterFactory(new NullWritingTypeAdapterFactory());
		builder.registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, UNSIGNED_LONG));
		return builder.create();
	}

	static ApiObjectBase deserialize(final String str, final Class<? extends ApiObjectBase> cls) {
		final Gson json = getDeserializer();
		return json.fromJson(str, cls);
	}

	static String serializeObject(final String typename, final ApiObjectBase obj, final String domain, final String project) {
		final Gson json = getSerializer();
		obj.updateQualifiedName(domain, project);
		if (obj instanceof VRouterApiObjectBase) {
			final JsonElement el = json.toJsonTree(obj);
			return el.toString();
		}
		final JsonObject js_dict = new JsonObject();
		js_dict.add(typename, json.toJsonTree(obj));

		return js_dict.toString();
	}
}
