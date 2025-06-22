package com.OnlineQuest.OnlineQuest.controllers;

import com.OnlineQuest.OnlineQuest.model.Option;
import com.OnlineQuest.OnlineQuest.model.Quest;
import com.OnlineQuest.OnlineQuest.model.Scene;
import com.OnlineQuest.OnlineQuest.repositories.OptionRepository;
import com.OnlineQuest.OnlineQuest.repositories.QuestRepository;
import com.OnlineQuest.OnlineQuest.repositories.SceneRepository;
import com.OnlineQuest.OnlineQuest.service.OptionalService;
import com.OnlineQuest.OnlineQuest.service.QuestService;
import com.OnlineQuest.OnlineQuest.service.SceneServise;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/quests")
public class QuestController {

    private final QuestService questService;
    private final SceneServise sceneServise;
    private final OptionalService optionalService;

    public QuestController(QuestService questService, SceneServise sceneServise, OptionalService optionalService) {
        this.questService = questService;
        this.sceneServise = sceneServise;
        this.optionalService = optionalService;
    }

    @GetMapping("/create")
    public String showCreateQuestForm(Model model) {

        model.addAttribute("quest", new Quest());

        List<Scene> allScenes = sceneServise.getAllScenes();
        model.addAttribute("scenes", allScenes);
        return "makequest";
    }

    @PostMapping("create")
    public String createQuest(@ModelAttribute Quest quest,
                              @RequestParam("image") MultipartFile imageFile) throws IOException {

        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get("uploads/quests"); // створити цю папку в resources/static або окремо
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            quest.setImagePath("/uploads/quests/" + fileName); // записуємо шлях у БД
        }

        // Прив’язуємо сценки до квесту
        Map<String, Scene> sceneNameMap = new HashMap<>();
        if (quest.getScenes() != null) {
            for (Scene scene : quest.getScenes()) {
                scene.setQuest(quest);
                sceneNameMap.put(scene.getName(), scene); // Зберігаємо сценки по назві
            }

            for (Scene scene : quest.getScenes()) {
                if (scene.getOptions() != null) {
                    for (Option option : scene.getOptions()) {
                        option.setCurrentScene(scene);

                        // Пошук nextScene по назві
                        String nextSceneName = option.getNextSceneName();
                        if (nextSceneName != null && sceneNameMap.containsKey(nextSceneName)) {
                            option.setNextSceneName(String.valueOf(sceneNameMap.get(nextSceneName)));
                        }
                    }
                }
            }
        }

        Quest save = questService.createQuest(quest);

        return "redirect:/quests/" + save.getId() + "/editoptions";
    }

   /* @GetMapping("/{id}/editoptions")
    public String showEditOptionsPage(@PathVariable Long id, Model model) {
        Quest quest = questService.getQuestById(id).orElseThrow();
        model.addAttribute("quest", quest);

        List<Scene> scenes = sceneServise.getScenesByQuestId(id);
        model.addAttribute("scenes", scenes);

        return "/editoption";
    }*/



    @GetMapping("/success")
    public String showSuccessPage() {
        return "questcreated"; // Це може бути проста HTML-сторінка з повідомленням
    }


}
