<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Kích hoạt tài khoản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-page">
<div class="auth-card">
    <h1>Kích hoạt tài khoản</h1>
    <p>Nhập email và OTP được gửi đến hộp thư của bạn. Mã có hiệu lực 10 phút.</p>
    <c:if test="${param.sendError eq '1'}">
        <div class="alert">Tài khoản đã được tạo nhưng chưa gửi được email. Hãy kiểm tra SMTP rồi yêu cầu gửi lại mã.</div>
    </c:if>
    <c:if test="${not empty message}"><div class="success">${message}</div></c:if>
    <c:if test="${not empty alert}"><div class="alert">${alert}</div></c:if>

    <form action="${pageContext.request.contextPath}/activate" method="post">
        <label for="email">Email</label>
        <input id="email" type="email" name="email" value="<c:out value='${email}'/>" required>
        <label for="otp">Mã OTP</label>
        <input id="otp" name="otp" inputmode="numeric" pattern="[0-9]{6}" maxlength="6">
        <button type="submit" name="action" value="activate">Kích hoạt</button>
        <button type="submit" name="action" value="resend">Gửi lại OTP</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
</div>
</body>
</html>
