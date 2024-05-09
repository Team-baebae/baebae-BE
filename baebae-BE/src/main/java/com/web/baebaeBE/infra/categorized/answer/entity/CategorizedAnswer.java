package com.web.baebaeBE.infra.categorized.answer.entity;

import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorized_answer")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CategorizedAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;
}