<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="footer"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<link rel="stylesheet"
	href="${path}/resources/assets/css/footer.css">

<style>
/* css 해당 ${path}를 가져오지 못해서 가져옴 */
#footer .link-list li {
    list-style-image: url('${path}/resources/assets/images/fishbread_icon_2.png');
    padding-left: 30px; /* 이미지와 텍스트 간의 간격 조정 */
}
</style>

<!-- Footer -->
<section id="footer">
	<div class="container">
		<div class="row">
			<div class="col-12 col-md-8">

				<!-- Links -->
				<section>
					<h2>
					<img src="${path}/resources/assets/images/fishbread_blog.png" alt="logo Image" class="footerIcon" />
					 개발자 블로그</h2>
					<div>
						<div class="row">
							<div class="col-12 col-md-3">
								<ul class="link-list last-child">
									<li><a href="https://blog.naver.com/wx_wns_"><span class="memberName">정재준</span></a><br>
										<a href="https://blog.naver.com/wx_wns_"><span class="memberRoll">중프 : 전체 팀장</span></a><br>
										<a href="https://blog.naver.com/wx_wns_"><span class="memberRoll">최프 : 전체 팀장</span></a>
									</li>
									<li><a href="https://hrong98.tistory.com/"><span class="memberName">조충현</span></a><br>
										<a href="https://hrong98.tistory.com/"><span class="memberRoll">중프 : V 팀장</span></a><br>
										<a href="https://hrong98.tistory.com/"><span class="memberRoll">최프 : M 팀장</span></a>
									</li>
								</ul>
							</div>
							<div class="col-12 col-md-3">
								<ul class="link-list last-child">
									<li><a href="https://blog.naver.com/study_j04"><span class="memberName">정재희</span></a><br>
										<a href=" https://blog.naver.com/study_j04"><span class="memberRoll">중프 : C 팀장</span></a><br>
										<a href=" https://blog.naver.com/study_j04"><span class="memberRoll">최프 : V 멤버</span></a>
									</li>
									<li><a href="https://jelkov-developer.notion.site/JAEHO-s-Hello-World-49ba695ecae34a729cce1f8b250c4502"><span class="memberName">안제호</span></a><br>
										<a href="https://jelkov-developer.notion.site/JAEHO-s-Hello-World-49ba695ecae34a729cce1f8b250c4502"><span class="memberRoll">중프 : C 멤버</span></a><br>
										<a href="https://jelkov-developer.notion.site/JAEHO-s-Hello-World-49ba695ecae34a729cce1f8b250c4502"><span class="memberRoll">최프 : V 멤버</span></a>
										</li>
								</ul>
							</div>
							<div class="col-12 col-md-3">
								<ul class="link-list last-child">
									<li><a href="https://minibcake.tistory.com/"><span class="memberName">한지윤</span></a><br>
										<a href=" https://minibcake.tistory.com/"><span class="memberRoll">중프 : M 팀장</span></a><br>
										<a href=" https://minibcake.tistory.com/"><span class="memberRoll">최프 : C팀장</span></a>
									</li>
									<li><a href="https://blog.naver.com/rhalwls56"><span class="memberName">고미진</span></a><br>
										<a href="https://blog.naver.com/rhalwls56"><span class="memberRoll">중프 : M 멤버</span></a><br>
										<a href="https://blog.naver.com/rhalwls56"><span class="memberRoll">최프 : V 팀장</span></a>
									</li>
								</ul>
							</div>

						</div>
					</div>
				</section>

			</div>
			<div class="col-12 col-md-4 imp-medium">

				<!-- Blurb -->
				<section>
					<h2>
						<img src="${path}/resources/assets/images/logo.png" alt="logo Image" class="footerIcon" />
						붕어빵 원정대
					</h2>
					<p id="siteContent">"갈빵질빵"은 전국의 붕어빵 애호가들이 모여 정보를 공유하고, 위치를 손쉽게 찾는 것을 목적으로 만들었습니다.
						다양한 커뮤니티 활동을 통해 붕어빵에 대한 애정을 나누며, 관련 상품을 구매합니다.</p>
				</section>

			</div>
		</div>
	</div>
</section>

<!-- Copyright -->
<div id="copyright">
	&copy; Untitled. All rights reserved. | Design: <a
		href="http://html5up.net">HTML5 UP</a>
</div>