package com.cal.yughistore.model.properties;

import com.cal.yughistore.model.YughioCard;
import jakarta.persistence.*;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CardProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "yughioCard_id")
    private YughioCard yughioCard;
}
