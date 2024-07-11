package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Autores(
        @JsonAlias("name") String name,
        @JsonAlias("birth_year") Integer birthYear,
        @JsonAlias("death_year") Integer deathYear
) {
}
