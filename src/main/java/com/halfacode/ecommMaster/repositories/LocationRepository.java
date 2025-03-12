package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Country;
import com.halfacode.ecommMaster.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.country = :country AND l.city = :city")
    List<Location> findByCountryAndCity(@Param("country") String country, @Param("city") String city);

    Optional<Location> findByName(String locationName);

    Location findNearestByCountryAndCity(String country, String city);
}
