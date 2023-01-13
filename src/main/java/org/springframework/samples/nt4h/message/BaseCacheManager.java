package org.springframework.samples.nt4h.message;

import com.google.common.collect.Lists;
import org.springframework.samples.nt4h.card.enemy.EnemyInGame;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseCacheManager {

    private final static String ATTACKED_ENEMY = "attackedEnemy"; // El enemigo que ha sido seleccionado.

    protected List<EnemyInGame> parseEnemies(HttpSession session, String name, Function<String, EnemyInGame> function) {
        Object enemies = session.getAttribute(name);
        if (enemies == null)
            return Lists.newArrayList();
        return Stream.of(enemies.toString().split(","))
                .map(function)
                .filter(enemy -> !enemy.isNew())
                .collect(Collectors.toList());
    }

    protected EnemyInGame parseEnemy(HttpSession session, Function<Integer, EnemyInGame> function) {
        Object enemy = session.getAttribute(ATTACKED_ENEMY);
        Integer idAttackedEnemy = enemy == null ? null : Integer.parseInt(enemy.toString());
        return idAttackedEnemy == null ? new EnemyInGame() : function.apply(idAttackedEnemy);
    }

    public void addEnemies(HttpSession session, String name, EnemyInGame enemyInGame, Predicate<EnemyInGame> predicate) {
        Object enemies = session.getAttribute(name);
        if (enemies == null)
            session.setAttribute(name, enemyInGame.getId());
        else if (!(predicate.test(enemyInGame)))
            session.setAttribute(name, enemies + "," + enemyInGame.getId());
    }

    public void addEnemies(HttpSession session, String name, EnemyInGame enemyInGame) {
        Object enemies = session.getAttribute(name);
        if (enemies == null)
            session.setAttribute(name, enemyInGame.getId());
        else
            session.setAttribute(name, enemies + "," + enemyInGame.getId());
    }

    protected void addInteger(HttpSession session, String name, Integer value) {
        if (session.getAttribute(name) == null)
            session.setAttribute(name, value);
        else
            session.setAttribute(name, Integer.parseInt(session.getAttribute(name).toString()) + value);
    }

    protected Integer getInteger(HttpSession session, String name) {
        Object value = session.getAttribute(name);
        return value == null ? 0 : Integer.parseInt(value.toString());
    }

    protected boolean hasAttribute(HttpSession session) {
        return session.getAttribute("nextUrl") != null;
    }

    protected Optional<String> getString(HttpSession session) {
        Object value = session.getAttribute("nextUrl");
        return value == null ? Optional.empty() :Optional.of(value.toString());
    }

    protected boolean getBoolean(HttpSession session, String name) {
        return session.getAttribute(name) != null;
    }
}
