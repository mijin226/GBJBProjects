package com.bungeobbang.app.view.paymentController;

import com.bungeobbang.app.biz.payment.PaymentInfoDTO;
import com.bungeobbang.app.biz.payment.PaymentDTO;
import com.bungeobbang.app.biz.payment.PaymentService;
import com.bungeobbang.app.biz.payment.PaymentTokenService;
import com.bungeobbang.app.biz.point.PointDTO;
import com.bungeobbang.app.biz.point.PointService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Slf4j
@RestController
public class PortOneAPIController { // 결제 API 비동기 Controller

    @Autowired
    private PaymentTokenService paymentTokenService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PointService pointService;

    // 결제 단건 조회
    @PostMapping(value = "/infoPayment.do")
    public @ResponseBody ResponseEntity<String> paymentTest(HttpSession session,PaymentInfoDTO paymentInfoDTO,
                                                            PaymentDTO paymentDTO,
                                                            PointDTO pointDTO) {

        log.info("[PaymentInfo] 시작");
        log.info("[PaymentInfo View에서 전달 해준 DTO 값] : " + paymentInfoDTO);

        Integer memberPK = (Integer) session.getAttribute("userPK");
        log.info("[PaymentInfo session에서 가지고온 memberPK 값] : " + memberPK);



        // 토큰 발급
        paymentTokenService.getAccessToken(paymentInfoDTO);
        log.info("[PaymentInfo 토큰 발급 완료]");

        // HTTP 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/payments/"+ paymentInfoDTO.getImp_uid() + "?_token=" + paymentInfoDTO.getToken()))
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString("{}"))
                .build();

        HttpResponse<String> response;
        JSONObject result = new JSONObject();
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.body());
            JSONObject responseObject = (JSONObject) jsonObject.get("response");
            log.info("[PayemntInfo 결제 단건 조회 이후 반환받은 response] : {}", responseObject.toJSONString());

//==========================================<상품 단건 조회 후 반환 response 확인용 로직>==========================================
            Long amount = (Long) responseObject.get("amount");
            log.info("[PaymentInfo] 상품 가격 : {}", amount);

            String merchant_uid = (String) responseObject.get("merchant_uid");
            log.info("[PaymentInfo] Merchant UID : {}", merchant_uid);

            String payment_nameName = (String) responseObject.get("name");
            log.info("[PaymentInfo] 상품 이름 : {}", payment_nameName);

            String member_email = (String) responseObject.get("buyer_email");
            log.info("[PaymentInfo] 사용자 이메일 : {}", member_email);

            //            String paidAt = (String) responseObject.get("paid_at");
            //            log.info("[PaymentInfo] 결제 시간 : {}", paidAt);

