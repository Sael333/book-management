package bookmanagement.scheduled;

import bookmanagement.dao.BookingDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskService {

    private final BookingDao bookingDao;

    @Scheduled(cron = "${application.scheduled.box-office.update-state}")
    public void refreshBoxOfficesState() {
        log.info("Init process to set available boxOffice when endDate expire...");
        bookingDao.updateStateByExpiration();
    }
}
