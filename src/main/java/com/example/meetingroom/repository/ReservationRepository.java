package com.example.meetingroom.repository;

import com.example.meetingroom.domain.Reservation;
import com.example.meetingroom.domain.ReservationStatus;
import com.example.meetingroom.domain.Room;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            select r
            from Reservation r
            where r.room = :room
              and r.status = :status
              and r.startAt < :endAt
              and r.endAt > :startAt
            """)
    List<Reservation> findOverlappingReservations(
            @Param("room") Room room,
            @Param("status") ReservationStatus status,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );

    List<Reservation> findByRoomIdAndStatusOrderByStartAtAsc(Long roomId, ReservationStatus status);

    @Query("""
            select r
            from Reservation r
            where lower(r.reserverEmail) = lower(:reserverEmail)
            order by r.startAt asc
            """)
    List<Reservation> findByReserverEmailOrderByStartAtAsc(@Param("reserverEmail") String reserverEmail);

    List<Reservation> findAllByOrderByStartAtAsc();
}
