package net.planet01.oauthauthorizationserver.service;

import net.planet01.oauthauthorizationserver.model.entity.User;
import net.planet01.oauthauthorizationserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {

    public static final int MAX_FAILED_ATTEMPTS = 6;
    private static final long LOCK_TIME_DURATION = 30; //30 minutes

    @Autowired
    private UserRepository userRepository;

    public int increaseFailedAttempts(User user) {
        int failedAttempts = user.getFailedAttempt() + 1;
        user.setFailedAttempt(failedAttempts);
        userRepository.save(user);

        return failedAttempts;
    }

    public void resetFailedAttempts(User user) {
        this.unlock(user);
    }

    public void applyLock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now());
        userRepository.save(user);
    }

    private void unlock(User user) {
        user.setAccountNonLocked(true);
        user.setLockTime(null);
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    public boolean isAccountLocked(User user) {
        return user.isAccountLocked() && isLockPeriodExpired(user.getLockTime());
    }

    private boolean isLockPeriodExpired(LocalDateTime date) {
        LocalDateTime lockPeriodExpireDate =  date.plusMinutes(LOCK_TIME_DURATION);
        return lockPeriodExpireDate.isAfter(LocalDateTime.now());
    }

    public boolean lockIfApplicable(User user, int failedAttempts) {
        if(failedAttempts >= MAX_FAILED_ATTEMPTS) {
            applyLock(user);
            return true;
        }
        return false;
    }

    public long remainingLockPeriod(LocalDateTime date) {
        LocalDateTime lockPeriodExpireDate =  date.plusMinutes(LOCK_TIME_DURATION);
        long expiredTimeInMilliSeconds = lockPeriodExpireDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long currentTimeInMilliSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        long diff = expiredTimeInMilliSeconds - currentTimeInMilliSeconds;

        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }
}
