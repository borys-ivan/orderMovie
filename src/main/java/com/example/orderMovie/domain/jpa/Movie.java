package com.example.orderMovie.domain.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "MOVIES")
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieID;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private String duration;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "age_limit")
    private Integer ageLimit;

    @Column(name = "language_translate")
    private String languageTranslate;

    @Column(name = "created")
    private Date created;

    ///////////////////////////////////////////
    // RELATIONS
    ///////////////////////////////////////////

    @OneToMany(mappedBy="movie")
    private Set<Order> orders;

}
