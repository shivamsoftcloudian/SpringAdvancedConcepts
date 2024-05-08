package com.softclouds.SpringAdvancedConcepts.ratelimit;

import com.softclouds.SpringAdvancedConcepts.entity.BlacklistToken;
import com.softclouds.SpringAdvancedConcepts.entity.User;
import com.softclouds.SpringAdvancedConcepts.repository.BlacklistTokenRepository;
import com.softclouds.SpringAdvancedConcepts.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Service
@Slf4j
@RequiredArgsConstructor
public class RateLimitAspectImpl {

    public static final String ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!";
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    private final BlacklistTokenRepository blacklistTokenRepository;
    private final UserRepository userRepository;

    @Value("${app.rate.limit}")
    private int RATE_LIMIT;

    @Value("${app.rate.duration.inMs}")
    private long RATE_DURATION_IN_MS;

    @Value("${login.lock.duration.inMs}")
    private long LOGIN_LOCK_DURATION_IN_MS;

    @Before("@annotation(com.softclouds.SpringAdvancedConcepts.ratelimit.WithRateLimitProtection)")
    public void rateLimit() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String key = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(key, new ArrayList<>());
        requestCounts.get(key).add(currentTime);
        cleanUpRequestCounts(currentTime);
        if (requestCounts.get(key).size() >= RATE_LIMIT) {
            expireUserSession(requestAttributes);
            log.info(String.format(
                    ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), key, RATE_DURATION_IN_MS));
            throw new RateLimitException(String.format(
                    ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), key, RATE_DURATION_IN_MS));
        }
    }

    private void expireUserSession(ServletRequestAttributes requestAttributes) {

        HttpServletRequest request = requestAttributes.getRequest();
        request.getSession().invalidate();
        log.info("User session is invalidated");
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            BlacklistToken blacklistToken = new BlacklistToken();
            blacklistToken.setToken(token);
            blacklistTokenRepository.save(blacklistToken);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetail.getUsername());
        user.setLocked(true);
        user.setLockExpiration(new Date(System.currentTimeMillis() + LOGIN_LOCK_DURATION_IN_MS));
        userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(null);

        log.debug("JWT token is blacklisted and user is locked for {}", LOGIN_LOCK_DURATION_IN_MS);

    }


    private void cleanUpRequestCounts(long currentTime) {
        requestCounts.values().forEach(requestCount -> requestCount
                .removeIf(time -> timeIsTooOld(currentTime, time)));

    }

    private boolean timeIsTooOld(long currentTime, long timeToCheck) {
        return currentTime - timeToCheck >= RATE_DURATION_IN_MS;
    }
}
