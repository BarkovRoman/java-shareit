package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerSTest {

    @MockBean
    private BookingService bookingService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private BookingDto bookingDto;
    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    public void setUp() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(5);

        bookingDto = BookingDto.builder()
                .id(1L).itemId(1L).status(BookingStatus.APPROVED)
                .start(start)
                .end(start.plusSeconds(10))
                .build();

        bookingResponseDto = new BookingResponseDto(
                1L,
                start,
                start.plusSeconds(10),  // end
                BookingStatus.APPROVED,
                new ItemResponseDto(1L, "Name"),
                new BookerResponseDto(1L));
    }

    @Test
    public void testBookItem() throws Exception {
        // given
        long userId = 1L;

        when(bookingService.add(any(), anyLong())).thenReturn(bookingResponseDto);

        // when + then
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()));
    }

    @Test
    public void update() throws Exception {
        // given
        long userId = 1L;

        when(bookingService.update(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingResponseDto);
        // when + then
        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()));
    }

    @Test
    public void getById() throws Exception {
        // given
        long userId = 1L;

        when(bookingService.getById(anyLong(), anyLong())).thenReturn(bookingResponseDto);
        // when + then
        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()));
    }

    @Test
    public void getByBookerIdAndState() throws Exception {
        // given
        long userId = 1L;
        List<BookingResponseDto> bookings = List.of(bookingResponseDto);

        when(bookingService.getByBookerIdAndState(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookings);
        // when + then
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookings.get(0).getId()));
    }

    @Test
    public void getAllOwnerId() throws Exception {
        // given
        long userId = 1L;
        List<BookingResponseDto> bookings = List.of(bookingResponseDto);

        when(bookingService.getAllOwnerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookings);
        // when + then
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookings.get(0).getId()));
    }

    @Test
    public void saveNewBookingStartEnd() throws Exception {
        bookingDto = BookingDto.builder().id(1L).itemId(1L).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now()).build();
        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 1L)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));
    }

    @Test
    public void getAllOwnerId1() throws Exception {
        // given
        long userId = 1L;
        List<BookingResponseDto> bookings = List.of(bookingResponseDto);

        when(bookingService.getAllOwnerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookings);
        // when + then
        mockMvc.perform(get("/bookings/owner?size=-2")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }
}
