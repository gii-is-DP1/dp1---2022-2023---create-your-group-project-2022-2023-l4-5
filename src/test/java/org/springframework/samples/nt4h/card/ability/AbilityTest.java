package org.springframework.samples.nt4h.card.ability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.nt4h.card.ability.*;
import org.springframework.samples.nt4h.card.hero.Role;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class AbilityTest {

    @Autowired
    private Validator validator;

    private Ability ability;
    @Autowired
    private AbilityRepository abilityRepository;

    @BeforeEach
    public void setup() {
        ability = new Ability();
        ability.setName("Test Ability");
        ability.setRole(Role.EXPLORER);
        ability.setAttack(3);
        ability.setQuantity(1);
        ability.setMaxUses(1);
        ability.setPathName("nerd");
    }

    @Test
    public void testAbilityProperties() {
        assertThat(ability.getName()).isEqualTo("Test Ability");
        assertThat(ability.getRole()).isEqualTo(Role.EXPLORER);
        assertThat(ability.getAttack()).isEqualTo(3);
        assertThat(ability.getQuantity()).isEqualTo(1);
    }

    @Test
    public void testAbilityConstraints() {
        ability.setName("");
        assertThat(validator.validate(ability)).isNotEmpty();
        ability.setName("A");
        assertThat(validator.validate(ability)).isNotEmpty();
        ability.setName("This is a very long name that exceeds the maximum length of 255 characters. This is a very long name that exceeds the maximum length of 255 characters. This is a very long name that exceeds the maximum length of 255 characters. This is a very long name that exceeds the maximum length of 255 characters. This is a very long name that exceeds the maximum length of 255 characters. This is a very long name that exceeds the maximum length of 255 characters. This is a very long name that exceeds the maximum length of 255 characters.");

        ability.setName("Test Ability");
        ability.setRole(null);
        assertThat(validator.validate(ability)).isNotEmpty();

        ability.setRole(Role.EXPLORER);
        ability.setAttack(-1);
        assertThat(validator.validate(ability)).isNotEmpty();
        ability.setAttack(5);
        assertThat(validator.validate(ability)).isNotEmpty();

        ability.setAttack(3);
        ability.setQuantity(0);
        assertThat(validator.validate(ability)).isNotEmpty();
    }

    @Test
    public void testAbilityLifecycle() {
        ability = abilityRepository.save(ability);
        assertThat(ability.getId()).isNotNull();

        ability.setName("Updated Test Ability");
        ability = abilityRepository.save(ability);
        assertThat(ability.getName()).isEqualTo("Updated Test Ability");

        abilityRepository.delete(ability);
        assertThat(abilityRepository.findById(ability.getId())).isEmpty();
    }

    @Test
    public void testAbilityQueries() {
        abilityRepository.save(ability);
        assertThat(abilityRepository.findAll().size()).isEqualTo(34);

        assertThat(abilityRepository.findById(ability.getId()).get()).isEqualTo(ability);
    }
}
