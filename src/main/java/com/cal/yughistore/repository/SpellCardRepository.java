package com.cal.yughistore.repository;

import com.cal.yughistore.model.SpellCard;
import com.cal.yughistore.model.enums.EnumCardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpellCardRepository extends JpaRepository<SpellCard, Long> {

    @Override
    <S extends SpellCard> S save(S entity);

    @Override
    SpellCard getReferenceById(Long api_id);

    List<SpellCard> getAll();
    List<SpellCard> getAllByType(EnumCardType type);
}
