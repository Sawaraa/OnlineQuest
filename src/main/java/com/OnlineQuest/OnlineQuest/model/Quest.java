package com.OnlineQuest.OnlineQuest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quests")
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imagePath;

    private String description;

    private int difficulty;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @OneToOne
    @JoinColumn(name = "start_scene_id")
    private Scene startScene;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Scene> scenes = new ArrayList<>();
}
