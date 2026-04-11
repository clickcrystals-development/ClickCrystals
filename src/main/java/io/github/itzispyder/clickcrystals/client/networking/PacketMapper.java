package io.github.itzispyder.clickcrystals.client.networking;


import io.github.itzispyder.clickcrystals.client.system.Config;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.network.protocol.Packet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
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
        generatePacketLogFile();
    }

    public static void touch() {}

    private static void generatePacketLogFile() {
        File file = new File(Config.PATH_DATA, "network_packets.md");
        StringBuilder contents = new StringBuilder();

        contents.append("""
                    [<-- Back to Legend](./legend.md)
                    
                    # Network Packets
                    There are two types of network packets in Minecraft:
                    - Client to Server (c2s)
                    - Server to Client (s2c)
                    
                    For the arguments that you saw in our [scripting legend](./legend.md), these
                    network packets are what represent the `<client-packet>` and `<server-packet>` respectively.
                    
                    ### Packet ID Table
                    Here's a table of what those network packet arguments should be:
                    
                    | **Packet** | **Type** | **Scripting ID** |
                    |:----------:|:--------:|:---------------:|
                    """);

        var serverBound = C2S.values().stream().sorted(Comparator.comparing(Info::id)).toList();
        var clientBound = S2C.values().stream().sorted(Comparator.comparing(Info::id)).toList();

        serverBound.forEach(info -> contents.append("| %s | C2S | %s |%n".formatted(info.className, info.id)));
        contents.append("| | |");
        clientBound.forEach(info -> contents.append("| %s | S2C | %s |%n".formatted(info.className, info.id)));
        
        var sbId = serverBound.stream().map(Info::id).toList();
        var cbId = clientBound.stream().map(Info::id).toList();
        contents.append("""
                
                ### ServerBound Packet ID List
                %s
                
                ### ClientBound Packet ID List
                %s
                
                """.formatted(String.join(", ", sbId), String.join(", ", cbId)));

        FileValidationUtils.validate(file);
        FileValidationUtils.quickWrite(file, contents.toString());
    }

    private static void init() {
        try {
            ClassLoader classLoader = PacketMapper.class.getClassLoader();
            Enumeration<URL> en = classLoader.getResources("net/minecraft/network");

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
                String id = matcher.group("id");
                id = id.substring(0, 1).toLowerCase() + id.substring(1);
                id = id.replaceAll("(Packet)|[$]", "");
                Info info = new Info(id, name);

//                ChatUtils.sendMessage(p.getFileName().toString());

                if (name.startsWith("Serverbound"))
                    loadInfo(C2S, classLoader, p, info);
                else if (name.startsWith("Clientbound"))
                    loadInfo(S2C, classLoader, p, info);
            });
        }
    }

    private static <T extends Class<? extends Packet<?>>> void loadInfo(Map<T, Info> packetMapping, ClassLoader classLoader, Path classPath, Info packetInfo) {
        try {
            String className = classPath.toString()
                    .replace(File.separatorChar, '.')
                    .replace('/', '.')
                    .substring(1, classPath.toString().length() - ".class".length());
            T packetClass = (T) classLoader.loadClass(className);
            packetMapping.put(packetClass, packetInfo);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void debugPrint() {
        ChatUtils.sendBlank(2);
        ChatUtils.sendPrefixMessage("Server Bound C2S (%s) Packets: %s"
                .formatted(C2S.size(), ArrayUtils.list2string(C2S.values().stream()
                        .map(Info::id)
                        .sorted()
                        .toList())));
        ChatUtils.sendBlank(1);
        ChatUtils.sendPrefixMessage("Client Bound S2C (%s) Packets: %s"
                .formatted(S2C.size(), ArrayUtils.list2string(S2C.values().stream()
                        .map(Info::id)
                        .sorted()
                        .toList())));
        ChatUtils.sendBlank(2);
    }
}