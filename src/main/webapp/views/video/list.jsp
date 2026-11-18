<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Video của tôi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<header class="topbar">
    <strong><a href="${pageContext.request.contextPath}/home">VideoHub</a></strong>
    <span>Xin chào <c:out value="${sessionScope.account.fullname}"/></span>
    <nav>
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </nav>
</header>
<main class="content video-page">
    <div class="video-heading">
        <div>
            <h1>Video của tôi</h1>
            <p>Tổng cộng ${totalVideos} video · 6 video mỗi trang</p>
        </div>
        <a class="button" href="${pageContext.request.contextPath}/video/add">Thêm video</a>
    </div>
    <c:if test="${param.created eq '1'}"><div class="success">Đã tải video lên.</div></c:if>
    <c:if test="${param.updated eq '1'}"><div class="success">Đã cập nhật video.</div></c:if>
    <c:if test="${param.deleted eq '1'}"><div class="success">Đã xóa video.</div></c:if>
    <c:choose>
        <c:when test="${empty videos}">
            <p>Bạn chưa có video nào.</p>
        </c:when>
        <c:otherwise>
            <table class="video-table">
                <thead>
                <tr><th>Tiêu đề</th><th>Mô tả</th><th>Ngày đăng</th><th>Thao tác</th></tr>
                </thead>
                <tbody>
                <c:forEach var="video" items="${videos}">
                    <tr>
                        <td><a href="${pageContext.request.contextPath}/video/detail?id=${video.videoId}"><c:out value="${video.title}"/></a></td>
                        <td><c:out value="${video.description}"/></td>
                        <td><fmt:formatDate value="${video.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td class="video-actions">
                            <a href="${pageContext.request.contextPath}/video/edit?id=${video.videoId}">Sửa</a>
                            <form action="${pageContext.request.contextPath}/video/delete" method="post"
                                  onsubmit="return confirm('Bạn có chắc muốn xóa video này?');">
                                <input type="hidden" name="id" value="${video.videoId}">
                                <button class="danger-button" type="submit">Xóa</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
    <c:if test="${totalPages gt 1}">
        <nav class="pagination" aria-label="Phân trang">
            <c:if test="${page gt 1}">
                <a href="${pageContext.request.contextPath}/video?page=${page - 1}">Trang trước</a>
            </c:if>
            <span>Trang ${page} / ${totalPages}</span>
            <c:if test="${page lt totalPages}">
                <a href="${pageContext.request.contextPath}/video?page=${page + 1}">Trang sau</a>
            </c:if>
        </nav>
    </c:if>
</main>
</body>
</html>
