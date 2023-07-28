package org.mauriciohonorato.cinemaroom.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CinemaRoomDTO(@JsonProperty("total_rows") int totalRows,
                            @JsonProperty("total_columns") int totalColumns,
                            @JsonProperty("available_seats") List<SeatDTO> availableSeats) {}
