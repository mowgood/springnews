package com.example.springnews.repository;

import com.example.springnews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {
    public List<News> findByContentContains(String keyword);
    public List<News> findByWriter(String name);
}
