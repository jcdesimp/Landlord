package com.DarkBladee12.ParticleAPI;

import org.bukkit.Bukkit;

/**
 * This class is part of the ReflectionHandler and follows the same usage conditions
 *  
 * @author DarkBlade12
 */
public enum PackageType {
	MINECRAFT_SERVER("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23)),
	CRAFTBUKKIT(Bukkit.getServer().getClass().getPackage().getName());

	private final String name;

	private PackageType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return name;
	}
}