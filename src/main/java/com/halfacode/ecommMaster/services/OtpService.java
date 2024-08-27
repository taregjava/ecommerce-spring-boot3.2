package com.halfacode.ecommMaster.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, Long> otpExpiry = new HashMap<>();
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
  /*  private final StringRedisTemplate redisTemplate;
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
*/
  /*  @Autowired
    public OtpService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }*/

    /*public String generateOtp(String username) {
        String otp = String.valueOf(new Random().nextInt(999999));
        redisTemplate.opsForValue().set(username, otp, OTP_VALID_DURATION, TimeUnit.MILLISECONDS);
        return otp;
    }*/
    public String generateOtp(String username) {
        String otp = String.valueOf(new Random().nextInt(999999));
        otpStorage.put(username, otp);
        otpExpiry.put(username, System.currentTimeMillis() + OTP_VALID_DURATION);
        return otp;
    }
    public void sendOtp(String phoneNumber, String otp) {
        // Implement the logic to send OTP to user's phone number via SMS
        System.out.println("Sending OTP " + otp + " to phone number " + phoneNumber);
    }

    public boolean verifyOtp(String username, String otp) {
        String storedOtp = otpStorage.get(username);
        Long expiryTime = otpExpiry.get(username);

        if (storedOtp == null || expiryTime == null) {
            return false;
        }

        if (System.currentTimeMillis() > expiryTime) {
            otpStorage.remove(username);
            otpExpiry.remove(username);
            return false;
        }

        return storedOtp.equals(otp);
    }
}
