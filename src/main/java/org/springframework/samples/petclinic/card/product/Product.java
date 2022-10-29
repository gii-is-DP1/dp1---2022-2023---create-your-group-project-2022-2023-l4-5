package org.springframework.samples.petclinic.card.product;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.samples.petclinic.capacity.Capacity;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.ability.Ability;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends Card {
    @Range(min = 3, max = 8)
    private Integer price;
    @Range(min = 0, max = 4)
    private Integer attack;
    @OneToOne(optional = false)
    private Ability ability;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Capacity> capacity;
}
