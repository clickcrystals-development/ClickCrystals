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
