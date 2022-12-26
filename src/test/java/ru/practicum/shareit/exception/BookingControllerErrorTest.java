package ru.practicum.shareit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void saveNewBookingStartEnd() throws Exception {
        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(1L).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now()).build();
        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 1L)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    @Test
    public void saveNewBookingNotUser() throws Exception {
        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(1L).status(BookingStatus.REJECTED)
                .start(LocalDateTime.now().plusSeconds(2)).end(LocalDateTime.now().plusSeconds(20)).build();
        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 100L)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(NotFoundException.class));
    }
}
