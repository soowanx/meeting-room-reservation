package com.example.meetingroom.service;

import com.example.meetingroom.domain.Room;
import com.example.meetingroom.dto.RoomForm;
import com.example.meetingroom.exception.BusinessException;
import com.example.meetingroom.repository.RoomRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getActiveRooms() {
        return roomRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAllByOrderByActiveDescNameAsc();
    }

    public Room getRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("존재하지 않는 회의실입니다."));
    }

    @Transactional
    public Room createRoom(RoomForm form) {
        if (roomRepository.existsByNameIgnoreCase(form.getName().trim())) {
            throw new BusinessException("같은 이름의 회의실이 이미 존재합니다.");
        }
        Room room = new Room();
        room.setName(form.getName().trim());
        room.setLocation(form.getLocation().trim());
        room.setCapacity(form.getCapacity());
        room.setDescription(form.getDescription().trim());
        room.setActive(true);
        return roomRepository.save(room);
    }

    @Transactional
    public void toggleRoom(Long roomId) {
        Room room = getRoom(roomId);
        room.setActive(!room.isActive());
    }
}
