package com.open.javabasetool.jucutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/11/15 14:43
 * @desc 并发测试类
 */
public class JucTest {
    /**
     * 将originalMap集合拆成numPartitions份
     * @param originalMap
     * @param numPartitions
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> List<Map<K, V>> partitionMap(Map<K, V> originalMap, int numPartitions) {
        return IntStream.range(0, numPartitions)
                .mapToObj(i -> originalMap.entrySet().stream()
                        .skip((long) i * originalMap.size() / numPartitions)
                        .limit((originalMap.size() + (i == numPartitions - 1? 0 : numPartitions - 1)) / numPartitions)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .collect(Collectors.toList());
    }

    /**
     * 定义线程池
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            //核心线程数
            20,
            //最大线程数
            200,
            //保持活力时间
            10,
            //时间单位
            TimeUnit.SECONDS,
            //阻塞队列10万
            new LinkedBlockingDeque<>(100000),
            //默认线程工厂
            Executors.defaultThreadFactory(),
            //拒绝策略，直接抛弃策略
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     *
     *
     */
    public static Map<String, Map<String, String>> oneTest(Map<String, Map<String, String>> scuColorObjectMap){
        //todo 优化方案1，CompletableFuture.supplyAsync：
        try {
            //开始时间ms
            long start = System.currentTimeMillis();
            // 将scuColorObjectMap分成指定10份
            List<Map<String, Map<String, String>>> partitions = partitionMap(scuColorObjectMap,scuColorObjectMap.size());
            // 使用CompletableFuture并发执行调用iSysOssService.pullTaoBaoImageByScuMap
            List<CompletableFuture<Map<String, Map<String, String>>>> futures = new ArrayList<>();
            for (Map<String, Map<String, String>> partition : partitions) {
                CompletableFuture<Map<String, Map<String, String>>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        partition.forEach((k, v) -> {
                            //睡眠3s
                            try {
                                System.out.println("根据SCU键值对批量上传淘宝图片成功一张图片================================="+Thread.currentThread()+v);
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        //System.out.println("根据SCU键值对批量上传淘宝图片成功一批================================="+Thread.currentThread());
                        return partition;
                    } catch (Exception e) {
                        // 这里可以根据具体需求处理异常，比如打印日志等
                        e.printStackTrace();
                        return new HashMap<>();
                    }
                },executor);
                futures.add(future);
            }
            // 等待所有任务完成并收集结果
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
            allFutures.join();
            // 合并所有结果到一个新的Map
            Map<String, Map<String, String>> combinedResult = new HashMap<>();
            for (CompletableFuture<Map<String, Map<String, String>>> future : futures) {
                Map<String, Map<String, String>> result = future.get();
                combinedResult.putAll(result);
            }
            long startEnd=System.currentTimeMillis()-start;
            System.out.println("根据SCU键值对批量上传淘宝图片耗时================================="+startEnd);

            return combinedResult;
        } catch (Exception e) {
            System.out.println("根据SCU键值对批量上传淘宝图片异常=================================");
        }

        return new HashMap<>();
    }

    public static void main(String[] args) {
        Long storeId=123456L;
        Map<String, Map<String, String>> scuColorObjectMap = new HashMap<>();
        // 模拟50条数据
        for (int i = 0; i < 50; i++) {
            // 外层键，这里简单使用数字作为键示例
            String outerKey = "key_" + i;

            // 内层Map
            Map<String, String> innerMap = new HashMap<>();

            // 内层键值对示例，这里简单设置两个键值对
            innerMap.put("color", "color_" + i);
            innerMap.put("size", "size_" + i);

            scuColorObjectMap.put(outerKey, innerMap);
        }
        // 可以在此处添加后续对scuColorObjectMap的操作，比如打印查看模拟的数据
        scuColorObjectMap.forEach((outerKey, innerMap) -> {
            //System.out.println("Outer Key: " + outerKey);
            innerMap.forEach((innerKey, value) -> {
                //System.out.println("  Inner Key: " + innerKey + ", Value: " + value);
            });
        });

        Map<String, Map<String, String>> combinedResult = oneTest(scuColorObjectMap);
        System.out.println("根据SCU键值对批量上传淘宝图片返回结果数量" +
                "=============================="+combinedResult.size());
    }
}
