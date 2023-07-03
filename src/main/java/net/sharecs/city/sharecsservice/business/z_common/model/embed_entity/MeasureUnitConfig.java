package net.sharecs.city.sharecsservice.business.z_common.model.embed_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasureUnitConfig {

    private String name;

    private Byte type;

    private Object unit;

}
