package pnnl.cyclist.model.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import pnnl.cyclist.view.tool.Tool;


public class ToolsService {

	private static ToolsService instance = null;
	
	private ServiceLoader<Tool> loader;
	
	public static ToolsService getInstance() {
		if (instance == null) {
			instance = new ToolsService();
		}
		return instance;
	}
	
	private ToolsService() {
		loader = ServiceLoader.load(Tool.class);
	}
	
	public List<Tool> getTools() {
		List<Tool> list = new ArrayList<>();
		
		try {
			for (Tool tool : loader)
				list.add(tool);
		} catch (ServiceConfigurationError e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Tool getTool(String name) {
		try {
			for (Tool provider : loader) {
				if (provider.getName().equals(name)) { 
					return provider;		
				}
			}
		} catch (ServiceConfigurationError e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
