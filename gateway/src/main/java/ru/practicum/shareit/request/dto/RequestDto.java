package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    @NotBlank
    String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestDto)) return false;
        return description != null && description.equals(((RequestDto) o).description);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
