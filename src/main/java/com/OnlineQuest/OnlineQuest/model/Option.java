package com.OnlineQuest.OnlineQuest.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text; // Текст кнопки або вибору

    @ManyToOne
    @JoinColumn(name = "scene_id")
    private Scene currentScene;

    @ManyToOne
    @JoinColumn(name = "next_scene_id")
    private Scene nextScene;
}
