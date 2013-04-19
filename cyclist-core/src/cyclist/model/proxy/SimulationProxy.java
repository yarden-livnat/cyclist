package cyclist.model.proxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import cyclist.controller.ApplicationConstants;
import cyclist.model.filter.Param;
import cyclist.model.filter.Param.Type;
import cyclist.model.vo.Agent;
import cyclist.model.vo.CyclistDataSource;
import cyclist.model.vo.Details;
import cyclist.model.vo.Element;
import cyclist.model.vo.Facility;
import cyclist.model.vo.FilterSet;
import cyclist.model.vo.Isotope;
import cyclist.model.vo.MaterialFlow;
import cyclist.model.vo.SimulationDataStream;

public class SimulationProxy extends Proxy implements SimulationDataStream {
	
	public static final String ALL_AGENTS_QUERY = 
			 "SELECT ID, AgentType, ModelType, Prototype, EnterDate, DeathDate "
			+"  FROM Agents As A JOIN AgentDeaths AS D ON A.ID=D.AgentID";
	
	 public static final String AGENT_QUERY = 
			 "SELECT ID, AgentType, ModelType, Prototype, EnterDate, DeathDate "
			+"  FROM Agents As A JOIN AgentDeaths AS D ON A.ID=D.AgentID"
			+"  where AgentType = ?";
	
	 public static final String ALL_FACILITIES_QUERY = 
			 "select ID, Name, Model, Prototype, institute, region, start, end "
			+"  from Facility ";

	 
	 public static final String ISOTOPE_QUERY = 
			 "select distinct IsoID "
			+"  from IsotopicStates "
			+"  order by IsoID"
			;
	
	
	private CyclistDataSource _ds;
	private ObjectProperty<State> _stateProperty = new SimpleObjectProperty<State>(State.NOT_READY);
	
	
	/**
	 * Constructor
	 * @param name
	 */
	public SimulationProxy(String name) {
		super(name);
	}
	
	public  ObjectProperty<State> stateProperty() {
		return _stateProperty;
	}
	
	public State getState() {
		return _stateProperty.get();
	}
	
	public void setState(State value) {
		_stateProperty.set(value);
	}
	
	public String getDataSourceName() {
		return _ds == null ? "" : _ds.getProperties().getProperty("name");
	}
	
	public boolean isReady() {
		return getState() == State.OK;
	}
	
	
	public SimulationDataStream getDataStream() {
		return this;
	}

	@Override
	public void setDataSource(CyclistDataSource ds) {
		_ds = ds;
		initds();
	}
	
	@Override
	public void onRegister() {
		
	}
	
