/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */
package net.juniper.contrail.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectReference<A extends ApiPropertyBase> implements Serializable {
	private static final long serialVersionUID = 1L;
	private final List<String> to;
	private final A attr;
	private final String href;
	private final String uuid;

	public ObjectReference(final List<String> to, final A attr) {
		this(to, attr, null, null);
	}

	public ObjectReference(final String uuid) {
		this(null, null, null, uuid);
	}

	public ObjectReference(final List<String> to, final A attr, final String href, final String uuid) {
		this.to = Collections.unmodifiableList(new ArrayList<>(to));
		this.attr = attr;
		this.href = href;
		this.uuid = uuid;
	}

	public String getHRef() {
		return href;
	}

	public String getUuid() {
		return uuid;
	}

	public List<String> getReferredName() {
		return to;
	}

	public A getAttr() {
		return attr;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ObjectReference)) {
			return false;
		}

		final ObjectReference<?> that = (ObjectReference<?>) o;

		if ((to != null) && (that.to != null)) {
			return to.equals(that.to);
		}
		return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;
	}

	@Override
	public int hashCode() {
		int result = to != null ? to.hashCode() : 0;
		return (31 * result) + (uuid != null ? uuid.hashCode() : 0);
	}

	public static <T extends ApiPropertyBase> String getReferenceListUuid(final List<ObjectReference<T>> reflist) {
		if ((reflist != null) && !reflist.isEmpty()) {
			final ObjectReference<T> ref = reflist.get(0);
			return ref.getUuid();
		}
		return null;
	}
}
