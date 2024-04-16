package com.web.baebaeBE.infra.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "example")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Example {

    @Id
    @Column(name = "example_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exampleId;

    @Column(name = "example_content")
    private String exampleContent;


}