	public ReadOnlyObjectProperty<ObservableList<Agent>> getAgents(final String agentType) {
		
		Task<ObservableList<Agent>> task = new Task<ObservableList<Agent>>() {
		    @Override protected ObservableList<Agent> call() throws Exception {
		    	List<Agent> list = new ArrayList<>();
				try {
					Connection conn = _ds.getConnection();
					PreparedStatement stmt;
					if (agentType == null) {
						stmt = conn.prepareStatement(ALL_AGENTS_QUERY);
					} else {
						stmt = conn.prepareStatement(AGENT_QUERY);
						stmt.setString(1, agentType);
					}
					
					ResultSet rs = stmt.executeQuery();
					
					while (rs.next()) {
						int id = rs.getInt("ID");
						String type = rs.getString("AgentType");
						String model = rs.getString("ModelType");
						String prototype = rs.getString("Prototype");
						int from = rs.getInt("EnterDate");
						int to = rs.getInt("DeathDate");
						
						System.out.println("id: "+id);
						list.add(new Agent(id, "no name", type, model, prototype, from, to));
					}
					System.out.println("num agents:"+list.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return FXCollections.observableList(list);
		    }
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		return task.valueProperty();
	}
	
	public ReadOnlyObjectProperty<ObservableList<Facility>> getFacilities() {		
			
		Task<ObservableList<Facility>> task = new Task<ObservableList<Facility>>() {
		    @Override protected ObservableList<Facility> call() throws Exception {
		    	List<Facility> list = new ArrayList<>();
				try {
					Connection conn = _ds.getConnection();
					PreparedStatement stmt = conn.prepareStatement(ALL_FACILITIES_QUERY);
					
					ResultSet rs = stmt.executeQuery();
					
					while (rs.next()) {
						int id = rs.getInt("ID");
						String name = rs.getString("name");
						String model = rs.getString("Model");
						String prototype = rs.getString("Prototype");
						String institute = rs.getString("Institute");
						String region = rs.getString("Region");
						int start = rs.getInt("start");
						int end = rs.getInt("end");
						
						list.add(new Facility(id, name, model, prototype, institute, region, start, end));
					}
					System.out.println("num facilities:"+list.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return FXCollections.observableList(list);
		    }
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
			
		return task.valueProperty();
	}
	
	public ReadOnlyObjectProperty<ObservableList<Isotope>> getIsotopes() {
		
		Task<ObservableList<Isotope>> task = new Task<ObservableList<Isotope>>() {
		    @Override protected ObservableList<Isotope> call() throws Exception {
		    	List<Isotope> list = new ArrayList<>();
				try {
					Connection conn = _ds.getConnection();
					PreparedStatement stmt = conn.prepareStatement(ISOTOPE_QUERY);
					
					ResultSet rs = stmt.executeQuery();
					
					while (rs.next()) {
						int code = rs.getInt("IsoID");
						
						list.add(new Isotope(code));
					}
					System.out.println("num isotopes:"+list.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return FXCollections.observableList(list);
		    }
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		return task.valueProperty();
	}
	
	public ReadOnlyObjectProperty<Map<String, List<MaterialFlow>>> getMaterialFlow(final List<FilterSet> filters, final Details details) {
		
		if (!isReady()) return null;
		
		Task<Map<String, List<MaterialFlow>>> task = new Task<Map<String, List<MaterialFlow>>>() {
		    @Override 
		    protected Map<String, List<MaterialFlow>> call() throws Exception {
		    	
		    	Map<String, List<MaterialFlow>> map = new HashMap<>();
		    	
				 try {
					Connection conn = _ds.getConnection();
					QueryBuilder qb = new QueryBuilder();
					String query = qb.create(filters, details);
					System.out.println("Query:\n"+query);
					
					PreparedStatement stmt = conn.prepareStatement(query);
					long t1 =  System.currentTimeMillis();
					
					ResultSet rs = stmt.executeQuery();
					long t2 =  System.currentTimeMillis();
					System.out.println(" execute time: "+(t2-t1)/1000.0+" ms");
					
					List<MaterialFlow> list = new ArrayList<MaterialFlow>();
					if (details.type == Details.Type.NONE) 
						map.put("default", list);
					
					
					while (rs.next()) {
						if (details.type != Details.Type.NONE) {
							String key = rs.getString("details");
							list = map.get(key);
							if (list == null) {
								list = new ArrayList<MaterialFlow>();
								map.put(key, list);
							}
						}

						int year = rs.getInt("year");
						double quantity = rs.getDouble("quantity");
						list.add(new MaterialFlow(year, quantity));
					}
					
					if (details.param == Param.Type.ELEMENT || details.param == Param.Type.ISOTOPE) 
						map = renameEntries(map, details.param);
					
					long t3 =  System.currentTimeMillis();
					System.out.println("flow size:"+list.size());
					System.out.println("time: execute: "+(t2-t1)/1000.0+" ms  retrieve: "+(t3-t2)/1000.0+" ms");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				return map;
		    }
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		return task.valueProperty();
	}
	
	private Map<String, List<MaterialFlow>> renameEntries( Map<String, List<MaterialFlow>> map, Param.Type details) {
		Map<String, List<MaterialFlow>> tmp = new HashMap<String, List<MaterialFlow>> ();

		for (String key : map.keySet()) {
			int num = Integer.valueOf(key);
			String name = Element.valueOf(num/10000).toString();
			if (details == Type.ISOTOPE) {
				name += num/10 % 1000;
			}
			tmp.put(name, map.get(key));
		}

		return tmp;
	}
	
	public static final String FACILITY_TABLE_EXIST = "select count(*) as n from Facility"; // "select name from sqlite_master where name= 'Facility'";
	public static final String DROP_FACILITY_TABLE = "drop table if exists Facility";
	
	public static final String CREATE_FACILITY_TABLE = 
			"create table Facility "
			+"  (id integer not null,"
			+"   name text, "
			+"   model varchar(50),"
			+"   prototype varchar(50),"
			+"   institute integer, "
			+"   region integer, "
			+"   start integer, "
			+"   end integer,"
			+"   primary key (id)"
			+"  );";
	
	public static final String FILL_FACILITY_TABLE = 
			 " insert into Facility (id, name, model, prototype, institute, region, start) "
			+"     select a.id as id, 'none' as name, a.ModelType as model, a.Prototype as prototype, a.parentid as institute, b.parentid as region, a.enterdate as start"
			+"     from agents as a, agents as b "
			+"     where a.AgentType = 'Facility'" 
			+"       and a.parentid = b.id ";
			
	public static final String ELEMENT_TABLE_EXIST = "select name from sqlite_master where name= 'Element'";
	
	public static final String CREATE_ELEMENT_TABLE =
			"create table Element "
			+"   (id integer not null,"
			+"    symbol varchar(2) not null); ";
	
	public static final String FILL_ELEMENT_TABLE = 
			"insert into Element values "
			;
	
	private void initds() {
		final Proxy self = this;
		
		stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == null) newValue = State.NOT_READY;
				switch (newValue) {
				case OK:
					self.sendNotification(ApplicationConstants.DATA_SOURCE_OK, self);
//					self.sendNotification(ApplicationConstants.DATA_AVAILABLE, name);
					break;
				case WARNING:
//					self.sendNotification(ApplicationConstants.DATA_SOURCE_WARNING, name);
					break;
				case ERROR:
//					self.sendNotification(ApplicationConstants.DATA_SOURCE_ERROR, name);
					break;
				case CONNECTING:
//					self.sendNotification(ApplicationConstants.DATA_SOURCE_CONNECTING, name);
					break;
				case NOT_READY:
					// ignore
					break;
				}
			}
		});
		
		Task<State> task = new Task<State>() {
			protected SimulationProxy.State call() throws Exception {
				try (Connection conn = _ds.getConnection();
						 Statement stmt = conn.createStatement())
				{
					
					boolean exist = false;
					try {
						ResultSet rs = stmt.executeQuery(FACILITY_TABLE_EXIST);
						exist = rs.next() && rs.getInt("n") > 1;
					} catch (Exception ignore) {
					} 
					
					if (!exist) {
						System.out.println("create facility table");
						stmt.execute(DROP_FACILITY_TABLE);
						stmt.execute(CREATE_FACILITY_TABLE);
						stmt.execute(FILL_FACILITY_TABLE);
					} 
					System.out.println("init done");
					return SimulationProxy.State.OK;
						
					} catch (SQLException e)  {				
						// report error
						e.printStackTrace();
						return SimulationProxy.State.ERROR;
					} 
				
			}
		};
		
		 
		
		stateProperty().bind(new When(task.valueProperty().isNull())
			.then(new SimpleObjectProperty<State>(State.CONNECTING))
			.otherwise(task.valueProperty()));
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
}
