package com.ly.cms.action;

import com.ly.comm.Bjui;
import com.ly.comm.Page;
import com.ly.comm.ParseObj;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheManager;


import com.ly.cms.vo.Funcname;
import com.ly.cms.service.FuncnameService;


@IocBean
@At("/funcname")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class FuncnameAction {

	private static final Log log = Logs.getLog(FuncnameAction.class);
	
	@Inject
	private FuncnameService funcnameService;

    @At("/")
    @Ok("beetl:/WEB-INF/cms/funcname_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Funcname funcname,
                      HttpServletRequest request){

        Cnd c = new ParseObj(funcname).getCnd();
        if (c == null || c.equals(""))
        {
            p.setRecordCount(funcnameService.listCount(c));
            request.setAttribute("list_obj", funcnameService.queryCache(c,p));
        }else{
            p.setRecordCount(funcnameService.count(c));
            request.setAttribute("list_obj", funcnameService.query(c,p));
        }

        request.setAttribute("page", p);
        request.setAttribute("funcname", funcname);
    }

    @At
    @Ok("beetl:/WEB-INF/cms/funcname.html")
    public void edit(@Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("funcname", null);
        }else{
            request.setAttribute("funcname", funcnameService.fetch(id));
        }
    }

    @At
    @Ok("json")
    public Map<String,String> save( @Param("..")Funcname funcname){
        Object rtnObject;
        if (funcname.getId() == null || funcname.getId() == 0) {
            rtnObject = funcnameService.dao().insert(funcname);
        }else{
            rtnObject = funcnameService.dao().updateIgnoreNull(funcname);
        }
        CacheManager.getInstance().getCache(FuncnameService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((rtnObject == null) ? false : true, "tab_funcname", true);

    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  funcnameService.delete(id);
        CacheManager.getInstance().getCache(FuncnameService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((num > 0) ? true : false , "tab_funcname",false);
    }

}
