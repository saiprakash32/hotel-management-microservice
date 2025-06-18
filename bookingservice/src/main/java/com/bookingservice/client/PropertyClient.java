package com.bookingservice.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookingservice.dto.APIResponse;
import com.bookingservice.dto.PropertyDto;
import com.bookingservice.dto.RoomAvailability;
import com.bookingservice.dto.Rooms;

@FeignClient(name = "PROPERTYSERVICE", url = "http://localhost:8081")
public interface PropertyClient {

	@GetMapping("/api/v1/property/property-id")
	public APIResponse<PropertyDto> getPropertyById(@RequestParam long id);
	
			
    @GetMapping("/api/v1/property/room-available-room-id")
	public APIResponse<List<RoomAvailability>> getTotalRoomsAvailable(@RequestParam long id);

	@GetMapping("/api/v1/property/room-id")
	public APIResponse<Rooms> getRoomType(@RequestParam long id);
	
	@PutMapping("/api/v1/property/updateRoomCount")
	public APIResponse<Boolean> updateRoomCount(@RequestParam long id,  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

}
