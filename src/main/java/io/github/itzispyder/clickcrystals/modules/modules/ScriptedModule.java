package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.client.networking.PacketMapper;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.*;
import io.github.itzispyder.clickcrystals.events.events.networking.GameJoinEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.GameLeaveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.events.events.world.*;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.syntax.listeners.*;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.Timer;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScriptedModule extends ListenerModule {

    public final List<ClickListener> clickListeners = new ArrayList<>();
    public final List<KeyListener> keyListeners = new ArrayList<>();
    public final List<MoveListener> moveListeners = new ArrayList<>();
    public final List<TickListener> preTickListeners = new ArrayList<>();
    public final List<TickListener> tickListeners = new ArrayList<>();
    public final List<TickListener> postTickListeners = new ArrayList<>();
    public final List<BlockPlaceListener> blockPlaceListeners = new ArrayList<>();
    public final List<BlockBreakListener> blockBreakListeners = new ArrayList<>();
    public final List<BlockPunchListener> blockPunchListeners = new ArrayList<>();
    public final List<BlockInteractListener> blockInteractListeners = new ArrayList<>();
    public final List<ItemUseListener> itemUseListeners = new ArrayList<>();
    public final List<ItemConsumeListener> itemConsumeListeners = new ArrayList<>();
    public final List<ChatReceiveListener> chatReceiveListeners = new ArrayList<>();
    public final List<ChatSendListener> chatSendListeners = new ArrayList<>();
    public final List<PacketListener> packetReceiveListeners = new ArrayList<>();
    public final List<PacketListener> packetSendListeners = new ArrayList<>();
    public final List<SoundPlayListener> soundPlayListeners = new ArrayList<>();
    public final List<Runnable> totemPopListeners = new ArrayList<>();
    public final List<Runnable> moduleEnableListeners = new ArrayList<>();
    public final List<Runnable> moduleDisableListeners = new ArrayList<>();
    public final List<Runnable> damageListeners = new ArrayList<>();
    public final List<Runnable> respawnListeners = new ArrayList<>();
    public final List<Runnable> deathListeners = new ArrayList<>();
    public final List<Runnable> gameJoinListeners = new ArrayList<>();
    public final List<Runnable> gameLeaveListeners = new ArrayList<>();
    public final List<Runnable> mouseWheelUpListeners = new ArrayList<>();
    public final List<Runnable> mouseWheelDownListeners = new ArrayList<>();
    public final List<Runnable> mouseWheelListeners = new ArrayList<>();


    public final String filepath, filename, parentFolder;
    public final Set<PacketMapper.Info> packetSendCancelQueue = new HashSet<>();
    public final Set<PacketMapper.Info> packetReadCancelQueue = new HashSet<>();


    public ScriptedModule(String name, String description, File file) {
        super(name, Categories.SCRIPTED, description);
        this.filepath = file.getPath();
        this.filename = file.getName();

        File parent = file.getParentFile();
        this.parentFolder = parent == null ? null : parent.getPath();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        moduleEnableListeners.forEach(Runnable::run);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        moduleDisableListeners.forEach(Runnable::run);
    }

    public void clearListeners() {
        clickListeners.clear();
        keyListeners.clear();
        moveListeners.clear();
        preTickListeners.clear();
        tickListeners.clear();
        postTickListeners.clear();

        totemPopListeners.clear();
        moduleEnableListeners.clear();
        moduleDisableListeners.clear();
        damageListeners.clear();
        respawnListeners.clear();
        deathListeners.clear();
        gameJoinListeners.clear();
        gameLeaveListeners.clear();

        blockPlaceListeners.clear();
        blockBreakListeners.clear();
        blockPunchListeners.clear();
        blockInteractListeners.clear();

        itemUseListeners.clear();
        itemConsumeListeners.clear();
        chatReceiveListeners.clear();
        chatSendListeners.clear();
        
        packetSendCancelQueue.clear();
        packetReadCancelQueue.clear();
        packetReceiveListeners.clear();
        packetSendListeners.clear();

        soundPlayListeners.clear();
        mouseWheelListeners.clear();
        mouseWheelUpListeners.clear();
        mouseWheelDownListeners.clear();
    }

    @EventHandler
    public void onMouseClick(MouseClickEvent e) {
        for (ClickListener l : clickListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onMouseScroll(MouseScrollEvent e) {
        double amount = e.getDeltaY();
        if (amount > 0) {
            mouseWheelListeners.forEach(Runnable::run);
            mouseWheelUpListeners.forEach(Runnable::run);
        }
        else if (amount < 0) {
            mouseWheelListeners.forEach(Runnable::run);
            mouseWheelDownListeners.forEach(Runnable::run);
        }
    }

    @EventHandler
    public void onTick(ClientTickStartEvent e) {
        for (TickListener l : preTickListeners) {
            l.pass(e);
        }
        for (TickListener l : tickListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onPostTick(ClientTickEndEvent e) {
        for (TickListener l : postTickListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        for (BlockPlaceListener l : blockPlaceListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        for (BlockBreakListener l : blockBreakListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onItemUse(ItemUseEvent e) {
        for (ItemUseListener l : itemUseListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onItemConsume(ItemConsumeEvent e) {
        for (ItemConsumeListener l : itemConsumeListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onChatReceive(ChatReceiveEvent e) {
        for (ChatReceiveListener l : chatReceiveListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onChatSend(ChatSendEvent e) {
        for (ChatSendListener l : chatSendListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onSoundPlay(SoundPlayEvent e) {
        for (SoundPlayListener l : soundPlayListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onPacketSend(PacketSendEvent e) {
        if (PlayerUtils.invalid())
            return;

        if (!packetSendCancelQueue.isEmpty()) {
            var target = e.getPacket().getClass();
            for (PacketMapper.Info info: new ArrayList<>(packetSendCancelQueue)) {
                if (info != PacketMapper.C2S.get(target))
                    continue;
                e.setCancelled(true);
                packetSendCancelQueue.remove(info);
                break;
            }
        }

        if (e.getPacket() != null) {
            for (PacketListener l : packetSendListeners) {
                l.pass(e.getPacket());
            }
        }

        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            for (BlockInteractListener l : blockInteractListeners) {
                l.pass(packet.getBlockHitResult(), packet.getHand());
            }
        }
        else if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                for (BlockPunchListener l : blockPunchListeners) {
                    l.pass(packet.getPos(), packet.getDirection());
                }
            }
        }
        else if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            for (MoveListener l : moveListeners) {
                l.pass(packet);
            }
        }
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        if (PlayerUtils.invalid())
            return;

        if (!packetReadCancelQueue.isEmpty()) {
            var target = e.getPacket().getClass();
            for (PacketMapper.Info info: new ArrayList<>(packetReadCancelQueue)) {
                if (info != PacketMapper.S2C.get(target))
                    continue;
                e.setCancelled(true);
                packetReadCancelQueue.remove(info);
                break;
            }
        }

        if (e.getPacket() != null) {
            for (PacketListener l : packetReceiveListeners) {
                l.pass(e.getPacket());
            }
        }

        var target = e.getPacket().getClass();
        for (PacketMapper.Info info: new ArrayList<>(packetReadCancelQueue)) {
            if (info == PacketMapper.S2C.get(target)) {
                e.setCancelled(true);
                packetReadCancelQueue.remove(info);
            }
        }

        if (e.getPacket() instanceof EntityStatusS2CPacket packet) {
            if (packet.getEntity(PlayerUtils.getWorld()) instanceof PlayerEntity p && p.getId() == PlayerUtils.player().getId()) {
                if (packet.getStatus() == EntityStatuses.USE_TOTEM_OF_UNDYING) {
                    totemPopListeners.forEach(Runnable::run);
                }
                else if (packet.getStatus() == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
                    deathListeners.forEach(Runnable::run);
                }
            }
        }
        else if (e.getPacket() instanceof PlayerRespawnS2CPacket) {
            respawnListeners.forEach(Runnable::run);
        }
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent e) {
        for (KeyListener l : keyListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.isSelf()) {
            damageListeners.forEach(Runnable::run);
        }
    }

    @EventHandler
    public void onGameJoin(GameJoinEvent e) {
        gameJoinListeners.forEach(Runnable::run);
    }

    @EventHandler
    public void onGameLeave(GameLeaveEvent e) {
        gameLeaveListeners.forEach(Runnable::run);
    }


    public static int runModuleScripts() {
        File parentFolder = new File(Config.PATH_SCRIPTS);
        if (!parentFolder.exists() || !parentFolder.isDirectory()) {
            parentFolder.mkdirs();
            return 0;
        }

        List<File> files = listScriptFiles(parentFolder);
        Timer timer = Timer.start();
        int total = files.size();

        system.printf("-> executing scripts (%s)...", total);
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            new ClickScript(file).execute();
            system.printf("<- [%s/%s] '%s'", i + 1, total, file.getName());
        }
        system.printf("<- [done] executed (%s) scripts in %s", total, timer.end().getStampPrecise());
        return total;
    }

    public static List<File> listScriptFiles(File parentFolder) {
        File[] filesArray = parentFolder.listFiles();
        if (filesArray == null || filesArray.length == 0) {
            return new ArrayList<>();
        }
        List<File> files = new ArrayList<>();

        for (File file : filesArray) {
            if (file == null) {
                continue;
            }

            String name = file.getName();
            boolean nameValid = name.endsWith(".ccs") || name.endsWith(".txt");

            if (file.isFile() && nameValid) {
                files.add(file);
            }
            else if (file.isDirectory()) {
                files.addAll(listScriptFiles(file));
            }
        }
        return files;
    }
}
