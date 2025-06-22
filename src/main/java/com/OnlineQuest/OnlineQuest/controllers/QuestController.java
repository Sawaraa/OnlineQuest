package com.OnlineQuest.OnlineQuest.controllers;

import com.OnlineQuest.OnlineQuest.model.Option;
import com.OnlineQuest.OnlineQuest.model.Quest;
import com.OnlineQuest.OnlineQuest.model.Scene;
import com.OnlineQuest.OnlineQuest.model.User;
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

//        // Тимчасово встановлюємо тестового користувача (наприклад, user з id = 1)
//        User testUser = userRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        quest.setCreatedBy(testUser);

        // Збереження зображення
        if (!imageFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get("src/main/resources/static/uploads/quests");
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            quest.setImagePath("/uploads/quests/" + fileName);
        }

        // Підв'язуємо сцени до квесту
        if (quest.getScenes() != null) {
            for (Scene scene : quest.getScenes()) {
                scene.setQuest(quest);

                if (scene.getOptions() != null) {
                    for (Option option : scene.getOptions()) {
                        option.setCurrentScene(scene);
                    }
                }
            }
        }

        // Зберігаємо квест з усіма сценами та опціями (але ще без nextScene, бо id ще не відомі)
        Quest savedQuest = questService.createQuest(quest);

        // Створюємо мапу для зв'язку "номер сцени → сама сцена"
        Map<Integer, Scene> indexToScene = new HashMap<>();
        for (int i = 0; i < savedQuest.getScenes().size(); i++) {
            indexToScene.put(i, savedQuest.getScenes().get(i));
        }

        // Проставляємо nextScene для кожного варіанта (тільки тепер, коли є ID)
        for (int i = 0; i < savedQuest.getScenes().size(); i++) {
            Scene scene = savedQuest.getScenes().get(i);
            List<Option> options = scene.getOptions();
            if (options != null) {
                for (Option option : options) {
                    // тут nextSceneId — це індекс сцени, який приходить з HTML як value в <option>
                    try {
                        int nextSceneIndex = Integer.parseInt(option.getNextScene() != null ? option.getNextScene().getId().toString() : "-1");
                        Scene nextScene = indexToScene.get(nextSceneIndex);
                        if (nextScene != null) {
                            option.setNextScene(nextScene);
                        }
                    } catch (NumberFormatException e) {
                        // некоректне значення — ігноруємо
                    }
                }
            }
        }

        questService.createQuest(savedQuest); // повторне збереження з nextScene

        return "redirect:/quests/success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "questcreated"; // Це може бути проста HTML-сторінка з повідомленням
    }


}
