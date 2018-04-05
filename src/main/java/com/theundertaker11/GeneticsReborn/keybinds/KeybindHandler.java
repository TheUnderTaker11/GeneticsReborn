package com.theundertaker11.geneticsreborn.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeybindHandler {

    public static KeyBinding keybindDragonsBreath;
    public static KeyBinding keybindTeleport;

    public static void init() {
        keybindDragonsBreath = new KeyBinding("Dragons Breath", Keyboard.KEY_H, "Genetics Reborn");
        ClientRegistry.registerKeyBinding(keybindDragonsBreath);
        keybindTeleport = new KeyBinding("Teleport", Keyboard.KEY_G, "Genetics Reborn");
        ClientRegistry.registerKeyBinding(keybindTeleport);
    }
}
