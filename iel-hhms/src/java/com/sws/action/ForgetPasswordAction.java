package com.sws.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sws.common.baseAction.BaseAction;

public class ForgetPasswordAction extends BaseAction<ForgetPasswordAction> {
	private static final long serialVersionUID=4577836778733454278L;
	private Logger log=LoggerFactory.getLogger(ForgetPasswordAction.class);
	
	/*
	 * 进入页面
	 */
	public String forgetPassword(){
		return "page";
	}

}
