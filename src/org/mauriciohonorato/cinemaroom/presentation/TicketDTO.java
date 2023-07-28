package org.mauriciohonorato.cinemaroom.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TicketDTO {
    private String token;

    @JsonProperty("ticket")
    private SeatDTO seatDTO;

    public TicketDTO(SeatDTO seatDTO, String token) {
        this.seatDTO = seatDTO;
        this.token = token;
    }
}
