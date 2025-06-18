package com.bookingservice.dto;

import java.time.LocalDate;

public class RoomAvailability {

	private long id;

	private LocalDate availableDate;

	private int availableCount;

	private double price;

	private RoomsDto room;

	public RoomsDto getRoom() {
		return room;
	}

	public void setRoom(RoomsDto room) {
		this.room = room;
	}

	public long getId() {
		return id;
	}

	public LocalDate getAvailableDate() {
		return availableDate;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public double getPrice() {
		return price;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setAvailableDate(LocalDate availableDate) {
		this.availableDate = availableDate;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	// Getters and Setters...

}