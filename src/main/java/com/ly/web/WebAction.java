package com.ly.web;

import com.ly.cms.service.FuncnameService;
import com.ly.cms.service.ProductService;
import com.ly.cms.service.WebmenuService;
import com.ly.cms.vo.Product;
import com.ly.cms.vo.Webmenu;
import com.ly.comm.Page;
import com.ly.comm.ParseObj;
import com.ly.sys.service.InfoService;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/")
@Fail("json")
public class WebAction {
	
	private static final Log log = Logs.getLog(WebAction.class);

    @Inject
    private InfoService infoService;

    @Inject
    private WebmenuService webmenuService;

    @Inject
    private ProductService productService;

    @Inject
    private FuncnameService funcnameService;
	
	@At("")
    @Ok("beetl:/WEB-INF/index.html")
	public void index(HttpServletRequest request)
	{
        Page p = new Page();
        p.setPageSize(4);

        Page p2 = new Page();
        p2.setPageSize(8);

        request.setAttribute("action_url","#");
        request.setAttribute("webmenu_list",webmenuService.queryCache(null,new Page()));

        //new
        request.setAttribute("newProduct_list",productService.query(Cnd.orderBy().desc("id"),p));

        //hot
        request.setAttribute("hotProduct_list",productService.query(Cnd.orderBy().desc("zan"),p));

        request.setAttribute("funcname_list",funcnameService.queryCache(null,p2));

        request.setAttribute("info", infoService.fetch(1L));
    }



    @At
    @Ok("beetl:/WEB-INF/compoent_list.html")
    public void search(HttpServletRequest request,
                       @Param("..")Page p,
                       @Param("..")Product product
    )
    {

        Page p2 = new Page();
        p2.setPageSize(8);

        Cnd c = new ParseObj(product).getCnd();
        if (c == null || c.equals(""))
        {
            p.setRecordCount(productService.listCount(c));
            request.setAttribute("product_list", productService.queryCache(c,p));
        }else{
            p.setRecordCount(productService.count(c));
            request.setAttribute("product_list", productService.query(c,p));
        }

        request.setAttribute("page", p);
        request.setAttribute("product", product);
        request.setAttribute("funcnameList",funcnameService.queryCache(null,new Page()));


        request.setAttribute("action_url","#");
        request.setAttribute("webmenu_list",webmenuService.queryCache(null,new Page()));
        request.setAttribute("funcname_list",funcnameService.queryCache(null,p2));
        request.setAttribute("info", infoService.fetch(1L));

        request.setAttribute("action_url",request.getServletPath());
    }


    @At
    @Ok("beetl:/WEB-INF/compoent.html")
    public void component(HttpServletRequest request,
                          @Param("productid")Long productid
    )
    {

        Page p2 = new Page();
        p2.setPageSize(8);


        request.setAttribute("product", productService.fetch(productid));

        request.setAttribute("action_url","search");
        request.setAttribute("webmenu_list",webmenuService.queryCache(null,new Page()));
        request.setAttribute("funcname_list",funcnameService.queryCache(null,p2));
        request.setAttribute("info", infoService.fetch(1L));

        request.setAttribute("action_url","search");
    }


    @At
    @Ok("beetl:/WEB-INF/index.html")
    public void newProduct(HttpServletRequest request)
    {

        request.setAttribute("action_url",request.getServletPath());
        request.setAttribute("webmenu_list",webmenuService.queryCache(null,new Page()));
        request.setAttribute("info", infoService.fetch(1L));
    }

    @At
    @Ok("beetl:/WEB-INF/linkme.html")
    public void linkme(HttpServletRequest request)
    {
        String action_url =  request.getServletPath();

        request.setAttribute("action_url",action_url);
        request.setAttribute("webmenu_list",webmenuService.queryCache(null,new Page()));
        request.setAttribute("info", infoService.fetch(1L));
    }


}
