package ru.otus.menu.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.menu.service.model.entity.Dish;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {

    @Query("SELECT DISTINCT d FROM Dish d LEFT JOIN FETCH d.products")
    List<Dish> findAllWithProducts();
}
