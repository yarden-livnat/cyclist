package pnnl.cyclist.model.vo;

import java.io.Serializable;

public class ToolInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -993463730154745959L;
	
	public String title;
	public String view;
	public String viewParam;
	public String mediator;
	public String mediatorParam;
	public String icon;
	
	public ToolInfo(String title, String icon, String view, String viewParam, String mediator, String mediatorParam) {
		this.title = title;
		this.icon = icon;
		this.view = view;
		this.viewParam = viewParam;
		this.mediator = mediator;
		this.mediatorParam = mediatorParam;
	}

}
