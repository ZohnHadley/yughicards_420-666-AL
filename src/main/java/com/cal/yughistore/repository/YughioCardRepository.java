package com.cal.yughistore.repository;

import com.cal.yughistore.model.abstractClasses.YughioCard;
import com.cal.yughistore.model.enums.EnumCardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YughioCardRepository extends JpaRepository<YughioCard, Long> {
    
    @Override
    <S extends YughioCard> S save(S entity);

    @Override
    YughioCard getReferenceById(Long api_id);

    YughioCard getYughioCardByApi_id(Long api_id);
    List<YughioCard> getAllByType(EnumCardType type);
}
