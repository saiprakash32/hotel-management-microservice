package com.paymentservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentservice.client.BookingClient;
import com.paymentservice.dto.ProductRequest;
import com.paymentservice.dto.StripeResponse;
import com.paymentservice.service.StripeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;


@RestController
@RequestMapping("/product/v1")
public class ProductCheckoutController {
	
	@Autowired
	private BookingClient bookingClient;
	@Autowired 
    private StripeService stripeService;


    
    
 

    @PostMapping("/checkout")
    public StripeResponse checkoutProducts(@RequestParam Long id) {
        // Fetch product details from Booking service
        ProductRequest productRequest = bookingClient.getProductRequest(id);
        // Process payment via Strip
        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
        return stripeResponse;
    }
    
    @GetMapping("/success")
    public String handleSuccess(@RequestParam("session_id") String sessionId, @RequestParam("booking_id") long bookingId) {
        Stripe.apiKey = ""; // Replace with your actual secret key

        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();

            if ("paid".equalsIgnoreCase(paymentStatus)) {
            	boolean updateBookingStatus = bookingClient.updateBookingStatus(bookingId);
                if (updateBookingStatus) {
                	//send mail
                }
                
                return "Payment successful";
            } else {
                System.out.println("❌ Payment not completed: false");
                return "Payment not completed";
            }

        } catch (StripeException e) {
            e.printStackTrace();
           return "Stripe error occurred";
        }
    }


    @GetMapping("/cancel")
    public String handleCancel() {
        System.out.println("❌ Payment cancelled: false");
        return "Payment cancelled";
    }
}