package com.OnlineQuest.OnlineQuest.controllers;

import com.OnlineQuest.OnlineQuest.model.Quest;
import com.OnlineQuest.OnlineQuest.model.Scene;
import com.OnlineQuest.OnlineQuest.service.QuestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ShowQuestController {

    public final QuestService questService;

    public ShowQuestController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping("/")
    public String showCreateQuestForm(Model model) {
        model.addAttribute("quests", questService.getAllQuests() );
        return "index";
    }

    @GetMapping("/quests/{id}")
    public String viewQuest(@PathVariable Long id, Model model) {
        Optional<Quest> questOptional = questService.getQuestById(id);

        if (questOptional.isPresent()) {
            model.addAttribute("quest", questOptional.get()); // ← передаємо сам об'єкт Quest
            return "questview";
        } else {
            return "redirect:/"; // або сторінка з помилкою
        }
    }

//    @GetMapping("/quests/start/{questId}")
//    public String startQuest(@PathVariable Long questId, Model model) {
//        Optional<Quest> questOpt = questService.getQuestById(questId);
//
//        if (questOpt.isEmpty()) {
//            return "redirect:/?error=questnotfound";
//        }
//
//        Quest quest = questOpt.get();
//        Scene startScene = quest.getStartScene();
//
//        return "redirect:/quests/scene/" + startScene.getId();
//    }

}
