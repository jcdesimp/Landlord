package com.jcdesimp.landlord.DarkBladee12.ParticleAPI;

/**
 * This class is part of the ReflectionHandler and follows the same usage conditions
 *  
 * @author DarkBlade12
 */
public enum PacketType {
	HANDSHAKING_IN_SET_PROTOCOL("PacketHandshakingInSetProtocol"),
	LOGIN_IN_ENCRYPTION_BEGIN("PacketLoginInEncryptionBegin"),
	LOGIN_IN_START("PacketLoginInStart"),
	LOGIN_OUT_DISCONNECT("PacketLoginOutDisconnect"),
	LOGIN_OUT_ENCRYPTION_BEGIN("PacketLoginOutEncryptionBegin"),
	LOGIN_OUT_SUCCESS("PacketLoginOutSuccess"),
	PLAY_IN_ABILITIES("PacketPlayInAbilities"),
	PLAY_IN_ARM_ANIMATION("PacketPlayInArmAnimation"),
	PLAY_IN_BLOCK_DIG("PacketPlayInBlockDig"),
	PLAY_IN_BLOCK_PLACE("PacketPlayInBlockPlace"),
	PLAY_IN_CHAT("PacketPlayInChat"),
	PLAY_IN_CLIENT_COMMAND("PacketPlayInClientCommand"),
	PLAY_IN_CLOSE_WINDOW("PacketPlayInCloseWindow"),
	PLAY_IN_CUSTOM_PAYLOAD("PacketPlayInCustomPayload"),
	PLAY_IN_ENCHANT_ITEM("PacketPlayInEnchantItem"),
	PLAY_IN_ENTITY_ACTION("PacketPlayInEntityAction"),
	PLAY_IN_FLYING("PacketPlayInFlying"),
	PLAY_IN_HELD_ITEM_SLOT("PacketPlayInHeldItemSlot"),
	PLAY_IN_KEEP_ALIVE("PacketPlayInKeepAlive"),
	PLAY_IN_LOOK("PacketPlayInLook"),
	PLAY_IN_POSITION("PacketPlayInPosition"),
	PLAY_IN_POSITION_LOOK("PacketPlayInPositionLook"),
	PLAY_IN_SET_CREATIVE_SLOT("PacketPlayInSetCreativeSlot "),
	PLAY_IN_SETTINGS("PacketPlayInSettings"),
	PLAY_IN_STEER_VEHICLE("PacketPlayInSteerVehicle"),
	PLAY_IN_TAB_COMPLETE("PacketPlayInTabComplete"),
	PLAY_IN_TRANSACTION("PacketPlayInTransaction"),
	PLAY_IN_UPDATE_SIGN("PacketPlayInUpdateSign"),
	PLAY_IN_USE_ENTITY("PacketPlayInUseEntity"),
	PLAY_IN_WINDOW_CLICK("PacketPlayInWindowClick"),
	PLAY_OUT_ABILITIES("PacketPlayOutAbilities"),
	PLAY_OUT_ANIMATION("PacketPlayOutAnimation"),
	PLAY_OUT_ATTACH_ENTITY("PacketPlayOutAttachEntity"),
	PLAY_OUT_BED("PacketPlayOutBed"),
	PLAY_OUT_BLOCK_ACTION("PacketPlayOutBlockAction"),
	PLAY_OUT_BLOCK_BREAK_ANIMATION("PacketPlayOutBlockBreakAnimation"),
	PLAY_OUT_BLOCK_CHANGE("PacketPlayOutBlockChange"),
	PLAY_OUT_CHAT("PacketPlayOutChat"),
	PLAY_OUT_CLOSE_WINDOW("PacketPlayOutCloseWindow"),
	PLAY_OUT_COLLECT("PacketPlayOutCollect"),
	PLAY_OUT_CRAFT_PROGRESS_BAR("PacketPlayOutCraftProgressBar"),
	PLAY_OUT_CUSTOM_PAYLOAD("PacketPlayOutCustomPayload"),
	PLAY_OUT_ENTITY("PacketPlayOutEntity"),
	PLAY_OUT_ENTITY_DESTROY("PacketPlayOutEntityDestroy"),
	PLAY_OUT_ENTITY_EFFECT("PacketPlayOutEntityEffect"),
	PLAY_OUT_ENTITY_EQUIPMENT("PacketPlayOutEntityEquipment"),
	PLAY_OUT_ENTITY_HEAD_ROTATION("PacketPlayOutEntityHeadRotation"),
	PLAY_OUT_ENTITY_LOOK("PacketPlayOutEntityLook"),
	PLAY_OUT_ENTITY_METADATA("PacketPlayOutEntityMetadata"),
	PLAY_OUT_ENTITY_STATUS("PacketPlayOutEntityStatus"),
	PLAY_OUT_ENTITY_TELEPORT("PacketPlayOutEntityTeleport"),
	PLAY_OUT_ENTITY_VELOCITY("PacketPlayOutEntityVelocity"),
	PLAY_OUT_EXPERIENCE("PacketPlayOutExperience"),
	PLAY_OUT_EXPLOSION("PacketPlayOutExplosion"),
	PLAY_OUT_GAME_STATE_CHANGE("PacketPlayOutGameStateChange"),
	PLAY_OUT_HELD_ITEM_SLOT("PacketPlayOutHeldItemSlot"),
	PLAY_OUT_KEEP_ALIVE("PacketPlayOutKeepAlive"),
	PLAY_OUT_KICK_DISCONNECT("PacketPlayOutKickDisconnect"),
	PLAY_OUT_LOGIN("PacketPlayOutLogin"),
	PLAY_OUT_MAP("PacketPlayOutMap"),
	PLAY_OUT_MAP_CHUNK("PacketPlayOutMapChunk"),
	PLAY_OUT_MAP_CHUNK_BULK("PacketPlayOutMapChunkBulk"),
	PLAY_OUT_MULTI_BLOCK_CHANGE("PacketPlayOutMultiBlockChange"),
	PLAY_OUT_NAMED_ENTITY_SPAWN("PacketPlayOutNamedEntitySpawn"),
	PLAY_OUT_NAMED_SOUND_EFFECT("PacketPlayOutNamedSoundEffect"),
	PLAY_OUT_OPEN_SIGN_EDITOR("PacketPlayOutOpenSignEditor"),
	PLAY_OUT_OPEN_WINDOW("PacketPlayOutOpenWindow"),
	PLAY_OUT_PLAYER_INFO("PacketPlayOutPlayerInfo"),
	PLAY_OUT_POSITION("PacketPlayOutPosition"),
	PLAY_OUT_REL_ENTITY_MOVE("PacketPlayOutRelEntityMove"),
	PLAY_OUT_REL_ENTITY_MOVE_LOOK("PacketPlayOutRelEntityMoveLook"),
	PLAY_OUT_REMOVE_ENTITY_EFFECT("PacketPlayOutRemoveEntityEffect"),
	PLAY_OUT_RESPAWN("PacketPlayOutRespawn"),
	PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE("PacketPlayOutScoreboardDisplayObjective"),
	PLAY_OUT_SCOREBOARD_OBJECTIVE("PacketPlayOutScoreboardObjective"),
	PLAY_OUT_SCOREBOARD_SCORE("PacketPlayOutScoreboardScore"),
	PLAY_OUT_SCOREBOARD_TEAM("PacketPlayOutScoreboardTeam"),
	PLAY_OUT_SET_SLOT("PacketPlayOutSetSlot"),
	PLAY_OUT_SPAWN_ENTITY("PacketPlayOutSpawnEntity"),
	PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB("PacketPlayOutSpawnEntityExperienceOrb"),
	PLAY_OUT_SPAWN_ENTITY_LIVING("PacketPlayOutSpawnEntityLiving"),
	PLAY_OUT_SPAWN_ENTITY_PAINTING("PacketPlayOutSpawnEntityPainting"),
	PLAY_OUT_SPAWN_ENTITY_WEATHER("PacketPlayOutSpawnEntityWeather"),
	PLAY_OUT_SPAWN_POSITION("PacketPlayOutSpawnPosition"),
	PLAY_OUT_STATISTIC("PacketPlayOutStatistic"),
	PLAY_OUT_TAB_COMPLETE("PacketPlayOutTabComplete"),
	PLAY_OUT_TILE_ENTITY_DATA("PacketPlayOutTileEntityData"),
	PLAY_OUT_TRANSACTION("PacketPlayOutTransaction"),
	PLAY_OUT_UPDATE_ATTRIBUTES("PacketPlayOutUpdateAttributes"),
	PLAY_OUT_UPDATE_HEALTH("PacketPlayOutUpdateHealth"),
	PLAY_OUT_UPDATE_SIGN("PacketPlayOutUpdateSign"),
	PLAY_OUT_UPDATE_TIME("PacketPlayOutUpdateTime"),
	PLAY_OUT_WINDOW_ITEMS("PacketPlayOutWindowItems"),
	PLAY_OUT_WORLD_EVENT("PacketPlayOutWorldEvent"),
	PLAY_OUT_WORLD_PARTICLES("PacketPlayOutWorldParticles"),
	STATUS_IN_PING("PacketStatusInPing"),
	STATUS_IN_START("PacketStatusInStart"),
	STATUS_OUT_PONG("PacketStatusOutPong"),
	STATUS_OUT_SERVER_INFO("PacketStatusOutServerInfo");

	private final String name;
	private Class<?> packet;

	private PacketType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.getName();
	}

	public Class<?> getPacket() throws Exception {
		return packet == null ? packet = ReflectionHandler.getClass(name, PackageType.MINECRAFT_SERVER) : packet;
	}
}