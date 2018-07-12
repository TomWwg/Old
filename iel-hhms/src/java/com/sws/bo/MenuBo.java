package com.sws.bo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;



/**
 * <p>菜单数据格式对象</p>
 * @author heyongliang 2015-1-30 上午08:51:57
 * @version V1.0
 */
public class MenuBo{

	/**
	 * iframe打开方式
	 */
	public final static String TARGET_IFRAME = "mainFrame";
	
	/**
	 * 新页面打开方式
	 */
	public final static String TARGET_BLANK = "_blank";
	
	/**
	 * 选中
	 */
	public final static int ACTIVE = 1;
	
	/**
	 * 未选中
	 */
	public final static int INACTIVE = 0;
	
	/**
	 * 菜单ID
	 */
	private long menuId;
	
	/**
	 * 上级菜单ID
	 */
	private long parentId;
	
	/**
	 * 子菜单列表
	 */
	private List<MenuBo> data;
	
	/**
	 * 菜单名称
	 */
	private String name;
	
	/**
	 * 是否选中 1为选中 0为未选中
	 */
	private int active = INACTIVE;
	
	/**
	 * 连接地址，选路径
	 */
	private String href;
	
	/**
	 * 打开方式
	 */
	private String target;
	
	private String className;
	
	private String baseUrl;
	
	private int type;
	
	private int openType;
	
	private String img;
	
	/**
	 * 对应与功能菜单的functionCode
	 */
	private String code;
	
	/**
	 * 服务类型
	 */
	private String serviceType;
	
	/**
	 * 自定义应用访问地址
	 */
	private String customAppUrl;
	
	/**
	 * 自定义首页菜单url
	 */
	private String customHomeMenuUrl;
	
	public MenuBo(long menuId, long parentId, String name, String href, int active, int openType, int type) {
		this(menuId, parentId, name, href, active,openType, "", "", "", type, "", "", "", "");
	}
    
	public MenuBo(long menuId, long parentId, String name, String href, int active, int openType, String css, String baseUrl, String img, int type, String code, String serviceType, String customAppUrl, String customHomeMenuUrl) {
	    super();
	    this.menuId = menuId;
	    this.parentId = parentId;
	    this.name = name;
	    this.href = href;
	    this.active = active;
	    setOpenType(openType);
	    this.className = css;
	    this.img = img;
	    this.baseUrl = baseUrl;
	    this.type = type;
	    this.code = code;
	    this.serviceType = serviceType;
	    this.customAppUrl = customAppUrl;
	    this.customHomeMenuUrl = customHomeMenuUrl;
    }
	
    public MenuBo() {
		// TODO Auto-generated constructor stub
	}

	public String getClassName() {
    	return className;
    }

    /**
     * 适配前端UI打开方式；openType=0，target = TARGET_IFRAME;
     * openType=1，target = TARGET_BLANK;
     * @author heyongliang 2015-4-13 下午08:26:16
     * @param openType
     */
	private void setOpenType(int openType) {
		this.openType = openType;
    	switch(openType){
    		case 0:
    			this.target = TARGET_IFRAME;
    			break;
    		case 1:
    			this.target = TARGET_BLANK;
    			break;
    		// 当前页打开，不设置target
    	}
	    
    }

	public long getMenuId() {
    	return menuId;
    }

	
    public long getParentId() {
    	return parentId;
    }

	
    public List<MenuBo> getData() {
    	return data;
    }
	
    
    public int getType() {
    	return type;
    }

	
    public int getOpenType() {
    	return openType;
    }

	public String getName() {
    	return name;
    }

	private boolean isTopMenu(){
		return this.parentId == 0;
	}
    
    public int getActive() {
//    	if(CollectionUtils.isNotEmpty(data)){ // 叶子节点，直接返回选中状态
//    		for(MenuBo menu : data){
//    			if(menu.getActive() == ACTIVE){
//    				return ACTIVE;
//    			}
//    		}
//    	}
    	return active;
    }
    
    public void setData(List<MenuBo> data) {
    	this.data = data;
    }

    public String firstActiveAppHref(){
//    	if(CollectionUtils.isNotEmpty(data)){
//    		MenuBo child = data.get(0);
//    		return firstChildHref(child, true);
//    	}
    	if(isTopMenu()){
    		return null;
    	}else{
    		return this.href;
    	}
    }
    
//    private String firstChildHref(boolean active){
//    	if(CollectionUtils.isNotEmpty(data)){
//			return data.get(0).firstChildHref(active);
//		}
//    	if(active){
//    		this.active = ACTIVE;
//    	}
//    	if(isTopMenu()){
//    		return null;
//    	}else{
//    		return this.href;
//    	}
//    }
    
    private String firstChildHref(MenuBo menu, boolean active){
    	if(menu == null) return null;
//    	if(StringUtils.isNotBlank(menu.href) || CollectionUtils.isEmpty(menu.data)){
//    		if(active) menu.active = ACTIVE;
//    		return menu.href;
//    	}
    	MenuBo child = menu.data.get(0);
    	return firstChildHref(child, active);
    }
    
	public String getHref() {
		// 链接地址不为空，则直接返回链接地址
		if(StringUtils.isNotBlank(this.href)) return this.href;
		//if(this.type == VportalConstants.MENU_APP){ // 应用菜单需要根据打开方式确定链接地址
		// appHref();
		//}
		return firstChildHref(this, false);
    }

	/**
	 * 获取App菜单链接，根据不同的打开方式，生成不同的链接地址
	 * @author heyongliang 2015-4-13 下午08:43:41
	 * @return
	 */
    private String appHref() {
	    /*switch(this.openType){
	    	case VportalConstants.OPENTYPE_LOCAL: // 本地打开类型，由门户跳转，采用门户通用打开方式
	    		return VportalUrlUtil.getMenuUrl(this.menuId);
	    	case VportalConstants.OPENTYPE_NEWWINDOW: // 新窗口打开
	    		if(CollectionUtils.isEmpty(this.data)) { // 无子菜单，则直接返回服务根地址
	    			return this.baseUrl;
	    		}else{
	    			return firstChildHref(this, false);
	    		}
	    	case VportalConstants.OPENTYPE_MAINFRAME: // IFrame中打开，包含平台头部栏
	    		if(CollectionUtils.isEmpty(this.data)) { // 无子菜单，则直接返回服务根地址
	    			return this.baseUrl;
	    		}else{
	    			return firstChildHref(this, false);
	    		}
	    }*/
	    return "";
    }

	public String getTarget() {
    	return target;
    }

	
    public String getImg() {
    	return img;
    }

	
    public void setImg(String img) {
    	this.img = img;
    }

	
    public String getCode() {
    	return code;
    }

	
    public String getServiceType() {
    	return serviceType;
    }

	
    public String getCustomAppUrl() {
    	return customAppUrl;
    }

	
    public String getCustomHomeMenuUrl() {
    	return customHomeMenuUrl;
    }

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public void setCustomAppUrl(String customAppUrl) {
		this.customAppUrl = customAppUrl;
	}

	public void setCustomHomeMenuUrl(String customHomeMenuUrl) {
		this.customHomeMenuUrl = customHomeMenuUrl;
	}
    
    

}
