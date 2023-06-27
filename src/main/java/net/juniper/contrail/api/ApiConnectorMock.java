/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.InetAddresses;

import net.juniper.contrail.api.types.Domain;
import net.juniper.contrail.api.types.FloatingIp;
import net.juniper.contrail.api.types.FloatingIpPool;
import net.juniper.contrail.api.types.InstanceIp;
import net.juniper.contrail.api.types.MacAddressesType;
import net.juniper.contrail.api.types.NetworkPolicy;
import net.juniper.contrail.api.types.Project;
import net.juniper.contrail.api.types.SecurityGroup;
import net.juniper.contrail.api.types.VirtualMachine;
import net.juniper.contrail.api.types.VirtualMachineInterface;
import net.juniper.contrail.api.types.VirtualNetwork;

public class ApiConnectorMock implements ApiConnector {
	private static final Logger s_logger = LoggerFactory.getLogger(ApiConnectorMock.class);

	private final ApiBuilder _apiBuilder;
	private HashMap<String, List<HashMap<String, ApiObjectBase>>> _map;
	private static final HashMap<Class<? extends ApiObjectBase>, Class<? extends ApiObjectBase>> _parentMap;
	static {
		final HashMap<Class<? extends ApiObjectBase>, Class<? extends ApiObjectBase>> parentMap = new HashMap<>();
		parentMap.put(Domain.class, Domain.class);
		parentMap.put(Project.class, Domain.class);
		parentMap.put(VirtualNetwork.class, Project.class);
		parentMap.put(VirtualMachineInterface.class, VirtualMachine.class);
		parentMap.put(NetworkPolicy.class, Project.class);
		parentMap.put(SecurityGroup.class, Project.class);
		parentMap.put(FloatingIp.class, FloatingIpPool.class);
		parentMap.put(FloatingIpPool.class, VirtualNetwork.class);
		_parentMap = parentMap;
	}

	private static String randomMacAddress() {
		final Random r = new Random();
		return IntStream.range(0, 12)
				.map(x -> r.nextInt(10))
				.mapToObj(x -> String.valueOf((char) ('0' + x)))
				.collect(Collectors.joining(":"));
	}

	private static void assignAutoProperty(final ApiObjectBase obj) {
		if (obj.getClass() == VirtualMachineInterface.class) {
			if (((VirtualMachineInterface) obj).getMacAddresses() != null) {
				return;
			}
			final String addr = randomMacAddress();

			final MacAddressesType macs = new MacAddressesType();
			macs.addMacAddress(addr);
			s_logger.debug("Assigned auto property mac address : " + addr);
			((VirtualMachineInterface) obj).setMacAddresses(macs);
		} else if (obj.getClass() == InstanceIp.class) {
			if (((InstanceIp) obj).getAddress() != null) {
				return;
			}
			final Random random = new Random();
			final String ipString = InetAddresses.fromInteger(random.nextInt()).getHostAddress();
			s_logger.debug("Assigned auto property ip address : " + ipString);
			((InstanceIp) obj).setAddress(ipString);
		}
	}

	private static Class<? extends ApiObjectBase> getVncClass(final String clsname) {
		String typename = new String();
		for (int i = 0; i < clsname.length(); i++) {
			char ch = clsname.charAt(i);
			if (i == 0) {
				ch = Character.toUpperCase(ch);
			} else if (ch == '_') {
				i++;
				ch = clsname.charAt(i);
				ch = Character.toUpperCase(ch);
			}
			typename += ch;
		}
		try {
			return (Class<? extends ApiObjectBase>) Class.forName("net.juniper.contrail.api.types." + typename);
		} catch (final Exception e) {
			s_logger.debug("Class not found <net.juniper.contrail.api.types." + typename + ">" + e);
		}
		return null;
	}

	private static HashMap<Class<? extends ApiObjectBase>, ApiObjectBase> _defaultObjectMap;

	ApiConnectorMock() {
		_apiBuilder = new ApiBuilder();
		initConfig();
	}

	public ApiConnectorMock(final String hostname, final int port) {
		this();
	}

