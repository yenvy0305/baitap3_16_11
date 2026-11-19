<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <base href="${pageContext.request.contextPath}/">
    <title>Quản lý danh mục</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="_top.jspf" %>

<h1>Quản lý danh mục</h1>
<p>Nơi bạn có thể quản lý các danh mục của mình</p>

<form method="get"
      action="${pageContext.request.contextPath}/admin/category/list"
      style="display:flex; justify-content:flex-end; align-items:center;
             gap:8px; margin:16px 0;">
    <input type="text"
           name="keyword"
           value="${keyword}"
           placeholder="Tìm danh mục"
           style="max-width:260px;">
    <button type="submit">Tìm kiếm</button>
    <a class="button secondary"
       href="${pageContext.request.contextPath}/admin/category/list">
        Xóa tìm kiếm
    </a>
</form>

<a class="button"
   href="${pageContext.request.contextPath}/admin/category/add">
    Thêm danh mục mới
</a>

<table>
    <thead>
        <tr>
            <th>STT</th>
            <th>Hình ảnh</th>
            <th>Tên danh mục</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${categories}" var="cate" varStatus="st">
            <tr>
                <td>${st.index + 1}</td>
                <td>
                    <c:if test="${not empty cate.icon}">
                        <img class="category-image"
                             src="${pageContext.request.contextPath}/image?fname=${cate.icon}"
                             alt="Ảnh ${cate.cateName}">
                    </c:if>
                </td>
                <td>${cate.cateName}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.cateId}">
                        Sửa
                    </a>
                    |
                    <a href="${pageContext.request.contextPath}/admin/category/delete?id=${cate.cateId}"
                       onclick="return confirm('Bạn chắc chắn muốn xóa danh mục này?')">
                        Xóa
                    </a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="_bottom.jspf" %>
</body>
</html>
