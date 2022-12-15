package org.springframework.samples.nt4h.action;

public enum Phase {
    EVADE,
    HERO_ATTACK,
    ENEMY_ATTACK,
    MARKET,
    RESUPPLY;

    @Override
    public String toString() {
        if (this == EVADE) {
            return "evasion";
        } else if (this == HERO_ATTACK) {
            return "heroAttack";
        } else if (this == ENEMY_ATTACK) {
            return "enemyAttack";
        } else if (this == MARKET) {
            return "market";
        } else if (this == RESUPPLY) {
            return "resupply";
        } else {
            return "Unknown";
        }
    }


}
