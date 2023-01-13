package org.springframework.samples.nt4h.statistic;

import lombok.AllArgsConstructor;
import org.javatuples.Quartet;
import org.springframework.samples.nt4h.achievement.Achievement;
import org.springframework.samples.nt4h.achievement.AchievementService;
import org.springframework.samples.nt4h.achievement.AchievementType;
import org.springframework.samples.nt4h.exceptions.NotFoundException;
import org.springframework.samples.nt4h.message.Advise;
import org.springframework.samples.nt4h.player.Player;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserRepository;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.function.ToIntFunction;

@Service
@AllArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;
    private final UserService userService;
    private final AchievementService achievementService;
    private final UserRepository userRepository;
    private final Advise advise;

    @Transactional(readOnly = true)
    public Statistic getStatisticById(int id) {
        return statisticRepository.findById(id).orElseThrow(() -> new NotFoundException("Statistic not found"));
    }

    @Transactional(readOnly = true)
    public List<Statistic> getAllStatistics() {
        return statisticRepository.findAll();
    }

    @Transactional
    public void saveStatistic(Statistic statistic) {
        statisticRepository.save(statistic);
    }

    @Transactional
    public void deleteStatistic(Statistic statistic) {
        statisticRepository.delete(statistic);
    }

    @Transactional
    public void deleteStatisticById(int id) {
        statisticRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean statisticExists(int id) {
        return statisticRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Integer getNumGamesByUser(int userId) { return statisticRepository.findNumPlayedGamesByUser(userId); }

    @Transactional
    public Integer getNumMinutesPlayedByUser(int userId) { return statisticRepository.findNumMinutesPlayedByUser(userId); }

    @Transactional
    public Integer getNumGamesByNumPlayers(int numP){
        return statisticRepository.findNumGamesByNumPlayers(numP);
    }

    @Transactional
    public Integer getNumGoldByUser(int userId) { return statisticRepository.findNumGoldByUser(userId);}

    @Transactional
    public Integer getNumGloryByUser(int userId) { return statisticRepository.findNumGloryByUser(userId);}

    @Transactional
    public Integer getNumOrcsByUser(int userId) { return statisticRepository.findNumOrcsByUser(userId);}

    @Transactional
    public Integer getNumWarLordByUser(int userId) { return statisticRepository.findNumWarLordByUser(userId);}

    @Transactional
    public Integer getNumDamageByUser(int userId) { return statisticRepository.findNumDamageByUser(userId);}

    @Transactional
    public Integer getNumWonGamesByUser(int userId) { return statisticRepository.findNumWonGamesByUser(userId);}

    @Transactional
    public List<User> listAllUsers() {return userService.getAllUsers(); }

    @Transactional
    public int getMin(ToIntFunction< User> function) {
        return listAllUsers().stream().mapToInt(function).filter(i -> i > 0).min().orElse(0);
    }

    @Transactional
    public int getMax(ToIntFunction< User> function) {
        return listAllUsers().stream().mapToInt(function).max().orElse(0);
    }

    @Transactional
    public double getAverage(ToIntFunction<User> function) {
        return listAllUsers().stream().mapToInt(function).average().orElse(0);
    }
    @Transactional
    public Quartet<String, Double, Integer, Integer> getStatisticNumPlayedGame() {
        ToIntFunction<User> function = user -> getNumGamesByNumPlayers(user.getId());
        return new Quartet<>("Most Played Game",getAverage(function), getMin(function), getMax(function));
    }
    @Transactional
    public Quartet<String, Double, Integer, Integer> getStatisticNumMinutesPlayed() {
        ToIntFunction<User> function = user -> getNumMinutesPlayedByUser(user.getId());
        return new Quartet<>("Most Played Game",getAverage(function), getMin(function), getMax(function));
    }

    @Transactional
    public Quartet<String, Double, Integer, Integer> getStatisticNumGold() {
        ToIntFunction<User> function = user -> getNumGoldByUser(user.getId());
        return new Quartet<>("Most Gold",getAverage(function), getMin(function), getMax(function));
    }

    @Transactional
    public Quartet<String, Double, Integer, Integer> getStatisticNumGlory() {
        ToIntFunction<User> function = user -> getNumGloryByUser(user.getId());
        return new Quartet<>("Most Glory",getAverage(function), getMin(function), getMax(function));
    }

    @Transactional
    public Quartet<String, Double, Integer, Integer> getStatisticNumOrcs() {
        ToIntFunction<User> function = user -> getNumOrcsByUser(user.getId());
        return new Quartet<>("Most Orcs",getAverage(function), getMin(function), getMax(function));
    }

    @Transactional
    public Quartet<String, Double, Integer, Integer> getStatisticNumWarLord() {
        ToIntFunction<User> function = user -> getNumWarLordByUser(user.getId());
        return new Quartet<>("Most WarLord",getAverage(function), getMin(function), getMax(function));
    }

    @Transactional
    public Quartet<String, Double, Integer, Integer> getStatisticNumDamage() {
        ToIntFunction<User> function = user -> getNumDamageByUser(user.getId());
        return new Quartet<>("Most Damage",getAverage(function), getMin(function), getMax(function));
    }
    @Transactional
    public Quartet<String, Double, Integer, Integer> getStatisticNumWonGames() {
        ToIntFunction<User> function = user -> getNumWonGamesByUser(user.getId());
        return new Quartet<>("Most Won Games",getAverage(function), getMin(function), getMax(function));
    }

    @Transactional
    public List<Quartet<String, Double, Integer, Integer>> getStatistics() {
        List<Quartet<String, Double, Integer, Integer>> statistics = new ArrayList<>();
        statistics.add(getStatisticNumPlayedGame());
        statistics.add(getStatisticNumMinutesPlayed());
        statistics.add(getStatisticNumGold());
        statistics.add(getStatisticNumGlory());
        statistics.add(getStatisticNumOrcs());
        statistics.add(getStatisticNumWarLord());
        statistics.add(getStatisticNumDamage());
        statistics.add(getStatisticNumWonGames());
        return statistics;
    }

    @Transactional(rollbackFor = Exception.class)
    public void gainGlory(Player player, Integer glory) {
        Statistic playerStatistic = player.getStatistic();
        User user = userService.getUserByUsername(player.getName());
        Statistic userStatistic = user.getStatistic();
        userStatistic.setGlory(userStatistic.getGlory() + glory);
        playerStatistic.setGlory(playerStatistic.getGlory() + glory);
        System.out.println(playerStatistic.getGlory());
        saveStatistic(userStatistic);
        saveStatistic(playerStatistic);
        advise.addGlory(glory);
    }

    @Transactional(rollbackFor = Exception.class)
    public void gainGold(Player player, Integer gold) {
        Statistic playerStatistic = player.getStatistic();
        User user = userService.getUserByUsername(player.getName());
        Statistic userStatistic = user.getStatistic();
        userStatistic.setGold(userStatistic.getGold() + gold);
        playerStatistic.setGold(playerStatistic.getGold() + gold);
        saveStatistic(userStatistic);
        saveStatistic(playerStatistic);
        advise.addGold(gold);
        //=============================================================\\
      List<Achievement> list= achievementService.getAllAchievementsByType(AchievementType.TOTAL_GOLD);
        for(int i=0; i< list.size(); i++){
            if(gold >= list.get(i).getThreshold()) {
                advise.addAchievement(list.get(i));
            user.getAchievements().add(list.get(i));
            userRepository.save(user);
            }
        }
    }


    @Transactional
    public void loseGold(Statistic statistic, Integer gold) {
        statistic.setGold(Math.max(statistic.getGold() - gold, 0));
        advise.loseGold(gold);
        saveStatistic(statistic);
    }

    @Transactional
    public void loseGlory(Statistic statistic, Integer glory) {
        statistic.setGlory(Math.max(statistic.getGlory() - glory, 0));
        advise.loseGlory(glory);
        saveStatistic(statistic);
    }

    @Transactional(rollbackFor = Exception.class)
    public void damageDealt(Player player, Integer damage) {
        Statistic playerStatistic = player.getStatistic();
        User user = userService.getUserByUsername(player.getName());
        Statistic userStatistic = user.getStatistic();
        userStatistic.setDamageDealt(userStatistic.getDamageDealt() + damage);
        playerStatistic.setDamageDealt(playerStatistic.getDamageDealt() + damage);
        saveStatistic(userStatistic);
        saveStatistic(playerStatistic);
        //=============================================================\\
        List<Achievement> list= achievementService.getAllAchievementsByType(AchievementType.DMG_TO_ORCS);
        for(int i=0; i < list.size(); i++){
            if(userStatistic.getDamageDealt()>= list.get(i).getThreshold()) {
                advise.addAchievement(list.get(i));
                user.getAchievements().add(list.get(i));
                userRepository.save(user);
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void killedOrcs(Player player) {
        Statistic playerStatistic = player.getStatistic();
        User user = userService.getUserByUsername(player.getName());
        Statistic userStatistic = user.getStatistic();
        userStatistic.setGlory(userStatistic.getNumOrcsKilled() + 1);
        playerStatistic.setGlory(playerStatistic.getNumOrcsKilled() + 1);
        saveStatistic(userStatistic);
        saveStatistic(playerStatistic);
        //=============================================================\\
        List<Achievement> list= achievementService.getAllAchievementsByType(AchievementType.KILLED_ORCS);
        for(int i=0; i< list.size(); i++){
            if(userStatistic.getNumOrcsKilled() >= list.get(i).getThreshold()) {
                advise.addAchievement(list.get(i));
                user.getAchievements().add(list.get(i));
                userRepository.save(user);
            }
        }
    }
}
