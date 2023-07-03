package net.sharecs.city.sharecsservice.business.block_unit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BlockUnitRepository extends JpaRepository<BlockUnit, String> {

    @Query(value = "select bu from blockUnit bu where bu.name in ?1")
    List<BlockUnit> findBlockUnitByNameIn(Set<String> blockUnitNames);

}
