/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.RequestExpectContinue;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.HttpClientHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ConnectionReuseStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
import org.apache.hc.core5.http.impl.io.DefaultBHttpClientConnection;
import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.DefaultHttpProcessor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.protocol.HttpProcessor;
import org.apache.hc.core5.http.protocol.RequestConnControl;
import org.apache.hc.core5.http.protocol.RequestContent;
import org.apache.hc.core5.http.protocol.RequestDate;
import org.apache.hc.core5.http.protocol.RequestTargetHost;
import org.apache.hc.core5.http.protocol.RequestUserAgent;
import org.apache.hc.core5.ssl.SSLContexts;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.client.IOSClientBuilder.V3;
import org.openstack4j.api.exceptions.AuthenticationException;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

@SuppressWarnings("deprecation")
class ApiConnectorImpl implements ApiConnector {
	private static final String FAILURE_MESSAGE = "Failure message: {}";

	private static final Logger LOG = LoggerFactory.getLogger(ApiConnectorImpl.class);

	private final String apiHostname;
	private final int apiPort;
	private String _username;
	private String _password;
	private String tenantId;
	private String domainId;
	private boolean hasInputAuthtoken;
	private String authToken;
	private String authType;
	private String authUrl;

	// HTTP Connection parameters
	private HttpProcessor httpProc;
	private HttpRequestExecutor httpExecutor;
	private HttpContext httpContext;
	private HttpHost httpHost;
	private DefaultBHttpClientConnection connection;
	private ConnectionReuseStrategy connectionStrategy;
	public static final int MAX_RETRIES = 5;
	public final int clientId = 2;

	private String projectName;

	private String domainName;

	private final boolean ssl;

	public ApiConnectorImpl(final URI url) {
		apiHostname = url.getHost();
		apiPort = (-1 == url.getPort()) ? 8082 : url.getPort();
		ssl = "https".equals(url.getScheme());
		hasInputAuthtoken = true;
		initHttpClient();
		initHttpServerParams(apiHostname, apiPort);
	}

	private void initHttpClient() {
		httpProc = new DefaultHttpProcessor(
				// Required protocol interceptors
				// new BasicHttpProcessor(),
				new RequestConnControl(),
				new RequestContent(),
				new RequestDate(),
				new RequestTargetHost(),
				// Recommended protocol interceptors
				new RequestUserAgent(),
				new RequestExpectContinue());
		httpExecutor = new HttpRequestExecutor();
		httpContext = new BasicHttpContext(null);
		final Http1Config config = Http1Config.custom().setBufferSize(8192).build();
		connection = new DefaultBHttpClientConnection(config);
		connectionStrategy = new DefaultConnectionReuseStrategy();
	}

	private void initHttpServerParams(final String hostname, final int port) {
		httpHost = new HttpHost(ssl ? "https" : "http", hostname, port);
//		httpContext.setAttribute(HttpCoreContext.HTTP_CONNECTION, connection);
//		httpContext.setAttribute(HttpCoreContext.HTTP_TARGET_HOST, httpHost);
	}

	@Override
	public ApiConnector credentials(final String username, final String password) {
		_username = username;
		_password = password;
		return this;
	}

	@Override
	public ApiConnector domainName(final String tenant) {
		domainId = tenant;
		return this;
	}

	@Override
	public ApiConnector tenantId(final String tenant) {
		this.tenantId = tenant;
		return this;
	}

	@Override
	public ApiConnector authToken(final String token) {
		hasInputAuthtoken = true;
		authToken = token;
		return this;
	}

	@Override
	public ApiConnector authServer(final String type, final String url) {
		hasInputAuthtoken = false;
		authType = type;
		authUrl = url;
		return this;
	}

	private void checkResponseKeepAliveStatus(final HttpResponse response) throws IOException {
//		if (!connectionStrategy.keepAlive(req, response, httpContext)) {
//			connection.close();
//		}
	}

	private void checkConnection() throws IOException {
		if (!connection.isOpen()) {
			LOG.trace("http connection <{}, {}> does not exit", httpHost.getHostName(), httpHost.getPort());
			// Socket is used by _connection.
			@SuppressWarnings("resource")
			final Socket socket = new Socket(httpHost.getHostName(), httpHost.getPort());
			connection.bind(socket);
			LOG.trace("http connection <{}, {}> established", httpHost.getHostName(), httpHost.getPort());
		}
	}

	@Override
	protected void finalize() {
		dispose();
	}

	public String getHostName() {
		return apiHostname;
	}

	public int getPort() {
		return apiPort;
	}

