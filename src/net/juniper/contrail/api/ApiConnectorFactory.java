/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.api;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiConnectorFactory {
	private static final Logger s_logger = LoggerFactory.getLogger(ApiConnectorImpl.class);

	static private ApiConnectorFactory _singleton;
	private Class<? extends ApiConnector> _cls;

	private ApiConnectorFactory() {
		_cls = ApiConnectorImpl.class;
	}

	private Constructor<? extends ApiConnector> getConstructor() throws NoSuchMethodException {
		return _cls.getConstructor(String.class, Integer.TYPE);
	}

	private static synchronized ApiConnectorFactory getInstance() {
		if (_singleton == null) {
			_singleton = new ApiConnectorFactory();
		}
		return _singleton;
	}

	/**
	 * Create an ApiConnector object.
	 *
	 * @param hostname name or IP address of contrail VNC api server.
	 * @port api server port.
	 * @return ApiConnector implementation.
	 */
	public static ApiConnector build(final String hostname, final int port) {
		final ApiConnectorFactory factory = getInstance();
		try {
			final Constructor<? extends ApiConnector> constructor = factory.getConstructor();
			return constructor.newInstance(hostname, port);
		} catch (final Exception ex) {
			s_logger.error("Unable to create object", ex);
		}
		return null;
	}

	public static void setImplementation(final Class<? extends ApiConnector> cls) {
		final ApiConnectorFactory factory = getInstance();
		factory._cls = cls;
	}
}
