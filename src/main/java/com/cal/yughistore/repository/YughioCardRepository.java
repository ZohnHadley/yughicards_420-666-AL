package com.cal.yughistore.repository;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YughioCardRepository extends JpaRepository<YughioCard, Long> {

    YughioCard getById(Long id);
    YughioCard getByName(String name);


    Page<YughioCard> getAllByFrameType(EnumFrameType frameType, Pageable pageable);
    Page<YughioCard> getAllByType(EnumCardType type, Pageable pageable);

}
