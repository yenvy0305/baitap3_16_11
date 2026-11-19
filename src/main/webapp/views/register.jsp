<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <base href="${pageContext.request.contextPath}/">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-page">
<div class="auth-card">
    <h1>Tạo tài khoản mới</h1>
    <c:if test="${not empty alert}">
        <div class="alert">${alert}</div>
    </c:if>
    <form class="needs-validation" action="${pageContext.request.contextPath}/register" method="post">
        <label>Email</label>
        <input class="form-control" type="email" name="email" maxlength="255" value="<c:out value='${param.email}'/>" required>
        <label>Tài khoản</label>
        <input class="form-control" name="username" minlength="3" maxlength="100" value="<c:out value='${param.username}'/>"
               pattern="[A-Za-z0-9._-]{3,100}" required>
        <label>Họ tên</label>
        <input class="form-control" name="fullname" maxlength="255" value="<c:out value='${param.fullname}'/>" required>
        <label>Số điện thoại</label>
        <input class="form-control" name="phone" pattern="[0-9+() .-]{7,30}" maxlength="30" value="<c:out value='${param.phone}'/>">
        <label>Mật khẩu</label>
        <input class="form-control" type="password" name="password" minlength="8" maxlength="128" required>
        <button class="btn btn-primary w-100" type="submit">Đăng ký</button>
    </form>
    <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
</div>
</body>
</html>
