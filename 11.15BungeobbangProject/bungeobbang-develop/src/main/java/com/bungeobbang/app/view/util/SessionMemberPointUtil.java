package com.bungeobbang.app.view.util;

import com.bungeobbang.app.biz.point.PointDTO;
import com.bungeobbang.app.biz.point.PointService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**autor 재준*/
@Slf4j
@Component
public class SessionMemberPointUtil {

    @Autowired
    private static PointService pointService;


    // 어플리케이션 속성에 포인트 저장하는 메서드 / util로 빼기
    public static void updatesessionPoint(HttpSession session) {
        log.info("[UpdateSessionPoint] 시작");
        Integer memberPK = (Integer) session.getAttribute("userPK");

        PointDTO pointDTO = new PointDTO();
        pointDTO.setMemberNum(memberPK);
        pointDTO.setCondition("SELECTONE_MEMBER_POINT");

        PointDTO memberPoint = pointService.selectOne(pointDTO);
        // 이때 반환값에 PointNum memberNum pointPlus pointMinus pointContent(충전 내역)
        if (memberPoint != null) {
            session.setAttribute("sessionPoint", memberPoint.getTotalMemberPoint());
            log.info("[UpdateSessionPoint 세션에 저장된 회원 포인트] : {}", session.getAttribute("sessionPoint"));
        }
    }
}

