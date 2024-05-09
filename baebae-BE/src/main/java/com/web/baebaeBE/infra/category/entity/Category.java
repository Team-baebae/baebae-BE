package com.web.baebaeBE.infra.category.entity;

import com.web.baebaeBE.infra.answer.entity.Answer;
import com.web.baebaeBE.infra.categorized.answer.entity.CategorizedAnswer;
import com.web.baebaeBE.infra.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "category_name", length = 30)
    private String categoryName;

    @Column(name="category_image")
    private String categoryImage;

    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<CategorizedAnswer> categoryAnswers = new ArrayList<>();

    public void updateCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void updateCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
}