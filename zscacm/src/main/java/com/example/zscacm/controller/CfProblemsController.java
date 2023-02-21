package com.example.zscacm.controller;

import com.example.zscacm.entity.CfProblemIds;
import com.example.zscacm.entity.CfProblems;
import com.example.zscacm.entity.CfProblemsType;
import com.example.zscacm.service.CfService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

import static java.lang.Math.min;

@RestController
public class CfProblemsController {

    @Resource
    private CfService cfService;

    public HashMap<String, String> initMap() {
        HashMap<String, String> typeMap = new HashMap();
        typeMap.put("2-SAT问题", "2-sat");
        typeMap.put("二分", "binary search");
        typeMap.put("位运算", "bitmasks");
        typeMap.put("暴力", "brute force");
        typeMap.put("中国剩余定理", "chinese remainder theorem");
        typeMap.put("组合数学", "combinatorics");
        typeMap.put("构造", "constructive algorithms");
        typeMap.put("数据结构", "data structures");
        typeMap.put("DFS", "dfs and similar");
        typeMap.put("分治", "divide and conquer");
        typeMap.put("动态规划", "dp");
        typeMap.put("并查集", "dsu");
        typeMap.put("表达式分析", "expression parsing");
        typeMap.put("FFT", "fft");
        typeMap.put("网络流", "flows");
        typeMap.put("博弈", "games");
        typeMap.put("几何", "geometry");
        typeMap.put("图的匹配", "graph matchings");
        typeMap.put("图", "graphs");
        typeMap.put("贪心", "greedy");
        typeMap.put("哈希", "hashing");
        typeMap.put("代码能力", "implementation");
        typeMap.put("交互", "interactive");
        typeMap.put("数学", "math");
        typeMap.put("矩阵", "matrices");
        typeMap.put("中途相遇", "meet-in-the-middle");
        typeMap.put("数论", "number theory");
        typeMap.put("概率", "probabilities");
        typeMap.put("安排", "schedules");
        typeMap.put("最短路", "shortest paths");
        typeMap.put("排序", "sortings");
        typeMap.put("字符串后缀数据结构", "string suffix structures");
        typeMap.put("字符串", "strings");
        typeMap.put("三分", "ternary search");
        typeMap.put("树", "trees");
        typeMap.put("尺取", "two points");

        return typeMap;

    }

    @GetMapping("/problemList")
    public List<HashMap<String, Object>> getProblemList(@RequestParam(value = "problemName", required = false) String problemName,
                                                        @RequestParam(value = "problemIds", required = false) String problemIds,
                                                        @RequestParam(value = "diff1", required = false) Integer diff1,
                                                        @RequestParam(value = "diff2", required = false) Integer diff2,
                                                        @RequestParam(value = "problemType", required = false) String[] problemType,
                                                        @RequestParam(value = "submited", required = false) Integer submited,
                                                        @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                        @RequestParam(value = "limit", required = false, defaultValue = "25") Integer limit) {

        int firstId = 0;
        int secondId = 0;
        int thirdId = 0;
        int flag = 3;
        if(problemIds != null && !problemName.isBlank() ) {
            flag = 0;
            for(int j = 0; j < problemIds.length(); j++) {
                if(flag == 0 && problemIds.charAt(j) >= '0' && problemIds.charAt(j) <= '9') {
                    firstId = firstId * 10 + (problemIds.charAt(j) - '0');
                } else if(flag == 0 && problemIds.charAt(j) >= 'A' && problemIds.charAt(j) <= 'Z') {
                    secondId = problemIds.charAt(j) - 'A' + 1;
                    flag = 1;
                } else if(flag == 1 && problemIds.charAt(j) >= '0' && problemIds.charAt(j) <= '9'){
                    thirdId = problemIds.charAt(j) - '0';
                    flag = 2;
                } else {
                    flag = -1;
                    break;
                }
            }
        }

        List<CfProblems> problemsList = cfService.selectCfProblems();

        Set<CfProblemIds> problems = new HashSet<>();

        for(CfProblems problem : problemsList) {
            if(problemName != null && !problemName.isBlank() && !problemName.equals(problem.getProblemName())) {
                continue;
            }
            if(flag != 3 && (flag != 2 || firstId != problem.getFirstId() || secondId != problem.getSecondId() || thirdId != problem.getThirdId())) {
                continue;
            }
            if(diff1 != null && diff1 > problem.getDifficulty() || diff2 != null && diff2 < problem.getDifficulty()) {
                continue;
            }
            CfProblemIds ids = CfProblemIds.builder().firstId(problem.getFirstId())
                    .secondId(problem.getSecondId()).thirdId(problem.getThirdId()).build();
            problems.add(ids);
        }
        List<HashMap<String, Object>> result = new ArrayList<>();

        if(problemType != null) {
            HashMap<String, String> typeMap = initMap();

            for(String type : problemType) {
                type = typeMap.get(type);

                Set<CfProblemIds> typeSet1 = new HashSet<>();
                List<CfProblemsType> types = cfService.selectTypeByType(type);
                for(CfProblemsType type1 : types) {
                    CfProblemIds ids = CfProblemIds.builder().firstId(type1.getFirstId())
                            .secondId(type1.getSecondId()).thirdId(type1.getThirdId()).build();
                    typeSet1.add(ids);
                }
                Set<CfProblemIds> typeSet2 = new HashSet<>(problems);
                problems.clear();
                problems.addAll(typeSet1);
                problems.retainAll(typeSet2);
            }
        }

        List<CfProblemIds> idsList = new ArrayList<>(problems);
        idsList.sort((o1, o2) -> o1.getFirstId() < o2.getFirstId() ? 1 : -1);


        int begin = limit * (currentPage - 1);
        int end = begin + limit - 1;

        if(begin < idsList.size()) {
            idsList = idsList.subList(begin, min(idsList.size(), end + 1));
        }

        for(CfProblemIds id : idsList) {
            CfProblems problem = cfService.selectProblemByIds(id.getFirstId(), id.getSecondId(), id.getThirdId());
            if(problem != null) {
                HashMap<String, Object> resultMap = new HashMap<>();
                resultMap.put("problem", problem);
                List<String> typeList = cfService.selectTypeById(problem.getFirstId(), problem.getSecondId(),
                        problem.getThirdId());
                resultMap.put("type", typeList);
                result.add(resultMap);
            }
        }

        return result;
    }
}
