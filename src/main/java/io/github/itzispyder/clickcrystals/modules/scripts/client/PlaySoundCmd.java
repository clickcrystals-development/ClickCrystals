package io.github.itzispyder.clickcrystals.modules.scripts.client;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.OnEventCmd;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvent;

public class PlaySoundCmd extends ScriptCommand {

    public PlaySoundCmd() {
        super("playsound");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.match(0, "stop")) {
            mc.getSoundManager().stopAll();
            return;
        }

        SoundEvent soundEvent = OnEventCmd.parseSoundEvent(args.get(0).toString());

        if (soundEvent == null) {
            throw new IllegalArgumentException("unknown sound " + args.get(0));
        }

        SoundInstance sound;

        switch (args.getSize()) {
            case 1 -> sound = PositionedSoundInstance.master(soundEvent, 1.0F);
            case 2 -> sound = PositionedSoundInstance.master(soundEvent, 1.0F, args.get(1).toFloat());
            case 3 -> sound = PositionedSoundInstance.master(soundEvent, args.get(2).toFloat(), args.get(1).toFloat());
            default -> throw new IllegalArgumentException("incorrect usage for playsound");
        }

        mc.getSoundManager().play(sound);
    }
}
