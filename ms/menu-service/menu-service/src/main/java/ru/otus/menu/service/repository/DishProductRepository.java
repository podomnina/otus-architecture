package ru.otus.menu.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.menu.service.model.entity.DishProduct;
import ru.otus.menu.service.model.entity.DishProductId;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishProductRepository extends JpaRepository<DishProduct, DishProductId> {

    List<DishProduct> findAllByDishIds(List<UUID> dishIds); //todo ???
}
