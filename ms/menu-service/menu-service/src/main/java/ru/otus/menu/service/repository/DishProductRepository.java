package ru.otus.menu.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.menu.service.model.entity.DishProduct;
import ru.otus.menu.service.model.entity.DishProductId;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishProductRepository extends JpaRepository<DishProduct, DishProductId> {

    @Query("SELECT dp FROM DishProduct dp WHERE dp.id.dishId IN :dishIds")
    List<DishProduct> findAllByDishIds(@Param("dishIds") List<Integer> dishIds);
}
