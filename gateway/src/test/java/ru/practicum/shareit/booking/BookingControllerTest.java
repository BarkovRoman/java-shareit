package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {

    @MockBean
    private BookingClient bookingClient;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void testBookItemStartEnd() throws Exception {
        // given
        long userId = 1L;
        LocalDateTime start = LocalDateTime.now().plusSeconds(5);

        // when + then
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(new BookItemRequestDto(1L, start, start)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
    public void testBookItemStartBefore() throws Exception {
        // given
        long userId = 1L;
        LocalDateTime start = LocalDateTime.now().plusSeconds(5);

        // when + then
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(new BookItemRequestDto(1L, start.minusDays(1), start.plusMinutes(2))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ValidationException.class));
    }

    @Test
    public void update() throws Exception {
        // given
        long userId = 1L;
        long bookingId = 1L;

        // when + then
        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookingClient).update(userId, bookingId, true);
    }

    @Test
    public void getById() throws Exception {
        // given
        long userId = 1L;
        long bookingId = 1L;

        // when + then
        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookingClient).getBooking(userId, bookingId);
    }

    @Test
    public void getByBookerIdAndState() throws Exception {
        // given
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/bookings?state=ALL&from=0&size=10")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookingClient).getBookings(userId, BookingState.ALL,0, 10);
    }

    @Test
    public void getAllOwnerId() throws Exception {
        // given
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/bookings/owner?state=ALL&from=0&size=10")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookingClient).getOwnerId(userId, BookingState.ALL,0, 10);
    }

    @Test
    public void getAllOwnerIdSize() throws Exception {
        // given
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/bookings/owner?state=ALL&from=0&size=0")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }
}
