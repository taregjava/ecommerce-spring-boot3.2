package com.halfacode.ecommMaster.extapi;

import com.halfacode.ecommMaster.models.AddressDetails;
import com.halfacode.ecommMaster.models.ItemDetail;
import com.halfacode.ecommMaster.models.TrackingEvent;
import com.halfacode.ecommMaster.models.TrackingInfo;
import com.halfacode.ecommMaster.response.ExternalApiTrackingResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalTrackingService {

    private final RestTemplate restTemplate;

    public ExternalTrackingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TrackingInfo getTrackingInfoFromExternalApi(String orderNumber, String apiKey) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://mweb.imile.com/api/tracking")
                .queryParam("orderNumber", orderNumber)
                .queryParam("keyValue", apiKey)
                .toUriString();

        ExternalApiTrackingResponse response = restTemplate.getForObject(url, ExternalApiTrackingResponse.class);
        return mapToTrackingInfo(response);
    }

    private TrackingInfo mapToTrackingInfo(ExternalApiTrackingResponse response) {
        TrackingInfo trackingInfo = new TrackingInfo();
        trackingInfo.setCurrentLocation(response.getCurrentLocation());
        trackingInfo.setDistanceToCustomer(response.getDistanceToCustomer());
        trackingInfo.setPackageStatus(response.getPackageStatus());
        trackingInfo.setOrderNumber(response.getOrderNumber());
        trackingInfo.setCourier(response.getCourier());

        AddressDetails addressDetails = new AddressDetails();
        addressDetails.setStreet(response.getStreet());
        addressDetails.setCity(response.getCity());
        addressDetails.setNearbyLandmark(response.getNearbyLandmark());
        addressDetails.setPostalCode(response.getPostalCode());
        trackingInfo.setAddressDetails(addressDetails);

        List<ItemDetail> items = response.getItems().stream()
                .map(item -> {
                    ItemDetail detail = new ItemDetail();
                    detail.setItemName(item.getItemName());
                    detail.setQuantity(item.getQuantity());
                    return detail;
                }).collect(Collectors.toList());
        trackingInfo.setItems(items);

        List<TrackingEvent> trackingHistory = response.getTrackingHistory().stream()
                .map(event -> {
                    TrackingEvent trackingEvent = new TrackingEvent();
                    trackingEvent.setStatus(event.getStatus());
                    trackingEvent.setLocation(event.getLocation());
                    trackingEvent.setTimestamp(event.getTimestamp());
                    return trackingEvent;
                }).collect(Collectors.toList());
        trackingInfo.setTrackingHistory(trackingHistory);

        return trackingInfo;
    }
}

