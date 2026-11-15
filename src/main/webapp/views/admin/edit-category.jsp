<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sửa danh mục</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="_top.jspf" %>
<h1>Chỉnh sửa danh mục</h1>
<form class="form-card" action="${pageContext.request.contextPath}/admin/category/edit"
      method="post" enctype="multipart/form-data">
    <input type="hidden" name="cateId" value="${category.cateId}">
    <label>Tên danh mục</label>
    <input name="cateName" value="${category.cateName}" required>
    <c:if test="${not empty category.icon}">
        <label>Ảnh hiện tại</label>
        <img class="category-image"
             src="${pageContext.request.contextPath}/image?fname=${category.icon}" alt="">
    </c:if>
    <label>Chọn ảnh mới (có thể để trống)</label>
    <input type="file" name="icon" accept="image/*">
    <button type="submit">Lưu thay đổi</button>
    <a class="button secondary" href="${pageContext.request.contextPath}/admin/category/list">Hủy</a>
</form>
<%@ include file="_bottom.jspf" %>
</body>
</html>