/*	
 * Decompiled with CFR 0.141.	
 */	
package com.jeesite.modules.job.web;	
	
import com.jeesite.autoconfigure.core.CacheAutoConfiguration;	
import com.jeesite.common.entity.Page;	
import com.jeesite.common.web.BaseController;	
import com.jeesite.modules.job.entity.JobLog;	
import com.jeesite.modules.job.service.JobLogService;	
import javax.servlet.http.HttpServletRequest;	
import javax.servlet.http.HttpServletResponse;	
import org.apache.shiro.authz.annotation.RequiresPermissions;	
import org.hyperic.sigar.ProcTime;	
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;	
import org.springframework.stereotype.Controller;	
import org.springframework.ui.Model;	
import org.springframework.validation.annotation.Validated;	
import org.springframework.web.bind.annotation.ModelAttribute;	
import org.springframework.web.bind.annotation.PostMapping;	
import org.springframework.web.bind.annotation.RequestMapping;	
import org.springframework.web.bind.annotation.ResponseBody;	
	
@Controller	
@RequestMapping(value={"${adminPath}/job/jobLog"})	
@ConditionalOnProperty(name={"job.enabled", "web.core.enabled"}, havingValue="true", matchIfMissing=true)	
public class JobLogController	
extends BaseController {	
    @Autowired	
    private JobLogService jobLogService;	
	
    /*	
     * WARNING - void declaration	
     */	
    @RequiresPermissions(value={"sys:job:view"})	
    @RequestMapping(value={"listData"})	
    @ResponseBody	
    public Page<JobLog> listData(JobLog jobLog, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {	
        void response;	
        void request;	
        void jobLog2;	
        jobLog2.setPage(new Page((HttpServletRequest)request, (HttpServletResponse)response));	
        return this.jobLogService.findPage((JobLog)jobLog2);	
    }	
	
    @RequiresPermissions(value={"sys:job:edit"})	
    @PostMapping(value={"save"})	
    @ResponseBody	
    public String save(@Validated JobLog jobLog) {	
        JobLogController jobLogController = this;	
        jobLogController.jobLogService.save(jobLog);	
        return jobLogController.renderResult("true", "保存调度日志成功！");	
    }	
	
    @RequiresPermissions(value={"sys:job:view"})	
    @RequestMapping(value={"list", ""})	
    public String list(JobLog jobLog, Model model) {	
        model.addAttribute("jobLog", jobLog);	
        return "modules/job/jobLogList";	
    }	
	
    @RequiresPermissions(value={"sys:job:view"})	
    @RequestMapping(value={"form"})	
    public String form(JobLog jobLog, Model model) {	
        model.addAttribute("jobLog", jobLog);	
        return "modules/job/jobLogForm";	
    }	
	
    @ModelAttribute	
    public JobLog get(String id, boolean isNewRecord) {	
        return (JobLog)this.jobLogService.get(id, isNewRecord);	
    }	
}	
	
