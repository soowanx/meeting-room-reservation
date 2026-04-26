package com.example.meetingroom.controller;

import com.example.meetingroom.domain.Room;
import com.example.meetingroom.dto.ReservationCreateForm;
import com.example.meetingroom.service.ReservationService;
import com.example.meetingroom.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    private final RoomService roomService;
    private final ReservationService reservationService;

    public HomeController(RoomService roomService, ReservationService reservationService) {
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("rooms", roomService.getActiveRooms());
        return "index";
    }

    @GetMapping("/rooms/{roomId}")
    public String roomDetail(
            @PathVariable Long roomId,
            @ModelAttribute("reservationForm") ReservationCreateForm reservationForm,
            Model model
    ) {
        Room room = roomService.getRoom(roomId);
        model.addAttribute("room", room);
        model.addAttribute("upcomingReservations", reservationService.getUpcomingReservationsForRoom(roomId));
        return "room-detail";
    }
}
