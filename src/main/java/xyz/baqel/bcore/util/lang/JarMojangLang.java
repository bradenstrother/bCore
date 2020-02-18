package xyz.baqel.bcore.util.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.bukkit.inventory.ItemStack;

/**
 * The complete listing of translations can be found in your craftbukkit,
 * minecraft, or minecraft_server jar at /assets/minecraft/lang/
 */
public class JarMojangLang extends MojangLang {

    /**
     * This must be called after Craftbukkit loads, before you attempt to
     * access any other methods of this class.
     */
    @Override
    public void index(String minecraftVersion, Locale locale) throws IllegalArgumentException, IOException {
        super.index(minecraftVersion, locale);

        try (InputStream stream = ItemStack.class.getResourceAsStream("/assets/minecraft/lang/" + locale.toLanguageTag() + ".lang");
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            this.finallyIndex(locale, reader);
        }
    }
}