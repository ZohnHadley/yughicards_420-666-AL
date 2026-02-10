package com.cal.yughistore.model.properties;

import jakarta.persistence.*;
import lombok.ToString;

@ToString
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SpecificProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
