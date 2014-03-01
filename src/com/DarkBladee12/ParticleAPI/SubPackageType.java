package com.DarkBladee12.ParticleAPI;

/**
 * This class is part of the ReflectionHandler and follows the same usage conditions
 *  
 * @author DarkBlade12
 */
public enum SubPackageType {
	BLOCK,
	CHUNKIO,
	COMMAND,
	CONVERSATIONS,
	ENCHANTMENS,
	ENTITY,
	EVENT,
	GENERATOR,
	HELP,
	INVENTORY,
	MAP,
	METADATA,
	POTION,
	PROJECTILES,
	SCHEDULER,
	SCOREBOARD,
	UPDATER,
	UTIL;

	private final String name;

	private SubPackageType() {
		name = PackageType.CRAFTBUKKIT + "." + name().toLowerCase();
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return name;
	}
}