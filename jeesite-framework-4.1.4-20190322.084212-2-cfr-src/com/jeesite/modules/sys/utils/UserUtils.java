/*	
 * Decompiled with CFR 0.141.	
 */	
package com.jeesite.modules.sys.utils;	
	
import com.jeesite.common.cache.CacheUtils;	
import com.jeesite.common.codec.Md5Utils;	
import com.jeesite.common.collect.ListUtils;	
import com.jeesite.common.collect.SetUtils;	
import com.jeesite.common.config.Global;	
import com.jeesite.common.lang.DateUtils;	
import com.jeesite.common.lang.ObjectUtils;	
import com.jeesite.common.lang.StringUtils;	
import com.jeesite.common.mybatis.annotation.MyBatisDao;	
import com.jeesite.common.service.ServiceException;	
import com.jeesite.common.shiro.realm.LoginInfo;	
import com.jeesite.common.shiro.session.StaticSession;	
import com.jeesite.common.utils.SpringUtils;	
import com.jeesite.common.web.http.ServletUtils;	
import com.jeesite.modules.sys.entity.Menu;	
import com.jeesite.modules.sys.entity.Role;	
import com.jeesite.modules.sys.entity.User;	
import com.jeesite.modules.sys.utils.j;	
import java.io.Serializable;	
import java.lang.annotation.Annotation;	
import java.lang.reflect.Constructor;	
import java.lang.reflect.Method;	
import java.util.ArrayList;	
import java.util.List;	
import java.util.Map;	
import java.util.concurrent.locks.Lock;	
import java.util.concurrent.locks.ReentrantLock;	
import javax.servlet.http.HttpServletRequest;	
import org.apache.shiro.SecurityUtils;	
import org.apache.shiro.UnavailableSecurityManagerException;	
import org.apache.shiro.authz.AuthorizationInfo;	
import org.apache.shiro.session.InvalidSessionException;	
import org.apache.shiro.session.Session;	
import org.apache.shiro.subject.Subject;	
import org.hyperic.jni.ArchNotSupportedException;	
import org.hyperic.sigar.pager.PageList;	
import org.slf4j.Logger;	
import org.slf4j.LoggerFactory;	
	
public class UserUtils {	
    private static Logger logger;	
    private static final String USER_CACHE_LOGIN_CODE_ = "login_";	
    public static final String CACHE_MENU_LIST = "menuList";	
    public static final String USER_CACHE = "userCache";	
    public static final String CACHE_AUTH_INFO = "authInfo";	
    private static Lock userCacheLock;	
    private static final String USER_CACHE_USER_TYPE_AND_REF_CODE_ = "type_ref_";	
    private static final String USER_CACHE_USER_CODE_ = "code_";	
	
    public static void clearCache(User user) {	
        if (user == null || StringUtils.isBlank(user.getUserCode())) {	
            return;	
        }	
        User a = (User)((Object)CacheUtils.get(USER_CACHE, new StringBuilder().insert(0, USER_CACHE_USER_CODE_).append(user.getUserCode()).toString()));	
        if (a != null && StringUtils.isNotBlank(a.getUserCode())) {	
            CacheUtils.remove(USER_CACHE, new StringBuilder().insert(0, USER_CACHE_USER_CODE_).append(a.getUserCode()).toString());	
            CacheUtils.remove(USER_CACHE, new StringBuilder().insert(0, USER_CACHE_LOGIN_CODE_).append(a.getLoginCode()).toString());	
            if (StringUtils.isNotBlank(a.getUserType()) && StringUtils.isNotBlank(a.getRefCode())) {	
                CacheUtils.remove(USER_CACHE, new StringBuilder().insert(0, USER_CACHE_USER_TYPE_AND_REF_CODE_).append(a.getUserType()).append("_").append(a.getRefCode()).toString());	
            }	
            CacheUtils.removeCache(new StringBuilder().insert(0, "userCache_").append(a.getUserCode()).toString());	
        }	
    }	
	
    static {	
        userCacheLock = new ReentrantLock();	
        logger = LoggerFactory.getLogger(UserUtils.class);	
    }	
	
