package org.springframework.samples.nt4h.player;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.nt4h.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {

    Optional<Player> findByName(String name);

    Optional<Player> findById(int id);

    List<Player> findAll();

    @Query("SELECT u FROM User u WHERE u.player = ?1")
    Optional<User> findUserByPlayer(Player player);


}
