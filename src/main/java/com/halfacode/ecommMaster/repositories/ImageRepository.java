package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
