package com.example.meetingroom.controller;

import com.example.meetingroom.dto.ReservationCreateForm;
import com.example.meetingroom.exception.BusinessException;
import com.example.meetingroom.service.ReservationService;
import com.example.meetingroom.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservationController {

    private final ReservationService reservationService;
    private final RoomService roomService;

    public ReservationController(ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    @PostMapping("/rooms/{roomId}/reservations")
    public String createReservation(
            @PathVariable Long roomId,
            @Valid @ModelAttribute("reservationForm") ReservationCreateForm reservationForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("room", roomService.getRoom(roomId));
            model.addAttribute("upcomingReservations", reservationService.getUpcomingReservationsForRoom(roomId));
            return "room-detail";
        }

        try {
            reservationService.createReservation(roomId, reservationForm);
            redirectAttributes.addFlashAttribute("successMessage", "예약이 완료되었습니다.");
            return "redirect:/rooms/" + roomId;
        } catch (BusinessException e) {
            model.addAttribute("room", roomService.getRoom(roomId));
            model.addAttribute("upcomingReservations", reservationService.getUpcomingReservationsForRoom(roomId));
            model.addAttribute("errorMessage", e.getMessage());
            return "room-detail";
        }
    }

    @GetMapping("/reservations/my")
    public String myReservations(@RequestParam(required = false) String email, Model model) {
        model.addAttribute("email", email);
        if (email != null && !email.isBlank()) {
            model.addAttribute("reservations", reservationService.getReservationsByEmail(email));
        }
        return "my-reservations";
    }

    @PostMapping("/reservations/{reservationId}/cancel")
    public String cancelReservation(
            @PathVariable Long reservationId,
            @RequestParam String email,
            RedirectAttributes redirectAttributes
    ) {
        try {
            reservationService.cancelByUser(reservationId, email);
            redirectAttributes.addFlashAttribute("successMessage", "예약이 취소되었습니다.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/reservations/my?email=" + email;
    }
}
