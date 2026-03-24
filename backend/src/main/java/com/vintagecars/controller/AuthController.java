package com.vintagecars.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vintagecars.model.User;
import com.vintagecars.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ NEW: Store OTP per user (does not break old logic)
    private Map<String, String> otpStore = new HashMap<>();


    // =========================
    // ✅ SIGNUP (UNCHANGED + SAFE CHECK)
    // =========================
    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody User user) {

        Map<String, String> response = new HashMap<>();

        User existing = userRepository.findByEmail(user.getEmail());

        if (existing != null) {
            response.put("status", "user_exists");
            response.put("message", "User already exists");
            return response;
        }

        userRepository.save(user);

        response.put("status", "success");
        response.put("message", "User registered successfully");

        return response;
    }


    // =========================
    // ✅ LOGIN (ONLY FIXED STORAGE)
    // =========================
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {

        Map<String, String> response = new HashMap<>();

        User existing = userRepository.findByEmail(user.getEmail());

        if (existing != null && existing.getPassword().equals(user.getPassword())) {

            Random random = new Random();
            String generatedOtp = String.valueOf(100000 + random.nextInt(900000));

            // ✅ FIX: normalize email
            String email = user.getEmail().trim().toLowerCase();

            // ✅ store OTP
            otpStore.put(email, generatedOtp);

            System.out.println("Login OTP for " + email + ": " + generatedOtp);

            response.put("status", "otp_sent");

        } else {

            response.put("status", "invalid_credentials");

        }

        return response;
    }


    // =========================
    // ✅ VERIFY OTP (FIXED BUT COMPATIBLE)
    // =========================
    @PostMapping("/verify")
    public Map<String, String> verifyOtp(@RequestBody Map<String, String> body) {

        Map<String, String> response = new HashMap<>();

        String email = body.get("email");
        String enteredOtp = body.get("otp");

        // ✅ safety handling (DOES NOT BREAK OLD FLOW)
        if (email != null) {
            email = email.trim().toLowerCase();
        }

        String storedOtp = otpStore.get(email);

        // 🔍 DEBUG (keep this for testing)
        System.out.println("Email received: " + email);
        System.out.println("Entered OTP: " + enteredOtp);
        System.out.println("Stored OTP: " + storedOtp);

        if (storedOtp != null && storedOtp.equals(enteredOtp)) {

            response.put("status", "success");

            // ✅ remove OTP after success
            otpStore.remove(email);

        } else {

            response.put("status", "failed");

        }

        return response;
    }
}