    /*	
     * Unable to fully structure code	
     * Enabled aggressive block sorting	
     * Lifted jumps to return sites	
     */	
    public static List<Menu> getMenuListByParentCode(String parentCode) {	
        block4 : {	
            block3 : {	
                a = ObjectUtils.toStringIgnoreNull(UserUtils.getSession().getAttribute("sysCode"), "default");	
                a = new StringBuilder().insert(0, "menuList_").append(a).append("_").append(parentCode).toString();	
                a = (List<Menu>)UserUtils.getCache(a);	
                if (a != null) return a;	
                var5_4 = new Menu();	
                v0 = a;	
                v0.setSysCode(a);	
                v0.setParentCode(parentCode);	
                a = UserUtils.getUser();	
                if (!User.isSuperAdmin(a.getUserCode())) break block3;	
                a.setWeight_gte(Menu.SUPER_ADMIN_GET_MENU_MIN_WEIGHT);	
                a = j.ALLATORIxDEMO().findList((Menu)a);	
                v1 = a;	
                break block4;	
            }	
            if (!"none".equals(a.getUserType()) && StringUtils.isNotBlank((CharSequence)(a = Global.getConfig(new StringBuilder().insert(0, "sys.user.defaultRoleCodes.").append(a.getUserType()).toString())))) {	
                a.setDefaultRoleCodes(StringUtils.split((String)a, ','));	
            }	
            if (!"1".equals(a.getMgrType())) ** GOTO lbl34	
            if (a.getDefaultRoleCodes() != null) {	
                v2 = a;	
                a = SetUtils.newHashSet(v2.getDefaultRoleCodes());	
                a.add((String)Role.CORP_ADMIN_ROLE_CODE);	
                v2.setDefaultRoleCodes(a.toArray(new String[a.size()]));	
                v3 = a;	
            } else {	
                v4 = new String[1];	
                v4[0] = Role.CORP_ADMIN_ROLE_CODE;	
                a.setDefaultRoleCodes(v4);	
lbl34: // 2 sources:	
                v3 = a;	
            }	
            v3.setUserCode(a.getId());	
            a = j.ALLATORIxDEMO().findByUserCode((Menu)a);	
            v1 = a;	
        }	
        UserUtils.putCache(v1, a);	
        return a;	
    }	
	
    public static List<Menu> getMenuTree() {	
        ArrayList a = ListUtils.newArrayList();	
        List<Menu> a2 = UserUtils.getMenuList();	
        j.ALLATORIxDEMO().convertChildList(a2, a, "0");	
        return a2;	
    }	
	
    /*	
     * WARNING - Removed try catching itself - possible behaviour change.	
     */	
    public static User getByTypeAndRef(String userType, String refCode) {	
        if (StringUtils.isBlank(userType) || StringUtils.isBlank(refCode)) {	
            return null;	
        }	
        String a = new StringBuilder().insert(0, USER_CACHE_USER_TYPE_AND_REF_CODE_).append(userType).append("_").append(refCode).toString();	
        String a2 = CacheUtils.get(USER_CACHE, a);	
        if (a2 != null) {	
            return UserUtils.get(a2);	
        }	
        userCacheLock.lock();	
        try {	
            User a3;	
            a2 = CacheUtils.get(USER_CACHE, a);	
            if (a2 != null) {	
                User user = UserUtils.get(a2);	
                return user;	
            }	
            User user = new User();	
            void v0 = a3;	
            v0.setUserType(userType);	
            v0.setRefCode(refCode);	
            a3 = j.ALLATORIxDEMO().getByUserTypeAndRefCode(a3);	
            a3 = UserUtils.returnUser(a3);	
            if (a3 != null) {	
                User user2 = (User)a3.clone();	
                return user2;	
            }	
        }	
        finally {	
            userCacheLock.unlock();	
        }	
        return null;	
    }	
	
    public static void removeCacheByKeyPrefix(String keyPrefix) {	
        LoginInfo a = UserUtils.getLoginInfo();	
        if (a != null) {	
            String a2 = a.getId();	
            CacheUtils.removeByKeyPrefix(new StringBuilder().insert(0, "userCache_").append(a2).toString(), keyPrefix);	
        }	
    }	
	
    public static <V> V getCache(String key, V defaultValue) {	
        V a = UserUtils.getCache(key);	
        if (a != null) {	
            return a;	
        }	
        return defaultValue;	
    }	
	
    public static AuthorizationInfo getAuthInfo() {	
        Session a = UserUtils.getSession();	
        return (AuthorizationInfo)UserUtils.getCache(new StringBuilder().insert(0, "authInfo_").append(a.getId()).toString());	
    }	
	
    public static <V> V getCache(String key) {	
        LoginInfo a = UserUtils.getLoginInfo();	
        if (a != null) {	
            String a2 = a.getId();	
            return (V)CacheUtils.get(new StringBuilder().insert(0, "userCache_").append(a2).toString(), key);	
        }	
        return (V)UserUtils.getSession().getAttribute(key);	
    }	
	
    public static void removeCache(String key) {	
        LoginInfo a = UserUtils.getLoginInfo();	
        if (a != null) {	
            String a2 = a.getId();	
            CacheUtils.remove(new StringBuilder().insert(0, "userCache_").append(a2).toString(), key);	
            return;	
        }	
        UserUtils.getSession().removeAttribute(key);	
    }	
	
