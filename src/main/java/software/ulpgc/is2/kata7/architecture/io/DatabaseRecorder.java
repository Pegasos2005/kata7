package software.ulpgc.is2.kata7.architecture.io;

import software.ulpgc.is2.kata7.architecture.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

public class DatabaseRecorder implements Recorder {
    private final Connection connection;
    private final PreparedStatement preparedStatement;
    private int count = 0;

    public DatabaseRecorder(Connection connection) throws SQLException {
        this.connection = connection;
        this.createTableIfNotExists();
        this.preparedStatement = connection.prepareStatement("insert into " +
                "movies(title, duration, year) values (?, ?, ?)");
    }

    private void createTableIfNotExists() throws SQLException {
        connection.createStatement().execute("Create table if not exists " +
                "movies (title text, duration integer, year integer)");
    }

    @Override
    public void record(Stream<Movie> movies) {
        try {
            movies.forEach(this::record);
            flushBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void flushBatch() throws SQLException {
        preparedStatement.executeBatch();
        connection.commit();
    }

    private void record(Movie movie) {
        try {
            insert(movie);
            flushBacthIfRequiered();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void flushBacthIfRequiered() throws SQLException {
        if (++count % 10000 == 0) {
            preparedStatement.executeBatch();
        }
    }

    private void insert(Movie movie) throws SQLException {
        preparedStatement.setString(1, movie.title());
        preparedStatement.setInt(2, movie.duration());
        preparedStatement.setInt(3, movie.year());
        preparedStatement.addBatch();
    }
}
