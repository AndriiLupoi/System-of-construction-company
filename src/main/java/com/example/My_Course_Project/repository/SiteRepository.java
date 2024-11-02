package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
    List<Site> findByNameContainingOrBuildingManagement_IdOrLocationContaining(String name,
                                                                               Integer managementId,
                                                                               String location);

    @Override
    Site save(Site site);
}
