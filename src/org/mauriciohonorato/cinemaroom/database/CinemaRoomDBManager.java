package org.mauriciohonorato.cinemaroom.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Component
public class CinemaRoomDBManager implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String jdbcUrl = "jdbc:h2:mem:cinema_db";
        String jdbcUser = "sa";
        String jdbcPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
            for (int row = 1; row <= 9; row++) {
                for (int column = 1; column <= 9; column++) {
                    String insertSeatSql = "INSERT INTO cinema_db (seat_row, seat_column, is_available, price, token) VALUES (?, ?, true, ?, NULL)";
                    try (PreparedStatement insertSeatStmt = connection.prepareStatement(insertSeatSql)) {
                        insertSeatStmt.setInt(1, row);
                        insertSeatStmt.setInt(2, column);
                        insertSeatStmt.setInt(3, (row > 4) ? 8 : 10);
                        insertSeatStmt.executeUpdate();
                    }
                }
            }
        }
    }
}
