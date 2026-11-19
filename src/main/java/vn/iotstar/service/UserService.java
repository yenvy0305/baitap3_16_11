package vn.iotstar.service;

import vn.iotstar.model.User;

public interface UserService {
    User login(String username, String password);
    boolean register(User user);
    void sendActivationOtp(String email);
    boolean activate(String email, String otp);
    void requestPasswordReset(String email);
    boolean resetPassword(String email, String otp, String newPassword);
    User findById(int id);
    boolean isEmailAvailable(String email, int currentUserId);
    void updateProfile(User user);
    void requestProfileEmailChange(int userId, String newEmail);
    boolean verifyProfileEmailChange(int userId, String otp);
    void resendProfileEmailOtp(int userId);
}
