package io.github.itzispyder.clickcrystals.util.minecraft;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ReflectUtils {

    public static void reflectWalkClasses(ClassLoader loader, String packagePath, Consumer<Path> pathConsumer) {
        try {
            Enumeration<URL> resources = loader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Path path = Path.of(url.toURI());
                reflectWalkPath(path, pathConsumer);
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void reflectWalkPath(Path path, Consumer<Path> pathConsumer) throws IOException {
        try (Stream<Path> stream = Files.walk(path)) {
            stream.filter(it -> it.getFileName().toString().endsWith(".class")).forEach(pathConsumer);
        }
    }

    public static String pathAsClassLoaderString(Path classPath) {
        return classPath.toString()
                .replace(File.separatorChar, '.')
                .replace('/', '.')
                .substring(1, classPath.toString().length() - ".class".length());
    }
}
