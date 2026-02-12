package com.cal.yughistore.repository;

import com.cal.yughistore.model.CardImages;
import com.cal.yughistore.model.YughioCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardImagesRepository extends JpaRepository<CardImages, Long> {
}
