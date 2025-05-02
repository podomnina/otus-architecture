package ru.otus.order.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.order.service.model.entity.IdempotencyKey;

@Repository
public interface IdempotencyRepository extends CrudRepository<IdempotencyKey, String> {
}