    public static LoginInfo getLoginInfo() {	
        try {	
            Subject a = UserUtils.getSubject();	
            LoginInfo a2 = (LoginInfo)a.getPrincipal();	
            if (a2 != null) {	
                return a2;	
            }	
        }	
        catch (UnavailableSecurityManagerException a) {	
        }	
        catch (InvalidSessionException a) {	
            // empty catch block	
        }	
        return null;	
    }	
	
    public static String getUserTypeValue(String userType, String key) {	
        if (j.ALLATORIxDEMO() == null) {	
            throw new ServiceException("jeesite.yml中的user.userTypeMap配置不正确或没有定义。");	
        }	
        if ("none".equals(userType)) {	
            return null;	
        }	
        Map a = (Map)j.ALLATORIxDEMO().get(userType);	
        if (a != null && a.get(key) != null) {	
            return (String)a.get(key);	
        }	
        logger.warn(new StringBuilder().insert(0, "jeesite.yml中的user.userTypeMap没有配置“").append(userType).append("”用户类型的“").append(key).append("”值。").toString());	
        return null;	
    }	
	
    public static String getSsoToken(String username) {	
        String a = Global.getConfig("shiro.sso.secretKey");	
        if (StringUtils.isBlank(a)) {	
            logger.warn("属性文件 shiro.sso.secretKey 为空，请设置后再操作。");	
            return null;	
        }	
        if (Global.getConfigToBoolean("shiro.sso.encryptKey", "true").booleanValue()) {	
            a = Global.getPropertyDecodeAndEncode("f6ac0085022dd47f1fe58e0dff6f0883", "shiro.sso.secretKey", a);	
        }	
        return Md5Utils.md5(new StringBuilder().insert(0, a).append(username).append(DateUtils.getDate("yyyyMMdd")).toString());	
    }	
	
    public static <V> void putCache(String key, V value) {	
        if (StringUtils.isNotBlank(key)) {	
            LoginInfo a = UserUtils.getLoginInfo();	
            if (a != null) {	
                String a2 = a.getId();	
                CacheUtils.put(new StringBuilder().insert(0, "userCache_").append(a2).toString(), key, value);	
                return;	
            }	
            UserUtils.getSession().setAttribute(key, value);	
        }	
    }	
	
    public static void clearCache() {	
        LoginInfo a = UserUtils.getLoginInfo();	
        if (a != null) {	
            String a2 = a.getId();	
            User a3 = (User)((Object)CacheUtils.get(USER_CACHE, new StringBuilder().insert(0, USER_CACHE_USER_CODE_).append(a2).toString()));	
            if (a3 != null) {	
                UserUtils.clearCache(a3);	
            }	
        }	
    }	
	
    public static Session getSession() {	
        try {	
            Subject a = UserUtils.getSubject();	
            Session a2 = a.getSession(false);	
            if (a2 == null) {	
                a2 = a.getSession(true);	
            }	
            if (a2 != null) {	
                return a2;	
            }	
        }	
        catch (UnavailableSecurityManagerException a) {	
        }	
        catch (InvalidSessionException a) {	
            // empty catch block	
        }	
        return StaticSession.INSTANCE;	
    }	
	
    public static User getUser() {	
        User a;	
        LoginInfo a2 = UserUtils.getLoginInfo();	
        if (a2 != null && (a = UserUtils.get(a2.getId())) != null) {	
            return a;	
        }	
        return new User();	
    }	
	
    /*	
     * WARNING - Removed try catching itself - possible behaviour change.	
     */	
    public static User get(String userCode) {	
        Object a;	
        User a2;	
        if (StringUtils.isBlank(userCode)) {	
            return null;	
        }	
        HttpServletRequest a3 = ServletUtils.getRequest();	
        if (a3 != null) {	
            a = (User)a3.getAttribute("__user__" + userCode);	
            if (a != null) {	
                return a;	
            }	
        }	
        if ((a2 = (User)((Object)CacheUtils.get(USER_CACHE, a = new StringBuilder().insert(0, USER_CACHE_USER_CODE_).append(userCode).toString()))) != null) {	
            return (User)a2.clone();	
        }	
        userCacheLock.lock();	
        try {	
            a2 = (User)CacheUtils.get(USER_CACHE, a);	
            if (a2 != null) {	
                User user = (User)a2.clone();	
                return user;	
            }	
            User a4 = new User(userCode);	
            a2 = j.ALLATORIxDEMO().get(a4);	
            a2 = UserUtils.returnUser(a2);	
            if (a3 != null) {	
                a3.setAttribute(new StringBuilder().insert(0, "__user__").append(userCode).toString(), a2);	
            }	
            if (a2 != null) {	
                User user = (User)a2.clone();	
                return user;	
            }	
            User user = null;	
            return user;	
        }	
        finally {	
            userCacheLock.unlock();	
        }	
    }	
	
