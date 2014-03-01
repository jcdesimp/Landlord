package com.DarkBladee12.ParticleAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the ReflectionHandler and follows the same usage conditions
 *  
 * @author DarkBlade12
 */
public enum DataType {
	BYTE(byte.class, Byte.class),
	SHORT(short.class, Short.class),
	INTEGER(int.class, Integer.class),
	LONG(long.class, Long.class),
	CHARACTER(char.class, Character.class),
	FLOAT(float.class, Float.class),
	DOUBLE(double.class, Double.class),
	BOOLEAN(boolean.class, Boolean.class);

	private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<Class<?>, DataType>();
	private final Class<?> primitive;
	private final Class<?> reference;

	static {
		for (DataType t : values()) {
			CLASS_MAP.put(t.primitive, t);
			CLASS_MAP.put(t.reference, t);
		}
	}

	private DataType(Class<?> primitive, Class<?> reference) {
		this.primitive = primitive;
		this.reference = reference;
	}

	public Class<?> getPrimitive() {
		return this.primitive;
	}

	public Class<?> getReference() {
		return this.reference;
	}

	public static DataType fromClass(Class<?> c) {
		return CLASS_MAP.get(c);
	}

	public static Class<?> getPrimitive(Class<?> c) {
		DataType t = fromClass(c);
		return t == null ? c : t.getPrimitive();
	}

	public static Class<?> getReference(Class<?> c) {
		DataType t = fromClass(c);
		return t == null ? c : t.getReference();
	}

	public static Class<?>[] convertToPrimitive(Class<?>[] classes) {
		int length = classes == null ? 0 : classes.length;
		Class<?>[] types = new Class<?>[length];
		for (int i = 0; i < length; i++)
			types[i] = getPrimitive(classes[i]);
		return types;
	}

	public static Class<?>[] convertToPrimitive(Object[] objects) {
		int length = objects == null ? 0 : objects.length;
		Class<?>[] types = new Class<?>[length];
		for (int i = 0; i < length; i++)
			types[i] = getPrimitive(objects[i].getClass());
		return types;
	}

	public static boolean equalsArray(Class<?>[] a1, Class<?>[] a2) {
		if (a1 == null || a2 == null || a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++)
			if (!a1[i].equals(a2[i]) && !a1[i].isAssignableFrom(a2[i]))
				return false;
		return true;
	}
}