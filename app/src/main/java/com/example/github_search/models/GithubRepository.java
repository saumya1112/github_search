package com.example.github_search.models;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
    @AllArgsConstructor
    public class GithubRepository {
        Integer id;
        String name;
        String description;
        String url;

    }

