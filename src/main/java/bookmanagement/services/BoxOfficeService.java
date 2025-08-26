package bookmanagement.services;

import bookmanagement.dao.BookingDao;
import bookmanagement.entity.BoxOffice;
import bookmanagement.model.response.AvailabilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoxOfficeService {

    private final BookingDao bookingDao;

    public AvailabilityResponse checkBoxOfficeAvailables() {
        AvailabilityResponse availabilityResponse = new AvailabilityResponse();
        List<BoxOffice> boxOfficeList = bookingDao.getListBoxOfficeAvailables();
        availabilityResponse.setAvailable(!boxOfficeList.isEmpty());
        List<String> sizes = boxOfficeList.stream()
                .map(BoxOffice::getSize)
                .filter(size -> "L".equals(size) || "XL".equals(size))
                .distinct()
                .collect(Collectors.toList());
        availabilityResponse.setSizes(sizes);
        return availabilityResponse;
    }
}
