package com.DarkBladee12.ParticleAPI;

/**
 * This class is part of the ReflectionHandler and follows the same usage conditions
 *  
 * @author DarkBlade12
 */
public final class FieldPair {
	private final String name;
	private final Object value;

	public FieldPair(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}
}