package com.example.meetingroom.service;

import com.example.meetingroom.domain.Reservation;
import com.example.meetingroom.domain.ReservationStatus;
import com.example.meetingroom.domain.Room;
import com.example.meetingroom.dto.ReservationCreateForm;
import com.example.meetingroom.exception.BusinessException;
import com.example.meetingroom.repository.ReservationRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;

    public ReservationService(ReservationRepository reservationRepository, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
    }

    public List<Reservation> getUpcomingReservationsForRoom(Long roomId) {
        return reservationRepository.findByRoomIdAndStatusOrderByStartAtAsc(roomId, ReservationStatus.CONFIRMED)
                .stream()
                .filter(reservation -> reservation.getEndAt().isAfter(LocalDateTime.now()))
                .toList();
    }

    public List<Reservation> getReservationsByEmail(String email) {
        return reservationRepository.findByReserverEmailOrderByStartAtAsc(email.trim());
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAllByOrderByStartAtAsc();
    }

    @Transactional
    public Reservation createReservation(Long roomId, ReservationCreateForm form) {
        Room room = roomService.getRoom(roomId);
        if (!room.isActive()) {
            throw new BusinessException("현재 예약할 수 없는 회의실입니다.");
        }
        if (!form.getEndAt().isAfter(form.getStartAt())) {
            throw new BusinessException("종료 시간은 시작 시간보다 늦어야 합니다.");
        }

        List<Reservation> overlaps = reservationRepository.findOverlappingReservations(
                room,
                ReservationStatus.CONFIRMED,
                form.getStartAt(),
                form.getEndAt()
        );
        if (!overlaps.isEmpty()) {
            throw new BusinessException("이미 예약된 시간입니다. 다른 시간을 선택해주세요.");
        }

        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setReserverName(form.getReserverName().trim());
        reservation.setReserverEmail(form.getReserverEmail().trim().toLowerCase());
        reservation.setPurpose(form.getPurpose().trim());
        reservation.setStartAt(form.getStartAt());
        reservation.setEndAt(form.getEndAt());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelByUser(Long reservationId, String email) {
        Reservation reservation = getReservation(reservationId);
        if (!reservation.getReserverEmail().equalsIgnoreCase(email.trim())) {
            throw new BusinessException("예약자 이메일이 일치하지 않습니다.");
        }
        cancel(reservation);
    }

    @Transactional
    public void cancelByAdmin(Long reservationId) {
        cancel(getReservation(reservationId));
    }

    private void cancel(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new BusinessException("이미 취소된 예약입니다.");
        }
        reservation.setStatus(ReservationStatus.CANCELED);
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("존재하지 않는 예약입니다."));
    }
}
