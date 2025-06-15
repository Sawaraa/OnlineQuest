package com.OnlineQuest.OnlineQuest.service;

import com.OnlineQuest.OnlineQuest.model.Option;
import com.OnlineQuest.OnlineQuest.model.Scene;
import com.OnlineQuest.OnlineQuest.repositories.OptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionalService {

    private final OptionRepository optionRepository;

    public OptionalService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public Option createOption(Option option) {
        return optionRepository.save(option);
    }

    public List<Option> getOptionsByScene(Scene scene) {
        return optionRepository.findByCurrentScene(scene);
    }

    public Optional<Option> getOptionById(Long id) {
        return optionRepository.findById(id);
    }


}
