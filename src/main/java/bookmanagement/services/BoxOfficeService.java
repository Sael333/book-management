package bookmanagement.services;

import bookmanagement.dao.BookingDao;
import bookmanagement.entity.BoxOffice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxOfficeService {

    private final BookingDao bookingDao;

    public boolean checkBoxOfficeAvailables() {
        List<BoxOffice> boxOfficeList = bookingDao.getListBoxOfficeAvailables();
        return !boxOfficeList.isEmpty();
    }
}
