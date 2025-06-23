package com.OnlineQuest.OnlineQuest.service;

import com.OnlineQuest.OnlineQuest.model.Quest;
import com.OnlineQuest.OnlineQuest.model.Scene;
import com.OnlineQuest.OnlineQuest.model.User;
import com.OnlineQuest.OnlineQuest.repositories.QuestRepository;
import com.OnlineQuest.OnlineQuest.repositories.SceneRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final SceneRepository sceneRepository;

    public QuestService(QuestRepository questRepository, SceneRepository sceneRepository) {
        this.questRepository = questRepository;
        this.sceneRepository = sceneRepository;
    }

    public Quest createQuest(Quest quest) {
        return questRepository.save(quest);
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public List<Quest> getQuestsByUser(User user) {
        return questRepository.findByCreatedBy(user);
    }

    public Optional<Quest> getQuestById(Long id) {
        return questRepository.findById(id);
    }

    public void deleteQuest(Long id) {
        questRepository.deleteById(id);
    }

    public Optional<Scene> getSceneById(Long id) {
        return sceneRepository.findById(id);
    }

}
