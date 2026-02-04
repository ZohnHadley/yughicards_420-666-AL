package com.cal.yughistore.repository;

import com.cal.yughistore.model.MonsterCard;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonsterCardRepository extends JpaRepository<MonsterCard, Long> {

    MonsterCard getTrapCardsById(Long id);

    MonsterCard getTrapCardsByName(String name);
}
