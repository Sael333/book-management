package bookmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOX_OFFICE", schema = "LOCKER")
@NoArgsConstructor
@Data
public class BoxOffice {

    @Id
    @Column(name = "BOX_ID")
    private Integer boxId;

    @Column(name = "LOCK_ID")
    private String lockId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "AVAILABLE")
    private String available;
}