package net.sharecs.city.sharecsservice.business.z_common.model.embed_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftInfo {
   
    private Byte orderNo;

    private String name;

    private Byte fillOutTimeType;

    private LocalTime fillOutTime;

    private LocalTime startTime;

    private LocalTime endTime;

}
