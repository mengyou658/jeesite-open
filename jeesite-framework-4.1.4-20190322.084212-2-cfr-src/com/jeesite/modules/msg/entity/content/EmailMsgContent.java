/*	
 * Decompiled with CFR 0.141.	
 */	
package com.jeesite.modules.msg.entity.content;	
	
import com.jeesite.modules.msg.entity.content.BaseMsgContent;	
	
public class EmailMsgContent	
extends BaseMsgContent {	
    private String bcc;	
    private static final long serialVersionUID = 1L;	
    private String cc;	
	
    public void setBcc(String bcc) {	
        this.bcc = bcc;	
    }	
	
    public String getCc() {	
        return this.cc;	
    }	
	
    public void setCc(String cc) {	
        this.cc = cc;	
    }	
	
    public String getBcc() {	
        return this.bcc;	
    }	
	
    @Override	
    public String getMsgType() {	
        return "email";	
    }	
}	
	
