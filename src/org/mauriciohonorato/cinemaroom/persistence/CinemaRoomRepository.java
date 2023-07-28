package org.mauriciohonorato.cinemaroom.persistence;

import org.mauriciohonorato.cinemaroom.business.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRoomRepository extends CrudRepository<Seat, String> {
    @Query("SELECT COALESCE(SUM(s.price), 0) FROM Seat s WHERE s.isAvailable = false AND s.token IS NOT NULL")
    int getCurrentIncome();

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.isAvailable = true")
    int getNumberOfAvailableSeats();

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.isAvailable = false AND s.token IS NOT NULL")
    int getNumberOfPurchasedTickets();

    Seat findByRowAndColumn(int row, int column);
    Seat getSeatByToken(String token);
}
