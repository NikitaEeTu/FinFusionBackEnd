package com.finfusion.APS.repository;

import com.finfusion.APS.entity.AssetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssetRepository extends CrudRepository<AssetEntity, UUID> {
}
