/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestDate;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.client.IOSClientBuilder.V3;
import org.openstack4j.api.exceptions.AuthenticationException;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;

@SuppressWarnings("deprecation")
class ApiConnectorImpl implements ApiConnector {
	private static final Logger s_logger = LoggerFactory.getLogger(ApiConnectorImpl.class);

	private final String _api_hostname;
	private final int _api_port;
	private final ApiBuilder _apiBuilder;

	private String _username;
	private String _password;
	private String _tenant;
	private boolean _has_input_authtoken;
	private String _authtoken;
	private String _authtype;
	private String _authurl;

	// HTTP Connection parameters
	private HttpParams _params;
	private HttpProcessor _httpproc;
	private HttpRequestExecutor _httpexecutor;
	private HttpContext _httpcontext;
	private HttpHost _httphost;
	private DefaultHttpClientConnection _connection;
	private ConnectionReuseStrategy _connectionStrategy;
	public final static int MAX_RETRIES = 5;
	public final int clientId = 2;

	public ApiConnectorImpl(final String hostname, final int port) {
		_api_hostname = hostname;
		_api_port = port;
		_has_input_authtoken = true;
		initHttpClient();
		initHttpServerParams(hostname, port);
		_apiBuilder = new ApiBuilder();
	}

	private void initHttpClient() {
		_params = new SyncBasicHttpParams();
		HttpProtocolParams.setVersion(_params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(_params, "UTF-8");
		HttpProtocolParams.setUseExpectContinue(_params, false);
		HttpProtocolParams.setHttpElementCharset(_params, "UTF-8");

		_httpproc = new ImmutableHttpProcessor(
				// Required protocol interceptors
				new BasicHttpProcessor(),
				new RequestConnControl(),
				new RequestContent(),
				new RequestDate(),
				new RequestTargetHost(),
				// Recommended protocol interceptors
				new RequestUserAgent(),
				new RequestExpectContinue());
		_httpexecutor = new HttpRequestExecutor();
		_httpcontext = new BasicHttpContext(null);
		_connection = new DefaultHttpClientConnection();
		_connectionStrategy = new DefaultConnectionReuseStrategy();
	}

	private void initHttpServerParams(final String hostname, final int port) {

		_httphost = new HttpHost(hostname, port, "https");
		_httpcontext.setAttribute(ExecutionContext.HTTP_CONNECTION, _connection);
		_httpcontext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, _httphost);

	}

	@Override
	public ApiConnector credentials(final String username, final String password) {
		_username = username;
		_password = password;
		return this;
	}

	@Override
	public ApiConnector tenantName(final String tenant) {
		_tenant = tenant;
		return this;
	}

	@Override
	public ApiConnector authToken(final String token) {
		_has_input_authtoken = true;
		_authtoken = token;
		return this;
	}

	@Override
	public ApiConnector authServer(final String type, final String url) {
		_has_input_authtoken = false;
		_authtype = type;
		_authurl = url;
		return this;
	}

	private void checkResponseKeepAliveStatus(final HttpResponse response) throws IOException {

		if (!_connectionStrategy.keepAlive(response, _httpcontext)) {
			_connection.close();
		}
	}

	private void checkConnection() throws IOException {
		if (!_connection.isOpen()) {
			s_logger.info("http connection <" + _httphost.getHostName() + ", " +
					_httphost.getPort() + "> does not exit");
			final Socket socket = new Socket(_httphost.getHostName(), _httphost.getPort());
			_connection.bind(socket, _params);
			s_logger.info("http connection <" + _httphost.getHostName() + ", " +
					_httphost.getPort() + "> established");
		}
	}

	@Override
	protected void finalize() {
		dispose();
	}

	public String getHostName() {
		return _api_hostname;
	}

	public int getPort() {
		return _api_port;
	}

