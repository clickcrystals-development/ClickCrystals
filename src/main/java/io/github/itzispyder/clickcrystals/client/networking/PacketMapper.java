//package io.github.itzispyder.clickcrystals.client.networking;
//
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class PacketMapper {
//
//    public static final Map<Class<? extends Packet<?>>, Info> C2S = new HashMap<>() {{
//        this.put(ServerboundChunkBatchReceivedPacket.class, new Info("acknowledgeChunks", "AcknowledgeChunksC2SPacket"));
//        this.put(ServerboundConfigurationAcknowledgedPacket.class, new Info("acknowledgeReconfiguration", "AcknowledgeReconfigurationC2SPacket"));
//        this.put(ServerboundSeenAdvancementsPacket.class, new Info("advancementTab", "AdvancementTabC2SPacket"));
//        this.put(ServerboundPaddleBoatPacket.class, new Info("boatPaddleState", "BoatPaddleStateC2SPacket"));
//        this.put(ServerboundEditBookPacket.class, new Info("bookUpdate", "BookUpdateC2SPacket"));
//        this.put(ServerboundSelectBundleItemPacket.class, new Info("bundleItemSelected", "BundleItemSelectedC2SPacket"));
//        this.put(ServerboundContainerButtonClickPacket.class, new Info("buttonClick", "ButtonClickC2SPacket"));
//        this.put(ServerboundChangeGameModePacket.class, new Info("changeGameMode", "ChangeGameModeC2SPacket"));
//        this.put(ServerboundChatCommandSignedPacket.class, new Info("chatCommandSigned", "ChatCommandSignedC2SPacket"));
//        this.put(ServerboundChatPacket.class, new Info("chatMessage", "ChatMessageC2SPacket"));
//        this.put(ServerboundContainerClickPacket.class, new Info("clickSlot", "ClickSlotC2SPacket"));
//        this.put(ServerboundPlayerCommandPacket.class, new Info("clientCommand", "ClientCommandC2SPacket"));
//        this.put(ServerboundClientCommandPacket.class, new Info("clientStatus", "ClientStatusC2SPacket"));
//        this.put(ServerboundClientTickEndPacket.class, new Info("clientTickEnd", "ClientTickEndC2SPacket"));
//        this.put(ServerboundContainerClosePacket.class, new Info("closeHandledScreen", "CloseHandledScreenC2SPacket"));
//        this.put(ServerboundChatCommandPacket.class, new Info("commandExecution", "CommandExecutionC2SPacket"));
//        this.put(ServerboundPlaceRecipePacket.class, new Info("craftRequest", "CraftRequestC2SPacket"));
//        this.put(ServerboundSetCreativeModeSlotPacket.class, new Info("creativeInventoryAction", "CreativeInventoryActionC2SPacket"));
//        this.put(ServerboundDebugSubscriptionRequestPacket.class, new Info("debugSubscriptionRequest", "DebugSubscriptionRequestC2SPacket"));
//        this.put(ServerboundSwingPacket.class, new Info("handSwing", "HandSwingC2SPacket"));
//        this.put(ServerboundJigsawGeneratePacket.class, new Info("jigsawGenerating", "JigsawGeneratingC2SPacket"));
//        this.put(ServerboundChatAckPacket.class, new Info("messageAcknowledgment", "MessageAcknowledgmentC2SPacket"));
//        this.put(ServerboundPickItemFromBlockPacket.class, new Info("pickItemFromBlock", "PickItemFromBlockC2SPacket"));
//        this.put(ServerboundPickItemFromEntityPacket.class, new Info("pickItemFromEntity", "PickItemFromEntityC2SPacket"));
//        this.put(ServerboundPlayerActionPacket.class, new Info("playerAction", "PlayerActionC2SPacket"));
//        this.put(ServerboundPlayerInputPacket.class, new Info("playerInput", "PlayerInputC2SPacket"));
//        this.put(ServerboundUseItemOnPacket.class, new Info("playerInteractBlock", "PlayerInteractBlockC2SPacket"));
//        this.put(ServerboundInteractPacket.class, new Info("playerInteractEntity", "PlayerInteractEntityC2SPacket"));
//        this.put(ServerboundUseItemPacket.class, new Info("playerInteractItem", "PlayerInteractItemC2SPacket"));
//        this.put(ServerboundPlayerLoadedPacket.class, new Info("playerLoaded", "PlayerLoadedC2SPacket"));
//        this.put(ServerboundMovePlayerPacket.class, new Info("playerMove", "PlayerMoveC2SPacket"));
//        this.put(ServerboundChatSessionUpdatePacket.class, new Info("playerSession", "PlayerSessionC2SPacket"));
//        this.put(ServerboundBlockEntityTagQueryPacket.class, new Info("queryBlockNbt", "QueryBlockNbtC2SPacket"));
//        this.put(ServerboundEntityTagQueryPacket.class, new Info("queryEntityNbt", "QueryEntityNbtC2SPacket"));
//        this.put(ServerboundRecipeBookSeenRecipePacket.class, new Info("recipeBookData", "RecipeBookDataC2SPacket"));
//        this.put(ServerboundRecipeBookChangeSettingsPacket.class, new Info("recipeCategoryOptions", "RecipeCategoryOptionsC2SPacket"));
//        this.put(ServerboundRenameItemPacket.class, new Info("renameItem", "RenameItemC2SPacket"));
//        this.put(ServerboundCommandSuggestionPacket.class, new Info("requestCommandCompletions", "RequestCommandCompletionsC2SPacket"));
//        this.put(ServerboundSelectTradePacket.class, new Info("selectMerchantTrade", "SelectMerchantTradeC2SPacket"));
//        this.put(ServerboundSetTestBlockPacket.class, new Info("setTestBlock", "SetTestBlockC2SPacket"));
//        this.put(ServerboundContainerSlotStateChangedPacket.class, new Info("slotChangedState", "SlotChangedStateC2SPacket"));
//        this.put(ServerboundTeleportToEntityPacket.class, new Info("spectatorTeleport", "SpectatorTeleportC2SPacket"));
//        this.put(ServerboundAcceptTeleportationPacket.class, new Info("teleportConfirm", "TeleportConfirmC2SPacket"));
//        this.put(ServerboundTestInstanceBlockActionPacket.class, new Info("testInstanceBlockAction", "TestInstanceBlockActionC2SPacket"));
//        this.put(ServerboundSetBeaconPacket.class, new Info("updateBeacon", "UpdateBeaconC2SPacket"));
//        this.put(ServerboundSetCommandBlockPacket.class, new Info("updateCommandBlock", "UpdateCommandBlockC2SPacket"));
//        this.put(ServerboundSetCommandMinecartPacket.class, new Info("updateCommandBlockMinecart", "UpdateCommandBlockMinecartC2SPacket"));
//        this.put(ServerboundChangeDifficultyPacket.class, new Info("updateDifficulty", "UpdateDifficultyC2SPacket"));
//        this.put(ServerboundLockDifficultyPacket.class, new Info("updateDifficultyLock", "UpdateDifficultyLockC2SPacket"));
//        this.put(ServerboundSetJigsawBlockPacket.class, new Info("updateJigsaw", "UpdateJigsawC2SPacket"));
//        this.put(ServerboundPlayerAbilitiesPacket.class, new Info("updatePlayerAbilities", "UpdatePlayerAbilitiesC2SPacket"));
//        this.put(ServerboundSetCarriedItemPacket.class, new Info("updateSelectedSlot", "UpdateSelectedSlotC2SPacket"));
//        this.put(ServerboundSignUpdatePacket.class, new Info("updateSign", "UpdateSignC2SPacket"));
//        this.put(ServerboundSetStructureBlockPacket.class, new Info("updateStructureBlock", "UpdateStructureBlockC2SPacket"));
//        this.put(ServerboundMoveVehiclePacket.class, new Info("vehicleMove", "VehicleMoveC2SPacket"));
//    }};
//
//    public static final Map<Class<? extends Packet<?>>, Info> S2C = new HashMap<>() {{
//        this.put(ClientboundUpdateAdvancementsPacket.class, new Info("advancementUpdate", "AdvancementUpdateS2CPacket"));
//        this.put(ClientboundBlockDestructionPacket.class, new Info("blockBreakingProgress", "BlockBreakingProgressS2CPacket"));
//        this.put(ClientboundBlockEntityDataPacket.class, new Info("blockEntityUpdate", "BlockEntityUpdateS2CPacket"));
//        this.put(ClientboundBlockEventPacket.class, new Info("blockEvent", "BlockEventS2CPacket"));
//        this.put(ClientboundBlockUpdatePacket.class, new Info("blockUpdate", "BlockUpdateS2CPacket"));
//        this.put(ClientboundDebugBlockValuePacket.class, new Info("blockValueDebug", "BlockValueDebugS2CPacket"));
//        this.put(ClientboundBossEventPacket.class, new Info("bossBar", "BossBarS2CPacket"));
//        this.put(ClientboundBundleDelimiterPacket.class, new Info("bundleDelimiter", "BundleDelimiterS2CPacket"));
//        this.put(ClientboundBundlePacket.class, new Info("bundle", "BundleS2CPacket"));
//        this.put(ClientboundPlayerChatPacket.class, new Info("chatMessage", "ChatMessageS2CPacket"));
//        this.put(ClientboundCustomChatCompletionsPacket.class, new Info("chatSuggestions", "ChatSuggestionsS2CPacket"));
//        this.put(ClientboundChunksBiomesPacket.class, new Info("chunkBiomeData", "ChunkBiomeDataS2CPacket"));
//        this.put(ClientboundLevelChunkWithLightPacket.class, new Info("chunkData", "ChunkDataS2CPacket"));
//        this.put(ClientboundSectionBlocksUpdatePacket.class, new Info("chunkDeltaUpdate", "ChunkDeltaUpdateS2CPacket"));
//        this.put(ClientboundSetChunkCacheRadiusPacket.class, new Info("chunkLoadDistance", "ChunkLoadDistanceS2CPacket"));
//        this.put(ClientboundSetChunkCacheCenterPacket.class, new Info("chunkRenderDistanceCenter", "ChunkRenderDistanceCenterS2CPacket"));
//        this.put(ClientboundChunkBatchFinishedPacket.class, new Info("chunkSent", "ChunkSentS2CPacket"));
//        this.put(ClientboundDebugChunkValuePacket.class, new Info("chunkValueDebug", "ChunkValueDebugS2CPacket"));
//        this.put(ClientboundClearTitlesPacket.class, new Info("clearTitle", "ClearTitleS2CPacket"));
//        this.put(ClientboundContainerClosePacket.class, new Info("closeScreen", "CloseScreenS2CPacket"));
//        this.put(ClientboundCommandSuggestionsPacket.class, new Info("commandSuggestions", "CommandSuggestionsS2CPacket"));
//        this.put(ClientboundCommandsPacket.class, new Info("commandTree", "CommandTreeS2CPacket"));
//        this.put(ClientboundCooldownPacket.class, new Info("cooldownUpdate", "CooldownUpdateS2CPacket"));
//        this.put(ClientboundPlaceGhostRecipePacket.class, new Info("craftFailedResponse", "CraftFailedResponseS2CPacket"));
//        this.put(ClientboundHurtAnimationPacket.class, new Info("damageTilt", "DamageTiltS2CPacket"));
//        this.put(ClientboundPlayerCombatKillPacket.class, new Info("deathMessage", "DeathMessageS2CPacket"));
//        this.put(ClientboundDebugSamplePacket.class, new Info("debugSample", "DebugSampleS2CPacket"));
//        this.put(ClientboundChangeDifficultyPacket.class, new Info("difficulty", "DifficultyS2CPacket"));
//        this.put(ClientboundPlayerCombatEndPacket.class, new Info("endCombat", "EndCombatS2CPacket"));
//        this.put(ClientboundPlayerCombatEnterPacket.class, new Info("enterCombat", "EnterCombatS2CPacket"));
//        this.put(ClientboundStartConfigurationPacket.class, new Info("enterReconfiguration", "EnterReconfigurationS2CPacket"));
//        this.put(ClientboundRemoveEntitiesPacket.class, new Info("entitiesDestroy", "EntitiesDestroyS2CPacket"));
//        this.put(ClientboundAnimatePacket.class, new Info("entityAnimation", "EntityAnimationS2CPacket"));
//        this.put(ClientboundSetEntityLinkPacket.class, new Info("entityAttach", "EntityAttachS2CPacket"));
//        this.put(ClientboundUpdateAttributesPacket.class, new Info("entityAttributes", "EntityAttributesS2CPacket"));
//        this.put(ClientboundDamageEventPacket.class, new Info("entityDamage", "EntityDamageS2CPacket"));
//        this.put(ClientboundSetEquipmentPacket.class, new Info("entityEquipmentUpdate", "EntityEquipmentUpdateS2CPacket"));
//        this.put(ClientboundSetPassengersPacket.class, new Info("entityPassengersSet", "EntityPassengersSetS2CPacket"));
//        this.put(ClientboundTeleportEntityPacket.class, new Info("entityPosition", "EntityPositionS2CPacket"));
//        this.put(ClientboundEntityPositionSyncPacket.class, new Info("entityPositionSync", "EntityPositionSyncS2CPacket"));
//        this.put(ClientboundMoveEntityPacket.class, new Info("entity", "EntityS2CPacket"));
//        this.put(ClientboundRotateHeadPacket.class, new Info("entitySetHeadYaw", "EntitySetHeadYawS2CPacket"));
//        this.put(ClientboundAddEntityPacket.class, new Info("entitySpawn", "EntitySpawnS2CPacket"));
//        this.put(ClientboundUpdateMobEffectPacket.class, new Info("entityStatusEffect", "EntityStatusEffectS2CPacket"));
//        this.put(ClientboundEntityEventPacket.class, new Info("entityStatus", "EntityStatusS2CPacket"));
//        this.put(ClientboundSetEntityDataPacket.class, new Info("entityTrackerUpdate", "EntityTrackerUpdateS2CPacket"));
//        this.put(ClientboundDebugEntityValuePacket.class, new Info("entityValueDebug", "EntityValueDebugS2CPacket"));
//        this.put(ClientboundSetEntityMotionPacket.class, new Info("entityVelocityUpdate", "EntityVelocityUpdateS2CPacket"));
//        this.put(ClientboundDebugEventPacket.class, new Info("eventDebug", "EventDebugS2CPacket"));
//        this.put(ClientboundSetExperiencePacket.class, new Info("experienceBarUpdate", "ExperienceBarUpdateS2CPacket"));
//        this.put(ClientboundExplodePacket.class, new Info("explosion", "ExplosionS2CPacket"));
//        this.put(ClientboundLoginPacket.class, new Info("gameJoin", "GameJoinS2CPacket"));
//        this.put(ClientboundSystemChatPacket.class, new Info("gameMessage", "GameMessageS2CPacket"));
//        this.put(ClientboundGameEventPacket.class, new Info("gameStateChange", "GameStateChangeS2CPacket"));
//        this.put(ClientboundGameTestHighlightPosPacket.class, new Info("gameTestHighlightPos", "GameTestHighlightPosS2CPacket"));
//        this.put(ClientboundSetHealthPacket.class, new Info("healthUpdate", "HealthUpdateS2CPacket"));
//        this.put(ClientboundContainerSetContentPacket.class, new Info("inventory", "InventoryS2CPacket"));
//        this.put(ClientboundTakeItemEntityPacket.class, new Info("itemPickupAnimation", "ItemPickupAnimationS2CPacket"));
//        this.put(ClientboundLightUpdatePacket.class, new Info("lightUpdate", "LightUpdateS2CPacket"));
//        this.put(ClientboundPlayerLookAtPacket.class, new Info("lookAt", "LookAtS2CPacket"));
//        this.put(ClientboundMapItemDataPacket.class, new Info("mapUpdate", "MapUpdateS2CPacket"));
//        this.put(ClientboundMoveMinecartPacket.class, new Info("moveMinecartAlongTrack", "MoveMinecartAlongTrackS2CPacket"));
//        this.put(ClientboundTagQueryPacket.class, new Info("nbtQueryResponse", "NbtQueryResponseS2CPacket"));
//        this.put(ClientboundMountScreenOpenPacket.class, new Info("openMountScreen", "OpenMountScreenS2CPacket"));
//        this.put(ClientboundOpenScreenPacket.class, new Info("openScreen", "OpenScreenS2CPacket"));
//        this.put(ClientboundOpenBookPacket.class, new Info("openWrittenBook", "OpenWrittenBookS2CPacket"));
//        this.put(ClientboundSetActionBarTextPacket.class, new Info("overlayMessage", "OverlayMessageS2CPacket"));
//        this.put(ClientboundLevelParticlesPacket.class, new Info("particle", "ParticleS2CPacket"));
//        this.put(ClientboundPlayerAbilitiesPacket.class, new Info("playerAbilities", "PlayerAbilitiesS2CPacket"));
//        this.put(ClientboundBlockChangedAckPacket.class, new Info("playerActionResponse", "PlayerActionResponseS2CPacket"));
//        this.put(ClientboundTabListPacket.class, new Info("playerListHeader", "PlayerListHeaderS2CPacket"));
//        this.put(ClientboundPlayerInfoUpdatePacket.class, new Info("playerList", "PlayerListS2CPacket"));
//        this.put(ClientboundPlayerPositionPacket.class, new Info("playerPositionLook", "PlayerPositionLookS2CPacket"));
//        this.put(ClientboundPlayerInfoRemovePacket.class, new Info("playerRemove", "PlayerRemoveS2CPacket"));
//        this.put(ClientboundRespawnPacket.class, new Info("playerRespawn", "PlayerRespawnS2CPacket"));
//        this.put(ClientboundPlayerRotationPacket.class, new Info("playerRotation", "PlayerRotationS2CPacket"));
//        this.put(ClientboundSetDefaultSpawnPositionPacket.class, new Info("playerSpawnPosition", "PlayerSpawnPositionS2CPacket"));
//        this.put(ClientboundSoundEntityPacket.class, new Info("playSoundFromEntity", "PlaySoundFromEntityS2CPacket"));
//        this.put(ClientboundSoundPacket.class, new Info("playSound", "PlaySoundS2CPacket"));
//        this.put(ClientboundDisguisedChatPacket.class, new Info("profilelessChatMessage", "ProfilelessChatMessageS2CPacket"));
//        this.put(ClientboundProjectilePowerPacket.class, new Info("projectilePower", "ProjectilePowerS2CPacket"));
//        this.put(ClientboundRecipeBookAddPacket.class, new Info("recipeBookAdd", "RecipeBookAddS2CPacket"));
//        this.put(ClientboundRecipeBookRemovePacket.class, new Info("recipeBookRemove", "RecipeBookRemoveS2CPacket"));
//        this.put(ClientboundRecipeBookSettingsPacket.class, new Info("recipeBookSettings", "RecipeBookSettingsS2CPacket"));
//        this.put(ClientboundRemoveMobEffectPacket.class, new Info("removeEntityStatusEffect", "RemoveEntityStatusEffectS2CPacket"));
//        this.put(ClientboundDeleteChatPacket.class, new Info("removeMessage", "RemoveMessageS2CPacket"));
//        this.put(ClientboundSetDisplayObjectivePacket.class, new Info("scoreboardDisplay", "ScoreboardDisplayS2CPacket"));
//        this.put(ClientboundSetObjectivePacket.class, new Info("scoreboardObjectiveUpdate", "ScoreboardObjectiveUpdateS2CPacket"));
//        this.put(ClientboundResetScorePacket.class, new Info("scoreboardScoreReset", "ScoreboardScoreResetS2CPacket"));
//        this.put(ClientboundSetScorePacket.class, new Info("scoreboardScoreUpdate", "ScoreboardScoreUpdateS2CPacket"));
//        this.put(ClientboundContainerSetDataPacket.class, new Info("screenHandlerPropertyUpdate", "ScreenHandlerPropertyUpdateS2CPacket"));
//        this.put(ClientboundContainerSetSlotPacket.class, new Info("screenHandlerSlotUpdate", "ScreenHandlerSlotUpdateS2CPacket"));
//        this.put(ClientboundSelectAdvancementsTabPacket.class, new Info("selectAdvancementTab", "SelectAdvancementTabS2CPacket"));
//        this.put(ClientboundServerDataPacket.class, new Info("serverMetadata", "ServerMetadataS2CPacket"));
//        this.put(ClientboundSetCameraPacket.class, new Info("setCameraEntity", "SetCameraEntityS2CPacket"));
//        this.put(ClientboundSetCursorItemPacket.class, new Info("setCursorItem", "SetCursorItemS2CPacket"));
//        this.put(ClientboundSetPlayerInventoryPacket.class, new Info("setPlayerInventory", "SetPlayerInventoryS2CPacket"));
//        this.put(ClientboundMerchantOffersPacket.class, new Info("setTradeOffers", "SetTradeOffersS2CPacket"));
//        this.put(ClientboundOpenSignEditorPacket.class, new Info("signEditorOpen", "SignEditorOpenS2CPacket"));
//        this.put(ClientboundSetSimulationDistancePacket.class, new Info("simulationDistance", "SimulationDistanceS2CPacket"));
//        this.put(ClientboundChunkBatchStartPacket.class, new Info("startChunkSend", "StartChunkSendS2CPacket"));
//        this.put(ClientboundAwardStatsPacket.class, new Info("statistics", "StatisticsS2CPacket"));
//        this.put(ClientboundStopSoundPacket.class, new Info("stopSound", "StopSoundS2CPacket"));
//        this.put(ClientboundSetSubtitleTextPacket.class, new Info("subtitle", "SubtitleS2CPacket"));
//        this.put(ClientboundUpdateRecipesPacket.class, new Info("synchronizeRecipes", "SynchronizeRecipesS2CPacket"));
//        this.put(ClientboundSetPlayerTeamPacket.class, new Info("team", "TeamS2CPacket"));
//        this.put(ClientboundTestInstanceBlockStatus.class, new Info("testInstanceBlockStatus", "TestInstanceBlockStatusS2CPacket"));
//        this.put(ClientboundTickingStepPacket.class, new Info("tickStep", "TickStepS2CPacket"));
//        this.put(ClientboundSetTitlesAnimationPacket.class, new Info("titleFade", "TitleFadeS2CPacket"));
//        this.put(ClientboundSetTitleTextPacket.class, new Info("title", "TitleS2CPacket"));
//        this.put(ClientboundForgetLevelChunkPacket.class, new Info("unloadChunk", "UnloadChunkS2CPacket"));
//        this.put(ClientboundSetHeldSlotPacket.class, new Info("updateSelectedSlot", "UpdateSelectedSlotS2CPacket"));
//        this.put(ClientboundTickingStatePacket.class, new Info("updateTickRate", "UpdateTickRateS2CPacket"));
//        this.put(ClientboundMoveVehiclePacket.class, new Info("vehicleMove", "VehicleMoveS2CPacket"));
//        this.put(ClientboundTrackedWaypointPacket.class, new Info("waypoint", "WaypointS2CPacket"));
//        this.put(ClientboundSetBorderCenterPacket.class, new Info("worldBorderCenterChanged", "WorldBorderCenterChangedS2CPacket"));
//        this.put(ClientboundInitializeBorderPacket.class, new Info("worldBorderInitialize", "WorldBorderInitializeS2CPacket"));
//        this.put(ClientboundSetBorderLerpSizePacket.class, new Info("worldBorderInterpolateSize", "WorldBorderInterpolateSizeS2CPacket"));
//        this.put(ClientboundSetBorderSizePacket.class, new Info("worldBorderSizeChanged", "WorldBorderSizeChangedS2CPacket"));
//        this.put(ClientboundSetBorderWarningDistancePacket.class, new Info("worldBorderWarningBlocksChanged", "WorldBorderWarningBlocksChangedS2CPacket"));
//        this.put(ClientboundSetBorderWarningDelayPacket.class, new Info("worldBorderWarningTimeChanged", "WorldBorderWarningTimeChangedS2CPacket"));
//        this.put(ClientboundLevelEventPacket.class, new Info("worldEvent", "WorldEventS2CPacket"));
//        this.put(ClientboundSetTimePacket.class, new Info("worldTimeUpdate", "WorldTimeUpdateS2CPacket"));
//    }};
//
//    public record Info(String id, String className) {}
//}
package io.github.itzispyder.clickcrystals.client.networking;


import net.minecraft.network.protocol.Packet;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PacketMapper {

    public static final Map<Class<? extends Packet<?>>, Info> C2S = new HashMap<>();
    public static final Map<Class<? extends Packet<?>>, Info> S2C = new HashMap<>();

    public record Info(String id, String className) {}

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("net/minecraft/network");

        if (resource == null)
            throw new RuntimeException("Failed to load PacketMapper resources");

        Path path = Paths.get(resource.getPath());
        Pattern packetClassPattern = Pattern.compile("(?<name>(Server|Client)Bound(?<id>.*))\\.class");
        try (Stream<Path> stream = Files.walk(path, FileVisitOption.FOLLOW_LINKS)) {
            stream.filter(p -> p.endsWith(".class")).forEach(p -> {
                Matcher matcher = packetClassPattern.matcher(p.getFileName().toString());
                if (!matcher.matches())
                    return;

                String name = matcher.group("name");
                String id = matcher.group("id");
                Info info = new Info(name, id);

                if (p.startsWith("ServerBound"))
                    loadInfo(C2S, classLoader, p, info);
                else if (p.startsWith("ClientBound"))
                    loadInfo(S2C, classLoader, p, info);
            });
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends Class<? extends Packet<?>>> void loadInfo(Map<T, Info> targetMapping, ClassLoader classLoader, Path classPath, Info info) {
        try {
            T packetClass = (T) classLoader.loadClass(classPath.getFileName().toString());
            targetMapping.put(packetClass, info);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}