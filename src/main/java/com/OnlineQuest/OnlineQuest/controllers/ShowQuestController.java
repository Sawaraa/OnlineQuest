package com.OnlineQuest.OnlineQuest.controllers;

import com.OnlineQuest.OnlineQuest.model.Quest;
import com.OnlineQuest.OnlineQuest.model.Scene;
import com.OnlineQuest.OnlineQuest.model.User;
import com.OnlineQuest.OnlineQuest.service.QuestService;
import com.OnlineQuest.OnlineQuest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ShowQuestController {

    public final QuestService questService;
    public final UserService userService;

    public ShowQuestController(QuestService questService, UserService userService) {
        this.questService = questService;
        this.userService = userService;
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

    @GetMapping("/quests/start/{questId}")
    public String startQuest(@PathVariable Long questId, Model model) {
        Optional<Quest> questOpt = questService.getQuestById(questId);

        if (questOpt.isEmpty()) {
            return "redirect:/?error=questnotfound";
        }

        Quest quest = questOpt.get();
        Scene startScene = quest.getStartScene();

        return "redirect:/quests/scene/" + startScene.getId();
    }

//    @GetMapping("/quests/scene/{sceneId}")
//    public String viewScene(@PathVariable Long sceneId, Model model) {
//        Optional<Scene> sceneOpt = questService.getSceneById(sceneId);
//
//        if (sceneOpt.isEmpty()) {
//            return "redirect:/?error=scenenotfound";
//        }
//
//        Scene scene = sceneOpt.get();
//        model.addAttribute("scene", scene);
//
//        return "sceneview";
//    }

    @GetMapping("/quests/scene/{sceneId}")
    public String viewScene(@PathVariable Long sceneId, Model model, HttpSession session) throws Exception {
        Optional<Scene> sceneOpt = questService.getSceneById(sceneId);

        if (sceneOpt.isEmpty()) {
            return "redirect:/?error=scenenotfound";
        }

        Scene scene = sceneOpt.get();
        model.addAttribute("scene", scene);

        // Якщо сцена фінальна – додаємо квест до списку пройдених
        if (scene.isFinal()) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            System.out.println("LoggedInUser from session: " + loggedInUser);
            if (loggedInUser != null) {
                // далі код
            } else {
                System.out.println("Користувач не авторизований або сесія не зберегла дані");
            }

            if (loggedInUser != null) {
                Quest quest = scene.getQuest();

                if (!loggedInUser.getCompletedQuests().contains(quest)) {
                    loggedInUser.getCompletedQuests().add(quest);
                    // збереження користувача з оновленим списком
                    userService.updateUser(loggedInUser);
                }
            }
        }

        return "sceneview";
    }

    @GetMapping("/profile/completed-quests")
    public String viewCompletedQuests(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("completedQuests", loggedInUser.getCompletedQuests());
        return "completed_quests"; // HTML-файл нижче
    }


}