	// return value indicates whether the token changed
	private boolean authenticate() {
		if (hasInputAuthtoken) {
			return false;
		}

		authToken = null;
		if ("keystone".equals(authType)) {
			try {
				final OSClientV2 os = OSFactory.builderV2().endpoint(authUrl)
						.credentials(_username, _password).tenantName(domainId).authenticate();
				authToken = os.getAccess().getToken().getId();
				return true;
			} catch (final AuthenticationException authe) {
				LOG.warn("authenticate to keystone {} failed: {}", authe, authUrl);
			}
		} else if ("keystonev3".equals(authType)) {
			try {
				final V3 base = OSFactory.builderV3().endpoint(authUrl);
				base.withConfig(Config.newConfig().withSSLVerificationDisabled());
				if (domainId != null) {
					final Identifier domainIdentifier = Identifier.byName(domainId);
					base.credentials(_username, _password, domainIdentifier);
				} else {
					base.credentials(_username, _password);
				}
				if (null != tenantId) {
					base.scopeToProject(Identifier.byId(tenantId));
				}
				final OSClientV3 os = base.authenticate();
				authToken = os.getToken().getId();
				final Project proj = os.getToken().getProject();
				projectName = proj.getName();
				if ("default".equalsIgnoreCase(proj.getDomainId())) {
					domainName = "default-domain";
				} else {
					domainName = proj.getDomain().getName();
				}
				return true;
			} catch (final AuthenticationException authe) {
				LOG.warn("authenticate to keystone {} failed: {}", authe, authUrl);
			}
		}
		// authenticate type unknown
		return false;
	}

	private ClassicHttpResponse execute(final String method, final String uri, final StringEntity entity) throws IOException {
		return executeDoauth(method, uri, entity, MAX_RETRIES);
	}

	private ClassicHttpResponse executeDoauth(final String method, final String uri, final StringEntity entity,
			final int retryCount) throws IOException {
		int rc = retryCount;
		checkConnection();
		if ((authToken == null) && (authType != null)) {
			authenticate();
		}
		final BasicClassicHttpRequest request = new BasicClassicHttpRequest(method, uri);
		if (entity != null) {
			request.setEntity(entity);
			try {
				LOG.debug(">> Request: {}, {}, {}", method, request.getRequestUri(), EntityUtils.toString(entity));
			} catch (ParseException | IOException e) {
				LOG.error("", e);
			}
		} else {
			LOG.debug(">> Request: {}, {}", method, request.getRequestUri());
		}
		// request.setParams(_params);
		if (authToken != null) {
			request.setHeader("X-AUTH-TOKEN", authToken);
		}
		CloseableHttpResponse response;
		try {
			// Do not close it.
			final CloseableHttpClient httpclient = createClient();
			response = httpclient.execute(httpHost, request);
			// response.setParams(_params);
			httpExecutor.postProcess(response, httpProc, httpContext);
		} catch (final Exception e) {
			if (rc == 0) {
				LOG.error("<< Received exception from the Api server, max retries exhausted: ", e);
				return null;
			}
			LOG.info("<< Api server connection timed out, retrying {} more times", rc);
			rc--;
			return executeDoauth(method, uri, entity, rc);
		}

		LOG.debug("<< Response Status: {}", response.getCode());
		if (((response.getCode() == HttpStatus.SC_UNAUTHORIZED)
				&& (rc > 0)) && authenticate()) {
			getResponseData(response);
			checkResponseKeepAliveStatus(response);
			LOG.error("<< Received \"unauthorized response from the Api server, retrying {} more times after authentication", rc);
			rc--;
			return executeDoauth(method, uri, entity, rc);
		}

		return response;
	}

