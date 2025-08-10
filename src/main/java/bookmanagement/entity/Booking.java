package bookmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "BOOKING", schema = "LOCKER")
@NoArgsConstructor
@Data
public class Booking {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "CODE")
    private Integer code;

    @Column(name = "BOX_ID")
    private Integer boxId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "START_DATE")
    private LocalDateTime creationDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "LAST_USE")
    private LocalDateTime lastUse;

    @Column(name = "NAME")
    private String name;
}
