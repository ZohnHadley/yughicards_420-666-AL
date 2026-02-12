package com.cal.yughistore.repository;

import com.cal.yughistore.model.CardPrices;
import com.cal.yughistore.model.properties.CardProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardPropertiesRepository extends JpaRepository<CardProperties, Long> {

    CardProperties getByYughioCard_Id(Long id);

//    List<CardProperties> getAllByPropertiesType(String propertiesType, Pageable pageable);
    List<CardProperties> findAllByYughioCard_IdIn(Iterable<Long> ids);
}
