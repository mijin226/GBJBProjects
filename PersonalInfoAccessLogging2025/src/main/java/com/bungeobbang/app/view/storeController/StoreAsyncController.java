package com.bungeobbang.app.view.storeController;

import com.bungeobbang.app.biz.store.StoreDTO;
import com.bungeobbang.app.biz.store.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
public class StoreAsyncController {
    @Autowired
    private StoreService storeService;

    private final String NO = "N";
    private final String YES = "Y";

    //지도 옆 주소 검색 : 가게
    @RequestMapping(value = "/loadListStoreMap.do", method = RequestMethod.POST)
    public @ResponseBody ArrayList<StoreDTO> loadListStoreMap(@RequestBody StoreDTO storeDTO) {
        log.info("log: /loadListStoreMap.do loadListStoreMap - start");
        log.info("log: loadListStoreMap - param keyword: " + storeDTO);
        HashMap<String, String> filterList = new HashMap<>(); //필터 검색용 맵
        filterList.put("STORE_ADDRESS", storeDTO.getStoreAddress()); //필터 검색용 값 설정
        filterList.put("STORE_SECRET", this.NO); //비공개 검색 방지
        storeDTO.setCondition("SELECTALL_ADDRESS");
        storeDTO.setFilterList(filterList);
        log.info("log: /loadListStoreMap.do loadListStoreMap - end");
        return storeService.selectAll(storeDTO); //DB검색 결과 반환
    }
}
