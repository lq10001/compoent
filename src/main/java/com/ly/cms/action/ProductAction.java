package com.ly.cms.action;

import com.aliyun.oss.OSSClient;
import com.ly.Global;
import com.ly.cms.service.FuncnameService;
import com.ly.cms.service.PlatformService;
import com.ly.comm.Bjui;
import com.ly.comm.OSSSingleton;
import com.ly.comm.Page;
import com.ly.comm.ParseObj;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheManager;


import com.ly.cms.vo.Product;
import com.ly.cms.service.ProductService;
import org.nutz.mvc.upload.UploadAdaptor;


@IocBean
@At("product")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class ProductAction {

	private static final Log log = Logs.getLog(ProductAction.class);
	
	@Inject
	private ProductService productService;

    @Inject
    private FuncnameService funcnameService;

    @Inject
    private PlatformService platformService;

    @At("/")
    @Ok("beetl:/WEB-INF/cms/product_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Product product,
                      HttpServletRequest request){

        Cnd c = new ParseObj(product).getCnd();
        if (c == null || c.equals(""))
        {
            p.setRecordCount(productService.listCount(c));
            request.setAttribute("list_obj", productService.queryCache(c,p));
        }else{
            p.setRecordCount(productService.count(c));
            request.setAttribute("list_obj", productService.query(c,p));
        }

        request.setAttribute("page", p);
        request.setAttribute("product", product);
        request.setAttribute("funcnameList",funcnameService.queryCache(null,new Page()));
        request.setAttribute("platformList",platformService.queryCache(null,new Page()));


    }

    @At
    @Ok("beetl:/WEB-INF/cms/product.html")
    public void edit(@Param("action")int action,
                     @Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("product", null);
        }else{

            Product product = productService.fetch(id);
            if (action == 3)
            {
                //product.setName(null);
            }
            request.setAttribute("product", product);
        }
        request.setAttribute("action", action);
        request.setAttribute("funcnameList",funcnameService.queryCache(null,new Page()));
        request.setAttribute("platformList",platformService.queryCache(null,new Page()));
    }

    @At
    @Ok("json")
    @AdaptBy(type = UploadAdaptor.class, args = { "ioc:uploadFile" })
    public Map<String,String> save(@Param("action")int action,
                                @Param("..")Product product,
                                @Param("file1") File f1,
                                @Param("file2") File f2,
                                HttpServletRequest request
     )throws IOException {
        Object rtnObject;

        String webPath =  request.getServletContext().getRealPath("/");
        String appPath = webPath + "upload/";

        if (f1 != null)
        {
            if (product.getFirstkey() != null && product.getFirstkey().trim().length() > 2)
            {
                OSSSingleton.getInstance().delFile(product.getFirstkey());
            }

            String fileName = System.currentTimeMillis()+f1.getName();

            OSSSingleton.getInstance().addFile(fileName,f1);

            product.setFirstkey(fileName);
            product.setSmallimage("http://"+ Global.bucketName+"." + Global.public_endpoint + "/" + fileName);

        }
        /*
        if (f2 != null)
        {
            if (product.getMaximage() != null) {
                String oldImgName = product.getMaximage().trim();
                if (oldImgName.length() > 2) {
                    Files.deleteFile(new File(appPath + oldImgName));
                }
            }

            String fileName2 = System.currentTimeMillis()+f2.getName();
            Files.copyFile(f2, new File(appPath + fileName2));
            product.setMaximage(fileName2);
        }
        */

        Date date = new Date();
        if (product.getId() == null || product.getId() == 0) {
            product.setAdddate(date);
            product.setEditdate(date);
            rtnObject = productService.dao().insert(product);
        }else{
            if (action == 3) {
                product.setId(null);
                product.setAdddate(date);
                product.setEditdate(date);
                rtnObject = productService.dao().insert(product);

            }else{
                product.setEditdate(date);
                rtnObject = productService.dao().updateIgnoreNull(product);
            }
        }
        CacheManager.getInstance().getCache(ProductService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((rtnObject == null) ? false : true, "tab_product", true);

    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  productService.delete(id);
        CacheManager.getInstance().getCache(ProductService.CACHE_NAME).removeAll();
        return Bjui.rtnMap((num > 0) ? true : false , "tab_product",false);
    }

}
