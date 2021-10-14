package com.example.threaddemo.config;

import com.example.threaddemo.entity.Information;
import com.example.threaddemo.exception.ErrorInfo;
import com.example.threaddemo.service.InformationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;


@Slf4j
public class Informationhread  implements Callable<Integer> {

    private Workbook workbook;

    private Integer startRow;

    private Integer endRow;

    private List<ErrorInfo> errorInfoList;

    private Set<String> names;

    private InformationService informationService;

    private Semaphore semaphore;

    private CountDownLatch latch;

    public Informationhread(Semaphore semaphore, Workbook workbook, Integer startRow, Integer endRow,
                            List<ErrorInfo> errorInfoList, Set<String> names, InformationService informationService,
                            CountDownLatch latch){
        this.workbook = workbook;
        this.startRow = startRow;
        this.endRow = endRow;
        this.errorInfoList = errorInfoList;
        this.names = names;
        this.informationService = informationService;
        this.semaphore = semaphore;
        this.latch = latch;
    }

    @Override
    public Integer call() throws Exception {
        log.info("线程ID：<{}>开始运行,startRow:{},endRow:{}",Thread.currentThread().getId(),startRow,endRow);
        semaphore.acquire();
        log.info("消耗了一个信号量，剩余信号量为:{}",semaphore.availablePermits());
        latch.countDown();
        Sheet sheet = workbook.getSheetAt(0);
        int count = 0;
        List<Information> saveList = new ArrayList<>();
        for(int i = startRow; i <= endRow; i++){
            Information information = new Information();
            Row row = sheet.getRow(i);
            Cell cell1 = row.getCell(0);
            String username = cell1.getStringCellValue();
            information.setName(username);
            information.setDetail("casa" + i);
            information.setInfo("case" + i);
            saveList.add(information);
            //这里进行数据库插入
        }
        semaphore.release();
        return saveList.size();
    }
}
