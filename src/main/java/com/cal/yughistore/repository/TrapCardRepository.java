package com.cal.yughistore.repository;

import com.cal.yughistore.model.TrapCard;
import com.cal.yughistore.model.enums.EnumCardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrapCardRepository extends JpaRepository<TrapCard, Long> {
}
