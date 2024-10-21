package com.noblesse.backend.post.query.infrastructure.persistence.repository;

import com.querydsl.core.Tuple;

import java.util.List;

public interface CustomPostRepository {
    List<Tuple> findPostsWithDetails();
}
