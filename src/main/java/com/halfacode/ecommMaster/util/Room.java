package com.halfacode.ecommMaster.util;

import java.util.*;

class Room {
    private int roomNumber;
    private double price;
    private boolean isBooked;

    public Room(int roomNumber, double price) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.isBooked = false;
    }

    public int getRoomNumber() { return roomNumber; }
    public double getPrice() { return price; }
    public boolean isBooked() { return isBooked; }

    public void bookRoom() { this.isBooked = true; }
    public void cancelBooking() { this.isBooked = false; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " ($" + price + ")";
    }
}

// Hotel Booking System
class HotelBookingSystem {
    private PriorityQueue<Room> availableRooms;  // Min-Heap (Cheapest room first)
    private Map<String, Room> bookedRooms; // Guest -> Room
    private Queue<String> waitlist; // Waitlist for guests

    public HotelBookingSystem() {
        availableRooms = new PriorityQueue<>(Comparator.comparingDouble(Room::getPrice));
        bookedRooms = new HashMap<>();
        waitlist = new LinkedList<>();

        // Add some rooms
        availableRooms.add(new Room(101, 80));
        availableRooms.add(new Room(102, 100));
        availableRooms.add(new Room(103, 90));
        availableRooms.add(new Room(104, 110));
    }

    // Book a room
    public void bookRoom(String guestName) {
        if (!availableRooms.isEmpty()) {
            Room room = availableRooms.poll();
            room.bookRoom();
            bookedRooms.put(guestName, room);
            System.out.println(guestName + " booked " + room);
        } else {
            waitlist.add(guestName);
            System.out.println("No rooms available. " + guestName + " is waitlisted.");
        }
    }

    // Cancel a booking
    public void cancelBooking(String guestName) {
        if (bookedRooms.containsKey(guestName)) {
            Room room = bookedRooms.remove(guestName);
            room.cancelBooking();
            availableRooms.add(room);
            System.out.println(guestName + " canceled their booking. Room " + room.getRoomNumber() + " is now available.");

            // Assign room to the first waitlisted guest
            if (!waitlist.isEmpty()) {
                String nextGuest = waitlist.poll();
                bookRoom(nextGuest);
            }
        } else {
            System.out.println("No booking found for " + guestName);
        }
    }

    public void displayBookings() {
        System.out.println("\nCurrent Bookings:");
        bookedRooms.forEach((guest, room) -> System.out.println(guest + " -> " + room));
        System.out.println("Waitlist: " + waitlist);
    }
}

// Main Class
