package com.cal.yughistore;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.services.DTOs.DTOMonsterCard;
import com.cal.yughistore.services.MonsterCardService;
import com.cal.yughistore.services.SpellCardService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YughistoreApplication {

	private final  MonsterCardService monsterCardService;
	private  final SpellCardService spellCardService;

    public YughistoreApplication(MonsterCardService monsterCardService, SpellCardService spellCardService) {
        this.monsterCardService = monsterCardService;
        this.spellCardService = spellCardService;
    }

    public static void main(String[] args) {
		SpringApplication.run(YughistoreApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			DTOMonsterCard dto = DTOMonsterCard.toDTO(new MonsterCard());
			monsterCardService.save(dto);
		};
	}
}
