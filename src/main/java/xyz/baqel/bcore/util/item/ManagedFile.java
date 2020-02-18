package xyz.baqel.bcore.util.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.baqel.bcore.bCore;

public class ManagedFile {

    public static void copyResourceAscii(String resourceName, File file) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(ManagedFile.class.getResourceAsStream(resourceName), StandardCharsets.UTF_8)) {
            MessageDigest digest = getDigest();
            try (DigestOutputStream digestStream = new DigestOutputStream(new FileOutputStream(file), digest); OutputStreamWriter writer = new OutputStreamWriter(digestStream, StandardCharsets.UTF_8)) {
                char[] buffer = new char[8192];
                int length;
                while ((length = reader.read(buffer)) >= 0) {
                    writer.write(buffer, 0, length);
                }
                writer.write("\n");
                writer.flush();
                digestStream.on(false);
                digestStream.write(35);
                digestStream.write(new BigInteger(1, digest.digest()).toString(16).getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    public static MessageDigest getDigest() throws IOException {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            throw new IOException(var1);
        }
    }
    private transient File file;

    public ManagedFile(String filename, JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), filename);
        if (!this.file.exists()) {
            try {
                copyResourceAscii('/' + filename, this.file);
            } catch (IOException var4) {
                plugin.getLogger().log(Level.SEVERE, "items.txt has not been loaded", var4);
            }
        }
    }

    public File getFile() {
        return this.file;
    }

    public List<String> getLines() {
        try (BufferedReader ex = Files.newBufferedReader(Paths.get(this.file.getPath(), new String[0]), StandardCharsets.UTF_8)) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = ex.readLine()) != null) {
                lines.add(line);
            }
            ArrayList<String> var3 = lines;
            return var3;
        } catch (IOException e) {
            bCore.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
