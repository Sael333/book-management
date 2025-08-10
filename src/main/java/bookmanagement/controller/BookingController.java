package bookmanagement.controller;

import bookmanagement.model.request.BookingRequest;
import bookmanagement.model.response.BookingResponse;
import bookmanagement.services.BookService;
import bookmanagement.services.BoxOfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookingController implements BookingControllerApi {
    public final BookService bookService;
    public final BoxOfficeService boxOfficeService;

    @Override
    public ResponseEntity<BookingResponse> createBook(BookingRequest bookingRequest) throws Exception {
        BookingResponse bookingResponse = bookService.createBook(bookingRequest);
        if (bookingResponse.getErrorCode() == HttpStatus.NO_CONTENT.value()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(bookingResponse);
    }

    @Override
    public ResponseEntity<BookingResponse> pickupLuggage(String bookingId) throws Exception {
        BookingResponse bookingResponse = bookService.generateSecurityCode(bookingId);
        if (bookingResponse == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(bookingResponse);
    }

    @Override
    public ResponseEntity<Boolean> checkBoxOfficeAvailable() {
        boolean hasAvailables = boxOfficeService.checkBoxOfficeAvailables();
        return ResponseEntity.ok(hasAvailables);
    }
}
