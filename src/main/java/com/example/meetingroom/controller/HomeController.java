package com.example.meetingroom.controller;

import com.example.meetingroom.domain.Room;
import com.example.meetingroom.domain.RoomType;
import com.example.meetingroom.domain.Reservation;
import com.example.meetingroom.dto.ReservationCreateForm;
import com.example.meetingroom.service.ReservationService;
import com.example.meetingroom.service.RoomService;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class HomeController {

    private static final int PAGE_SIZE = 6;

    private final RoomService roomService;
    private final ReservationService reservationService;

    public HomeController(RoomService roomService, ReservationService reservationService) {
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(required = false) List<RoomType> roomTypes,
            @RequestParam(required = false, defaultValue = "1") Integer minCapacity,
            @RequestParam(required = false, defaultValue = "80") Integer maxCapacity,
            @RequestParam(required = false) List<String> regions,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            Model model
    ) {
        int normalizedMin = Math.max(1, minCapacity == null ? 1 : minCapacity);
        int normalizedMax = Math.max(normalizedMin, maxCapacity == null ? 80 : maxCapacity);
        int requestedPage = page == null ? 1 : page;

        List<Room> filteredRooms = roomService.searchActiveRooms(roomTypes, normalizedMin, normalizedMax, regions);
        int totalRooms = filteredRooms.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalRooms / PAGE_SIZE));
        int currentPage = Math.min(Math.max(1, requestedPage), totalPages);
        int fromIndex = Math.min((currentPage - 1) * PAGE_SIZE, totalRooms);
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalRooms);
        List<Room> pageRooms = filteredRooms.subList(fromIndex, toIndex);

        model.addAttribute("rooms", pageRooms);
        model.addAttribute("roomTypes", roomService.getRoomTypes());
        model.addAttribute("regions", roomService.getRegions());
        model.addAttribute("selectedRoomTypes", roomTypes == null ? List.of() : roomTypes);
        model.addAttribute("selectedRegions", regions == null ? List.of() : regions);
        model.addAttribute("minCapacity", normalizedMin);
        model.addAttribute("maxCapacity", normalizedMax);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("pageNumbers", buildPageNumbers(totalPages));
        model.addAttribute("pageUrls", buildPageUrls(roomTypes, normalizedMin, normalizedMax, regions, totalPages));
        return "index";
    }

    @GetMapping("/rooms/{roomId}")
    public String roomDetail(
            @PathVariable Long roomId,
            @ModelAttribute("reservationForm") ReservationCreateForm reservationForm,
            Model model
    ) {
        Room room = roomService.getRoom(roomId);
        List<Reservation> upcomingReservations = reservationService.getUpcomingReservationsForRoom(roomId);
        model.addAttribute("room", room);
        model.addAttribute("upcomingReservations", upcomingReservations);
        model.addAttribute(
                "reservationSchedules",
                upcomingReservations.stream()
                        .map(this::toReservationSchedule)
                        .toList()
        );
        model.addAttribute(
                "initialStartAt",
                reservationForm.getStartAt() == null ? null : reservationForm.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        );
        model.addAttribute(
                "initialEndAt",
                reservationForm.getEndAt() == null ? null : reservationForm.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        );
        return "room-detail";
    }

    private List<Integer> buildPageNumbers(int totalPages) {
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        return pageNumbers;
    }

    private Map<Integer, String> buildPageUrls(
            List<RoomType> roomTypes,
            int minCapacity,
            int maxCapacity,
            List<String> regions,
            int totalPages
    ) {
        java.util.LinkedHashMap<Integer, String> urls = new java.util.LinkedHashMap<>();
        for (int page = 1; page <= totalPages; page++) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/")
                    .queryParam("minCapacity", minCapacity)
                    .queryParam("maxCapacity", maxCapacity)
                    .queryParam("page", page);
            if (roomTypes != null) {
                roomTypes.forEach(type -> builder.queryParam("roomTypes", type.name()));
            }
            if (regions != null) {
                regions.forEach(region -> builder.queryParam("regions", region));
            }
            urls.put(page, builder.build().encode().toUriString());
        }
        return urls;
    }

    private Map<String, String> toReservationSchedule(Reservation reservation) {
        return Map.of(
                "startAt", reservation.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                "endAt", reservation.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        );
    }
}
