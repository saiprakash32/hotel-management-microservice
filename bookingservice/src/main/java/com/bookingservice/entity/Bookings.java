package com.bookingservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name="bookings")
public class Bookings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long propertyId;
	private long roomId;
	private long RoomAvailabilityid;
	private String name;
	private String email;
	private String mobile;
	private String propertyName;
	private double BasePrice;
	private int TotalNights;
	private double totalPrice; 
	private String status;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(long propertyId) {
		this.propertyId = propertyId;
	}
	public long getRoomId() {
		return roomId;
	}
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	public long getRoomAvailabilityid() {
		return RoomAvailabilityid;
	}
	public void setRoomAvailabilityid(long roomAvailabilityid) {
		RoomAvailabilityid = roomAvailabilityid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public double getBasePrice() {
		return BasePrice;
	}
	public void setBasePrice(double basePrice) {
		BasePrice = basePrice;
	}
	public int getTotalNights() {
		return TotalNights;
	}
	public void setTotalNights(int totalNights) {
		TotalNights = totalNights;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	

	
	
	
	
	
}
