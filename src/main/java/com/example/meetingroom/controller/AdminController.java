package com.example.meetingroom.controller;

import com.example.meetingroom.config.AdminInterceptor;
import com.example.meetingroom.domain.RoomType;
import com.example.meetingroom.dto.AdminLoginForm;
import com.example.meetingroom.dto.RoomForm;
import com.example.meetingroom.exception.BusinessException;
import com.example.meetingroom.service.AdminAuthService;
import com.example.meetingroom.service.ReservationService;
import com.example.meetingroom.service.RoomService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private final AdminAuthService adminAuthService;
    private final RoomService roomService;
    private final ReservationService reservationService;

    public AdminController(
            AdminAuthService adminAuthService,
            RoomService roomService,
            ReservationService reservationService
    ) {
        this.adminAuthService = adminAuthService;
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    @GetMapping("/admin/login")
    public String loginPage(@ModelAttribute("loginForm") AdminLoginForm loginForm) {
        return "admin-login";
    }

    @PostMapping("/admin/login")
    public String login(
            @Valid @ModelAttribute("loginForm") AdminLoginForm loginForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "admin-login";
        }
        if (!adminAuthService.matches(loginForm.getPassword())) {
            model.addAttribute("errorMessage", "관리자 비밀번호가 올바르지 않습니다.");
            return "admin-login";
        }
        session.setAttribute(AdminInterceptor.ADMIN_SESSION_KEY, true);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(@ModelAttribute("roomForm") RoomForm roomForm, Model model) {
        populateDashboard(model);
        return "admin-dashboard";
    }

    @PostMapping("/admin/rooms")
    public String createRoom(
            @Valid @ModelAttribute("roomForm") RoomForm roomForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateDashboard(model);
            return "admin-dashboard";
        }
        try {
            roomService.createRoom(roomForm);
            redirectAttributes.addFlashAttribute("successMessage", "회의실이 등록되었습니다.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @ModelAttribute("roomTypes")
    public RoomType[] roomTypes() {
        return RoomType.values();
    }

    private void populateDashboard(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("reservations", reservationService.getAllReservations());
        model.addAttribute("regions", roomService.getRegions());
    }

    @PostMapping("/admin/rooms/{roomId}/toggle")
    public String toggleRoom(@PathVariable Long roomId, RedirectAttributes redirectAttributes) {
        roomService.toggleRoom(roomId);
        redirectAttributes.addFlashAttribute("successMessage", "회의실 상태를 변경했습니다.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/reservations/{reservationId}/cancel")
    public String cancelReservation(@PathVariable Long reservationId, RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelByAdmin(reservationId);
            redirectAttributes.addFlashAttribute("successMessage", "예약을 취소했습니다.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}
