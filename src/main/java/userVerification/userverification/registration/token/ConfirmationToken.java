package userVerification.userverification.registration.token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import userVerification.userverification.appuser.AppUser;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @SequenceGenerator(name = "confirmation_token_sequence", sequenceName = "confirmation_token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

    public ConfirmationToken(String token, LocalDateTime createAt, LocalDateTime expiredAt, AppUser appUser) {
        this.token = token;
        this.createAt = createAt;
        this.expiresAt = expiredAt;
        this.appUser = appUser;
    }
}
