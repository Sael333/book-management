package bookmanagement.repository;

import bookmanagement.entity.BoxOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxOfficeRepository extends JpaRepository<BoxOffice, Integer> {
    List<BoxOffice> findByAvailable(String available);
}
