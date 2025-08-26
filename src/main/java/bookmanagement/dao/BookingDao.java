package bookmanagement.dao;

import bookmanagement.entity.Booking;
import bookmanagement.entity.BoxOffice;
import bookmanagement.model.request.BookingRequest;
import bookmanagement.repository.BookingRepository;
import bookmanagement.repository.BoxOfficeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingDao {

    private final BookingRepository bookingRepository;
    private final BoxOfficeRepository boxOfficeRepository;

    @Transactional
    public void generateBooking(BookingRequest bookingRequest, Booking booking, BoxOffice boxOffice) {
        try {
            bookingRepository.save(booking);

            boxOffice.setUserId(bookingRequest.getEmail());
            boxOffice.setEndDate(bookingRequest.getEndDate());
            boxOffice.setAvailable("N");
            boxOfficeRepository.save(boxOffice);
        } catch (Exception e) {
            log.error("Error while generate booking: {}", e);
            throw e;
        }
    }

    public List<BoxOffice> getListBoxOfficeAvailables() {
        List<BoxOffice> boxOfficeList = new ArrayList<>();
        try {
            boxOfficeList = boxOfficeRepository.findByAvailableOrderByBoxId("Y");
        } catch (Exception e) {
            log.error("Error while getting box office availables :{}", e);
            throw e;
        }
        return boxOfficeList;
    }

    @Transactional
    public void updateStateByExpiration(){
        try {
            List<BoxOffice> boxOfficeList = boxOfficeRepository.findByAvailableOrderByBoxId("N");
            boxOfficeList.forEach(boxOffice -> {
                if (boxOffice.getEndDate().isBefore(LocalDateTime.now())){
                    boxOffice.setAvailable("Y");
                    boxOffice.setUserId(StringUtils.EMPTY);
                    boxOffice.setEndDate(null);
                }
            });
            boxOfficeRepository.saveAll(boxOfficeList);
        } catch (Exception e) {
            log.error("Error while updating box office :{}", e);
            throw e;
        }
    }

    public BoxOffice findBoxOfficeById(String bookingId) {
        Optional<BoxOffice> boxOffice;
        try {
            boxOffice = boxOfficeRepository.findById(Integer.valueOf(bookingId));
        } catch (Exception e) {
            log.error("Error while getting box office by id :{}", e);
            throw e;
        }
        if (boxOffice.isPresent()) {
            return boxOffice.get();
        } else {
            throw new RuntimeException("No booking found with this id");
        }
    }

    public Booking findBookingById(String bookingId) {
        Optional<Booking> booking;
        try {
            booking = bookingRepository.findById(bookingId);
        } catch (Exception e) {
            log.error("Error while getting box office by id :{}", e);
            throw e;
        }
        if (booking.isPresent()) {
            return booking.get();
        } else {
            throw new RuntimeException("No booking found with this id");
        }
    }

    public void saveBooking(Booking booking) {
        try {
            bookingRepository.save(booking);
        } catch (Exception e) {
            log.error("Error while getting box office by id :{}", e);
            throw e;
        }
    }

}
