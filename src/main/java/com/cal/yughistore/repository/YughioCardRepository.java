package com.cal.yughistore.repository;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.model.YughioCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YughioCardRepository extends JpaRepository<YughioCard, Long> {
}
