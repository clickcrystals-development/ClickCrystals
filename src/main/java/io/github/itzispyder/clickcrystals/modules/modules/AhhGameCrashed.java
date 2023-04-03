package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

/**
 * AhhGameCrashed module
 */
public class AhhGameCrashed extends Module {

    public AhhGameCrashed() {
        super("AhhGameCrashed","\"oops, my game crashed i swear! rematch?\"");
    }

    @Override
    protected void onEnable() {
        super.setEnabled(false);
        CrashReport cr = new CrashReport("eeeEEEEeeeEe how did this happen", new IllegalStateException());
        throw new CrashException(cr);
    }

    @Override
    protected void onDisable() {

    }
}
