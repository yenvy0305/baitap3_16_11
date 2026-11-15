<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="_top.jspf" %>
<h1>Thêm danh mục mới</h1>
<c:if test="${not empty alert}"><div class="alert">${alert}</div></c:if>
<form class="form-card" action="${pageContext.request.contextPath}/admin/category/add"
      method="post" enctype="multipart/form-data">
    <label>Tên danh mục</label>
    <input name="cateName" required>
    <label>Ảnh đại diện</label>
    <input type="file" name="icon" accept="image/*">
    <button type="submit">Thêm</button>
    <a class="button secondary" href="${pageContext.request.contextPath}/admin/category/list">Hủy</a>
</form>
<%@ include file="_bottom.jspf" %>
</body>
</html>