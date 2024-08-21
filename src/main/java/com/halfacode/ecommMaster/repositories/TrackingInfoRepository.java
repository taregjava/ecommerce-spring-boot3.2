package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.TrackingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingInfoRepository extends JpaRepository<TrackingInfo, Long> {
    TrackingInfo findByOrderNumber(String orderNumber);
}

