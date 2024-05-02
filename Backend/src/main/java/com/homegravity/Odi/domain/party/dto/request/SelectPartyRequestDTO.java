package com.homegravity.Odi.domain.party.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homegravity.Odi.domain.party.entity.GenderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString()
@NoArgsConstructor
public class SelectPartyRequestDTO {

    private boolean isToday;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departuresDate;

    private GenderType gender;

    private String category;

}
