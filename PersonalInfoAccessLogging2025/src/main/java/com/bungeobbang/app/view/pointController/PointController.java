package com.bungeobbang.app.view.pointController;

import com.bungeobbang.app.biz.order.OrderDTO;
import com.bungeobbang.app.biz.order.OrderService;
import com.bungeobbang.app.biz.point.PointDTO;
import com.bungeobbang.app.biz.point.PointService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Slf4j
@Controller
public class PointController {

    @Autowired
    private PointService pointService;

    @Autowired
    private OrderService orderService;

    // 포인트 사용 마이너스 포인트
    @PostMapping(value = "/minusPoint.do")
    public boolean minusPoint(HttpSession session, PointDTO pointDTO, @RequestParam int amount){
        log.info("[MinusPoint] 시작");

        Integer memberPK = (Integer) session.getAttribute("userPK"); // session에 저장된 memberPK 가져오기

        // view에서 전달해주는 product의 가격을 받아 사용자의 point에서 minus 진행
        pointDTO.setMemberNum(memberPK);
        pointDTO.setPointMinus(amount);
        log.info("[MinusPoint View에서 전달해준 값] : {}" + pointDTO);
        boolean flag = pointService.insert(pointDTO);
        log.info("[MinusPoint DB 처리 결과] : {}" + flag);
        return flag;
    }

    // 해당 회원의 모든 포인트 사용내역 보기
    @PostMapping(value = "/loadListPoint.do")
    public String loadListPoint(HttpSession session, OrderDTO orderDTO, Model model, PointDTO pointDTO){
        log.info("[LoadListPoint] 시작");
        Integer memberPK = (Integer) session.getAttribute("userPK"); // session에 저장된 memberPK 가져오기
        log.info("[LoadListPoint session에 저장된 userPK] : {}", memberPK);

        if(memberPK == null){
            return "redirect:/login.do";
        }

        orderDTO.setCondition("SELECTALL_MEMBER");
        orderDTO.setMemberNum(memberPK);
        List<OrderDTO> pointHistoryList = orderService.selectAll(orderDTO);
        if(pointHistoryList == null && pointHistoryList.isEmpty()){
            log.info("[LoadListpoint 반환값 null]");
            return null;
        }
        // session에 memberPoint 업데이트 해주기 ----------------------------------------
        pointDTO.setCondition("SELECTONE_MEMBER_POINT");
        pointDTO.setMemberNum(memberPK);
        pointDTO = pointService.selectOne(pointDTO);
        session.setAttribute("userPoint", pointDTO.getTotalMemberPoint());
        //----------------------------------------------------------------------------

        model.addAttribute("pointHistoryList", pointHistoryList);
        log.info("[LoadListPoint selectAll 이후 반환해주는 model 값] : {}", model);
        return "pointHistory";
    }
}