	// return value indicates whether the token changed
	private boolean authenticate() {
		if (_has_input_authtoken) {
			return false;
		}

		_authtoken = null;
		if ("keystone".equals(_authtype)) {
			try {
				final OSClientV2 os = OSFactory.builderV2().endpoint(_authurl)
						.credentials(_username, _password).tenantName(_tenant).authenticate();
				_authtoken = os.getAccess().getToken().getId();
				return true;
			} catch (final AuthenticationException authe) {
				s_logger.warn("authenticate to keystone {} failed: {}", authe, _authurl);
			}
		} else if ("keystonev3".equals(_authtype)) {
			try {
				final V3 base = OSFactory.builderV3().endpoint(_authurl);
				if (_tenant != null) {
					final Identifier domainIdentifier = Identifier.byName(_tenant);
					base.credentials(_username, _password, domainIdentifier);
				} else {
					base.credentials(_username, _password);
				}
				final OSClientV3 os = base.authenticate();
				_authtoken = os.getToken().getId();
				return true;
			} catch (final AuthenticationException authe) {
				s_logger.warn("authenticate to keystone {} failed: {}", authe, _authurl);
			}
		}

		// authenticate type unknown
		return false;
	}

	private HttpResponse execute(final String method, final String uri, final StringEntity entity) throws IOException {
		return execute_doauth(method, uri, entity, MAX_RETRIES);
	}

	private HttpResponse execute_doauth(final String method, final String uri, final StringEntity entity,
			int retry_count) throws IOException {

		checkConnection();

		final BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest(method, uri);
		if (entity != null) {
			request.setEntity(entity);
			s_logger.debug(">> Request: " + method + ", " + request.getRequestLine().getUri() +
					", " + EntityUtils.toString(entity));
		} else {
			s_logger.debug(">> Request: " + method + ", " + request.getRequestLine().getUri());
		}
		HttpResponse response = null;
		request.setParams(_params);
		if (_authtoken != null) {
			request.setHeader("X-AUTH-TOKEN", _authtoken);
		}
		try {
			_httpexecutor.preProcess(request, _httpproc, _httpcontext);
			response = _httpexecutor.execute(request, _connection, _httpcontext);
			response.setParams(_params);
			_httpexecutor.postProcess(response, _httpproc, _httpcontext);
		} catch (final Exception e) {
			if (retry_count == 0) {
				s_logger.error("<< Received exception from the Api server, max retries exhausted: " + e);
				s_logger.error(Throwables.getStackTraceAsString(e));
				return null;
			}
			s_logger.info("<< Api server connection timed out, retrying " + retry_count + " more times");
			return execute_doauth(method, uri, entity, --retry_count);
		}

		s_logger.debug("<< Response Status: " + response.getStatusLine());
		if (((response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED)
				&& (retry_count > 0)) && authenticate()) {
			getResponseData(response);
			checkResponseKeepAliveStatus(response);
			s_logger.error("<< Received \"unauthorized response from the Api server, retrying "
					+ retry_count + " more times after authentication");
			return execute_doauth(method, uri, entity, --retry_count);
		}

		return response;
	}

	private String getResponseData(final HttpResponse response) {
		final HttpEntity entity = response.getEntity();
		if (entity == null) {
			return null;
		}
		String data;
		try {
			data = EntityUtils.toString(entity);
			EntityUtils.consumeQuietly(entity);
		} catch (final Exception ex) {
			s_logger.warn("Unable to read http response", ex);
			return null;
		}
		return data;
	}

	private void updateObject(final ApiObjectBase obj, final ApiObjectBase resp) {
		Class<?> cls = obj.getClass();

		// getDeclaredFields doesn't return fields from parent class (ApiObjectBase)
		// go up the hierarchy until ApiObjectBase inclusively
		do {
			updateFields(obj, resp, cls);
			cls = cls.getSuperclass();
		} while (cls != Object.class);
	}

