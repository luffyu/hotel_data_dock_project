package com.rubber.project.controller;

import cn.hutool.luffyu.util.result.ResultMsg;
import com.rubber.project.task.AutoClearExecWaterTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luffyu
 * Created on 2021/5/16
 */
@RestController
@RequestMapping("/test")
public class TestAdminController {

    @Autowired
    private AutoClearExecWaterTask autoClearExecWaterTask;


    @PostMapping("/clear_water")
    public ResultMsg clearWater(){
        autoClearExecWaterTask.doClearExecWater();
        return ResultMsg.success();
    }
}
