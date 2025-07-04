package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemRequestDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    public BookingDto addBooking(
            @RequestBody BookingItemRequestDto bookingRequestDto,
            @Valid @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пришел DTO {}  ", bookingRequestDto);
        return bookingService.addBooking(bookingRequestDto, userId);
    }

    @GetMapping
    public List<BookingDto> getAllUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingDtoByUserId(userId, BookingState.ALL);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingByBookingId(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("запрос public BookingDto getBookingByBookingId");
        return bookingService.getBookingDto(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingDtoByUserId(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam boolean approved,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("Запрос на обновление Approved");
        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }

}
