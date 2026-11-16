package vn.iotstar.service.impl;

import java.sql.Timestamp;
import java.time.Instant;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.MailService;
import vn.iotstar.util.OtpUtil;
import vn.iotstar.util.PasswordUtil;

public class UserServiceImpl implements UserService {
    private static final long OTP_VALIDITY_MINUTES = 10;
    private final UserDao dao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = dao.findByUsername(username);
        if (user == null || !user.isActive()) return null;

        String stored = user.getPassword();
        if (PasswordUtil.verify(password, stored)) return user;

        // Chuyển đổi tài khoản cũ đang lưu mật khẩu dạng thường sau lần đăng nhập hợp lệ.
        if (PasswordUtil.matchesLegacyPlaintext(password, stored)) {
            user.setPassword(PasswordUtil.hash(password));
            dao.update(user);
            return user;
        }
        return null;
    }

    @Override
    public boolean register(User user) {
        if (dao.existsEmail(user.getEmail())
                || dao.existsUsername(user.getUsername())
                || dao.existsPhone(user.getPhone())) {
            return false;
        }

        String otp = OtpUtil.generate();
        user.setRoleid(5);
        user.setAvatar(null);
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        user.setActive(false);
        user.setActivationCodeHash(PasswordUtil.hash(otp));
        user.setActivationExpiresAt(expiry());
        dao.insert(user);
        MailService.sendOtp(user.getEmail(), "kích hoạt tài khoản", otp);
        return true;
    }

    @Override
    public void sendActivationOtp(String email) {
        User user = dao.findByEmail(email);
        if (user == null || user.isActive()) return;

        String otp = OtpUtil.generate();
        user.setActivationCodeHash(PasswordUtil.hash(otp));
        user.setActivationExpiresAt(expiry());
        dao.update(user);
        MailService.sendOtp(email, "kích hoạt tài khoản", otp);
    }

    @Override
    public boolean activate(String email, String otp) {
        User user = dao.findByEmail(email);
        if (user == null || user.isActive()
                || !isValidOtp(otp, user.getActivationCodeHash(), user.getActivationExpiresAt())) {
            return false;
        }

        user.setActive(true);
        user.setActivationCodeHash(null);
        user.setActivationExpiresAt(null);
        dao.update(user);
        return true;
    }

    @Override
    public void requestPasswordReset(String email) {
        User user = dao.findByEmail(email);
        // Trả cùng một kết quả cho email có và không có trong hệ thống.
        if (user == null || !user.isActive()) return;

        String otp = OtpUtil.generate();
        user.setResetCodeHash(PasswordUtil.hash(otp));
        user.setResetExpiresAt(expiry());
        dao.update(user);
        MailService.sendOtp(email, "đặt lại mật khẩu", otp);
    }

    @Override
    public boolean resetPassword(String email, String otp, String newPassword) {
        User user = dao.findByEmail(email);
        if (user == null || !user.isActive()
                || !isValidOtp(otp, user.getResetCodeHash(), user.getResetExpiresAt())) {
            return false;
        }

        user.setPassword(PasswordUtil.hash(newPassword));
        user.setResetCodeHash(null);
        user.setResetExpiresAt(null);
        dao.update(user);
        return true;
    }

    private Timestamp expiry() {
        return Timestamp.from(Instant.now().plusSeconds(OTP_VALIDITY_MINUTES * 60));
    }

    private boolean isValidOtp(String otp, String hash, Timestamp expiresAt) {
        return otp != null && !otp.isBlank() && hash != null && expiresAt != null
                && Instant.now().isBefore(expiresAt.toInstant())
                && PasswordUtil.verify(otp.trim(), hash);
    }
}
