package io.github.itzispyder.clickcrystals.scripting.format.components;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

public class DynamicallyFetchedGroupComponent<T extends GroupComponent> {

    private final String url;
    private Function<Document, T> transform;

    public DynamicallyFetchedGroupComponent(String url, Function<Document, T> transform) {
        this.url = url;
        this.transform = transform;
    }

    public DynamicallyFetchedGroupComponent(String url) {
        this(url, null);
    }

    public T fetch() {
        try {
            if (transform == null)
                throw new IllegalArgumentException("transformer is null");

            URL link = URI.create(url).toURL();
            Document document = Jsoup.parse(link, 0);
            return transform.apply(document);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setTransform(Function<Document, T> transform) {
        this.transform = transform;
    }

    public Function<Document, T> getTransform() {
        return transform;
    }

    public String getUrl() {
        return url;
    }
}
