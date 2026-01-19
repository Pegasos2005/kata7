package software.ulpgc.is2.kata7.architecture.io;

import software.ulpgc.is2.kata7.architecture.model.Movie;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class RemoteStore implements Store {
    private final String url;
    public final Function<String, Movie> parser;

    public RemoteStore(String url, Function<String, Movie> parser) {
        this.url = url;
        this.parser = parser;
    }

    @Override
    public Stream<Movie> movies() {
        try {
            return loadFrom(new URL(url).openConnection());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> loadFrom(URLConnection connection) throws IOException {
        return loadFrom(new GZIPInputStream(new BufferedInputStream(connection.getInputStream())));
    }

    private Stream<Movie> loadFrom(GZIPInputStream gzip) throws IOException {
        return loadFrom(new BufferedReader(new InputStreamReader(gzip)));
    }

    private Stream<Movie> loadFrom(BufferedReader reader) throws IOException {
        return reader.lines().skip(1).map(parser);
    }

}
