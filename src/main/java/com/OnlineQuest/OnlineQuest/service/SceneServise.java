package com.OnlineQuest.OnlineQuest.service;

import com.OnlineQuest.OnlineQuest.model.Quest;
import com.OnlineQuest.OnlineQuest.model.Scene;
import com.OnlineQuest.OnlineQuest.repositories.SceneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SceneServise {

    private final SceneRepository sceneRepository;

    public SceneServise(SceneRepository sceneRepository) {
        this.sceneRepository = sceneRepository;
    }


    public List<Scene> getScenesByQuestId(Long questId) {
        return sceneRepository.findByQuestId(questId);
    }

    public List<Scene> getAllScenes() {
        return sceneRepository.findAll();
    }

    public Scene createScene(Scene scene) {
        return sceneRepository.save(scene);
    }

    public List<Scene> getScenesByQuest(Quest quest) {
        return sceneRepository.findByQuest(quest);
    }

    public Optional<Scene> getSceneById(Long id) {
        return sceneRepository.findById(id);
    }

}