//============================================================================================================================


            // 반환된 값 paymentDTO에 담아서 DB에게 전달 insert
            paymentDTO.setPaymentAmount(amount.intValue());
            log.info("[PaymentInfo paymentDTO에 저장된 상품 가격] : {}", paymentDTO.getPaymentAmount());
            paymentDTO.setImpUUid(merchant_uid);
            log.info("[PaymentInfo paymentDTO에 저장된 Merchant_uid] : {}", paymentDTO.getImpUUid());
            paymentDTO.setPaymentName(payment_nameName);
            log.info("[PaymentInfo paymentDTO에 저장된 상품 이름] : {}", paymentDTO.getPaymentName());
            paymentDTO.setMemberEmail(member_email);
            log.info("[PaymentInfo paymentDTO에 저장된 사용자 이메일] : {}", paymentDTO.getMemberEmail());
            paymentDTO.setMemberNum(memberPK);
            log.info("[PaymentInfo paymentDTO에 저장된 회원 번호] : {}", paymentDTO.getMemberNum());
            paymentDTO.setCondition("INSERT_PAYMENT");
            log.info("[PaymentInfo paymentDTO에 저장된 condition] : {}", paymentDTO.getCondition());

            boolean flag = paymentService.insert(paymentDTO); // 반환값 DTO에 담아서 insert
            log.info("[PaymentInfo DB에 저장하고 반환받은 flag값] : {}", flag);

            if(flag == true) { // 만약 DB에 구매 정보가 저장이 됐다면 트리거 발생으로 포인트 저장됨

                // 포인트 저장 확인용
                pointDTO.setCondition("SELECTONE_MEMBER_POINT");
                pointDTO.setMemberNum(memberPK);
                pointDTO = pointService.selectOne(pointDTO);
                session.setAttribute("userPoint", pointDTO.getTotalMemberPoint());
                Integer userPoint = (Integer) session.getAttribute("userPoint");

                result.put("result", true);
                result.put("userPoint", userPoint);
            }

        } catch (IOException | InterruptedException | ParseException e) {
            log.info("[PaymentInfo 결제내역 단건조회 실패]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.toString());
        }
        log.info("[PaymentInfo 결제내역 단건조회 완료]");
        return ResponseEntity.ok(result.toString());
    }

    // 사전 검증 등록
    @RequestMapping(value = "/addPrepare.do")
    public @ResponseBody boolean prepare(PaymentInfoDTO paymentInfoDTO) {
        log.info("[AddPrepare] 사전검증 등록 시작");
        paymentInfoDTO=paymentTokenService.getAccessToken(paymentInfoDTO);
        log.info("[AddPrepare 토큰 발급 완료]");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/payments/prepare"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + paymentInfoDTO.getToken())
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"merchant_uid\":\"" + paymentInfoDTO.getMerchant_uid() + "\",\"amount\":" + paymentInfoDTO.getAmount() + "}"))
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info("[AddPrepare 사전검증 등록 이후 포트원 반환 값] : {}", response.body());
            log.info("[AddPrepare 사전검증 등록 성공]");
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            log.info("[AddPrepare 사전검증 등록 실패]");
            return false;
        }
    }

    // 사전 검증 조회
    @RequestMapping(value = "/checkPrepareResult.do")
    public @ResponseBody ResponseEntity<String> prepareResult(PaymentInfoDTO paymentInfoDTO) {
        log.info("[CheckPrepareResult] 등록된 사전검증 조회 시작");
        log.info("[CheckPrepareResult View에서 받아온 DTO값] : " + paymentInfoDTO.getAmount());
        log.info("[CheckPrepareResult View에서 받아온 DTO값] : " + paymentInfoDTO.getMerchant_uid());

        paymentTokenService.getAccessToken(paymentInfoDTO);
        log.info("[CheckPrepareResult 토큰 발급 완료]");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/payments/prepare/" + paymentInfoDTO.getMerchant_uid() + "?_token=" + paymentInfoDTO.getToken()))
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString("{}"))
                .build();

        JSONObject result = new JSONObject();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.body());
            JSONObject responseObject = (JSONObject) jsonObject.get("response");

            paymentInfoDTO.setAmount(((Long) responseObject.get("amount")).intValue());
            paymentInfoDTO.setMerchant_uid((String) responseObject.get("merchant_uid"));

            log.info("[CheckPrepareResult 사전검증 이후 반환되는 상품 가격] : {}", paymentInfoDTO.getAmount());
            log.info("[CheckPrepareResult 사전검증 이후 반환되는 Merchant_uid] : {}", paymentInfoDTO.getMerchant_uid());

            result.put("amount", paymentInfoDTO.getAmount());
            result.put("merchant_uid", paymentInfoDTO.getMerchant_uid());
        } catch (IOException | InterruptedException | ParseException e) {
            log.info("[CheckPrepareResult 사전검증 조회 실패]");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.toString());
        }
        log.info("[CheckPrepareResult 사전검증 조회 완료]");
        return ResponseEntity.ok(result.toString());
    }

    // 결제 취소
    @PostMapping(value = "/cancelPayment.do")
    public boolean cancelPayment(PaymentInfoDTO paymentInfoDTO) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/payments/cancel?_token=" + paymentInfoDTO.getToken()))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"merchant_uid\":\"" + paymentInfoDTO.getMerchant_uid() + "\",\"checksum\":" + paymentInfoDTO.getAmount() + "}"))
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