	public void initConfig() {
		_map = new HashMap<>();
		buildDefaultConfig();
		buildDefaultObjectMap();
	}

	void buildDefaultConfig() {
		try {
			final InputStream fin = getClass().getResourceAsStream("/default_config");
			final ObjectInputStream ois = new ObjectInputStream(fin);
			final HashMap<Class<? extends ApiObjectBase>, List<HashMap<String, ApiObjectBase>>> defaultConfigMap = (HashMap<Class<? extends ApiObjectBase>, List<HashMap<String, ApiObjectBase>>>) ois.readObject();
			final Iterator it = defaultConfigMap.entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry pairs = (Map.Entry) it.next();
				final Class<? extends ApiObjectBase> cls = (Class<? extends ApiObjectBase>) pairs.getKey();
				s_logger.debug("buildDefaultConfig: " + _apiBuilder.getTypename(cls));
				_map.put(_apiBuilder.getTypename(cls), (List<HashMap<String, ApiObjectBase>>) pairs.getValue());
			}
		} catch (final Exception e) {
			s_logger.debug("buildDefaultConfig: " + e);
		}
	}

	void buildDefaultObjectMap() {
		_defaultObjectMap = new HashMap<>();
		try {
			_defaultObjectMap.put(Domain.class, findByFQN(Domain.class, "default-domain"));
			_defaultObjectMap.put(Project.class, findByFQN(Project.class, "default-domain:default-project"));
		} catch (final Exception e) {
			s_logger.debug("", e);
		}
	}

	List getClassData(final Class<?> cls) {
		final String typename = _apiBuilder.getTypename(cls);
		return _map.get(typename);
	}

	void setClassData(final Class<?> cls, final List clsData) {
		final String typename = _apiBuilder.getTypename(cls);
		_map.put(typename, clsData);
	}

	HashMap<String, ApiObjectBase> getFqnMap(final List clsData) {
		if (clsData != null) {
			return (HashMap<String, ApiObjectBase>) clsData.get(1);
		}
		return null;
	}

	HashMap<String, ApiObjectBase> getUuidMap(final List clsData) {
		if (clsData != null) {
			return (HashMap<String, ApiObjectBase>) clsData.get(0);
		}
		return null;
	}

	private String getFqnString(final List<String> name_list) {
		return name_list.stream().collect(Collectors.joining(":"));
	}

	private boolean validate(final ApiObjectBase obj) throws IOException {
		String uuid = obj.getUuid();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			obj.setUuid(uuid);
		}
		final String fqn = getFqnString(obj.getQualifiedName());
		final List clsData = getClassData(obj.getClass());
		HashMap<String, ApiObjectBase> uuidMap = null;
		HashMap<String, ApiObjectBase> fqnMap = null;
		if (clsData != null) {
			uuidMap = getUuidMap(clsData);
			fqnMap = getFqnMap(clsData);
			if ((uuidMap.get(uuid) != null) || (fqnMap.get(fqn) != null)) {
				s_logger.warn("api object: " + obj.getName() + " already exists");
				return false;
			}
		}
		// check for parent child references and backrefs
		ApiObjectBase parent = obj.getParent();
		if (parent == null) {
			parent = getDefaultParent(obj);
		}
		if (parent != null) {
			try {
				s_logger.debug("Verify establish parent(" + _apiBuilder.getTypename(parent.getClass()) + ", " + parent.getName()
						+ ") => child (" + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getName());
				/* update parent object with new child info */
				updateObjectVerify(parent, obj, getRefname(obj.getClass()) + "s");
				/* update child object back reference to its parent */
				s_logger.debug("Verify Establish child(" + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getName()
						+ ") => backref to parent(" + _apiBuilder.getTypename(parent.getClass()) + ", " + parent.getName() + ")");
			} catch (final Exception e) {
				s_logger.debug("Exception in updateObject : " + e);
				return false;
			}
		} else {
			s_logger.debug("no default parent for : " + obj.getName());
		}
		try {
			/* update object references it has with associated back refs */
			updateRefsVerify(obj);
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public ApiConnector credentials(final String username, final String password) {
		return this;
	}

	public ApiConnector tenantName(final String tenant) {
		return this;
	}

	@Override
	public ApiConnector authToken(final String token) {
		return this;
	}

	@Override
	public ApiConnector authServer(final String type, final String url) {
		return this;
	}

	@Override
	public synchronized Status create(final ApiObjectBase obj) throws IOException {
		s_logger.debug("create(cls, obj): " + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getName());
		if (!validate(obj)) {
			s_logger.error("can not create (cls, obj): " + _apiBuilder.getTypename(obj.getClass()) + ", "
					+ obj.getName() + ", validate failed");
			return Status.failure("Validation failed");
		}
		String uuid = obj.getUuid();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			obj.setUuid(uuid);
		}
		final String fqn = getFqnString(obj.getQualifiedName());
		List clsData = getClassData(obj.getClass());
		HashMap<String, ApiObjectBase> uuidMap = null;
		HashMap<String, ApiObjectBase> fqnMap = null;
		if (clsData == null) {
			clsData = new ArrayList<HashMap<String, ApiObjectBase>>();
			uuidMap = new HashMap<>();
			fqnMap = new HashMap<>();
			clsData.add(uuidMap);
			clsData.add(fqnMap);
			setClassData(obj.getClass(), clsData);
		} else {
			uuidMap = getUuidMap(clsData);
			fqnMap = getFqnMap(clsData);
		}
		if ((uuidMap.get(uuid) != null) || (fqnMap.get(fqn) != null)) {
			s_logger.warn("api object: " + obj.getName() + " already exists");
			return Status.failure("Object already exists");
		}
		uuidMap.put(uuid, obj);
		fqnMap.put(fqn, obj);

		// update parent child references and backrefs
		ApiObjectBase parent = obj.getParent();
		if (parent == null) {
			parent = getDefaultParent(obj);
		}
		if (parent != null) {
			try {
				s_logger.debug("Establish parent(" + _apiBuilder.getTypename(parent.getClass()) + ", " + parent.getName()
						+ ") => child (" + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getName() + ")");
				/* update parent object with new child info */
				updateObject(parent, obj, getRefname(obj.getClass()) + "s");
				obj.setParent(parent);
			} catch (final Exception e) {
				s_logger.debug("Exception in updateObject : " + e);
			}
		} else {
			s_logger.debug("no default parent for : " + obj.getName());
		}
		/* update object references it has with associated back refs */
		updateRefs(obj);
		/* assign auto property, if any */
		assignAutoProperty(obj);
		return Status.success();
	}

	ApiObjectBase getDefaultParent(final ApiObjectBase obj) {
		if ((obj.getClass() == Domain.class) || "default-domain".equals(obj.getName())) {
			return null;
		}
		final Class<? extends ApiObjectBase> parentCls = _parentMap.get(obj.getClass());
		return _defaultObjectMap.get(parentCls);
	}

	@Override
	public synchronized Status commitDrafts(final ApiObjectBase obj) throws IOException {
		s_logger.debug("commit drafts: " + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getUuid());
		return Status.success();
	}

	@Override
	public synchronized Status discardDrafts(final ApiObjectBase obj) throws IOException {
		s_logger.debug("discard drafts: " + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getUuid());
		return Status.success();
	}

	@Override
	public synchronized Status update(final ApiObjectBase obj) throws IOException {
		s_logger.debug("update(cls, obj): " + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getName());
		final String fqn = getFqnString(obj.getQualifiedName());
		if (fqn == null) {
			return Status.failure("Object does not have qualified name.");
		}
		final List clsData = getClassData(obj.getClass());
		if (clsData == null) {
			return Status.failure("No class data.");
		}
		final HashMap<String, ApiObjectBase> uuidMap = getUuidMap(clsData);
		final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);
		final ApiObjectBase old = fqnMap.get(fqn);
		final String uuid = old.getUuid();
		fqnMap.put(fqn, obj);
		uuidMap.put(uuid, obj);
		return Status.success();
	}

	@Override
	public synchronized Status read(ApiObjectBase obj) throws IOException {
		s_logger.debug("read(cls, obj): " + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getName());
		final String fqn = getFqnString(obj.getQualifiedName());
		if (fqn == null) {
			return Status.failure("Object does not have qualified name.");
		}
		final List clsData = getClassData(obj.getClass());
		if (clsData == null) {
			return Status.failure("No class data.");
		}
		final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);
		if ((fqnMap == null) || (fqnMap.get(fqn) == null)) {
			return Status.failure("No qualified name.");
		}
		obj = fqnMap.get(fqn);
		return Status.success();
	}

	@Override
	public Status delete(final ApiObjectBase obj) throws IOException {
		s_logger.debug("delete(cls, obj): " + _apiBuilder.getTypename(obj.getClass()) + "," + obj.getName());
		if (isChildrenExists(obj)) {
			s_logger.warn("children exist, can not delete");
			return Status.failure("Object already exists.");
		}
		final String uuid = obj.getUuid();
		final String fqn = getFqnString(obj.getQualifiedName());
		if ((fqn == null) || (uuid == null)) {
			s_logger.debug("can not delete - no uuid/fqn");
			return Status.failure("UUID or FQN not specified.");
		}
		final List clsData = getClassData(obj.getClass());
		if (clsData == null) {
			s_logger.debug("can not delete - not exists");
			return Status.success();
		}
		final HashMap<String, ApiObjectBase> uuidMap = getUuidMap(clsData);
		final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);
		fqnMap.remove(fqn);
		uuidMap.remove(uuid);
		return Status.success();
	}

	@Override
	public synchronized Status delete(final Class<? extends ApiObjectBase> cls, final String uuid) throws IOException {
		s_logger.debug("delete(cls, uuid): " + _apiBuilder.getTypename(cls) + ", " + uuid);
		final List clsData = getClassData(cls);
		if (clsData == null) {
			s_logger.debug("can not delete - not exists");
			return Status.success();
		}
		final HashMap<String, ApiObjectBase> uuidMap = getUuidMap(clsData);
		final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);

		final ApiObjectBase obj = uuidMap.get(uuid);
		if ((obj != null) && isChildrenExists(obj)) {
			final String reason = "Cannot delete object having children.";
			s_logger.warn(reason);
			return Status.failure(reason);
		}
		uuidMap.remove(uuid);
		if (obj != null) {
			fqnMap.remove(getFqnString(obj.getQualifiedName()));
		}
		return Status.success();
	}

	@Override
	public synchronized ApiObjectBase find(final Class<? extends ApiObjectBase> cls, final ApiObjectBase parent, final String name) throws IOException {
		s_logger.debug("find(cls, parent, name) : " + _apiBuilder.getTypename(cls) + ", " + parent.getName() + ", " + name);
		final List clsData = getClassData(cls);
		if (clsData == null) {
			s_logger.debug("not found");
			return null;
		}
		final String fqn = getFqnString(parent.getQualifiedName()) + ":" + name;
		final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);
		return fqnMap.get(fqn);
	}

	@Override
	public ApiObjectBase findByFQN(final Class<? extends ApiObjectBase> cls, final String fullName) throws IOException {
		s_logger.debug("findFQN(cls, fqn) : " + _apiBuilder.getTypename(cls) + ", " + fullName);
		final List clsData = getClassData(cls);
		if (clsData == null) {
			s_logger.debug("not found");
			return null;
		}
		final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);
		return fqnMap.get(fullName);
	}

	@Override
	public synchronized ApiObjectBase findById(final Class<? extends ApiObjectBase> cls, final String uuid) throws IOException {
		s_logger.debug("findById(cls, uuid) : " + _apiBuilder.getTypename(cls) + ", " + uuid);
		final List clsData = getClassData(cls);
		if (clsData == null) {
			s_logger.debug("not found");
			return null;
		}
		final HashMap<String, ApiObjectBase> uuidMap = getUuidMap(clsData);
		return uuidMap.get(uuid);
	}

	@Override
	public String findByName(final Class<? extends ApiObjectBase> cls, final ApiObjectBase parent, final String name) throws IOException {
		s_logger.debug("findByName(cls, parent, name) : " + _apiBuilder.getTypename(cls) + ", " + name);
		final List<String> name_list = new ArrayList<>();
		if (parent != null) {
			name_list.addAll(parent.getQualifiedName());
		} else {
			try {
				name_list.addAll(cls.newInstance().getDefaultParent());
			} catch (final Exception e) {
				s_logger.warn("", e);
			}
		}
		name_list.add(name);
		return findByName(cls, name_list);
	}

	@Override
	// POST http://hostname:port/fqname-to-id
	// body: {"type": class, "fq_name": [parent..., name]}
	public synchronized String findByName(final Class<? extends ApiObjectBase> cls, final List<String> name_list) throws IOException {
		final String fqn = name_list.stream().collect(Collectors.joining(":"));
		s_logger.debug("findByName(cls, name_list) : " + _apiBuilder.getTypename(cls) + ", " + fqn);
		final List clsData = getClassData(cls);
		if (clsData == null) {
			s_logger.debug("cls not found");
			return null;
		}
		final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);
		final ApiObjectBase obj = fqnMap.get(fqn);
		if (obj != null) {
			return obj.getUuid();
		}
		s_logger.debug("not found");
		return null;
	}

	@Override
	public synchronized List<? extends ApiObjectBase> list(final Class<? extends ApiObjectBase> cls, final List<String> parent) throws IOException {
		String fqnParent = null;
		if (parent != null) {
			fqnParent = parent.stream().collect(Collectors.joining(":"));
			s_logger.debug("list(cls, parent_name_list) : " + _apiBuilder.getTypename(cls) + ", " + fqnParent);
		} else {
			s_logger.debug("list(cls, parent_name_list) : " + _apiBuilder.getTypename(cls) + ", null");
		}
		final List clsData = getClassData(cls);
		if (clsData == null) {
			s_logger.debug("cls not found");
			return null;
		}
		final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);
		final ArrayList<ApiObjectBase> arr = new ArrayList<>(fqnMap.values());
		final List<ApiObjectBase> list = new ArrayList<>();
		for (final ApiObjectBase obj : arr) {
			if ((fqnParent != null) && getFqnString(obj.getQualifiedName()).startsWith(fqnParent + ":")) {
			}
			list.add(obj);
		}
		return list;
	}

	private boolean isChildrenExists(final ApiObjectBase parent) {
		final String fqnParent = getFqnString(parent.getQualifiedName());
		final ArrayList<List<HashMap<String, ApiObjectBase>>> clsDataList = new ArrayList<>(_map.values());
		for (final List<HashMap<String, ApiObjectBase>> clsData : clsDataList) {
			final HashMap<String, ApiObjectBase> fqnMap = getFqnMap(clsData);
			final ArrayList<ApiObjectBase> arr = new ArrayList<>(fqnMap.values());
			final List<ApiObjectBase> list = new ArrayList<>();
			for (final ApiObjectBase obj : arr) {
				if (getFqnString(obj.getQualifiedName()).startsWith(fqnParent + ":") && (obj.getParent() == parent)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public <T extends ApiPropertyBase> List<? extends ApiObjectBase> getObjects(final Class<? extends ApiObjectBase> cls, final List<ObjectReference<T>> refList) throws IOException {
		s_logger.debug("getObjects(cls, refList): " + _apiBuilder.getTypename(cls));
		final List<ApiObjectBase> list = new ArrayList<>();
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

	public String getRefname(final Class<?> cls) {
		String clsname = cls.getName();
		final int loc = clsname.lastIndexOf('.');
		if (loc > 0) {
			clsname = clsname.substring(loc + 1);
		}
		final StringBuilder typename = new StringBuilder().append(new String());
		for (int i = 0; i < clsname.length(); i++) {
			char ch = clsname.charAt(i);
			if (Character.isUpperCase(ch)) {
				if (i > 0) {
					typename.append("_");
				}
				ch = Character.toLowerCase(ch);
			}
			typename.append(ch);
		}
		return typename.toString();
	}

	private void updateRefsVerify(final ApiObjectBase obj) throws IOException {
		final Class<?> cls = obj.getClass();
		s_logger.debug("updateRefsVerify: " + obj.getName() + ", class: " + _apiBuilder.getTypename(cls));
		for (final Field f : cls.getDeclaredFields()) {
			f.setAccessible(true);
			if (!f.getName().endsWith("_refs") || f.getName().endsWith("_back_refs")) {
				continue;
			}
			List<ObjectReference<ApiPropertyBase>> nv;
			try {
				nv = (List<ObjectReference<ApiPropertyBase>>) f.get(obj);
			} catch (final Exception ex) {
				s_logger.warn("Unable to read value for " + f.getName() + ": " + ex.getMessage());
				throw new IOException("Unable to read value for " + f.getName() + ": " + ex.getMessage());
			}

			if ((nv == null) || nv.isEmpty()) {
				continue;
			}

			final String refName = f.getName().substring(0, f.getName().lastIndexOf("_refs"));
			final Class<? extends ApiObjectBase> refCls = getVncClass(refName);
			for (final ObjectReference<ApiPropertyBase> ref : nv) {
				final String uuid = findByName(refCls, ref.getReferredName());
				final ApiObjectBase refObj = findById(refCls, uuid);
				if (refObj == null) {
					s_logger.debug("Can not find obj for class: " + _apiBuilder.getTypename(refCls)
							+ ", uuid: " + ref.getUuid() + " , href: " + ref.getHRef());
					throw new IOException("Obj " + obj.getName() + " has a reference of type<" + _apiBuilder.getTypename(refCls) + ", but object does not exist");
				}
				s_logger.debug("Verify establish backref on(cls, obj) : " + _apiBuilder.getTypename(refCls) + " => " + refObj.getName() + " with ref(cls, obj): " + _apiBuilder.getTypename(cls) + ", " + obj.getName());
				updateObjectVerify(refObj, obj, getRefname(obj.getClass()) + "_back_refs");
			}
		}
	}

	private void updateRefs(final ApiObjectBase obj) throws IOException {
		final Class<?> cls = obj.getClass();
		s_logger.debug("updateRefs: " + obj.getName() + ", class: " + _apiBuilder.getTypename(cls));
		for (final Field f : cls.getDeclaredFields()) {
			f.setAccessible(true);
			if (!f.getName().endsWith("_refs") || f.getName().endsWith("_back_refs")) {
				continue;
			}
			List<ObjectReference<ApiPropertyBase>> nv;
			try {
				nv = (List<ObjectReference<ApiPropertyBase>>) f.get(obj);
			} catch (final Exception ex) {
				s_logger.warn("Unable to read value for " + f.getName() + ": " + ex.getMessage());
				continue;
			}

			if ((nv == null) || nv.isEmpty()) {
				s_logger.debug("no refs of type: " + f.getName());
				continue;
			}

			final String refName = f.getName().substring(0, f.getName().lastIndexOf("_refs"));
			s_logger.debug("ref name: " + refName);
			final Class<? extends ApiObjectBase> refCls = getVncClass(refName);
			for (final ObjectReference<ApiPropertyBase> ref : nv) {
				final String uuid = findByName(refCls, ref.getReferredName());
				updateField(ref, "uuid", uuid);
				final ApiObjectBase refObj = findById(refCls, uuid);
				if (refObj == null) {
					s_logger.debug("Can not find obj for class: " + _apiBuilder.getTypename(refCls)
							+ ", uuid: " + ref.getUuid() + " , href: " + ref.getHRef());
					throw new IOException("Obj " + obj.getName() + " has a reference of type<" + _apiBuilder.getTypename(refCls) + ", but object does not exist");
				}
				s_logger.debug("Establish backref on(cls, obj) : " + _apiBuilder.getTypename(refCls) + " => " + refObj.getName() + " with ref(cls, obj): " + _apiBuilder.getTypename(cls) + ", " + obj.getName());
				updateObject(refObj, obj, getRefname(obj.getClass()) + "_back_refs");
			}
		}
	}

	private void updateField(final ObjectReference<ApiPropertyBase> obj, final String fieldName, final String value) {
		final Class<?> cls = obj.getClass();

		Field field = null;
		try {
			field = cls.getDeclaredField(fieldName);
			field.setAccessible(true);
		} catch (final Exception e) {
			s_logger.debug("no field " + fieldName + ", \n" + e);
			return;
		}
		try {
			field.set(obj, value);
		} catch (final Exception ex) {
			s_logger.warn("Unable to set " + field.getName() + ": " + ex.getMessage());
		}
		s_logger.debug("Updated " + fieldName + " to " + value + " \n");
	}

	private void updateObjectVerify(final ApiObjectBase obj, final ApiObjectBase other, final String fieldName) throws IOException {
		s_logger.debug("updateObjectVerify(cls, obj, other-cls, other, field): " + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getName() + "," + _apiBuilder.getTypename(other.getClass()) + ", " + other.getName() + "," + fieldName);
		final Class<?> cls = obj.getClass();

		Field fRefs = null;
		try {
			fRefs = cls.getDeclaredField(fieldName);
			fRefs.setAccessible(true);
		} catch (final Exception e) {
			s_logger.debug("no field " + fieldName + ", \n" + e);
			throw new IOException("no field " + fieldName + ", \n" + e);
		}
		List<ObjectReference<ApiPropertyBase>> nv;
		try {
			nv = (List<ObjectReference<ApiPropertyBase>>) fRefs.get(obj);
		} catch (final Exception ex) {
			s_logger.warn("Unable to read new value for " + fRefs.getName() + ": " + ex.getMessage());
			throw new IOException("Unable to read new value for " + fRefs.getName() + ": " + ex.getMessage());
		}
	}

	private void updateObject(final ApiObjectBase obj, final ApiObjectBase other, final String fieldName) throws IOException {
		s_logger.debug("updateObject(cls, obj, other-cls, other, field): " + _apiBuilder.getTypename(obj.getClass()) + ", " + obj.getName() + "," + _apiBuilder.getTypename(other.getClass()) + ", " + other.getName() + "," + fieldName);
		final Class<?> cls = obj.getClass();

		Field fRefs = null;
		try {
			fRefs = cls.getDeclaredField(fieldName);
			fRefs.setAccessible(true);
		} catch (final Exception e) {
			s_logger.debug("no field " + fieldName + ", \n" + e);
			return;
		}
		List<ObjectReference<ApiPropertyBase>> nv;
		try {
			nv = (List<ObjectReference<ApiPropertyBase>>) fRefs.get(obj);
		} catch (final Exception ex) {
			s_logger.warn("Unable to read new value for " + fRefs.getName() + ": " + ex.getMessage());
			return;
		}

		if (nv == null) {
			nv = new ArrayList<>();
		}

		final String href = "http://localhost:8082/" + _apiBuilder.getTypename(other.getClass()) + '/' + other.getUuid();
		final ObjectReference<ApiPropertyBase> objRef = new ObjectReference<>(other.getQualifiedName(), null, href, other.getUuid());
		nv.add(objRef);
		try {
			fRefs.set(obj, nv);
		} catch (final Exception ex) {
			s_logger.warn("Unable to set " + fRefs.getName() + ": " + ex.getMessage());
		}
	}

	public void dumpConfig(final Class<? extends ApiObjectBase> cls) throws Exception {
		final List<? extends ApiObjectBase> list = list(cls, null);
		for (final ApiObjectBase obj : list) {
			s_logger.debug("Class : " + _apiBuilder.getTypename(cls) + ", name: " + obj.getName());
		}
	}

	@Override
	public Status sync(final String uri) throws IOException {
		return Status.success();
	}

	@Override
	public void dispose() {
	}

	@Override
	public ApiConnector domainName(final String tenantName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiConnector tenantId(final String tenantId) {
		// TODO Auto-generated method stub
		return null;
	}
}
