package com.sws.action;

import com.sws.common.baseAction.BaseAction;

public class HelpAction extends BaseAction<HelpAction>{

	private static final long serialVersionUID = 2859462393014913213L;

	
	public String help(){
		return PAGE;
	}
	
	
	public String contact(){
		return "contactPage";
	}
}
