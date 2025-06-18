package com.bookingservice.controller;

import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookingservice.client.PropertyClient;
import com.bookingservice.dto.APIResponse;
import com.bookingservice.dto.BookingDto;
import com.bookingservice.dto.ProductRequest;
import com.bookingservice.dto.PropertyDto;
import com.bookingservice.dto.RoomAvailability;
import com.bookingservice.dto.Rooms;
import com.bookingservice.entity.BookingDate;
import com.bookingservice.entity.Bookings;
import com.bookingservice.repository.BookingDateRepository;
import com.bookingservice.repository.BookingRepository;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

	@Autowired
	private PropertyClient propertyClient;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private BookingDateRepository bookingDateRepository;

	@PostMapping("/add-to-cart")
	public APIResponse<List<String>> cart(@RequestBody BookingDto bookingDto) {

		APIResponse<List<String>> apiResponse = new APIResponse<>();

		List<String> messages = new ArrayList<>();

		APIResponse<PropertyDto> response = propertyClient.getPropertyById(bookingDto.getPropertyId());

		APIResponse<Rooms> roomType = propertyClient.getRoomType(bookingDto.getRoomId());

		APIResponse<List<RoomAvailability>> totalRoomsAvailable = propertyClient
				.getTotalRoomsAvailable(bookingDto.getRoomId());

		List<RoomAvailability> availableRooms = totalRoomsAvailable.getData();

		Optional<RoomAvailability> matchedRoom = Optional.empty();

		// Check if rooms are available on each requested date
		for (LocalDate date : bookingDto.getDate()) {
			boolean isAvailable = availableRooms.stream()
					.anyMatch(ra -> ra.getAvailableDate().equals(date) && ra.getAvailableCount() > 0);

			System.out.println("Date " + date + " available: " + isAvailable); // Debugging log

			if (!isAvailable) {
				messages.add("Room not available on: " + date);
				apiResponse.setMessage("Sold Out");
				apiResponse.setStatus(500); // ⚠️ Should be 400 or 409 instead of 500
				apiResponse.setData(messages);
				return apiResponse;
			}

			// Get a room that is available on the given date
			matchedRoom = availableRooms.stream()
					.filter(r -> r.getAvailableDate().equals(date) && r.getAvailableCount() > 0).findFirst();
		}

		// Save booking with pending status
		Bookings bookings = new Bookings();
		bookings.setName(bookingDto.getName());
		bookings.setEmail(bookingDto.getEmail());
		bookings.setMobile(bookingDto.getMobile());
		bookings.setPropertyName(response.getData().getName());
		bookings.setPropertyId(response.getData().getId());
		bookings.setRoomId(roomType.getData().getId());
		bookings.setRoomAvailabilityid(matchedRoom.get().getId()); // ⚠️ matchedRoom is assumed to be present
		bookings.setStatus("pending"); // ❗ Should ideally use an Enum for status
		bookings.setTotalPrice(roomType.getData().getBasePrice() * bookingDto.getTotalNigths());
		bookings.setTotalNights(bookingDto.getTotalNigths());
		bookings.setBasePrice(roomType.getData().getBasePrice());
		// Persist booking entity
		Bookings savedBooking = bookingRepository.save(bookings);

		// Save each booking date
		for (LocalDate date : bookingDto.getDate()) {
			BookingDate bookingDate = new BookingDate();
			bookingDate.setDate(date);
			bookingDate.setBookings(savedBooking);
			BookingDate savedBookingDate = bookingDateRepository.save(bookingDate);

			if (savedBookingDate != null) {
				// Update room availability for the date
				propertyClient.updateRoomCount(savedBooking.getRoomAvailabilityid(), date);

			}

			// Add success message for this date
			messages.add("Booking successfully added to cart of date:" + date);

		}

		apiResponse.setMessage("Success");
		apiResponse.setStatus(200);
		apiResponse.setData(messages);

		return apiResponse;
	}

	@GetMapping("/get-product-request")
	public ProductRequest getProductRequest(Long Id) {
		Bookings booking = bookingRepository.findById(Id).orElseThrow(() -> new RuntimeException("Booking not found"));

		ProductRequest productRequest = new ProductRequest();

		productRequest.setPropertyName(booking.getPropertyName());
		productRequest.setCurrency("USD"); // hardcoded or fetched from related entities
		productRequest.setTotalNigths(booking.getTotalNights());

		productRequest.setBasePrice((long) booking.getBasePrice());
		productRequest.setBookingId(booking.getId());
		return productRequest;
	}

	@PutMapping("/update-status-booking")
	public boolean updateBookingStatus(@RequestParam long bookingId) {
	    return bookingRepository.findById(bookingId)
	            .map(booking -> {
	                booking.setStatus("confirmed");
	                bookingRepository.save(booking);
	                return true;
	            })
	            .orElse(false);
	}


}
