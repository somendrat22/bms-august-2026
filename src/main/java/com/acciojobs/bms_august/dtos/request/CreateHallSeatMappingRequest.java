package com.acciojobs.bms_august.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHallSeatMappingRequest {
    private String row;
    private String seatRange;
    private String seatBreak;
}
