package com.example.threaddemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.threaddemo.config.Informationhread;
import com.example.threaddemo.exception.ErrorInfo;
import com.example.threaddemo.service.InformationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InformationServiceImpl implements InformationService {

    @Override
    public Map<String, Object> importData(MultipartFile file) throws ExecutionException, InterruptedException, IOException {
        final Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("{},开始导入数据...", format.format(now));
        //设置一个信号量为5的信号量，限制同时运行的线程数量最大为5
        Semaphore semaphore = new Semaphore(5);

        Map<String, Object> map = new HashMap<>();
        //多线程编程需要一个线程安全的ArrayList
        List<ErrorInfo> list = Collections.synchronizedList(new ArrayList<>());
        Workbook workbook = null;
        String filename = file.getOriginalFilename();
        if (filename.endsWith("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if (filename.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setErrorMsg("请上传xlx或xlsx格式的文件");
            list.add(errorInfo);
            map.put("code", 500);
            map.put("data", list);
            return map;
        }
        Sheet sheet = workbook.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        log.info("获取到workbook中的总行数:{}", physicalNumberOfRows);
        //第一行是表头，实际行数要减1
        int rows = physicalNumberOfRows - 1;
        //线程数量
        int threadNum = rows / 1000 + 1;

        //设置一个倒计时门闩，用来处理主线程等待蚂蚁线程执行完成工作之后再运行
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        //查询是否重名  如果要校验与数据库重复时，可以去数据库中查询到 半并且相比较 这里只做一个定义
        Set<String> names = new HashSet<>();
        //创建一个定长的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        log.info("开始创建线程,数据总行数:{},线程数量:{}", rows, threadNum);

        List<Future<Integer>> futures = new ArrayList<>();
        int successCount = 0;

        for (int i = 1; i <= threadNum; i++) {

            int startRow = (i - 1) * 1000 + 1;
            int endRow = i * 1000;
            if (i == threadNum) {
                endRow = rows;
            }
            log.info("开始执行线程方法,线程ID:<{}>,线程名称:<{}>", Thread.currentThread().getId(), Thread.currentThread().getName());
            Future<Integer> future = executorService.submit(new Informationhread(semaphore, workbook,
                    startRow, endRow, list, names, this, countDownLatch));
            futures.add(future);
            log.info("结束线程执行方法,返回结果:<{}>,当前线程ID:<{}>,当前线程名称:<{}>", JSON.toJSONString(future), Thread.currentThread().getId(), Thread.currentThread().getName());
        }

        for (Future<Integer> future : futures) {
            successCount += future.get();
        }
        //主线程等待子线程全部跑完才继续运行。设置60秒等待时间，超时后继续执行。
        countDownLatch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();

        Date endDate = new Date();
        long difference = endDate.getTime() - now.getTime();
        String duration = DurationFormatUtils.formatDuration(difference, "HH:mm:ss");
        log.info("执行完成,错误信息:{}", JSON.toJSONString(list));
        log.info("{},结束导入,共{}条数据，导入成功:{}，耗时={}", format.format(endDate), rows, successCount, duration);
        map.put("code", 200);
        map.put("msg", "结束导入,共" + rows + "条数据，导入成功" + successCount + "条，耗时:" + duration);
        map.put("data", list);
        return map;
    }


}
