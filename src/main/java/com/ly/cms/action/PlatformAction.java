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


import com.ly.cms.vo.Platform;
import com.ly.cms.service.PlatformService;


@IocBean
@At("/platform")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class PlatformAction {

	private static final Log log = Logs.getLog(PlatformAction.class);
	
	@Inject
	private PlatformService platformService;

    @At("/")
    @Ok("beetl:/WEB-INF/cms/platform_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Platform platform,
                      HttpServletRequest request){

        Cnd c = new ParseObj(platform).getCnd();
        if (c == null || c.equals(""))
        {
            p.setRecordCount(platformService.listCount(c));
            request.setAttribute("list_obj", platformService.queryCache(c,p));
        }else{
            p.setRecordCount(platformService.count(c));
            request.setAttribute("list_obj", platformService.query(c,p));
        }

        request.setAttribute("page", p);
        request.setAttribute("platform", platform);
    }

    @At
    @Ok("beetl:/WEB-INF/cms/platform.html")
    public void edit(@Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("platform", null);
        }else{
            request.setAttribute("platform", platformService.fetch(id));
        }
    }

    @At
    @Ok("json")
    public Map<String,String> save( @Param("..")Platform platform){
        Object rtnObject;
        if (platform.getId() == null || platform.getId() == 0) {
            rtnObject = platformService.dao().insert(platform);
        }else{
            rtnObject = platformService.dao().updateIgnoreNull(platform);
        }
        CacheManager.getInstance().getCache(PlatformService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((rtnObject == null) ? false : true, "tab_platform", true);

    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  platformService.delete(id);
        CacheManager.getInstance().getCache(PlatformService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((num > 0) ? true : false , "tab_platform",false);
    }

}
