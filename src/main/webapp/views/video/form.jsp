<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
    <c:when test="${editing}">
        <c:set var="formAction" value="${pageContext.request.contextPath}/video/edit?id=${video.videoId}"/>
        <c:set var="pageTitle" value="Cập nhật video"/>
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="${pageContext.request.contextPath}/video/add"/>
        <c:set var="pageTitle" value="Thêm video"/>
    </c:otherwise>
</c:choose>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${pageTitle}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<header class="topbar">
    <strong><a href="${pageContext.request.contextPath}/home">VideoHub</a></strong>
    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</header>
<main class="content">
    <h1><c:out value="${pageTitle}"/></h1>
    <c:if test="${not empty alert}"><div class="alert"><c:out value="${alert}"/></div></c:if>
    <form class="form-card video-form" action="${formAction}" method="post" enctype="multipart/form-data">
        <label for="title">Tiêu đề</label>
        <input id="title" name="title" maxlength="255" value="<c:out value='${video.title}'/>" required>

        <label for="description">Mô tả</label>
        <textarea id="description" name="description" rows="5"><c:out value="${video.description}"/></textarea>

        <label for="videoFile">File video <c:if test="${editing}">(để trống nếu không đổi file)</c:if></label>
        <input id="videoFile" type="file" name="videoFile" accept=".mp4,.webm,.ogg,video/mp4,video/webm,video/ogg"
               <c:if test="${not editing}">required</c:if>>
        <small>Hỗ trợ MP4, WebM, OGG. Tối đa 200 MB.</small>

        <button type="submit">Lưu video</button>
        <a class="button secondary" href="${pageContext.request.contextPath}/video">Quay lại</a>
    </form>
</main>
</body>
</html>
