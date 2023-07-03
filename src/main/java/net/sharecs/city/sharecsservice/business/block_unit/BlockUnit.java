package net.sharecs.city.sharecsservice.business.block_unit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sharecs.city.sharecsservice.business.block.Block;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

/*
 * @Author: Nguyen Tuan - Sharecs.net
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "blockUnit")
@Table(name = "block_unit")
public class BlockUnit {

    public BlockUnit(String id) {
        this.setId(id);
    }

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(32) character set latin1 collate latin1_bin")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "floor")
    private String floor;

    @Column(name = "square")
    private Float square;

    @Column(name = "number_of_room")
    private Byte numberOfRoom;

    @Column(name = "type")
    private Byte type;

    @Column(name = "free_from_date")
    private LocalDate freeFromDate;

    @Column(name = "free_to_date")
    private LocalDate freeToDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Block block;

}
