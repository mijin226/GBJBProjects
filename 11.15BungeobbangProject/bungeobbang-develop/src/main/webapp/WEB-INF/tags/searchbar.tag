<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="searchbar"%>
<%@ attribute name="placeholder" %>
<%@ attribute name="value" %>

<div class="searchInput">
	<i class="bi bi-search"></i> <input type="text" name="keyword"
		placeholder="${placeholder}" value="${value}" id="searchInput"> <input  id="searchInputBTN" type="submit"
		value=" 검색 ">
</div>