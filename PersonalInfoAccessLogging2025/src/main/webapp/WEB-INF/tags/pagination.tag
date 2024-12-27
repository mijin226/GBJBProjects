<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="pagination"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- 페이지네이션 -->
				<section id="pagination">
					<div class="pagination">
						<!-- 이전 페이지 버튼 -->
						<c:if test="${currentPage > 1}">
							<a
								href="?page=${currentPage - 1}&storeName=${param.storeName != null ? param.storeName : ''}"
								id="pagenationPreValue">&laquo; 이전</a>
						</c:if>

						<c:set var="startPage" value="${currentPage - 5}" />
						<c:set var="endPage" value="${currentPage + 4}" />

						<c:if test="${startPage < 1}">
							<c:set var="startPage" value="1" />
						</c:if>
						<c:if test="${endPage > totalPages}">
							<c:set var="endPage" value="${totalPages}" />
						</c:if>

						<c:forEach var="i" begin="${startPage}" end="${endPage}">
							<c:choose>
								<c:when test="${i == currentPage}">
									<strong>${i}</strong>
								</c:when>
								<c:otherwise>
									<a
										href="?page=${i}&storeName=${param.storeName != null ? param.storeName : ''}"
										id="pagenationValue">${i}</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<c:if test="${currentPage < totalPages}">
							<a
								href="?page=${currentPage + 1}&storeName=${param.storeName != null ? param.storeName : ''}"
								id="pagenationNextValue">다음 &raquo;</a>
						</c:if>
					</div>
				</section>
				<!-- 페이지네이션 종료 -->