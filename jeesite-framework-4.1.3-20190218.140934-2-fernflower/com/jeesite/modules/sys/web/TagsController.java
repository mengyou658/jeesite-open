package com.jeesite.modules.sys.web;	
	
import com.jeesite.common.web.BaseController;	
import com.jeesite.common.web.http.ServletUtils;	
import javax.servlet.http.HttpServletRequest;	
import org.hyperic.sigar.SysInfo;	
import org.hyperic.sigar.cmd.Tail;	
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;	
import org.springframework.stereotype.Controller;	
import org.springframework.ui.Model;	
import org.springframework.web.bind.annotation.RequestMapping;	
	
@Controller	
@RequestMapping({"tags"})	
@ConditionalOnProperty(	
   name = {"web.core.enabled"},	
   havingValue = "true",	
   matchIfMissing = true	
)	
public class TagsController extends BaseController {	
   @RequestMapping({"treeselect"})	
   public String treeselect(HttpServletRequest request, Model model) {	
      model.addAllAttributes(ServletUtils.getParameters(request));	
      return "tagsview/form/treeselect";	
   }	
	
   @RequestMapping({"iconselect"})	
   public String iconselect(HttpServletRequest request, Model model) {	
      model.addAllAttributes(ServletUtils.getParameters(request));	
      return "tagsview/form/iconselect";	
   }	
	
   @RequestMapping({"imageclip"})	
   public String imageclip(HttpServletRequest request, Model model) {	
      model.addAllAttributes(ServletUtils.getParameters(request));	
      return "tagsview/form/imageclip";	
   }	
}	
