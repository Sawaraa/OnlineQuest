package com.OnlineQuest.OnlineQuest.repositories;

import com.OnlineQuest.OnlineQuest.model.Option;
import com.OnlineQuest.OnlineQuest.model.Scene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByCurrentScene(Scene scene);
}
