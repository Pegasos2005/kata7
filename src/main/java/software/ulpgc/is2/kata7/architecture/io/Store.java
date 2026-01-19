package software.ulpgc.is2.kata7.architecture.io;

import software.ulpgc.is2.kata7.architecture.model.Movie;

import java.util.stream.Stream;

public interface Store {
    Stream<Movie> movies();
}
