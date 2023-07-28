package org.mauriciohonorato.cinemaroom.business;

import org.mauriciohonorato.cinemaroom.persistence.CinemaRoomRepository;
import org.mauriciohonorato.cinemaroom.presentation.CinemaRoomDTO;
import org.mauriciohonorato.cinemaroom.presentation.SeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaRoomService {
    private CinemaRoomRepository cinemaRoomRepository;
    private final String MGMT_PASSWORD = "super_secret";
    private final int CINEMA_ROWS = 9;
    private final int CINEMA_COLUMNS = 9;

    @Autowired
    public CinemaRoomService(CinemaRoomRepository cinemaRoomRepository) {
        this.cinemaRoomRepository = cinemaRoomRepository;
    }

    private List<Seat> getAllSeats() {
        return (List<Seat>) cinemaRoomRepository.findAll();
    }

    public CinemaRoomDTO getFormattedCinemaRoom() {
        List<SeatDTO> availableSeats = this.getAllSeats().stream()
                .map(this::convertSeatToDTO)
                .filter(this::isSeatAvailable)
                .toList();

        return new CinemaRoomDTO(this.CINEMA_ROWS, this.CINEMA_COLUMNS, availableSeats);
    }

    private SeatDTO convertSeatToDTO(Seat seatToConvert) {
        return new SeatDTO(seatToConvert.getRow(), seatToConvert.getColumn(),
                seatToConvert.isAvailable(), seatToConvert.getPrice());
    }

    private Seat convertDTOToSeat(SeatDTO seatDTOToConvert) {
        return cinemaRoomRepository.findByRowAndColumn(seatDTOToConvert.row(), seatDTOToConvert.column());
    }

    private void toggleSeatAvailability(SeatDTO seatDTO) {
        Seat seat = this.convertDTOToSeat(seatDTO);
        seat.setAvailable(!seat.isAvailable());
        cinemaRoomRepository.save(seat);
    }

    private void assignTokenToSeat(SeatDTO seatDTO, String token) {
        Seat seat = this.convertDTOToSeat(seatDTO);
        seat.setToken(token);
        cinemaRoomRepository.save(seat);
    }

    public boolean isSeatOutOfBounds(SeatDTO seatDTO) {
        return seatDTO.row() < 1 || seatDTO.row() > this.CINEMA_ROWS || seatDTO.column() < 1 || seatDTO.column() > this.CINEMA_COLUMNS;
    }

    public boolean isSeatAvailable(SeatDTO seatDTO) {
        Seat seat = this.convertDTOToSeat(seatDTO);
        return seat.isAvailable();
    }

    public int getSeatPrice(SeatDTO seatDTO) {
        Seat seat = this.convertDTOToSeat(seatDTO);
        return seat.getPrice();
    }

    public SeatDTO getSeatDTOByToken(String token) {
        return this.convertSeatToDTO(cinemaRoomRepository.getSeatByToken(token));
    }

    public boolean isValidToken(String token) {
        Seat seat = cinemaRoomRepository.getSeatByToken(token);
        return seat != null;
    }

    private void nullifyToken(SeatDTO seatDTO) {
        Seat seat = this.convertDTOToSeat(seatDTO);
        seat.setToken(null);
        cinemaRoomRepository.save(seat);
    }

    public void confirmSeatPurchase(SeatDTO chosenSeatDTO, String token) {
        this.toggleSeatAvailability(chosenSeatDTO);
        this.assignTokenToSeat(chosenSeatDTO, token);
    }

    public void confirmSeatReturn(SeatDTO seatToRefundDTO) {
        this.toggleSeatAvailability(seatToRefundDTO);
        this.nullifyToken(seatToRefundDTO);
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.equals(this.MGMT_PASSWORD);
    }

    public int getCurrentIncome() {
        return cinemaRoomRepository.getCurrentIncome();
    }

    public int getNumberOfAvailableSeats() {
        return cinemaRoomRepository.getNumberOfAvailableSeats();
    }

    public int getNumberOfPurchasedTickets() {
        return cinemaRoomRepository.getNumberOfPurchasedTickets();
    }
}
