package org.mauriciohonorato.cinemaroom.presentation;

import org.mauriciohonorato.cinemaroom.business.CinemaRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class CinemaRoomController {
    @Autowired
    private CinemaRoomService cinemaRoomService;

    @GetMapping("/seats")
    public CinemaRoomDTO getAvailableSeats() {
        return this.cinemaRoomService.getFormattedCinemaRoom();
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody SeatDTO chosenSeat) {
        if (cinemaRoomService.isSeatOutOfBounds(chosenSeat)) {
            return new ResponseEntity(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }

        if (!cinemaRoomService.isSeatAvailable(chosenSeat)) {
            return new ResponseEntity(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
        }

        int seatPrice = cinemaRoomService.getSeatPrice(chosenSeat);
        SeatDTO chosenSeatDTO = new SeatDTO(chosenSeat.row(),
                chosenSeat.column(),
                chosenSeat.isAvailable(),
                seatPrice
        );

        String token = UUID.randomUUID().toString();
        cinemaRoomService.confirmSeatPurchase(chosenSeatDTO, token);

        TicketDTO ticketDTO = new TicketDTO(chosenSeatDTO, token);
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<?> refundSeat(@RequestBody Map<String, String> tokenMap) {
        String token = tokenMap.get("token");

        if (cinemaRoomService.isValidToken(token)) {
            SeatDTO seatToRefundDTO = cinemaRoomService.getSeatDTOByToken(token);
            cinemaRoomService.confirmSeatReturn(seatToRefundDTO);

            return new ResponseEntity(Map.of("returned_ticket", seatToRefundDTO), HttpStatus.OK);
        }

        return new ResponseEntity(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getRevenueLogs(@RequestParam(required = false) String password) {
        if (cinemaRoomService.isPasswordValid(password)) {
            RevenueDTO revenueDTO = new RevenueDTO(cinemaRoomService.getCurrentIncome(),
                    cinemaRoomService.getNumberOfAvailableSeats(),
                    cinemaRoomService.getNumberOfPurchasedTickets()
            );
            return new ResponseEntity<>(revenueDTO, HttpStatus.OK);
        }

        return new ResponseEntity(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
    }
}
