/*	
 * Decompiled with CFR 0.141.	
 */	
package com.jeesite.modules.sys.entity;	
	
import com.jeesite.common.entity.DataEntity;	
import com.jeesite.common.mybatis.annotation.Column;	
import com.jeesite.common.mybatis.annotation.Table;	
	
@Table(name="${_prefix}sys_user_role", alias="a", columns={@Column(name="user_code", attrName="userCode", label="\u7528\u6237\u7f16\u7801", isPK=true), @Column(name="role_code", attrName="roleCode", label="\u89d2\u8272\u7f16\u7801", isPK=true)}, orderBy="a.user_code ASC")	
public class UserRole	
extends DataEntity<UserRole> {	
    private String roleCode;	
    private String userCode;	
    private static final long serialVersionUID = 1L;	
	
    public UserRole(String id) {	
        super(id);	
    }	
	
    public UserRole() {	
        this(null);	
    }	
	
    public String getUserCode() {	
        return this.userCode;	
    }	
	
    public String getRoleCode() {	
        return this.roleCode;	
    }	
	
    public void setUserCode(String userCode) {	
        this.userCode = userCode;	
    }	
	
    public void setRoleCode(String roleCode) {	
        this.roleCode = roleCode;	
    }	
}	
	
