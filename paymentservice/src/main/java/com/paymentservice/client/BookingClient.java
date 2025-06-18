package com.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymentservice.dto.ProductRequest;

@FeignClient(name="BOOKINGSERVICE", url = "http://localhost:8085")
public interface BookingClient {

	@GetMapping("/api/v1/booking/get-product-request")
    ProductRequest getProductRequest(@RequestParam Long Id);
	
	
	@PutMapping("/api/v1/booking/update-status-booking")
	public boolean updateBookingStatus(@RequestParam long bookingId);


	
	
	

}
