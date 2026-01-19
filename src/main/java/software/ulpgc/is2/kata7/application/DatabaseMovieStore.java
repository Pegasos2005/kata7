package software.ulpgc.is2.kata7.application;

import software.ulpgc.is2.kata7.architecture.io.DatabaseRecorder;
import software.ulpgc.is2.kata7.architecture.io.MovieParserFromTsv;
import software.ulpgc.is2.kata7.architecture.io.RemoteStore;
import software.ulpgc.is2.kata7.architecture.model.Movie;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DatabaseMovieStore {
    private final Connection connection;
    private static final String url = "https://datasets.imdbws.com/title.basics.tsv.gz";
    private static final String database = "movies.db";

    private DatabaseMovieStore(Connection connection) {
        this.connection = connection;
    }

    public static DatabaseMovieStore with(Connection connection) throws SQLException {
        if (isDatabaseEmpty()) importMoviesInto(connection);
        return new DatabaseMovieStore(connection);
    }

    private static void importMoviesInto(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        Stream<Movie> movies = new RemoteStore(url, MovieParserFromTsv::fromTsv).movies();
        new DatabaseRecorder(connection).record(movies);

        connection.createStatement().execute("CREATE INDEX IF NOT EXISTS idx_year ON movies(year)");
        connection.commit();
        connection.setAutoCommit(true);
    }

    private static boolean isDatabaseEmpty() {
        return new File(database).length() == 0;
    }

    public List<String> search(String yearParam) {
        if (yearParam != null) {
            return queryByYear(Integer.parseInt(yearParam));
        }
        return queryAll();
    }

    private List<String> queryAll() {
        return query("SELECT title FROM movies LIMIT 100");
    }

    private List<String> queryByYear(int year) {
        return query("SELECT title FROM movies WHERE year = " + year);
    }

    private List<String> query(String sql) {
        List<String> result = new ArrayList<>();
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()){
                result.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
