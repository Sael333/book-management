package bookmanagement.mapper;

import bookmanagement.entity.Booking;
import bookmanagement.entity.BoxOffice;
import bookmanagement.model.request.BookingRequest;
import bookmanagement.model.response.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface BookingMapper {
    @Mapping(target = "endDate", source = "bookingRequest.endDate")
    @Mapping(target = "boxId", source = "boxOffice.boxId")
    @Mapping(target = "creationDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "userId", source = "bookingRequest.email")
    @Mapping(target = "code", source = "securityCode")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "bookingRequest.name")
    Booking mapBooking(int id, int securityCode, BookingRequest bookingRequest, BoxOffice boxOffice);

    @Mapping(target = "name", source = "bookingRequest.name")
    @Mapping(target = "creationDate", source = "booking.creationDate")
    @Mapping(target = "endDate", source = "bookingRequest.endDate")
    @Mapping(target = "boxOffice", source = "booking.boxId")
    @Mapping(target = "securityCode", source = "booking.code")
    @Mapping(target = "bookingId", source = "booking.id")
    BookingResponse mapBookingResponse(BookingRequest bookingRequest, Booking booking);
}
