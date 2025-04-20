package ru.otus.inventory.service.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.otus.inventory.service.model.entity.Inventory;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAllByIdsForUpdate(Iterable<UUID> ids);
}
