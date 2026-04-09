package io.github.itzispyder.clickcrystals.client.networking;


import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.network.protocol.Packet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
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
        init();
    }

    public static void init() {
        try {
            ClassLoader classLoader = PacketMapper.class.getClassLoader();
            String pathString = "net/minecraft/network";
            Enumeration<URL> en = classLoader.getResources(pathString);

            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                Path path = Paths.get(url.toURI());
                scanPath(path, classLoader);
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private static void scanPath(Path root, ClassLoader classLoader) throws IOException {
        try (Stream<Path> stream = Files.walk(root)) {
            Pattern packetClassPattern = Pattern.compile("(?<name>(Server|Client)bound(?<id>.*))\\.class");
            stream.filter(p -> p.toString().endsWith(".class")).forEach(p -> {
                Matcher matcher = packetClassPattern.matcher(p.getFileName().toString());
                if (!matcher.matches())
                    return;

                String name = matcher.group("name");
                String id = matcher.group("id").toLowerCase();
                Info info = new Info(name, id);

//                ChatUtils.sendMessage(p.getFileName().toString());

                if (name.startsWith("Serverbound"))
                    loadInfo(C2S, classLoader, p, info);
                else if (name.startsWith("Clientbound"))
                    loadInfo(S2C, classLoader, p, info);
            });
        }
    }

    private static <T extends Class<? extends Packet<?>>> void loadInfo(Map<T, Info> targetMapping, ClassLoader classLoader, Path classPath, Info info) {
        try {
            String className = classPath.toString()
                    .replace(File.separatorChar, '.')
                    .replace('/', '.')
                    .substring(1, classPath.toString().length() - ".class".length());
            T packetClass = (T) classLoader.loadClass(className);
            targetMapping.put(packetClass, info);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void debugPrint() {
        ChatUtils.sendBlank(2);
        ChatUtils.sendPrefixMessage("Server Bound C2S (%s) Packets".formatted(C2S.size()));
        C2S.forEach((key, info) ->
                ChatUtils.sendMessage("%s: %s".formatted(info.id, info.className)));
        ChatUtils.sendBlank(1);
        ChatUtils.sendPrefixMessage("Client Bound S2C (%s) Packets".formatted(S2C.size()));
        S2C.forEach((key, info) ->
                ChatUtils.sendMessage("%s: %s".formatted(info.id, info.className)));
        ChatUtils.sendBlank(2);
    }
}