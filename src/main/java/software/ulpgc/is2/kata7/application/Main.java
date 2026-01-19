package software.ulpgc.is2.kata7.application;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import software.ulpgc.is2.kata7.architecture.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:movies.db");

        DatabaseMovieStore store = DatabaseMovieStore.with(connection);

        Javalin app = Javalin.create().start(7070);

        app.get("/movies", ctx -> movies(ctx, store));
    }

    private static void movies(@NotNull Context ctx, DatabaseMovieStore store) {
        String yearParam = ctx.queryParam("year");
        try {
            List<String> titles = store.search(yearParam);

            if (titles.isEmpty()) {
                ctx.status(404).result("No movies found");
                return;
            }

            ctx.result(String.join("\n", titles));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid year");
        }
    }
}
