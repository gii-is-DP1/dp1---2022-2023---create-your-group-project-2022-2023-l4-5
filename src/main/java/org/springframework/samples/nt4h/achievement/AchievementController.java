package org.springframework.samples.nt4h.achievement;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/achievements")
public class AchievementController {

    private final static String ACHIEVEMENTS_LIST_VIEW ="achievements/achievementsList";
    private final static String VIEW_ACHIEVEMENTS_CREATE_OR_UPDATE_FORM ="achievements/createOrUpdateAchievementsForm";

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @ModelAttribute("achievements")
    public List<Achievement> getAchievements() {
        return this.achievementService.getAllAchievements();
    }

    @ModelAttribute("achievementType")
    public List<AchievementType> getAchievementType() {
        return Lists.newArrayList(AchievementType.PLAYED_GAMES,
            AchievementType.WON_GAMES, AchievementType.DURATION,
            AchievementType.TOTAL_GOLD, AchievementType.TOTAL_GLORY,
            AchievementType.DMG_TO_ORCS, AchievementType.KILLED_ORCS,
            AchievementType.KILLED_WARLORDS);
    }

    @GetMapping
    public String showAllAchievements() {
        return ACHIEVEMENTS_LIST_VIEW;
    }

    @Transactional()
    @GetMapping("/new")
    public String createAchievement(ModelMap model) {
        model.put("achievement", new Achievement());
        return VIEW_ACHIEVEMENTS_CREATE_OR_UPDATE_FORM;
    }

    //falta una redireccion a /achievement
    @PostMapping("/new")
    public String saveAchievement(@Valid Achievement achievement, BindingResult br, ModelMap model) {
        if(br.hasErrors())
            return createAchievement(model);
        achievementService.saveAchievement(achievement);
        model.put("achievement", "The achievement was created successfully");
        return showAllAchievements();
    }

    @GetMapping("/{achievementId}/edit")
    public String editAchievement(@PathVariable int achievementId, ModelMap model) {
        Achievement achievement = achievementService.getAchievementById(achievementId);
        model.addAttribute(achievement);
        return VIEW_ACHIEVEMENTS_CREATE_OR_UPDATE_FORM;
    }

    //falta una redireccion a /achievement
    @PostMapping("/{achievementId}/edit")
    public String saveAchievement(@PathVariable int achievementId,@Valid Achievement actualAchievement, BindingResult br, ModelMap model) {
        if(br.hasErrors())
            return editAchievement(achievementId, model);
        Achievement achievementToBeUpdated = achievementService.getAchievementById(achievementId);
        BeanUtils.copyProperties(actualAchievement, achievementToBeUpdated,"id");
        achievementService.saveAchievement(achievementToBeUpdated);
        model.put("message", "The achievement was updated successfully");
        return showAllAchievements();
    }

    //falta una redireccion a /achievement
    @GetMapping(value = "{achievementId}/delete")
    public String deleteAchievement(@PathVariable int achievementId, ModelMap model) {
        achievementService.deleteAchievementById(achievementId);
        model.put("message", "The achievement was deleted successfully");
        return showAllAchievements();
    }
}
