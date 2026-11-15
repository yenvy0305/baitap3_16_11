<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-page">
<div class="auth-card">
    <h1>Tạo tài khoản mới</h1>
    <c:if test="${not empty alert}">
        <div class="alert">${alert}</div>
    </c:if>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <label>Email</label>
        <input type="email" name="email" required>
        <label>Tài khoản</label>
        <input name="username" required>
        <label>Họ tên</label>
        <input name="fullname" required>
        <label>Số điện thoại</label>
        <input name="phone">
        <label>Mật khẩu</label>
        <input type="password" name="password" required>
        <button type="submit">Đăng ký</button>
    </form>
    <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
</div>
</body>
</html>