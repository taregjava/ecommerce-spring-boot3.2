package com.halfacode.ecommMaster.util;

public class HotelReservationApp {
    public static void main(String[] args) {
        HotelBookingSystem hotel = new HotelBookingSystem();

        hotel.bookRoom("Alice");
        hotel.bookRoom("Bob");
        hotel.bookRoom("Charlie");
        hotel.bookRoom("David");
        hotel.bookRoom("Eve");  // No rooms left, goes to waitlist

        hotel.displayBookings();

        hotel.cancelBooking("Charlie");  // Eve gets assigned Charlie's room

        hotel.displayBookings();
    }
}

