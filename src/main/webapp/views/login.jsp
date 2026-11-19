<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <base href="${pageContext.request.contextPath}/">
    <title>Đăng nhập</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-page">
<div class="auth-card">
    <h1>Đăng nhập</h1>

    <c:if test="${param.registered eq '1'}">
        <div class="success">Đăng ký thành công. Hãy đăng nhập.</div>
    </c:if>
    <c:if test="${param.activated eq '1'}">
        <div class="success">Kích hoạt tài khoản thành công. Hãy đăng nhập.</div>
    </c:if>
    <c:if test="${param.passwordReset eq '1'}">
        <div class="success">Đổi mật khẩu thành công. Hãy đăng nhập bằng mật khẩu mới.</div>
    </c:if>

    <c:if test="${not empty alert}">
        <div class="alert">${alert}</div>
    </c:if>

    <form class="needs-validation" action="${pageContext.request.contextPath}/login" method="post">
        <label for="username">Tài khoản</label>
        <input id="username" name="username"
               value="${rememberedUsername}" minlength="3" maxlength="100"
               pattern="[A-Za-z0-9._-]{3,100}" required>

        <label for="password">Mật khẩu</label>
        <input id="password" name="password" type="password" maxlength="128" required>

        <div style="display:flex; align-items:center; justify-content:flex-start; gap:8px; margin:14px 0 20px;">
    		<input id="remember"
           		type="checkbox"
           		name="remember"
           		value="on"
           		style="width:16px !important; height:16px; flex:0 0 16px; margin:0; padding:0;">
    		<label for="remember"
           		style="display:inline !important; width:auto; margin:0; font-weight:600;">
        	  Ghi nhớ tài khoản
    		</label>
		</div>

        <button class="btn btn-primary w-100" type="submit">Đăng nhập</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a></p>

    <p>Chưa có tài khoản?
       <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
    </p>
</div>
</body>
</html>
