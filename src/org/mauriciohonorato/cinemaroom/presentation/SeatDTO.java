package org.mauriciohonorato.cinemaroom.presentation;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SeatDTO(int row, int column, @JsonIgnore boolean isAvailable, int price) {}