    public static List<Menu> getMenuList() {	
        return UserUtils.getMenuListByParentCode(null);	
    }	
	
    /*	
     * WARNING - Removed try catching itself - possible behaviour change.	
     */	
    public static User getByLoginCode(String loginCode) {	
        if (StringUtils.isBlank(loginCode)) {	
            return null;	
        }	
        String a = new StringBuilder().insert(0, USER_CACHE_LOGIN_CODE_).append(loginCode).toString();	
        String a2 = CacheUtils.get(USER_CACHE, a);	
        if (a2 != null) {	
            return UserUtils.get(a2);	
        }	
        userCacheLock.lock();	
        try {	
            a2 = CacheUtils.get(USER_CACHE, a);	
            if (a2 != null) {	
                User user = UserUtils.get(a2);	
                return user;	
            }	
            User a3 = new User();	
            a3.setLoginCode(loginCode);	
            a3 = j.ALLATORIxDEMO().getByLoginCode(a3);	
            a3 = UserUtils.returnUser(a3);	
            if (a3 != null) {	
                User user = (User)a3.clone();	
                return user;	
            }	
        }	
        finally {	
            userCacheLock.unlock();	
        }	
        return null;	
    }	
	
    /*	
     * Enabled aggressive block sorting	
     * Enabled unnecessary exception pruning	
     * Enabled aggressive exception aggregation	
     */	
    public static void loadRefObj(User user) {	
        if (user.getRefObj() != null) return;	
        if (!StringUtils.isNotBlank(user.getUserType())) return;	
        if (!StringUtils.isNotBlank(user.getRefCode())) return;	
        String a = UserUtils.getUserTypeValue(user.getUserType(), "dao");	
        if (!StringUtils.isNotBlank(a)) return;	
        try {	
            Object a2;	
            User user2;	
            block6 : {	
                int n;	
                a2 = SpringUtils.getBean(a);	
                Class<?>[] arrclass = a2.getClass().getInterfaces();	
                int n2 = arrclass.length;	
                int n3 = n = 0;	
                while (n3 < n2) {	
                    MyBatisDao a3 = arrclass[n].getAnnotation(MyBatisDao.class);	
                    if (a3 != null) {	
                        Class[] arrclass2 = new Class[1];	
                        arrclass2[0] = String.class;	
                        Object[] arrobject = new Object[1];	
                        arrobject[0] = user.getRefCode();	
                        user.setRefObj(a3.entity().getConstructor(arrclass2).newInstance(arrobject));	
                        user2 = user;	
                        break block6;	
                    }	
                    n3 = ++n;	
                }	
                user2 = user;	
            }	
            if (user2.getRefObj() == null) return;	
            Class[] arrclass = new Class[1];	
            arrclass[0] = Object.class;	
            Object[] arrobject = new Object[1];	
            arrobject[0] = user.getRefObj();	
            Object a4 = a2.getClass().getMethod("get", arrclass).invoke(a2, arrobject);	
            if (a4 == null) return;	
            user.setRefObj(a4);	
            return;	
        }	
        catch (Exception a5) {	
            a5.printStackTrace();	
        }	
    }	
	
    public static Subject getSubject() {	
        return SecurityUtils.getSubject();	
    }	
	
    private static /* synthetic */ User returnUser(User user) {	
        if (user == null) {	
            return null;	
        }	
        if (!"none".equals(user.getUserType())) {	
            UserUtils.loadRefObj(user);	
        }	
        user.setRoleList(j.ALLATORIxDEMO().findListByUserCode(new Role(user)));	
        CacheUtils.put(USER_CACHE, new StringBuilder().insert(0, USER_CACHE_USER_CODE_).append(user.getId()).toString(), user);	
        CacheUtils.put(USER_CACHE, new StringBuilder().insert(0, USER_CACHE_LOGIN_CODE_).append(user.getLoginCode()).toString(), user.getUserCode());	
        if (StringUtils.isNotBlank(user.getUserType()) && StringUtils.isNotBlank(user.getRefCode())) {	
            CacheUtils.put(USER_CACHE, new StringBuilder().insert(0, USER_CACHE_USER_TYPE_AND_REF_CODE_).append(user.getUserType()).append("_").append(user.getRefCode()).toString(), user.getUserCode());	
        }	
        return user;	
    }	
}	
	
