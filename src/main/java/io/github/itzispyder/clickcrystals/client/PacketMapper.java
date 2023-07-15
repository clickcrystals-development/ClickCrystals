package io.github.itzispyder.clickcrystals.client;

import io.github.itzispyder.clickcrystals.util.ManualMap;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PacketMapper {

    private static final Map<Class<? extends Packet<?>>, String> c2s = ManualMap.fromItems(
            ClickSlotC2SPacket.class, "ClickSlotC2SPacket",
            ClientSettingsC2SPacket.class, "ClientSettingsC2SPacket",
            CustomPayloadC2SPacket.class, "CustomPayloadC2SPacket",
            CommandExecutionC2SPacket.class, "CommandExecutionC2SPacket",
            ClientCommandC2SPacket.class, "ClientCommandC2SPacket",
            ClientSettingsC2SPacket.class, "ClientSettingsC2SPacket",
            ChatMessageC2SPacket.class, "ChatMessageC2SPacket",
            ClientStatusC2SPacket.class, "ClientStatusC2SPacket",
            CraftRequestC2SPacket.class, "CraftRequestC2SPacket",
            CloseHandledScreenC2SPacket.class, "CloseHandledScreenC2SPacket",
            CreativeInventoryActionC2SPacket.class, "CreativeInventoryActionC2SPacket",
            PlayerMoveC2SPacket.class, "PlayerMoveC2SPacket",
            PlayerInteractItemC2SPacket.class, "PlayerInteractItemC2SPacket",
            PlayerInteractEntityC2SPacket.class, "PlayerInteractEntityC2SPacket",
            PlayerActionC2SPacket.class, "PlayerActionC2SPacket",
            PlayerInteractBlockC2SPacket.class, "PlayerInteractBlockC2SPacket",
            PlayerInputC2SPacket.class, "PlayerInputC2SPacket",
            PlayerSessionC2SPacket.class, "PlayerSessionC2SPacket",
            ResourcePackStatusC2SPacket.class, "ResourcePackStatusC2SPacket",
            AdvancementTabC2SPacket.class, "AdvancementTabC2SPacket",
            BoatPaddleStateC2SPacket.class, "BoatPaddleStateC2SPacket",
            JigsawGeneratingC2SPacket.class, "JigsawGeneratingC2SPacket",
            BookUpdateC2SPacket.class, "BookUpdateC2SPacket",
            HandSwingC2SPacket.class, "HandSwingC2SPacket",
            KeepAliveC2SPacket.class, "KeepAliveC2SPacket",
            MessageAcknowledgmentC2SPacket.class, "MessageAcknowledgmentC2SPacket",
            PickFromInventoryC2SPacket.class, "PickFromInventoryC2SPacket",
            PlayPongC2SPacket.class, "PlayPongC2SPacket",
            QueryBlockNbtC2SPacket.class, "QueryBlockNbtC2SPacket",
            QueryEntityNbtC2SPacket.class, "QueryEntityNbtC2SPacket",
            RecipeBookDataC2SPacket.class, "RecipeBookDataC2SPacket",
            RenameItemC2SPacket.class, "RenameItemC2SPacket",
            SelectMerchantTradeC2SPacket.class, "SelectMerchantTradeC2SPacket",
            SpectatorTeleportC2SPacket.class, "SpectatorTeleportC2SPacket",
            UpdateBeaconC2SPacket.class, "UpdateBeaconC2SPacket",
            UpdateDifficultyC2SPacket.class, "UpdateDifficultyC2SPacket",
            UpdateDifficultyLockC2SPacket.class, "UpdateDifficultyLockC2SPacket",
            UpdateJigsawC2SPacket.class, "UpdateJigsawC2SPacket",
            UpdatePlayerAbilitiesC2SPacket.class, "UpdatePlayerAbilitiesC2SPacket",
            UpdateSelectedSlotC2SPacket.class, "UpdateSelectedSlotC2SPacket",
            UpdateSignC2SPacket.class, "UpdateSignC2SPacket",
            VehicleMoveC2SPacket.class, "VehicleMoveC2SPacket",
            ButtonClickC2SPacket.class, "ButtonClickC2SPacket",
            TeleportConfirmC2SPacket.class, "TeleportConfirmC2SPacket",
            RequestCommandCompletionsC2SPacket.class, "RequestCommandCompletionsC2SPacket",
            RecipeCategoryOptionsC2SPacket.class, "RecipeCategoryOptionsC2SPacket",
            UpdateCommandBlockC2SPacket.class, "UpdateCommandBlockC2SPacket",
            UpdateCommandBlockMinecartC2SPacket.class, "UpdateCommandBlockMinecartC2SPacket"
    );

    private static final Map<Class<? extends Packet<?>>, String> s2c = ManualMap.fromItems(
            StatisticsS2CPacket.class, "StatisticsS2CPacket",
            SubtitleS2CPacket.class, "SubtitleS2CPacket",
            ScoreboardDisplayS2CPacket.class, "ScoreboardDisplayS2CPacket",
            ServerMetadataS2CPacket.class, "ServerMetadataS2CPacket",
            StopSoundS2CPacket.class, "StopSoundS2CPacket",
            SimulationDistanceS2CPacket.class, "SimulationDistanceS2CPacket",
            SynchronizeRecipesS2CPacket.class, "SynchronizeRecipesS2CPacket",
            SynchronizeTagsS2CPacket.class, "SynchronizeTagsS2CPacket",
            SetCameraEntityS2CPacket.class, "SetCameraEntityS2CPacket",
            ScoreboardObjectiveUpdateS2CPacket.class, "ScoreboardObjectiveUpdateS2CPacket",
            ScoreboardPlayerUpdateS2CPacket.class, "ScoreboardPlayerUpdateS2CPacket",
            SelectAdvancementTabS2CPacket.class, "SelectAdvancementTabS2CPacket",
            SetTradeOffersS2CPacket.class, "SetTradeOffersS2CPacket",
            SignEditorOpenS2CPacket.class, "SignEditorOpenS2CPacket",
            ScreenHandlerPropertyUpdateS2CPacket.class, "ScreenHandlerPropertyUpdateS2CPacket",
            ScreenHandlerSlotUpdateS2CPacket.class, "ScreenHandlerSlotUpdateS2CPacket",
            EntityVelocityUpdateS2CPacket.class, "EntityVelocityUpdateS2CPacket",
            ParticleS2CPacket.class, "ParticleS2CPacket",
            WorldEventS2CPacket.class, "WorldEventS2CPacket",
            WorldTimeUpdateS2CPacket.class, "WorldTimeUpdateS2CPacket",
            EntityDamageS2CPacket.class, "EntityDamageS2CPacket",
            DisconnectS2CPacket.class, "DisconnectS2CPacket",
            ChatMessageS2CPacket.class, "ChatMessageS2CPacket",
            BlockEventS2CPacket.class, "BlockEventS2CPacket",
            BlockBreakingProgressS2CPacket.class, "BlockBreakingProgressS2CPacket",
            InventoryS2CPacket.class, "InventoryS2CPacket",
            BossBarS2CPacket.class, "BossBarS2CPacket",
            AdvancementUpdateS2CPacket.class, "AdvancementUpdateS2CPacket",
            BlockEntityUpdateS2CPacket.class, "BlockEntityUpdateS2CPacket",
            BlockUpdateS2CPacket.class, "BlockUpdateS2CPacket",
            BundleS2CPacket.class, "BundleS2CPacket",
            ChunkBiomeDataS2CPacket.class, "ChunkBiomeDataS2CPacket",
            ChunkDataS2CPacket.class, "ChunkDataS2CPacket",
            ChunkDeltaUpdateS2CPacket.class, "ChunkDeltaUpdateS2CPacket",
            ChunkLoadDistanceS2CPacket.class, "ChunkLoadDistanceS2CPacket",
            ChunkRenderDistanceCenterS2CPacket.class, "ChunkRenderDistanceCenterS2CPacket",
            ClearTitleS2CPacket.class, "ClearTitleS2CPacket",
            CommandTreeS2CPacket.class, "CommandTreeS2CPacket",
            CooldownUpdateS2CPacket.class, "CooldownUpdateS2CPacket",
            CraftFailedResponseS2CPacket.class, "CraftFailedResponseS2CPacket",
            CustomPayloadS2CPacket.class, "CustomPayloadS2CPacket",
            DamageTiltS2CPacket.class, "DamageTiltS2CPacket",
            DeathMessageS2CPacket.class, "DeathMessageS2CPacket",
            DifficultyS2CPacket.class, "DifficultyS2CPacket",
            EndCombatS2CPacket.class, "EndCombatS2CPacket",
            EnterCombatS2CPacket.class, "EnterCombatS2CPacket",
            EntitiesDestroyS2CPacket.class, "EntitiesDestroyS2CPacket",
            EntityAnimationS2CPacket.class, "EntityAnimationS2CPacket",
            EntityAttachS2CPacket.class, "EntityAttachS2CPacket",
            EntityAttributesS2CPacket.class, "EntityAttributesS2CPacket",
            EntityEquipmentUpdateS2CPacket.class, "EntityEquipmentUpdateS2CPacket",
            EntityPositionS2CPacket.class, "EntityPositionS2CPacket",
            EntityS2CPacket.class, "EntityS2CPacket",
            EntityTrackerUpdateS2CPacket.class, "EntityTrackerUpdateS2CPacket",
            ExplosionS2CPacket.class, "ExplosionS2CPacket",
            ExperienceBarUpdateS2CPacket.class, "ExperienceBarUpdateS2CPacket",
            FeaturesS2CPacket.class, "FeaturesS2CPacket",
            GameJoinS2CPacket.class, "GameJoinS2CPacket",
            GameMessageS2CPacket.class, "GameMessageS2CPacket",
            HealthUpdateS2CPacket.class, "HealthUpdateS2CPacket",
            ItemPickupAnimationS2CPacket.class, "ItemPickupAnimationS2CPacket",
            LightUpdateS2CPacket.class, "LightUpdateS2CPacket",
            LookAtS2CPacket.class, "LookAtS2CPacket",
            MapUpdateS2CPacket.class, "MapUpdateS2CPacket",
            NbtQueryResponseS2CPacket.class, "NbtQueryResponseS2CPacket",
            PlayerActionResponseS2CPacket.class, "PlayerActionResponseS2CPacket",
            PlayerListHeaderS2CPacket.class, "PlayerListHeaderS2CPacket",
            PlayerListS2CPacket.class, "PlayerListS2CPacket",
            OpenHorseScreenS2CPacket.class, "OpenHorseScreenS2CPacket",
            OpenScreenS2CPacket.class, "OpenScreenS2CPacket",
            OpenWrittenBookS2CPacket.class, "OpenWrittenBookS2CPacket",
            EntitySpawnS2CPacket.class, "EntitySpawnS2CPacket",
            PlayerSpawnPositionS2CPacket.class, "PlayerSpawnPositionS2CPacket",
            PlayerSpawnS2CPacket.class, "PlayerSpawnS2CPacket",
            PlayerRemoveS2CPacket.class, "PlayerRemoveS2CPacket",
            GameStateChangeS2CPacket.class, "GameStateChangeS2CPacket",
            PlaySoundFromEntityS2CPacket.class, "PlaySoundFromEntityS2CPacket",
            EntitySetHeadYawS2CPacket.class, "EntitySetHeadYawS2CPacket",
            WorldBorderSizeChangedS2CPacket.class, "WorldBorderSizeChangedS2CPacket",
            WorldBorderInterpolateSizeS2CPacket.class, "WorldBorderInterpolateSizeS2CPacket",
            EntityStatusEffectS2CPacket.class, "EntityStatusEffectS2CPacket",
            RemoveEntityStatusEffectS2CPacket.class, "RemoveEntityStatusEffectS2CPacket",
            ExperienceOrbSpawnS2CPacket.class, "ExperienceOrbSpawnS2CPacket",
            ChatSuggestionsS2CPacket.class, "ChatSuggestionsS2CPacket",
            UnloadChunkS2CPacket.class, "UnloadChunkS2CPacket",
            ProfilelessChatMessageS2CPacket.class, "ProfilelessChatMessageS2CPacket",
            RemoveMessageS2CPacket.class, "RemoveMessageS2CPacket",
            PlayerPositionLookS2CPacket.class, "PlayerPositionLookS2CPacket",
            VehicleMoveS2CPacket.class, "VehicleMoveS2CPacket",
            TeamS2CPacket.class, "TeamS2CPacket",
            TitleFadeS2CPacket.class, "TitleFadeS2CPacket",
            TitleS2CPacket.class, "TitleS2CPacket",
            UnlockRecipesS2CPacket.class, "UnlockRecipesS2CPacket",
            WorldBorderInitializeS2CPacket.class, "WorldBorderInitializeS2CPacket",
            WorldBorderWarningBlocksChangedS2CPacket.class, "WorldBorderWarningBlocksChangedS2CPacket",
            WorldBorderWarningTimeChangedS2CPacket.class, "WorldBorderWarningTimeChangedS2CPacket",
            EntityPassengersSetS2CPacket.class, "EntityPassengersSetS2CPacket",
            PlaySoundS2CPacket.class, "PlaySoundS2CPacket",
            ResourcePackSendS2CPacket.class, "ResourcePackSendS2CPacket"
    );

    public static Map<Class<? extends Packet<?>>, String> getC2S() {
        return new HashMap<>(c2s);
    }

    public static Map<Class<? extends Packet<?>>, String> getS2C() {
        return new HashMap<>(s2c);
    }

    public static Set<Class<? extends Packet<?>>> getC2SPackets() {
        return getC2S().keySet();
    }

    public static Set<Class<? extends Packet<?>>> getS2CPackets() {
        return getS2C().keySet();
    }

    public static List<String> getC2SNames() {
        return getC2S().values().stream().toList();
    }

    public static List<String> getS2CNames() {
        return getS2C().values().stream().toList();
    }
}
