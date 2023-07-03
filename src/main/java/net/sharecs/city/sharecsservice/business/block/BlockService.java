package net.sharecs.city.sharecsservice.business.block;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class BlockService {

    private final EntityManager entityManager;

    /**
     *
     * @param blockNames
     * @return
     */
    public Map<String, String> findBlockIdsByNames(Set<String> blockNames) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
        Root<Block> root = query.from(Block.class);

        query.multiselect(root.get(Block_.ID), root.get(Block_.NAME)).where(root.get(Block_.NAME).in(blockNames));

        List<Tuple> tuples = entityManager.createQuery(query).getResultList();
        Map<String, String> mapBlockIdByName = new HashMap<>();

        for (Tuple tuple: tuples) {
            mapBlockIdByName.put(tuple.get(1, String.class), tuple.get(0, String.class));
        }

        return mapBlockIdByName;
    }

}
