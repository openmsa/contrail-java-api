/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.api;

import java.lang.reflect.Constructor;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiConnectorFactory {
	private static final Logger s_logger = LoggerFactory.getLogger(ApiConnectorFactory.class);

	private static ApiConnectorFactory singleton;
	private Class<? extends ApiConnector> cls;

	private ApiConnectorFactory() {
		cls = ApiConnectorImpl.class;
	}

	private Constructor<? extends ApiConnector> getConstructor() throws NoSuchMethodException {
		return cls.getConstructor(URI.class);
	}

	private static synchronized ApiConnectorFactory getInstance() {
		if (singleton == null) {
			singleton = new ApiConnectorFactory();
		}
		return singleton;
	}

	/**
	 * Create an ApiConnector object.
	 *
	 * @param url name or IP address of contrail VNC api server.
	 * @port api server port.
	 * @return ApiConnector implementation.
	 */

	public static ApiConnector build(final URI url) {
		final ApiConnectorFactory factory = getInstance();
		try {
			final Constructor<? extends ApiConnector> constructor = factory.getConstructor();
			return constructor.newInstance(url);
		} catch (final Exception ex) {
			s_logger.error("Unable to create object", ex);
		}
		return null;
	}

	public static void setImplementation(final Class<? extends ApiConnector> cls) {
		final ApiConnectorFactory factory = getInstance();
		factory.cls = cls;
	}
}
