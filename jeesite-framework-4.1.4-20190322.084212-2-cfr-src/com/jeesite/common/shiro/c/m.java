/*	
 * Decompiled with CFR 0.141.	
 */	
package com.jeesite.common.shiro.c;	
	
import com.jeesite.common.shiro.session.SessionDAO;	
import org.apache.shiro.realm.AuthorizingRealm;	
	
public abstract class m	
extends AuthorizingRealm {	
    protected SessionDAO sessionDAO;	
	
    public void setSessionDAO(SessionDAO sessionDAO) {	
        this.sessionDAO = sessionDAO;	
    }	
}	
	
