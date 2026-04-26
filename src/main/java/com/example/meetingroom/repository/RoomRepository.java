package com.example.meetingroom.repository;

import com.example.meetingroom.domain.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByOrderByActiveDescNameAsc();

    List<Room> findByActiveTrueOrderByNameAsc();

    boolean existsByNameIgnoreCase(String name);
}
