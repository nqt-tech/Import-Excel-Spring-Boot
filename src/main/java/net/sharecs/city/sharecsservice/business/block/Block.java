package net.sharecs.city.sharecsservice.business.block;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/*
 * @Author: Nguyen Tuan - Sharecs.net
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "block")
@Table(name = "block")
public class Block {

    public Block(String id) {
        this.setId(id);
    }

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(32) character set latin1 collate latin1_bin")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "bottom_floor")
    private String bottomFloor;

    @Column(name = "top_floor")
    private String topFloor;

    @Column(name = "total_apartment")
    private Integer totalApartment;

    @Column(name = "total_square")
    private Float totalSquare;
    
}
