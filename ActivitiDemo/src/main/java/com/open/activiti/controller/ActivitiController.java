package com.open.activiti.controller;


import com.open.activiti.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/process")
public class ActivitiController {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private TaskService taskService;


    /**
     * 手动部署流程定义
     * @param name
     * @param filePath
     * @return
     *
     * 可以通过如下方式指定bpmn文件的目录：filePath进行部署
     * 由于是对流程定义的操作，所以我们采用 RepositoryService 这个服务进行部署
     *
     */
    @PostMapping("deploy")
    public ResponseResult deployProcess(@RequestParam(name = "name") String name,
                                        @RequestParam(name= "filePath") String filePath){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource(filePath)
                .deploy();
        log.info("{} 流程定义完成部署",deploy.getName());
        return ResponseResult.getSuccessResult(deploy);
    }

    /**
     * 查询流程
     * @param key
     * @return
     *
     * 查询流程定义，依旧是对流程定义的操作，所以采用 RepositoryService 服务进行查询
     */
    @GetMapping(value = {"/list/{key}","/list"})
    public ResponseResult getProcessList(@PathVariable(name = "key",required = false) String key) {
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> definitionList;
        if (key!=null){
            definitionList = definitionQuery
                    .processDefinitionKey(key)
                    .list();
        }

       definitionList = definitionQuery.list();

        List<String> processList = new ArrayList<>();
        for (ProcessDefinition processDefinition : definitionList) {
            processList.add(processDefinition.getName());
        }
        return ResponseResult.getSuccessResult(processList);
    }

    /**
     * 启动流程定义（由流程定义-》流程实例）
     * @param key 流程id
     * @return
     *
     * 启动请假申请（leaveApplication）流程，由于是运行时状态，
     * 所以采用RuntimeService的startProcessInstanceByKey(key,map)方法进行启动
     * 访问成功后，看看表中的数据有没有变化呢，查看 ACT_RU_TASK 表，该表中新增了一条记录
     */
    @PostMapping("start/{key}")
    public ResponseResult startProcess(@PathVariable(name = "key") String key){
        Map<String,Object> map = new HashMap<>();
        //加入负责人
        map.put("assignee0","Jack");
        map.put("assignee1","Marry");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key,map);
        ResponseResult result =ResponseResult.getSuccessResult(processInstance.getProcessDefinitionName());
        log.info("流程实例的内容：{}",processInstance);
        return result;

    }


    /**
     * 查看当前负责人的任务列表
     * @return
     *
     * 当前登录的用户就是 Jack，此时我们通过接口 /process/task/list 接口来查看 Jack 的任务列表
     */
    @GetMapping("/task/list")
    public ResponseResult getMyTaskList(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取当前登录的用户名
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        if (principal instanceof Principal) {
            username = ((Principal) principal).getName();
        }
        //获取任务
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(username)
                .list();
        //获取任务名称列表
        List<String> TaskList = tasks.stream()
                .map(Task::getName)
                .collect(Collectors.toList());

        return ResponseResult.getSuccessResult(TaskList);
    }

    /**
     * 完成任务
     * @param key 流程id
     * @param assigne 负责人
     * @return
     *
     * 对任务的完成，依旧是采用TaskService进行
     *
     * 访问成功之后，我们再来查看 ACT_RU_TASK（当前任务表），按照预期，此时正要处理的任务为审批申请，并且负责人为 Marry
     *
     * 当Marry完成任务后，再次查询我们的 ACT_RU_TASK 表，会发现我们已经没有了正在处理的任务，说明我们已完成了所有的任务
     *
     * 再次查看历史表 ACT_HI_TASKINST，会看到我们所处理过的两个任务
     */
    @PostMapping("complete")
    public ResponseResult doTask(@RequestParam(name = "key") String key,@RequestParam(name = "assignee")String assigne){
        List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee(assigne)
                .list();
        if (tasks!=null && tasks.size()>0){
            for (Task task : tasks) {
                log.info("任务名称：{}",task.getName());
                taskService.complete(task.getId());
                log.info("{}，任务已完成",task.getName());

            }
        }
        return ResponseResult.getSuccessResult(null);

    }

    /**
     * 删除部署
     * @param deploymentId
     * @return
     */
    @PostMapping("delete/{id}")
    public ResponseResult deleteDeployment(@PathVariable(name = "id") String deploymentId){
        /**
         * deleteDeployment() 方法的第二个参数 cascade 设置为 true,表示需要进行级联删除，从而可以删除掉未完成的任务
         */
        repositoryService.deleteDeployment(deploymentId,true);
        return ResponseResult.getSuccessResult(null);
    }
}
