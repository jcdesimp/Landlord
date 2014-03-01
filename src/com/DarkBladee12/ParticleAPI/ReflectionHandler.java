package com.DarkBladee12.ParticleAPI;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * ReflectionHandler v1.0
 * 
 * This class makes dealing with reflection much easier, especially when working with Bukkit
 * 
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * 1. Don't claim this class as your own
 * 2. Don't remove this text
 * 
 * (Would be nice if you provide credit to me)
 * 
 * @author DarkBlade12
 */
public final class ReflectionHandler {
	private ReflectionHandler() {}

	public static Class<?> getClass(String name, PackageType type) throws Exception {
		return Class.forName(type + "." + name);
	}

	public static Class<?> getClass(String name, SubPackageType type) throws Exception {
		return Class.forName(type + "." + name);
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) {
		Class<?>[] p = DataType.convertToPrimitive(parameterTypes);
		for (Constructor<?> c : clazz.getConstructors())
			if (DataType.equalsArray(DataType.convertToPrimitive(c.getParameterTypes()), p))
				return c;
		return null;
	}

	public static Constructor<?> getConstructor(String className, PackageType type, Class<?>... parameterTypes) throws Exception {
		return getConstructor(getClass(className, type), parameterTypes);
	}

	public static Constructor<?> getConstructor(String className, SubPackageType type, Class<?>... parameterTypes) throws Exception {
		return getConstructor(getClass(className, type), parameterTypes);
	}

	public static Object newInstance(Class<?> clazz, Object... args) throws Exception {
		return getConstructor(clazz, DataType.convertToPrimitive(args)).newInstance(args);
	}

	public static Object newInstance(String className, PackageType type, Object... args) throws Exception {
		return newInstance(getClass(className, type), args);
	}

	public static Object newInstance(String className, SubPackageType type, Object... args) throws Exception {
		return newInstance(getClass(className, type), args);
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		Class<?>[] p = DataType.convertToPrimitive(parameterTypes);
		for (Method m : clazz.getMethods())
			if (m.getName().equals(name) && DataType.equalsArray(DataType.convertToPrimitive(m.getParameterTypes()), p))
				return m;
		return null;
	}

	public static Method getMethod(String className, PackageType type, String name, Class<?>... parameterTypes) throws Exception {
		return getMethod(getClass(className, type), name, parameterTypes);
	}

	public static Method getMethod(String className, SubPackageType type, String name, Class<?>... parameterTypes) throws Exception {
		return getMethod(getClass(className, type), name, parameterTypes);
	}

	public static Object invokeMethod(String name, Object instance, Object... args) throws Exception {
		return getMethod(instance.getClass(), name, DataType.convertToPrimitive(args)).invoke(instance, args);
	}

	public static Object invokeMethod(Class<?> clazz, String name, Object instance, Object... args) throws Exception {
		return getMethod(clazz, name, DataType.convertToPrimitive(args)).invoke(instance, args);
	}

	public static Object invokeMethod(String className, PackageType type, String name, Object instance, Object... args) throws Exception {
		return invokeMethod(getClass(className, type), name, instance, args);
	}

	public static Object invokeMethod(String className, SubPackageType type, String name, Object instance, Object... args) throws Exception {
		return invokeMethod(getClass(className, type), name, instance, args);
	}

	public static Field getField(Class<?> clazz, String name) throws Exception {
		Field f = clazz.getField(name);
		f.setAccessible(true);
		return f;
	}

	public static Field getField(String className, PackageType type, String name) throws Exception {
		return getField(getClass(className, type), name);
	}

	public static Field getField(String className, SubPackageType type, String name) throws Exception {
		return getField(getClass(className, type), name);
	}

	public static Field getDeclaredField(Class<?> clazz, String name) throws Exception {
		Field f = clazz.getDeclaredField(name);
		f.setAccessible(true);
		return f;
	}

	public static Field getDeclaredField(String className, PackageType type, String name) throws Exception {
		return getDeclaredField(getClass(className, type), name);
	}

	public static Field getDeclaredField(String className, SubPackageType type, String name) throws Exception {
		return getDeclaredField(getClass(className, type), name);
	}

