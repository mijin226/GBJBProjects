package com.bungeobbang.app.view.memberController;

import com.bungeobbang.app.biz.declare.DeclareDTO;
import com.bungeobbang.app.biz.declare.DeclareService;
import com.bungeobbang.app.biz.order.OrderDTO;
import com.bungeobbang.app.biz.order.OrderService;
import com.bungeobbang.app.biz.store.StoreDTO;
import com.bungeobbang.app.biz.store.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
@Slf4j
public class AdminController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private DeclareService declareService;
    @Autowired
    private HttpSession session;

    private final String FAIL_DO = "redirect:failInfo.do"; //기본 실패 처리
    private final String FAIL_URL = "failInfo2"; //실패 처리할 페이지

    //msg
    private final String ORDER_UPDATE_FAIL = "주문 완료 처리를 실패했습니다.";
    private final String STORE_DELETE_FAIL = "가게 정보 삭제에 실패했습니다.";
    private final String CLOSE_FAIL_MSG = "폐점 전환에 실패했습니다.";
    private final String SECRET_FAIL_MSG = "가게 승인(공개) 전환에 실패했습니다.";
    private final String REPORT_FAIL_MSG = "신고 삭제에 실패했습니다.";
    private final String STORE_DELETE = "가게를 삭제했습니다.";

    private final String NO = "N";
    private final String YES = "Y";

    //page
    private final String PAGE_ORDER_LIST = "adminMainProduct"; //views 하위 주문 목록 페이지
    private final String PAGE_REPORT_LIST = "closedStoreDeclareList"; //views 하위 신고목록 페이지
    private final String PAGE_TIPOFF_LIST = "storeDeclareList"; //views 하위 가게 제보목록 페이지

    //session
    private final String SESSION_ROLE = "userRole";

    //ADMIN 구분값
    private final String ADMIN = "ADMIN";

    //가게 주문 완료처리
    @RequestMapping("/updateOrderCheck.do")
    public String updateOrderCheck(OrderDTO orderDTO, Model model) {
        log.info("log: /updateOrderCheck.do updateOrderCheck - start");
        log.info("log: updateOrderCheck - param orderDTO: {}", orderDTO);
        //만약 관리자가 아니라면
        if(!session.getAttribute(SESSION_ROLE).equals(ADMIN)){
            //기본 실패처리
            log.error("log: updateOrderCheck - not admin");
            return FAIL_DO;
        }
        //현재 값과 반대로 변환해 업데이트 진행
        if(orderDTO.getAdminChecked().equals(this.NO)){ 
            //Y로 전환해야하는 경우
            orderDTO.setAdminChecked(this.YES);
        }
        else {
            //N으로 전환해야하는 경우
            orderDTO.setAdminChecked(this.NO);
        }
        if(!orderService.update(orderDTO)){
            log.error("log: updateOrderCheck - update failed");
            model.addAttribute("msg", ORDER_UPDATE_FAIL);
            model.addAttribute("path", "");
            return FAIL_URL;
        }
        log.info("log: /updateOrderCheck.do updateOrderCheck - end");
        return "redirect:/loadListOrder.do";
    }

    //가게 주문 리스트
    @RequestMapping("/loadListOrder.do")
    public String loadListOrder(Model model){
        log.info("log: /loadListOrder.do loadListOrder - start");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCondition("SELECTALL");
        ArrayList<OrderDTO> orderList = orderService.selectAll(orderDTO);
        model.addAttribute("orderList", orderList);
        log.info("log: loadListOrder - send orderList : {}", orderList);
        log.info("log: /loadListOrder.do loadListOrder - end");
        return PAGE_ORDER_LIST;
    }

    //가게 신고 리스트
    @RequestMapping("/loadListStoreReport.do")
    public String loadListStoreReport(Model model){
        log.info("log: /loadListStoreReport.do loadListStoreReport - start");
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setCondition("SELECTALL_DECLARED_CNT");
        ArrayList<StoreDTO> storeReportList = storeService.selectAll(storeDTO);
        model.addAttribute("storeReportList", storeReportList);
        log.info("log: loadListStoreReport - send storeReportList : {}", storeReportList);
        log.info("log: /loadListStoreReport.do loadListStoreReport - end");
        return PAGE_REPORT_LIST;
    }

    //가게 제보리스트
    @RequestMapping("/loadListStoreTipOff.do")
    public String loadListStoreTipOff(Model model){
        log.info("log: /loadListStoreTipOff.do loadListStoreTipOff - start");
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setCondition("STORE_TIP_OFF_LIST");
        ArrayList<StoreDTO> storeTipOffList = storeService.selectAll(storeDTO);
        model.addAttribute("storeTipOffList", storeTipOffList);
        log.info("log: loadListStoreTipOff - send storeTipOffList : {}", storeTipOffList);
        log.info("log: /loadListStoreTipOff.do loadListStoreTipOff - end");
        return PAGE_TIPOFF_LIST;
    }

    //가게 삭제
    @RequestMapping("/deleteStore.do")
    public String deleteStore(StoreDTO storeDTO, Model model){
        log.info("log: /deleteStore.do deleteStore - start");
        log.info("log: deleteStore - param storeDTO: {}", storeDTO);
        //만약 관리자가 아니라면
        if(!session.getAttribute(SESSION_ROLE).equals(ADMIN)){
            //기본 실패처리
            log.error("log: deleteStore - not admin");
            return FAIL_DO;
        }
        //관리자라면 삭제 진행
        if(!storeService.delete(storeDTO)){
            log.error("log: deleteStore - delete failed");
            model.addAttribute("msg", STORE_DELETE_FAIL);
            model.addAttribute("path", "infoStore.do?storeNum=" + storeDTO.getStoreNum());
            return FAIL_URL;
        }
        log.info("log: /deleteStore.do deleteStore - end");
        model.addAttribute("msg", STORE_DELETE);
        model.addAttribute("path", "main.do");
        return FAIL_URL;
    }

    //가게 폐점설정
    @RequestMapping("/updateStoreClose.do")
    public String updateStoreClose(StoreDTO storeDTO, Model model){
        log.info("log: /updateStoreClose.do updateStoreClose - start");
        log.info("log: updateStoreClose - param storeDTO num : [{}]", storeDTO.getStoreNum());
        //만약 관리자가 아니라면
//        if(!session.getAttribute(SESSION_ROLE).equals(ADMIN)){
//            //기본 실패처리
//            log.error("log: updateStoreClose - not admin");
//            return FAIL_DO;
//        }
        HashMap<String, String> filterList = new HashMap<>();
        filterList.put("UPDATE_CLOSED", this.YES);
        storeDTO.setFilterList(filterList);
        if(!storeService.update(storeDTO)){
            //폐점처리 실패시
            log.error("log: updateStoreClose - store update closed failed");
            model.addAttribute("msg", CLOSE_FAIL_MSG);
            //관리자 가게 신고 목록으로 이동
            model.addAttribute("path", "loadListStoreReport.do");
            return FAIL_URL;
        }
        log.info("log: /updateStoreClose.do updateStoreClose - end");
        //폐점의심 제보 목록 삭제 처리
        return "redirect:deleteReport.do?storeNum=" + storeDTO.getStoreNum();
    }

    //해당 가게의 신고 전부 삭제하기
    @RequestMapping("/deleteReport.do")
    public String deleteReport(DeclareDTO declareDTO, Model model){
        log.info("log: /deleteReport.do deleteReport.do - start");
        boolean flag = false;//초기값 설정 (신고 삭제에 한 번이라도 성공해야 성공처리)
        log.info("log: deleteReport.do - declareDTO : {}", declareDTO);
        for(DeclareDTO dto : declareService.selectAll(declareDTO)){ //해당 가게 번호를 가진 신고 리스트
            flag = declareService.delete(dto); //신고를 하나씩 삭제
            if(!flag){ //신고삭제에 실패했다면
                log.error("log: deleteReport.do - error : {}", dto);
                break; //종료
            }
            log.info("log: deleteReport.do - delete");
        }
        if(!flag){
            log.error("log: deleteReport - store report delete failed");
            model.addAttribute("msg", REPORT_FAIL_MSG);
            //관리자 가게 신고 목록으로 이동
            model.addAttribute("path", "loadListStoreReport.do");
            return FAIL_URL;
        }
        log.info("log: /deleteReport.do deleteReport.do - end : {}", flag);
        return "redirect:loadListStoreReport.do";
    }

    //가게 비공개 설정 (온 오프)
    @RequestMapping("/updateStoreVisible.do")
    public String updateStoreVisible(StoreDTO storeDTO, Model model){
        log.info("log: /updateStoreVisible.do updateStoreVisible - start");
        log.info("log: updateStoreVisible - param storeDTO num : [{}]", storeDTO.getStoreNum());
        //만약 관리자가 아니라면
        if(!session.getAttribute(SESSION_ROLE).equals(ADMIN)){
            //기본 실패처리
            log.error("log: updateStoreVisible - not admin");
            return FAIL_DO;
        }
        HashMap<String, String> filterList = new HashMap<>();
        filterList.put("UPDATE_SECRET", storeDTO.getStoreSecret());
        storeDTO.setFilterList(filterList);
        if(!storeService.update(storeDTO)){
            log.error("log: updateStoreVisible - store update visible failed");
            model.addAttribute("msg", SECRET_FAIL_MSG);
            //관리자 가게 제보 목록으로 이동
            model.addAttribute("path", "loadListStoreTipOff.do");
            return FAIL_URL;
        }
        log.info("log: /updateStoreVisible.do updateStoreVisible - end");
        //관리자 가게 제보 목록으로 이동
        return "redirect:loadListStoreTipOff.do";
    }
}
