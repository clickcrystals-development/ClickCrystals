package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.events.events.world.BlockBreakEvent;
import io.github.itzispyder.clickcrystals.events.events.world.BlockPlaceEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.util.misc.Timer;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptedModule extends ListenerModule {

    public static final String PATH = "ClickCrystalsClient/scripts";
    public final List<ClickListener> clickListeners = new ArrayList<>();
    public final List<TickListener> tickListeners = new ArrayList<>();
    public final List<BlockPlaceListener> blockPlaceListeners = new ArrayList<>();
    public final List<BlockBreakListener> blockBreakListeners = new ArrayList<>();
    public final List<BlockPunchListener> blockPunchListeners = new ArrayList<>();
    public final List<BlockInteractListener> blockInteractListeners = new ArrayList<>();

    public ScriptedModule(String name, String description) {
        super(name, Categories.SCRIPTED, description);
    }

    @EventHandler
    public void onMouseClick(MouseClickEvent e) {
        if (e.isScreenNull()) {
            for (ClickListener l : clickListeners) {
                l.pass(e);
            }
        }
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        for (TickListener l : tickListeners) {
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
    public void onPacketSend(PacketSendEvent e) {
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
    }

    @FunctionalInterface
    public interface ClickListener {
        void pass(MouseClickEvent e);
    }

    @FunctionalInterface
    public interface TickListener {
        void pass(ClientTickEndEvent e);
    }

    @FunctionalInterface
    public interface BlockBreakListener {
        void pass(BlockBreakEvent e);
    }

    @FunctionalInterface
    public interface BlockPlaceListener {
        void pass(BlockPlaceEvent e);
    }

    @FunctionalInterface
    public interface BlockPunchListener {
        void pass(BlockPos pos, Direction dir);
    }

    @FunctionalInterface
    public interface BlockInteractListener {
        void pass(BlockHitResult hit, Hand hand);
    }

    public static void runModuleScripts() {
        File parentFolder = new File(PATH);
        if (!parentFolder.exists() || !parentFolder.isDirectory()) {
            parentFolder.mkdirs();
            return;
        }

        File[] files = parentFolder.listFiles(f -> f.isFile() && f.getPath().endsWith(".ccs"));
        if (files == null || files.length == 0) {
            return;
        }

        Timer timer = Timer.start();
        int total = files.length;

        system.printf("-> executing scripts ({x})...", total);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            new ClickScript(file).execute();
            system.printf("<- [{x}/{x}] '{x}'", i + 1, total, file.getName());
        }
        system.printf("<- [done] executed ({x}) scripts in {x}", total, timer.end().getStampPrecise());
    }
}
