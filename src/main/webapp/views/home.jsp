<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <base href="${pageContext.request.contextPath}/">
    <title>Trang chủ video</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="content video-page">
    <div class="video-heading">
        <div>
            <h1>10 video mới nhất</h1>
            <p>Video được đăng gần đây trên hệ thống.</p>
        </div>
        <a class="button" href="${pageContext.request.contextPath}/video/add">Tải video lên</a>
    </div>
    <c:choose>
        <c:when test="${empty videos}">
            <p>Chưa có video nào. Hãy tải video đầu tiên lên.</p>
        </c:when>
        <c:otherwise>
            <div class="video-grid">
                <c:forEach var="video" items="${videos}">
                    <article class="video-card">
                        <a class="video-title" href="${pageContext.request.contextPath}/video/detail?id=${video.videoId}">
                            <c:out value="${video.title}"/>
                        </a>
                        <p class="video-description"><c:out value="${video.description}"/></p>
                        <div class="video-meta">
                            <span><c:out value="${video.user.fullname}"/></span>
                            <span><fmt:formatDate value="${video.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                        </div>
                        <a href="${pageContext.request.contextPath}/video/detail?id=${video.videoId}">Xem video</a>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
