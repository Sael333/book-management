package bookmanagement.controller;

import bookmanagement.model.request.BookingRequest;
import bookmanagement.model.response.AvailabilityResponse;
import bookmanagement.model.response.BookingResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/v1",
        produces = MediaType.APPLICATION_JSON_VALUE)
public interface BookingControllerApi {

    @PostMapping(value = "/book")
    ResponseEntity<BookingResponse> createBook(
            @Valid @RequestBody BookingRequest bookRequest) throws Exception;

    @PutMapping(value = "/pickupLuggage")
    ResponseEntity<BookingResponse> pickupLuggage(
            @Valid @RequestBody String bookingId) throws Exception;

    @GetMapping(value = "/checkBoxOfficeAvailable")
    ResponseEntity<AvailabilityResponse> checkBoxOfficeAvailable();
}
