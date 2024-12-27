<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="store" type="com.bungeobbang.app.biz.store.StoreDTO" %>
<div class="storeData">
	<div class="storeDataTitle">
		<a href="infoStore.do?storeNum=${store.storeNum}">
			<h4 class="text-hover">${store.storeName}</h4></a>
	</div>
	<div class="storeDataContent">
		<div class="col-1 nonePadding">
			<i class="fas fa-map"></i>
		</div>
		<div class="col-9 leftPadding text-start">
			<span id="address">${store.storeAddress} <br> ${store.storeAddressDetail}
			</span>
		</div>
		<div class="col-2 nonePadding">
			<button class="copy" value="${store.storeAddress} ${store.storeAddressDetail}">복사</button>
		</div>
	</div>
	<div class="storeDataContent">
		<div class="col-1 nonePadding">
			<i class="fas fa-phone"></i>
		</div>
		<div class="col-9 leftPadding text-start">
			<span>${store.storeContact}</span>
		</div>
		<div class="col-2 nonePadding">
			<button class="copy" value="${store.storeContact}">복사</button>
		</div>
	</div>
</div>