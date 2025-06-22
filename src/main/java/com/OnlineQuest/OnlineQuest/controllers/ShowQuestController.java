package com.OnlineQuest.OnlineQuest.controllers;

import com.OnlineQuest.OnlineQuest.model.Quest;
import com.OnlineQuest.OnlineQuest.model.Scene;
import com.OnlineQuest.OnlineQuest.service.QuestService;
import jakarta.persistence.Column;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

}
