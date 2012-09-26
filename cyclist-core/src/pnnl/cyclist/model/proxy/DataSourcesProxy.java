package pnnl.cyclist.model.proxy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.puremvc.java.multicore.patterns.proxy.Proxy;

import pnnl.cyclist.CyclistNames;
import pnnl.cyclist.controller.ApplicationConstants;
import pnnl.cyclist.model.vo.CyclistDataSource;


public class DataSourcesProxy extends Proxy {	
	static Logger log = Logger.getLogger(DataSourcesProxy.class);
	
	private Map<String, CyclistDataSource> _dataSources = new HashMap<>();
	private CyclistDataStream _defaultDataStream = null;
	
	public DataSourcesProxy() {
		super(CyclistNames.DATA_SOURCES_PROXY);
		
		init();
	}

	public String[] getDataSourcesNames() {
		String[] array = _dataSources.keySet().toArray(new String[0]);
		Arrays.sort(array);
		return array;
	}
	
	public CyclistDataSource[] getDataSources() {
		String[] names = _dataSources.keySet().toArray(new String[0]);
		Arrays.sort(names);
		
		CyclistDataSource[] values = new CyclistDataSource[names.length]; 
		for (int i=0; i<names.length; i++) {
			values[i] = _dataSources.get(names[i]);
		}
		return values;
	}
	
	public void addDataSource(CyclistDataSource ds) {
		String name = ds.getProperties().getProperty("name");
		if (name != null && !name.equals("")) 
			addDataSource(name, ds);
	}
	
	public void addDataSource(String name, CyclistDataSource ds) {
		_dataSources.put(name, ds);
		store(name, ds);
		sendNotification(ApplicationConstants.DATA_SOURCE_ADDED, ds);
	}
	
	public void removeDataSource(String name) {
		CyclistDataSource ds = _dataSources.get(name);
		if (ds == null) {
			log.warn("Data source '"+name+"' does not exist. (while trying to delete data source");
		} else {
			delete(name);
		}
	}
	
	public void updateDataSource(CyclistDataSource ds) {
		store(ds.getProperties().getProperty("name"), ds);
	}
	
	public CyclistDataSource getDataSource(String name) {
		return _dataSources.get(name);
	}
	
	public CyclistDataStream getCyclistDataStream(String name) {
		String proxyName = name+"-"+CyclistNames.CYCLIST_PROXY;
		DBProxy proxy = (DBProxy) getFacade().retrieveProxy(proxyName);
		if (proxy == null) {
			CyclistDataSource ds = getDataSource(name);
			if (ds != null) {
				// temporary hack
//				sqliteHack(ds);
				
				// TODO: need a factory to create the correct proxy based on the name
				// In Cyclist it was SimulationProy while in Weather it is WeatherProxy
				proxy = new WeatherProxy(proxyName);
				getFacade().registerProxy(proxy);
				proxy.setDataSource(ds);
			}
		}
		
		return proxy != null ? proxy.getDataStream() : null;
	}
	
//	public SimulationDataStream getSimulationDataStread(String name) {
//		String proxyName = name+"-"+CyclistNames.SIMULATION_PROXY;
//		SimulationProxy proxy = (SimulationProxy) getFacade().retrieveProxy(proxyName);
//		if (proxy == null) {
//			CyclistDataSource ds = getDataSource(name);
//			if (ds != null) {
//				// temporary hack
//				sqliteHack(ds);
//				
//				proxy = new SimulationProxy(proxyName);
//				getFacade().registerProxy(proxy);
//				proxy.setDataSource(ds);
//			}
//		}
//		
//		return proxy != null ? proxy.getDataStream() : null;
//	}
	
	private void init() {
		CyclistDataSource ds;
		
		Preferences pkgPref = Preferences.userNodeForPackage(DataSourcesProxy.class);
		Preferences pref = pkgPref.node("data-sources");
	
		if (pref == null) {
			log.warn("Can not find data-sources preferences.");
			return;
		}
	
		log.debug("DataSources:");
		try {
			for (String name : pref.childrenNames()) {
				log.debug("\t"+name);
				Preferences dsPref = pref.node(name);
				ds = new CyclistDataSource();
				Properties prop = ds.getProperties();
				for (String key : dsPref.keys()) {
					if (key.equals("url"))
						ds.setURL(dsPref.get(key, ""));
					else 
						prop.setProperty(key, dsPref.get(key, ""));
				}
				_dataSources.put(name, ds);
			}
		} catch (BackingStoreException e) {
			log.warn("Unavailable BackingStor", e);
		}

	}

	public CyclistDataStream getDefaultDataStream() {
		return _defaultDataStream;
	}
	
	public void setDefaultDataStream(CyclistDataStream stream) {
		_defaultDataStream = stream;
	}
	
	private void store(String name, CyclistDataSource ds) {
		Preferences pkgPref = Preferences.userNodeForPackage(DataSourcesProxy.class);
		Preferences pref = pkgPref.node("data-sources/"+name);
		if (pref == null) {
			log.warn("Can not find data-sources preferences. Data source ["+name+"] not stored");
			return;
		}
		
		pref.put("url", ds.getURL());
		Properties prop = ds.getProperties();
		for (String key : prop.stringPropertyNames()) {
			pref.put(key, prop.getProperty(key));
		}
	}
	
	private void delete(String name) {
		Preferences pkgPref = Preferences.userNodeForPackage(DataSourcesProxy.class);
		Preferences pref = pkgPref.node("data-sources/"+name);
		try {
			pref.removeNode();
		} catch (BackingStoreException e) {
			log.warn("Can not remove data source", e);
		};
	}
	
}
