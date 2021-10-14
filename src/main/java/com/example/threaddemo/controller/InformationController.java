package com.example.threaddemo.controller;

import com.example.threaddemo.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController("/information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    /**
     * 多线程导入
     * @param file
     * @return
     */
    @PostMapping("/importManyThread")
    public Map importData(@RequestParam("file") MultipartFile file){
        Map<String, Object> map = null;
        try {
            map = informationService.importData(file);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",501);
            map.put("msg","数据出错");
            return map;
        }
    }

}
