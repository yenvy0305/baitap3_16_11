<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${video.title}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<header class="topbar">
    <strong><a href="${pageContext.request.contextPath}/home">VideoHub</a></strong>
    <span>Xin chào <c:out value="${sessionScope.account.fullname}"/></span>
    <nav>
        <a href="${pageContext.request.contextPath}/video">Video của tôi</a>
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </nav>
</header>
<main class="content video-detail">
    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
    <h1><c:out value="${video.title}"/></h1>
    <p class="video-meta">
        Đăng bởi <c:out value="${video.user.fullname}"/>
        · <fmt:formatDate value="${video.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
    </p>
    <video controls preload="metadata" playsinline>
        <source src="${pageContext.request.contextPath}/video/file?name=${video.fileName}"
                type="${video.mimeType}">
        Trình duyệt của bạn không hỗ trợ phát video.
    </video>
    <div class="video-detail-description"><c:out value="${video.description}"/></div>
    <c:if test="${sessionScope.account.id eq video.user.id}">
        <a class="button" href="${pageContext.request.contextPath}/video/edit?id=${video.videoId}">Sửa video</a>
    </c:if>
</main>
</body>
</html>
