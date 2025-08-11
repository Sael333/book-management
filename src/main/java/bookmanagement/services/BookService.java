package bookmanagement.services;

import bookmanagement.dao.BookingDao;
import bookmanagement.dto.EmailMessageDto;
import bookmanagement.entity.Booking;
import bookmanagement.entity.BoxOffice;
import bookmanagement.mapper.BookingMapper;
import bookmanagement.model.request.BookingRequest;
import bookmanagement.model.response.BookingResponse;
import bookmanagement.util.CodeGenerationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookingDao bookingDao;
    private final BookingMapper bookingMapper;
    private final TtlockApiService ttlockApiService;
    private final EmailNotificationService emailNotificationService;

    public BookingResponse createBook(BookingRequest bookingRequest) throws Exception {

        //Listamos las taquillas disponibles
        List<BoxOffice> boxOfficeList = bookingDao.getListBoxOfficeAvailables();
        if (!boxOfficeList.isEmpty()) {
            String passCode = String.valueOf(CodeGenerationUtils.generatePasscode());
            String expiration = LocalDateTime.now().plusHours(2).toString();
            ttlockApiService.generateSecurityCode(boxOfficeList.get(0).getLockId(), bookingRequest, expiration, passCode);
            int bookingCodeId = CodeGenerationUtils.generateBookingCodeId();
            //sustituir por el id del servicio ttlock
            Booking booking = bookingMapper.mapBooking(bookingCodeId, Integer.parseInt(passCode), bookingRequest, boxOfficeList.get(0));
            bookingDao.generateBooking(bookingRequest, booking, boxOfficeList.get(0));
            emailNotificationService.sendEmail(EmailMessageDto.builder()
                            .bookingId(bookingCodeId)
                            .to(bookingRequest.getEmail())
                            .securityCode(Integer.parseInt(passCode))
                            .name(bookingRequest.getName())
                            .boxId(String.valueOf(boxOfficeList.get(0).getBoxId()))
                            .endDate(bookingRequest.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                            .build(), "booking");
            BookingResponse bookingResponse = bookingMapper.mapBookingResponse(bookingRequest, booking);
            return bookingResponse;
        } else {
            return BookingResponse.builder().errorCode(204).build();
        }
    }

    public BookingResponse generateSecurityCode(String bookingId) {
        Booking booking = bookingDao.findBookingById(bookingId);
        BoxOffice boxOffice = bookingDao.findBoxOfficeById(String.valueOf(booking.getBoxId()));
        if (booking.getEndDate().isAfter(LocalDateTime.now())) {
            String passCode = String.valueOf(CodeGenerationUtils.generatePasscode());
            String expiration = LocalDateTime.now().plusMinutes(15).toString();
            BookingRequest bookingRequest = BookingRequest.builder()
                    .email(booking.getUserId())
                    .name(booking.getName())
                    .phone(StringUtils.EMPTY)
                    .endDate(booking.getEndDate())
                    .name(booking.getName())
                    .build();
            ttlockApiService.generateSecurityCode(boxOffice.getLockId(), bookingRequest, expiration, passCode);
            booking.setCode(Integer.valueOf(passCode));
            booking.setLastUse(LocalDateTime.now());
            bookingDao.saveBooking(booking);
            emailNotificationService.sendEmail(EmailMessageDto.builder()
                    .bookingId(Integer.parseInt(booking.getId()))
                    .to(bookingRequest.getEmail())
                    .securityCode(Integer.parseInt(passCode))
                    .name(bookingRequest.getName())
                    .boxId(String.valueOf(boxOffice.getBoxId()))
                    .endDate(bookingRequest.getEndDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                    .build(), "pickup");
            return bookingMapper.mapBookingResponse(bookingRequest, booking);
        } else {
            return null;
        }
    }
}
