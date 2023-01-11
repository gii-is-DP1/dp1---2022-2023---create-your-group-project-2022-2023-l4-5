package org.springframework.samples.nt4h.capacity;

import lombok.*;
import org.springframework.samples.nt4h.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "capacities")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Capacity extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    private StateCapacity stateCapacity;
    @NotNull
    private Boolean lessDamage;
}