	private static CloseableHttpClient createClient() {
		final HttpClientBuilder cli = HttpClients.custom();
		final SSLContext sslContext = createSslContext();
		final ConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HttpClientHostnameVerifier() {
			@Override
			public boolean verify(final String hostname, final SSLSession session) {
				return true;
			}

			@Override
			public void verify(final String host, final X509Certificate cert) throws SSLException {
				//
			}
		});
		final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf)
				.register("http", new PlainConnectionSocketFactory())
				.build();
		final BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
		cli.setConnectionManager(connectionManager);
		return cli.build();
	}

	private static SSLContext createSslContext() {
		try {
			return SSLContexts.custom().loadTrustMaterial(null,
					(chain, authType) -> true)
					.build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			throw new ContrailException(e);
		}
	}

	private static String getResponseData(final ClassicHttpResponse response) {
		final HttpEntity entity = response.getEntity();
		if (entity == null) {
			return null;
		}
		try (InputStream is = entity.getContent()) {
			return new String(is.readAllBytes());
		} catch (final Exception ex) {
			LOG.warn("Unable to read http response", ex);
			return null;
		}
	}

	private static void updateObject(final ApiObjectBase obj, final ApiObjectBase resp) {
		Class<?> cls = obj.getClass();

		// getDeclaredFields doesn't return fields from parent class (ApiObjectBase)
		// go up the hierarchy until ApiObjectBase inclusively
		do {
			updateFields(obj, resp, cls);
			cls = cls.getSuperclass();
		} while (cls != Object.class);
	}

	private static void updateFields(final ApiObjectBase obj, final ApiObjectBase resp, final Class<?> cls) {
		for (final Field f : cls.getDeclaredFields()) {
			f.setAccessible(true);
			final Object nv;
			try {
				nv = f.get(resp);
			} catch (final Exception ex) {
				LOG.warn("Unable to read new value for {}: {}", f.getName(), ex.getMessage());
				continue;
			}
			final Object value;
			try {
				value = f.get(obj);
			} catch (final Exception ex) {
				LOG.warn("Unable to read current value for {}: {}", f.getName(), ex.getMessage());
				continue;
			}
			if ((value == null) && (nv != null)) {
				try {
					f.set(obj, nv);
				} catch (final Exception ex) {
					LOG.warn("Unable to set {}: {}", f.getName(), ex.getMessage());
				}
			}
		}
	}

	private static Status noResponseStatus() {
		return Status.failure("No response from API server.");
	}

	@Override
	public synchronized Status commitDrafts(final ApiObjectBase obj) throws IOException {
		return draftOperation("commit", obj.getUuid());
	}

	@Override
	public synchronized Status discardDrafts(final ApiObjectBase obj) throws IOException {
		return draftOperation("discard", obj.getUuid());
	}

	private Status draftOperation(final String action, final String scopeUuid) throws IOException {
		final String jsdata = buildDraftActionJson(action, scopeUuid);

		final ClassicHttpResponse response = execute(HttpPost.METHOD_NAME, "/security-policy-draft",
				new StringEntity(jsdata, ContentType.APPLICATION_JSON));

		if ((response == null)) {
			return noResponseStatus();
		}

		final int status = response.getCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_ACCEPTED)) {
			final String reason = response.getReasonPhrase();
			LOG.warn("<< Response: {}", reason);
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		EntityUtils.consumeQuietly(response.getEntity());
		checkResponseKeepAliveStatus(response);
		return Status.success();
	}

	private static String buildDraftActionJson(final String action, final String scopeUuid) {
		final JsonObject jsDict = new JsonObject();
		jsDict.addProperty("scope_uuid", scopeUuid);
		jsDict.addProperty("action", action);
		return jsDict.toString();
	}

	@Override
	public synchronized Status create(final ApiObjectBase obj) throws IOException {
		if ((authToken == null) && (authType != null)) {
			authenticate();
		}
		final String typename = ApiBuilder.getTypename(obj.getClass());
		final String jsdata = ApiSerializer.serializeObject(typename, obj, projectName, domainName);

		ClassicHttpResponse response;
		if (obj instanceof VRouterApiObjectBase) {
			response = execute(HttpPost.METHOD_NAME, "/" + typename,
					new StringEntity(jsdata, ContentType.APPLICATION_JSON));
		} else {
			obj.updateQualifiedName(projectName, domainName);
			response = execute(HttpPost.METHOD_NAME, "/" + typename + "s",
					new StringEntity(jsdata, ContentType.APPLICATION_JSON));
		}

		if ((response == null)) {
			return noResponseStatus();
		}
		final int status = response.getCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_CREATED)
				&& (status != HttpStatus.SC_ACCEPTED)) {

			final String reason = response.getReasonPhrase();
			LOG.error("create api request failed: {}", reason);
			final String res = getResponseData(response);
			LOG.error(FAILURE_MESSAGE, res);
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason + " => " + res);
		}
		LOG.info("Contrail status: {}", status);
		final ApiObjectBase resp = ApiBuilder.jsonToApiObject(getResponseData(response), obj.getClass());
		if (resp == null) {
			final String reason = "Unable to decode Create response (response is null for class: " + obj.getClass() + ")";
			LOG.error(reason);
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		final String uuid = obj.getUuid();
		if (uuid == null) {
			obj.setUuid(resp.getUuid());
		} else if (!uuid.equals(resp.getUuid())
				&& !(obj instanceof VRouterApiObjectBase)) {
			LOG.warn("Response contains unexpected uuid: {}", resp.getUuid());
			checkResponseKeepAliveStatus(response);
			return Status.success();
		}
		LOG.debug("Create {} uuid: {}", typename, obj.getUuid());
		checkResponseKeepAliveStatus(response);
		return Status.success();
	}

	@Override
	public synchronized Status update(final ApiObjectBase obj) throws IOException {
		if ((authToken == null) && (authType != null)) {
			authenticate();
		}
		final String typename = ApiBuilder.getTypename(obj.getClass());
		final String jsdata = ApiSerializer.serializeObject(typename, obj, projectName, domainName);
		final ClassicHttpResponse response = execute(HttpPut.METHOD_NAME, "/" + typename + '/' + obj.getUuid(),
				new StringEntity(jsdata, ContentType.APPLICATION_JSON));

		if ((response == null)) {
			return noResponseStatus();
		}

		final int status = response.getCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_ACCEPTED)) {
			final String reason = response.getReasonPhrase();
			LOG.warn("<< Response: {}", reason);
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		EntityUtils.consumeQuietly(response.getEntity());
		checkResponseKeepAliveStatus(response);
		return Status.success();
	}

	@Override
	public synchronized Status read(final ApiObjectBase obj) throws IOException {
		final String typename = ApiBuilder.getTypename(obj.getClass());
		final ClassicHttpResponse response = execute(HttpGet.METHOD_NAME,
				"/" + typename + '/' + obj.getUuid(), null);

		if ((response == null)) {
			return noResponseStatus();
		}

		final int status = response.getCode();
		if (status != HttpStatus.SC_OK) {
			final String reason = response.getReasonPhrase();
			LOG.warn("GET failed: {}", reason);
			if (status != HttpStatus.SC_NOT_FOUND) {
				LOG.error(FAILURE_MESSAGE, getResponseData(response));
			}
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}
		LOG.debug("Response: {}", response);
		final ApiObjectBase resp = ApiBuilder.jsonToApiObject(getResponseData(response), obj.getClass());
		if (resp == null) {
			final String message = "Unable to decode GET response.";
			LOG.warn(message);
			checkResponseKeepAliveStatus(response);
			return Status.failure(message);
		}
		updateObject(obj, resp);
		checkResponseKeepAliveStatus(response);
		return Status.success();
	}

	@Override
	public Status delete(final ApiObjectBase obj) throws IOException {
		return delete(obj.getClass(), obj.getUuid());
	}

	@Override
	public synchronized Status delete(final Class<? extends ApiObjectBase> cls, final String uuid) throws IOException {
		if (findById(cls, uuid) == null) {
			// object does not exist so we are ok
			return Status.success();
		}

		final String typename = ApiBuilder.getTypename(cls);
		final ClassicHttpResponse response = execute(HttpDelete.METHOD_NAME, "/" + typename + '/' + uuid, null);

		if ((response == null)) {
			return Status.failure("No response from API server.");
		}

		final int status = response.getCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_NO_CONTENT)
				&& (status != HttpStatus.SC_ACCEPTED)) {
			final String reason = response.getReasonPhrase();
			LOG.warn("Delete failed: {}", reason);
			if (status != HttpStatus.SC_NOT_FOUND) {
				LOG.error(FAILURE_MESSAGE, getResponseData(response));
			}
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}
		EntityUtils.consumeQuietly(response.getEntity());
		checkResponseKeepAliveStatus(response);
		return Status.success();
	}

	@Override
	public synchronized ApiObjectBase find(final Class<? extends ApiObjectBase> cls, final ApiObjectBase parent, final String name) throws IOException {
		final String uuid = findByName(cls, parent, name);
		if (uuid == null) {
			return null;
		}
		return findById(cls, uuid);
	}

	@Override
	public ApiObjectBase findByFQN(final Class<? extends ApiObjectBase> cls, final String fullName) throws IOException {
		final List<String> fqn = Collections.unmodifiableList(Arrays.asList(fullName.split(":")));
		final String uuid = findByName(cls, fqn);
		if (uuid == null) {
			return null;
		}
		return findById(cls, uuid);
	}

	@Override
	public synchronized ApiObjectBase findById(final Class<? extends ApiObjectBase> cls, final String uuid) throws IOException {
		final String typename = ApiBuilder.getTypename(cls);
		final ClassicHttpResponse response = execute(HttpGet.METHOD_NAME,
				'/' + typename + '/' + uuid, null);

		if ((response == null)) {
			return null;
		}

		if (response.getCode() != HttpStatus.SC_OK) {
			EntityUtils.consumeQuietly(response.getEntity());
			checkResponseKeepAliveStatus(response);
			return null;
		}
		final ApiObjectBase object = ApiBuilder.jsonToApiObject(getResponseData(response), cls);
		if (object == null) {
			LOG.warn("Unable to decode find response");
		}

		checkResponseKeepAliveStatus(response);
		return object;
	}

	@Override
	public String findByName(final Class<? extends ApiObjectBase> cls, final ApiObjectBase parent, final String name) throws IOException {
		final List<String> nameList = new ArrayList<>();
		if (parent != null) {
			nameList.addAll(parent.getQualifiedName());
		} else {
			try {
				nameList.addAll(cls.newInstance().getDefaultParent());
			} catch (final Exception ex) {
				// Instantiation or IllegalAccess
				LOG.error("Failed to instantiate object of class {}", cls.getName(), ex);
				return null;
			}
		}
		nameList.add(name);
		return findByName(cls, nameList);
	}

	@Override
	// POST http://hostname:port/fqname-to-id
	// body: {"type": class, "fq_name": [parent..., name]}
	public synchronized String findByName(final Class<? extends ApiObjectBase> cls, final List<String> name_list) throws IOException {
		final String jsonStr = ApiBuilder.buildFqnJsonString(cls, name_list);
		final ClassicHttpResponse response = execute(HttpPost.METHOD_NAME, "/fqname-to-id",
				new StringEntity(jsonStr, ContentType.APPLICATION_JSON));

		if ((response == null)) {
			return null;
		}

		if (response.getCode() != HttpStatus.SC_OK) {
			EntityUtils.consumeQuietly(response.getEntity());
			checkResponseKeepAliveStatus(response);
			return null;
		}

		final String data = getResponseData(response);
		if (data == null) {
			checkResponseKeepAliveStatus(response);
			return null;
		}
		LOG.debug("<< Response Data: {}", data);

		final String uuid = ApiBuilder.getUuid(data);
		if (uuid == null) {
			LOG.warn("Unable to parse response");
			checkResponseKeepAliveStatus(response);
			return null;
		}
		checkResponseKeepAliveStatus(response);
		return uuid;
	}

	@Override
	public synchronized List<? extends ApiObjectBase> list(final Class<? extends ApiObjectBase> cls, final List<String> parent) throws IOException {
		final String typename = ApiBuilder.getTypename(cls);
		final ClassicHttpResponse response = execute(HttpGet.METHOD_NAME, '/' + typename + 's', null);

		if ((response == null)) {
			return null;
		}

		if (response.getCode() != HttpStatus.SC_OK) {
			LOG.warn("list failed with : {}", response.getReasonPhrase());
			EntityUtils.consumeQuietly(response.getEntity());
			checkResponseKeepAliveStatus(response);
			return null;
		}

		final String data = getResponseData(response);
		if (data == null) {
			checkResponseKeepAliveStatus(response);
			return null;
		}
		final List<? extends ApiObjectBase> list = ApiBuilder.jsonToApiObjects(data, cls, parent);
		if (list == null) {
			LOG.warn("Unable to parse/deserialize response: {}", data);
		}
		checkResponseKeepAliveStatus(response);
		return list;
	}

	@Override
	public <T extends ApiPropertyBase> List<? extends ApiObjectBase> getObjects(final Class<? extends ApiObjectBase> cls, final List<ObjectReference<T>> refList) throws IOException {

		final List<ApiObjectBase> list = new ArrayList<>();
		for (final ObjectReference<T> ref : refList) {
			final ApiObjectBase obj = findById(cls, ref.getUuid());
			if (obj == null) {
				LOG.warn("Unable to find element with uuid: {}", ref.getUuid());
				continue;
			}
			list.add(obj);
		}
		return list;
	}

	@Override
	public Status sync(final String uri) throws IOException {

		final String jsdata = "{\"type\":" + clientId + "}";
		final ClassicHttpResponse response = execute(HttpPost.METHOD_NAME, uri, new StringEntity(jsdata, ContentType.APPLICATION_JSON));
		if ((response == null)) {
			return noResponseStatus();
		}

		final int status = response.getCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_CREATED)
				&& (status != HttpStatus.SC_ACCEPTED)
				&& (status != HttpStatus.SC_NO_CONTENT)) {
			final String reason = response.getReasonPhrase();
			LOG.error("sync request failed: {}", reason);
			if (status != HttpStatus.SC_NOT_FOUND) {
				LOG.error(FAILURE_MESSAGE, getResponseData(response));
			}
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		return Status.success();
	}

	@Override
	public void dispose() {
		try {
			if (connection.isOpen()) {
				connection.close(); // close server connection
			}
		} catch (final IOException ex) {
			LOG.warn("Exception while closing server connection: {}", ex.getMessage());
		}
	}
}
