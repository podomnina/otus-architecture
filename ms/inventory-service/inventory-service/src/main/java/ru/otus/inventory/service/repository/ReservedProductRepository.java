package ru.otus.inventory.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.inventory.service.model.entity.ReservedProduct;
import ru.otus.inventory.service.model.entity.ReservedProductId;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservedProductRepository extends JpaRepository<ReservedProduct, ReservedProductId> {

    void deleteAllByOrderId(UUID orderId); //todo???

    List<ReservedProduct> findAllByOrderId(UUID orderId); //todo for update??
}
