package software.ulpgc.is2.kata7.architecture.io;

import software.ulpgc.is2.kata7.architecture.model.Movie;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class DatabaseStore implements Store {
    private final Connection connection;

    public DatabaseStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stream<Movie> movies() {
        try {
            return moviesIn(resultSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet resultSet() throws SQLException {
        return connection.createStatement().executeQuery("Select * from movies");
    }

    private Stream<Movie> moviesIn(ResultSet resultSet) {
        return Stream.generate(() -> nextMovieIn(resultSet))
                .onClose(() -> close(resultSet))
                .takeWhile(Objects::nonNull);
    }

    private Movie nextMovieIn(ResultSet rs) {
        try {
            return rs.next() ? readMovieIn(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie readMovieIn(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getString(1),
                rs.getInt(3),
                rs.getInt(2)
        );
    }

    private void close(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
