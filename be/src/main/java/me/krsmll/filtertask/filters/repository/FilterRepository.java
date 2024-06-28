package me.krsmll.filtertask.filters.repository;

import me.krsmll.filtertask.filters.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> {}
