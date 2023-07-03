package net.sharecs.city.sharecsservice.business.z_common.model.map_composite_key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class BlockUnitBlockKey {

    private String blockName;

    private String blockUnitName;

    @Override
    public int hashCode() {
        return Objects.hash(this.getBlockName() + this.getBlockUnitName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BlockUnitBlockKey key = (BlockUnitBlockKey) obj;

        if (Objects.isNull(key.getBlockName()) || Objects.isNull(key.getBlockUnitName())) {
            return false;
        }

        return this.getBlockName().equals(key.getBlockName()) && this.getBlockUnitName().equals(key.getBlockUnitName());
    }

}
