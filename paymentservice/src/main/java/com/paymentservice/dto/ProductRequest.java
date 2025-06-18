package com.paymentservice.dto;

public class ProductRequest {
	private String propertyName;
    private String currency;
    private long totalNigths;
    private long BasePrice;
    private long bookingId;
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public long getTotalNigths() {
		return totalNigths;
	}
	public void setTotalNigths(long totalNigths) {
		this.totalNigths = totalNigths;
	}
	public long getBasePrice() {
		return BasePrice;
	}
	public void setBasePrice(long basePrice) {
		BasePrice = basePrice;
	}
	public long getBookingId() {
		return bookingId;
	}
	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}
	
		
}