	public static Object getValue(Object instance, String fieldName) throws Exception {
		return getField(instance.getClass(), fieldName).get(instance);
	}

	public static Object getValue(Class<?> clazz, Object instance, String fieldName) throws Exception {
		return getField(clazz, fieldName).get(instance);
	}

	public static Object getValue(String className, PackageType type, Object instance, String fieldName) throws Exception {
		return getValue(getClass(className, type), instance, fieldName);
	}

	public static Object getValue(String className, SubPackageType type, Object instance, String fieldName) throws Exception {
		return getValue(getClass(className, type), instance, fieldName);
	}

	public static Object getDeclaredValue(Object instance, String fieldName) throws Exception {
		return getDeclaredField(instance.getClass(), fieldName).get(instance);
	}

	public static Object getDeclaredValue(Class<?> clazz, Object instance, String fieldName) throws Exception {
		return getDeclaredField(clazz, fieldName).get(instance);
	}

	public static Object getDeclaredValue(String className, PackageType type, Object instance, String fieldName) throws Exception {
		return getDeclaredValue(getClass(className, type), instance, fieldName);
	}

	public static Object getDeclaredValue(String className, SubPackageType type, Object instance, String fieldName) throws Exception {
		return getDeclaredValue(getClass(className, type), instance, fieldName);
	}

	public static void setValue(Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getField(instance.getClass(), fieldName);
		f.set(instance, fieldValue);
	}

	public static void setValue(Object instance, FieldPair pair) throws Exception {
		setValue(instance, pair.getName(), pair.getValue());
	}

	public static void setValue(Class<?> clazz, Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getField(clazz, fieldName);
		f.set(instance, fieldValue);
	}

	public static void setValue(Class<?> clazz, Object instance, FieldPair pair) throws Exception {
		setValue(clazz, instance, pair.getName(), pair.getValue());
	}

	public static void setValue(String className, PackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setValue(getClass(className, type), instance, fieldName, fieldValue);
	}

	public static void setValue(String className, PackageType type, Object instance, FieldPair pair) throws Exception {
		setValue(className, type, instance, pair.getName(), pair.getValue());
	}

	public static void setValue(String className, SubPackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setValue(getClass(className, type), instance, fieldName, fieldValue);
	}

	public static void setValue(String className, SubPackageType type, Object instance, FieldPair pair) throws Exception {
		setValue(className, type, instance, pair.getName(), pair.getValue());
	}

	public static void setValues(Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setValue(instance, pair);
	}

	public static void setValues(Class<?> clazz, Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setValue(clazz, instance, pair);
	}

	public static void setValues(String className, PackageType type, Object instance, FieldPair... pairs) throws Exception {
		setValues(getClass(className, type), instance, pairs);
	}

	public static void setValues(String className, SubPackageType type, Object instance, FieldPair... pairs) throws Exception {
		setValues(getClass(className, type), instance, pairs);
	}

	public static void setDeclaredValue(Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getDeclaredField(instance.getClass(), fieldName);
		f.set(instance, fieldValue);
	}

	public static void setDeclaredValue(Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(instance, pair.getName(), pair.getValue());
	}

	public static void setDeclaredValue(Class<?> clazz, Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getDeclaredField(clazz, fieldName);
		f.set(instance, fieldValue);
	}

	public static void setDeclaredValue(Class<?> clazz, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(clazz, instance, pair.getName(), pair.getValue());
	}

	public static void setDeclaredValue(String className, PackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
	}

	public static void setDeclaredValue(String className, PackageType type, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
	}

	public static void setDeclaredValue(String className, SubPackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
	}

	public static void setDeclaredValue(String className, SubPackageType type, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
	}

	public static void setDeclaredValues(Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setDeclaredValue(instance, pair);
	}

	public static void setDeclaredValues(Class<?> clazz, Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setDeclaredValue(clazz, instance, pair);
	}

	public static void setDeclaredValues(String className, PackageType type, Object instance, FieldPair... pairs) throws Exception {
		setDeclaredValues(getClass(className, type), instance, pairs);
	}

	public static void setDeclaredValues(String className, SubPackageType type, Object instance, FieldPair... pairs) throws Exception {
		setDeclaredValues(getClass(className, type), instance, pairs);
	}
}