	private void updateFields(final ApiObjectBase obj, final ApiObjectBase resp, final Class<?> cls) {
		for (final Field f : cls.getDeclaredFields()) {
			f.setAccessible(true);
			final Object nv;
			try {
				nv = f.get(resp);
			} catch (final Exception ex) {
				s_logger.warn("Unable to read new value for " + f.getName() + ": " + ex.getMessage());
				continue;
			}
			final Object value;
			try {
				value = f.get(obj);
			} catch (final Exception ex) {
				s_logger.warn("Unable to read current value of " + f.getName() + ": " + ex.getMessage());
				continue;
			}
			if ((value == null) && (nv != null)) {
				try {
					f.set(obj, nv);
				} catch (final Exception ex) {
					s_logger.warn("Unable to set " + f.getName() + ": " + ex.getMessage());
				}
			}
		}
	}

	private Status noResponseStatus() {
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

		final HttpResponse response = execute(HttpPost.METHOD_NAME, "/security-policy-draft",
				new StringEntity(jsdata, ContentType.APPLICATION_JSON));

		if ((response == null) || (response.getStatusLine() == null)) {
			return noResponseStatus();
		}

		final int status = response.getStatusLine().getStatusCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_ACCEPTED)) {
			final String reason = response.getStatusLine().getReasonPhrase();
			s_logger.warn("<< Response:" + reason);
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		EntityUtils.consumeQuietly(response.getEntity());
		checkResponseKeepAliveStatus(response);
		return Status.success();
	}

	private String buildDraftActionJson(final String action, final String scopeUuid) {
		final JsonObject jsDict = new JsonObject();
		jsDict.addProperty("scope_uuid", scopeUuid);
		jsDict.addProperty("action", action);
		return jsDict.toString();
	}

	@Override
	public synchronized Status create(final ApiObjectBase obj) throws IOException {
		final String typename = _apiBuilder.getTypename(obj.getClass());
		final String jsdata = ApiSerializer.serializeObject(typename, obj);

		HttpResponse response;
		if (obj instanceof VRouterApiObjectBase) {
			response = execute(HttpPost.METHOD_NAME, "/" + typename,
					new StringEntity(jsdata, ContentType.APPLICATION_JSON));
		} else {
			obj.updateQualifiedName();
			response = execute(HttpPost.METHOD_NAME, "/" + typename + "s",
					new StringEntity(jsdata, ContentType.APPLICATION_JSON));
		}

		if ((response == null) || (response.getStatusLine() == null)) {
			return noResponseStatus();
		}
		final int status = response.getStatusLine().getStatusCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_CREATED)
				&& (status != HttpStatus.SC_ACCEPTED)) {

			final String reason = response.getStatusLine().getReasonPhrase();
			s_logger.error("create api request failed: " + reason);
			if (status != HttpStatus.SC_NOT_FOUND) {
				s_logger.error("Failure message: " + getResponseData(response));
			}
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		final ApiObjectBase resp = _apiBuilder.jsonToApiObject(getResponseData(response), obj.getClass());
		if (resp == null) {
			final String reason = "Unable to decode Create response";
			s_logger.error(reason);
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		final String uuid = obj.getUuid();
		if (uuid == null) {
			obj.setUuid(resp.getUuid());
		} else if (!uuid.equals(resp.getUuid())
				&& !(obj instanceof VRouterApiObjectBase)) {
			s_logger.warn("Response contains unexpected uuid: " + resp.getUuid());
			checkResponseKeepAliveStatus(response);
			return Status.success();
		}
		s_logger.debug("Create " + typename + " uuid: " + obj.getUuid());
		checkResponseKeepAliveStatus(response);
		return Status.success();
	}

	@Override
	public synchronized Status update(final ApiObjectBase obj) throws IOException {
		final String typename = _apiBuilder.getTypename(obj.getClass());
		final String jsdata = ApiSerializer.serializeObject(typename, obj);
		final HttpResponse response = execute(HttpPut.METHOD_NAME, "/" + typename + '/' + obj.getUuid(),
				new StringEntity(jsdata, ContentType.APPLICATION_JSON));

		if ((response == null) || (response.getStatusLine() == null)) {
			return noResponseStatus();
		}

		final int status = response.getStatusLine().getStatusCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_ACCEPTED)) {
			final String reason = response.getStatusLine().getReasonPhrase();
			s_logger.warn("<< Response:" + reason);
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		EntityUtils.consumeQuietly(response.getEntity());
		checkResponseKeepAliveStatus(response);
		return Status.success();
	}

	@Override
	public synchronized Status read(final ApiObjectBase obj) throws IOException {
		final String typename = _apiBuilder.getTypename(obj.getClass());
		final HttpResponse response = execute(HttpGet.METHOD_NAME,
				"/" + typename + '/' + obj.getUuid(), null);

		if ((response == null) || (response.getStatusLine() == null)) {
			return noResponseStatus();
		}

		final int status = response.getStatusLine().getStatusCode();
		if (status != HttpStatus.SC_OK) {
			final String reason = response.getStatusLine().getReasonPhrase();
			s_logger.warn("GET failed: " + reason);
			if (status != HttpStatus.SC_NOT_FOUND) {
				s_logger.error("Failure message: " + getResponseData(response));
			}
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}
		s_logger.debug("Response: " + response);
		final ApiObjectBase resp = _apiBuilder.jsonToApiObject(getResponseData(response), obj.getClass());
		if (resp == null) {
			final String message = "Unable to decode GET response.";
			s_logger.warn(message);
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

		final String typename = _apiBuilder.getTypename(cls);
		final HttpResponse response = execute(HttpDelete.METHOD_NAME,
				"/" + typename + '/' + uuid, null);

		if ((response == null) || (response.getStatusLine() == null)) {
			return Status.failure("No response from API server.");
		}

		final int status = response.getStatusLine().getStatusCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_NO_CONTENT)
				&& (status != HttpStatus.SC_ACCEPTED)) {
			final String reason = response.getStatusLine().getReasonPhrase();
			s_logger.warn("Delete failed: " + reason);
			if (status != HttpStatus.SC_NOT_FOUND) {
				s_logger.error("Failure message: " + getResponseData(response));
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
		final List<String> fqn = ImmutableList.copyOf(StringUtils.split(fullName, ':'));
		final String uuid = findByName(cls, fqn);
		if (uuid == null) {
			return null;
		}
		return findById(cls, uuid);
	}

	@Override
	public synchronized ApiObjectBase findById(final Class<? extends ApiObjectBase> cls, final String uuid) throws IOException {
		final String typename = _apiBuilder.getTypename(cls);
		final HttpResponse response = execute(HttpGet.METHOD_NAME,
				'/' + typename + '/' + uuid, null);

		if ((response == null) || (response.getStatusLine() == null)) {
			return null;
		}

		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			EntityUtils.consumeQuietly(response.getEntity());
			checkResponseKeepAliveStatus(response);
			return null;
		}
		final ApiObjectBase object = _apiBuilder.jsonToApiObject(getResponseData(response), cls);
		if (object == null) {
			s_logger.warn("Unable to decode find response");
		}

		checkResponseKeepAliveStatus(response);
		return object;
	}

	@Override
	public String findByName(final Class<? extends ApiObjectBase> cls, final ApiObjectBase parent, final String name) throws IOException {
		final List<String> name_list = new ArrayList<String>();
		if (parent != null) {
			name_list.addAll(parent.getQualifiedName());
		} else {
			try {
				name_list.addAll(cls.newInstance().getDefaultParent());
			} catch (final Exception ex) {
				// Instantiation or IllegalAccess
				s_logger.error("Failed to instantiate object of class " + cls.getName(), ex);
				return null;
			}
		}
		name_list.add(name);
		return findByName(cls, name_list);
	}

	@Override
	// POST http://hostname:port/fqname-to-id
	// body: {"type": class, "fq_name": [parent..., name]}
	public synchronized String findByName(final Class<? extends ApiObjectBase> cls, final List<String> name_list) throws IOException {
		final String jsonStr = _apiBuilder.buildFqnJsonString(cls, name_list);
		final HttpResponse response = execute(HttpPost.METHOD_NAME, "/fqname-to-id",
				new StringEntity(jsonStr, ContentType.APPLICATION_JSON));

		if ((response == null) || (response.getStatusLine() == null)) {
			return null;
		}

		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			EntityUtils.consumeQuietly(response.getEntity());
			checkResponseKeepAliveStatus(response);
			return null;
		}

		final String data = getResponseData(response);
		if (data == null) {
			checkResponseKeepAliveStatus(response);
			return null;
		}
		s_logger.debug("<< Response Data: " + data);

		final String uuid = _apiBuilder.getUuid(data);
		if (uuid == null) {
			s_logger.warn("Unable to parse response");
			checkResponseKeepAliveStatus(response);
			return null;
		}
		checkResponseKeepAliveStatus(response);
		return uuid;
	}

	@Override
	public synchronized List<? extends ApiObjectBase> list(final Class<? extends ApiObjectBase> cls, final List<String> parent) throws IOException {
		final String typename = _apiBuilder.getTypename(cls);
		final HttpResponse response = execute(HttpGet.METHOD_NAME, '/' + typename + 's', null);

		if ((response == null) || (response.getStatusLine() == null)) {
			return null;
		}

		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			s_logger.warn("list failed with :" + response.getStatusLine().getReasonPhrase());
			EntityUtils.consumeQuietly(response.getEntity());
			checkResponseKeepAliveStatus(response);
			return null;
		}

		final String data = getResponseData(response);
		if (data == null) {
			checkResponseKeepAliveStatus(response);
			return null;
		}
		final List<? extends ApiObjectBase> list = _apiBuilder.jsonToApiObjects(data, cls, parent);
		if (list == null) {
			s_logger.warn("Unable to parse/deserialize response: " + data);
		}
		checkResponseKeepAliveStatus(response);
		return list;
	}

	@Override
	public <T extends ApiPropertyBase> List<? extends ApiObjectBase> getObjects(final Class<? extends ApiObjectBase> cls, final List<ObjectReference<T>> refList) throws IOException {

		final List<ApiObjectBase> list = new ArrayList<ApiObjectBase>();
		for (final ObjectReference<T> ref : refList) {
			final ApiObjectBase obj = findById(cls, ref.getUuid());
			if (obj == null) {
				s_logger.warn("Unable to find element with uuid: " + ref.getUuid());
				continue;
			}
			list.add(obj);
		}
		return list;
	}

	@Override
	public Status sync(final String uri) throws IOException {
		HttpResponse response;
		final String jsdata = "{\"type\":" + clientId + "}";
		response = execute(HttpPost.METHOD_NAME, uri,
				new StringEntity(jsdata, ContentType.APPLICATION_JSON));

		if ((response == null) || (response.getStatusLine() == null)) {
			return noResponseStatus();
		}

		final int status = response.getStatusLine().getStatusCode();
		if ((status != HttpStatus.SC_OK)
				&& (status != HttpStatus.SC_CREATED)
				&& (status != HttpStatus.SC_ACCEPTED)
				&& (status != HttpStatus.SC_NO_CONTENT)) {
			final String reason = response.getStatusLine().getReasonPhrase();
			s_logger.error("sync request failed: " + reason);
			if (status != HttpStatus.SC_NOT_FOUND) {
				s_logger.error("Failure message: " + getResponseData(response));
			}
			checkResponseKeepAliveStatus(response);
			return Status.failure(reason);
		}

		return Status.success();
	}

	@Override
	public void dispose() {
		try {
			if (_connection.isOpen()) {
				_connection.close(); // close server connection
			}
		} catch (final IOException ex) {
			s_logger.warn("Exception while closing server connection: " + ex.getMessage());
		}
	}
}
