package com.garen.community.repository;

import com.garen.community.domain.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface BookRepository extends ElasticsearchCrudRepository<Book, Integer> {
}
