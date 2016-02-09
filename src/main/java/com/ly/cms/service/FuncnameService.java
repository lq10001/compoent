package  com.ly.cms.service;

import com.ly.cms.vo.Funcname;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.service.IdEntityService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.nutz.dao.Cnd;
import com.ly.comm.Page;

import java.util.List;


@IocBean(fields = { "dao" })
public class FuncnameService extends IdEntityService<Funcname> {

	public static String CACHE_NAME = "funcname";
    public static String CACHE_COUNT_KEY = "funcname_count";

    public List<Funcname> queryCache(Cnd c,Page p)
    {
        List<Funcname> list_funcname = null;
        String cacheKey = "funcname_list_" + p.getPageCurrent();

        Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
        if(cache.get(cacheKey) == null)
        {
            list_funcname = this.query(c, p);
            cache.put(new Element(cacheKey, list_funcname));
        }else{
            list_funcname = (List<Funcname>)cache.get(cacheKey).getObjectValue();
        }
        return list_funcname;
    }

    public int listCount(Cnd c)
    {
        Long num = 0L;
        Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
        if(cache.get(CACHE_COUNT_KEY) == null)
        {
            num = Long.valueOf(this.count(c));
            cache.put(new Element(CACHE_COUNT_KEY, num));
        }else{
            num = (Long)cache.get(CACHE_COUNT_KEY).getObjectValue();
        }
        return num.intValue();
    }



}


