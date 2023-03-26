package com.example.zscacm.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.zscacm.entity.*;
import com.example.zscacm.service.CfService;
import com.example.zscacm.service.LuoguService;
import com.example.zscacm.service.UserService;
import com.example.zscacm.service.VjService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.min;

@RestController
@CrossOrigin
public class CfProblemsController {

    @Resource
    private CfService cfService;

    @Resource
    private UserService userService;

    @Resource
    private LuoguService luoguService;

    @Resource
    private VjService vjService;

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
                                                        @RequestParam(value = "limit", required = false, defaultValue = "25") Integer limit,
                                                        @RequestParam(value = "sysUser", required = false) String sysUser,
                                                        @RequestParam(value = "grade", required = false) Integer grade) {

        String handle;
        Integer cfId = null;

        Set<CfProblemIds> gradeProblems = new HashSet<>();

        //查询用户信息
        if(sysUser != null) {
            handle = userService.selectHandleByName(sysUser);
             cfId = cfService.selectUserId(handle);
        }

        //查询所选年级的解题信息
        if(grade != null) {
            List<SysUser> gradeUser = userService.selectUserByGrade(grade);
            for(SysUser user : gradeUser) {
                List<CfProblemIds> problemsId = cfService.selectUserSubmitIds(user.getId());
                gradeProblems.addAll(problemsId);
            }
        }

        int firstId = 0;
        int secondId = 0;
        int thirdId = 0;
        int flag = 3;

        //解析题目id
        if(problemIds != null && !problemIds.isBlank()) {
            flag = 0;
            for(int j = 0; j < problemIds.length(); j++) {
                if(flag == 0 && problemIds.charAt(j) >= '0' && problemIds.charAt(j) <= '9') {
                    firstId = firstId * 10 + (problemIds.charAt(j) - '0');
                } else if(flag == 0 && problemIds.charAt(j) >= 'A' && problemIds.charAt(j) <= 'Z') {
                    secondId = problemIds.charAt(j) - 'A' + 1;
                    flag = 1;
                    if(j + 1 == problemIds.length()) {
                        flag = 2;
                    }
                } else if(flag == 1 && problemIds.charAt(j) >= '0' && problemIds.charAt(j) <= '9'){
                    thirdId = problemIds.charAt(j) - '0';
                    flag = 2;
                } else {
                    flag = -1;
                    break;
                }
            }
        }

        List<CfProblems> problemsList = new ArrayList<>();

        //查找题目列表
        if(submited == 1) {
            List<CfUserSubmits> userSubmits = cfService.selectUserSubmit(cfId);
            for(CfUserSubmits userSubmit : userSubmits) {
                CfProblems problem = cfService.selectProblemByIds(
                        userSubmit.getFirstId(), userSubmit.getSecondId(), userSubmit.getThirdId());
                if(problem != null) {
                    problemsList.add(problem);
                }
            }
        } else {
            problemsList = cfService.selectCfProblems();
        }

        Set<CfProblemIds> problems = new HashSet<>();

        //题目过滤
        for(CfProblems problem : problemsList) {
            if(problemName != null) {
                String pName = problem.getProblemName();
                int begin = 0, end = pName.length() - 1, ff = 0;
                for(int i = 0; i < pName.length(); i++) {
                    if(pName.charAt(i) == ' ' && ff == 0) begin = i + 1;
                    else break;
                }
                ff = 0;
                for(int i = pName.length() - 1; i >= 0; i--) {
                    if(pName.charAt(i) == ' ' && ff == 0) end = i - 1;
                    else break;
                }
                problem.setProblemName(problem.getProblemName().substring(begin, end + 1));
                if(!problemName.isBlank() && !problemName.equals(problem.getProblemName())) {
                    continue;
                }
            }



            if(gradeProblems.contains(new CfProblemIds(problem.getFirstId(), problem.getSecondId(), problem.getThirdId()))) {
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



        //类型过滤
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


        //排序
        List<CfProblemIds> idsList = new ArrayList<>(problems);
        idsList.sort((o1, o2) -> {
            if(o1.getFirstId() < o2.getFirstId()) {
                return 1;
            } else if(o1.getFirstId() > o2.getFirstId()) {
                return -1;
            } else {
                if(o1.getSecondId() < o2.getSecondId()) {
                    return 1;
                } else if(o1.getSecondId() > o2.getSecondId()) {
                    return -1;
                } else {
                    if(o1.getThirdId() < o2.getThirdId()) {
                        return 1;
                    } else if(o1.getThirdId() > o2.getThirdId()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        });

        int total = idsList.size();

        int begin = limit * (currentPage - 1);
        int end = begin + limit - 1;

        if(begin < idsList.size()) {
            idsList = idsList.subList(begin, min(idsList.size(), end + 1));
        }



        List<HashMap<String, Object>> result = null;

        if(idsList.size() != 0) {
            try {
                result = Thread(idsList, 15, cfId, sysUser);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            result = new ArrayList<>();
        }

        HashMap<String, Object> totalMap = new HashMap<>();
        totalMap.put("total", total);
        result.add(totalMap);
        return result;
    }

    @GetMapping("/users")
    public ResponseResult getUsers(@RequestParam(value = "grade", required = false) Integer grade) {
        List<SysUser> userList;
        if(grade == null) {
            userList = userService.selectUser();
        } else {
            userList = userService.selectUserByGrade(grade);
        }

        List<HashMap<String, Object>> users = new ArrayList<>();
        for(SysUser user : userList) {
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("name", user.getUsername());
            resultMap.put("grade", user.getGrade());

            int lgProblem = luoguService.selectTotalProblemByLgid(user.getLgid());
            int vjProblem = vjService.selectTotalProblemByName(user.getVjName());
            CfUser cfUser = cfService.selectUser(user.getHandle());
            int cfProblem = cfUser.getSolvedNum();
            resultMap.put("solved", lgProblem + vjProblem + cfProblem);
            resultMap.put("rating", cfUser.getRating());
            resultMap.put("maxRating", cfUser.getMaxRating());

            int cfContestNum = cfService.selectUserContestNum(user.getHandle());
            resultMap.put("contestNum", cfContestNum);

            users.add(resultMap);
        }

        return new ResponseResult(200, "查询成功", users);
    }

    public synchronized List<HashMap<String, Object>> Thread(List<CfProblemIds> list, int nThread, int cfId, String sysUser) throws InterruptedException {

        if (CollectionUtils.isEmpty(list) || nThread <= 0 || CollectionUtils.isEmpty(list)) {
            return null;
        }
        CountDownLatch latch = new CountDownLatch(list.size());//创建一个计数器（大小为当前数组的大小，确保所有执行完主线程才结束）
        ExecutorService pool = Executors.newFixedThreadPool(nThread);//创建一个固定的线程池

        List<HashMap<String, Object>> result = new ArrayList<>();
        for(CfProblemIds id : list) {
            Integer finalCfId = cfId;
            //加入线程池
            pool.execute(() -> {
                //查询问题对象
                CfProblems problem = cfService.selectProblemByIds(id.getFirstId(), id.getSecondId(), id.getThirdId());
                if(problem != null) {
                    HashMap<String, Object> resultMap = new HashMap<>();
                    resultMap.put("problem", problem);
                    //查询类型列表
                    List<String> typeList = cfService.selectTypeById(problem.getFirstId(), problem.getSecondId(),
                            problem.getThirdId());
                    resultMap.put("type", typeList);

                    //若用户已登录，查询题目提交状态
                    if(sysUser != null && !sysUser.isBlank()) {
                        CfUserSubmits submit = cfService.selectSubmitById(finalCfId, id.getFirstId(), id.getSecondId(), id.getThirdId());

                        if(submit != null) {
                            resultMap.put("status", submit.getStatus());
                        }
                        else resultMap.put("status", "-");
                    } else {
                        resultMap.put("status", "-");
                    }

                    if(!resultMap.get("status").equals("SKIPPED")){
                        result.add(resultMap);
                    }
                }
                latch.countDown();
            });
        }
        //等待计数器归零
        latch.await();
        pool.shutdown();

        return result;
    }
}
