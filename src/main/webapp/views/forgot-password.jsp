<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <base href="${pageContext.request.contextPath}/">
    <title>Quên mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-page">
<div class="auth-card">
    <h1>Quên mật khẩu</h1>
    <c:if test="${not empty message}"><div class="success">${message}</div></c:if>
    <c:if test="${not empty alert}"><div class="alert">${alert}</div></c:if>

    <form class="needs-validation" action="${pageContext.request.contextPath}/forgot-password" method="post">
        <input type="hidden" name="action" value="request">
        <label for="email">Email tài khoản</label>
        <input id="email" class="form-control" type="email" name="email" maxlength="255" value="<c:out value='${email}'/>" required>
        <button class="btn btn-primary" type="submit">Gửi mã OTP</button>
    </form>

    <c:if test="${step eq 'verify'}">
        <hr>
        <form class="needs-validation" action="${pageContext.request.contextPath}/forgot-password" method="post">
            <input type="hidden" name="action" value="reset">
            <input type="hidden" name="email" value="<c:out value='${email}'/>">
            <label for="otp">Mã OTP</label>
            <input id="otp" class="form-control" name="otp" inputmode="numeric" pattern="[0-9]{6}" minlength="6" maxlength="6" required>
            <label for="newPassword">Mật khẩu mới</label>
            <input id="newPassword" class="form-control" type="password" name="newPassword" minlength="8" maxlength="128" required>
            <label for="confirmPassword">Nhập lại mật khẩu mới</label>
            <input id="confirmPassword" class="form-control" type="password" name="confirmPassword" minlength="8" maxlength="128" required>
            <button class="btn btn-primary" type="submit">Đặt lại mật khẩu</button>
        </form>
    </c:if>
    <p><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
</div>
</body>
</html>
