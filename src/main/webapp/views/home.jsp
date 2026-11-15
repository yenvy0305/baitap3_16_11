<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang người dùng</title>
</head>
<body>
    <h1>Xin chào ${sessionScope.account.fullname}</h1>
    <p>Bạn đã đăng nhập với quyền người dùng.</p>
    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</body>
</html>