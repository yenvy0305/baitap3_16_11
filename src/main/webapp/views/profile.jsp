<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <base href="${pageContext.request.contextPath}/">
    <title>Hồ sơ người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<section class="profile-page">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1 class="h2 mb-1">Hồ sơ người dùng</h1>
            <p class="text-secondary mb-0">Cập nhật thông tin và ảnh đại diện.</p>
        </div>
    </div>

    <c:if test="${param.updated eq '1'}"><div class="alert alert-success">Đã cập nhật hồ sơ.</div></c:if>
    <c:if test="${param.otpSent eq '1'}"><div class="alert alert-info">Đã gửi OTP đến email mới. Nhập mã để xác nhận thay đổi email.</div></c:if>
    <c:if test="${param.emailUpdated eq '1'}"><div class="alert alert-success">Email mới đã được xác nhận và cập nhật.</div></c:if>
    <c:if test="${param.otpError eq '1'}"><div class="alert alert-warning">Chưa gửi được OTP. Bạn có thể yêu cầu gửi lại bên dưới.</div></c:if>
    <c:if test="${not empty alert}"><div class="alert alert-danger"><c:out value="${alert}"/></div></c:if>

    <div class="card shadow-sm mb-4">
        <div class="card-body p-4">
            <form class="needs-validation" action="${pageContext.request.contextPath}/profile"
                  method="post" enctype="multipart/form-data">
                <div class="row g-4">
                    <div class="col-md-4 text-center">
                        <c:choose>
                            <c:when test="${not empty profile.avatar}">
                                <img class="profile-avatar mb-3" src="<c:out value='${profile.avatar}'/>" alt="Ảnh đại diện">
                            </c:when>
                            <c:otherwise>
                                <div class="profile-avatar profile-avatar-empty mb-3">Ảnh đại diện</div>
                            </c:otherwise>
                        </c:choose>
                        <label class="form-label" for="avatar">Chọn ảnh mới</label>
                        <input class="form-control" id="avatar" type="file" name="avatar"
                               accept="image/jpeg,image/png,image/webp">
                        <div class="form-text">JPG, PNG hoặc WebP; tối đa 5 MB. Ảnh được tải lên Cloudinary.</div>
                    </div>
                    <div class="col-md-8">
                        <label class="form-label" for="username">Tài khoản</label>
                        <input class="form-control" id="username" value="<c:out value='${profile.username}'/>" readonly>

                        <label class="form-label mt-3" for="fullname">Họ tên</label>
                        <input class="form-control" id="fullname" name="fullname" maxlength="255"
                               value="<c:out value='${profile.fullname}'/>" required>

                        <label class="form-label mt-3" for="phone">Số điện thoại</label>
                        <input class="form-control" id="phone" name="phone" maxlength="30"
                               pattern="[0-9+() .-]{7,30}" value="<c:out value='${profile.phone}'/>">

                        <label class="form-label mt-3" for="currentEmail">Email hiện tại</label>
                        <input class="form-control" id="currentEmail" value="<c:out value='${profile.email}'/>" readonly>

                        <label class="form-label mt-3" for="newEmail">Email mới (để trống nếu không đổi)</label>
                        <input class="form-control" id="newEmail" type="email" name="newEmail" maxlength="255"
                               value="<c:out value='${newEmail}'/>">
                        <button class="btn btn-primary mt-4" type="submit">Lưu hồ sơ</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <c:if test="${not empty profile.profilePendingEmail}">
        <div class="card border-info shadow-sm">
            <div class="card-body p-4">
                <h2 class="h5">Xác nhận email mới</h2>
                <p>Đã gửi mã đến <strong><c:out value="${profile.profilePendingEmail}"/></strong>. OTP có hiệu lực 10 phút.</p>
                <form class="needs-validation" action="${pageContext.request.contextPath}/profile/verify-email" method="post">
                    <label class="form-label" for="profileOtp">Mã OTP</label>
                    <input class="form-control" id="profileOtp" name="otp" inputmode="numeric"
                           pattern="[0-9]{6}" minlength="6" maxlength="6" required>
                    <button class="btn btn-success mt-3" type="submit">Xác nhận email</button>
                </form>
                <form action="${pageContext.request.contextPath}/profile/resend-email-otp" method="post">
                    <button class="btn btn-outline-primary mt-2" type="submit">Gửi lại OTP</button>
                </form>
            </div>
        </div>
    </c:if>
</section>
</body>
</html>
