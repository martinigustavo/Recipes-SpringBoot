package com.gustavomartini.recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String category;

    @NonNull
    private LocalDateTime date;

    @NotEmpty
    @ElementCollection
    @Size(min = 1, message = "Provide at least 2 ingredients")
    private List<String> ingredients;

    @NotEmpty
    @ElementCollection
    @Size(min = 1, message = "Provide at least 3 directions")
    private List<String> directions;

    @LastModifiedBy
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "added_by")
    private User user;
}
