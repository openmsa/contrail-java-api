/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */
package net.juniper.contrail.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class ApiObjectBase implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String uuid;
	private List<String> fq_name;
	private transient ApiObjectBase parent;
	private String parent_type;
	private String parent_uuid;

	public String getName() {
		if ((name == null) && (fq_name != null)) {
			name = fq_name.get(fq_name.size() - 1);
		}
		return name;
	}

	/**
	 * Retrieves a parent object that may be cached in the object due to a setParent
	 * operation.
	 *
	 * @return parent
	 */
	public ApiObjectBase getParent() {
		return parent;
	}

	protected void setParent(final ApiObjectBase parent) {
		this.parent = parent;
		this.fq_name = null;
		if (parent != null) {
			parent_type = parent.getObjectType();
			parent_uuid = parent.getUuid();
		} else {
			parent_type = null;
			parent_uuid = null;
		}
	}

	public void setName(final String name) {
		this.name = name;
		if (fq_name != null) {
			fq_name.set(fq_name.size() - 1, name);
		}
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	public String getParentUuid() {
		return parent_uuid;
	}

	public String getParentType() {
		return parent_type;
	}

	protected void updateQualifiedNames() {
		if (fq_name == null) {
			List<String> parentQn;
			if (parent != null) {
				parentQn = parent.getQualifiedName();
				parent_type = parent.getObjectType();
			} else {
				parentQn = getDefaultParent();
				parent_type = getDefaultParentType();
				if (parentQn == null) {
					throw new IllegalStateException("Parent of type " + getClass().getSimpleName() + " has to be specified explicitly.");
				}
			}
			parentQn.add(name);
			fq_name = parentQn;
		}
	}

	protected void updateQualifiedName(final String domain, final String project) {
		if (fq_name == null) {
			List<String> parentQn;
			if (parent != null) {
				parentQn = new ArrayList<>(parent.getQualifiedName());
				parent_type = parent.getObjectType();
			} else {
				parentQn = Optional.ofNullable(getDefaultParent())
						.map(ArrayList::new)
						.orElseThrow(() -> new IllegalStateException("Parent of type " + getClass().getSimpleName() + " has to be specified explicitly."));
				parent_type = getDefaultParentType();
			}
			parentQn.add(name);
			Optional.ofNullable(project).ifPresent(x -> Collections.replaceAll(parentQn, "default-domain", x));
			Optional.ofNullable(domain).ifPresent(x -> Collections.replaceAll(parentQn, "default-project", x));
			fq_name = parentQn;
		}
	}

	public List<String> getQualifiedName() {
		if ("config-root".equals(getObjectType()) || (null == fq_name)) {
			return new ArrayList<>();
		}
		return new ArrayList<>(fq_name);
	}

	public abstract String getObjectType();

	public abstract List<String> getDefaultParent();

	public abstract String getDefaultParentType();
}
