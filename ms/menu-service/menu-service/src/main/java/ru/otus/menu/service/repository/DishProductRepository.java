package ru.otus.menu.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.menu.service.model.entity.DishProduct;
import ru.otus.menu.service.model.entity.DishProductId;

@Repository
public interface DishProductRepository extends JpaRepository<DishProduct, DishProductId> {
}
