package pnnl.cyclist.model.proxy;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pnnl.cyclist.model.vo.Intersect;
import pnnl.cyclist.model.vo.MonthDegreeDay;
import pnnl.cyclist.model.vo.Node;
import pnnl.cyclist.model.vo.NodeType;
import pnnl.cyclist.model.vo.Station;
import pnnl.cyclist.model.vo.Weather;
import pnnl.cyclist.model.vo.WeatherData;
import pnnl.cyclist.model.vo.World;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.util.Pair;


public class WeatherProxy extends DBProxy implements WeatherDataStream {
	
	public static final String FETCH_NODE_TYPES 		= "select * from regional_building_groups";
	public static final String FETCH_NODES 				= "select * from nodes";
	public static final String FETCH_STATIONS 			= "select * from stations"; 
	public static final String FETCH_INTERSECTS			= "select * from intersects";
	public static final String FETCH_WEATHER_AT_TIME 	= "select * from weather where time_id = ?";
	public static final String FETCH_DEGREE_DAYS		= 
			" select intersect_id, month, sum(if (t>0, t, 0)) as hdd, sum(if (t<0, t, 0)) as cdd , " +
			"       sum(if (t>0,1,0)) as h_days, sum(if (t>0,0,1)) as c_days " +
			" from " +
			"    (select intersect_id, simulation_day, month, 18-avg(dry_bulb) as t from weather, time_table" +
			"      where weather.time_id = time_table.id " +
			"      group by intersect_id, month, simulation_day" +
			"    ) as tmp " +
			" group by intersect_id, month";
	
	private ObjectProperty<World> _world; 
	private Map<Integer, ReadOnlyObjectProperty<Weather>> _weatherMap = new HashMap<>(); // time_id
	private ObjectProperty<Collection<MonthDegreeDay>> _degreeDays;
		
	public WeatherProxy(String name) {
		super(name);
	}
	
	public ReadOnlyObjectProperty<World> getWorld() {
		if (_world == null) {
			_world = new SimpleObjectProperty<>();
			
			Task<World> task = new Task<World>() {
	
				@Override
				protected World call()  {
					Map<Integer, Station> stations = new HashMap<>();
					Map<Integer, Node> nodes = new HashMap<>();
					Map<Integer, NodeType> nodeTypes = new HashMap<>();
					Map<Integer, Intersect> intersects = new HashMap<>();
					
					try (Connection conn = _ds.getConnection())
					{
						// load node types
						PreparedStatement stmt = conn.prepareStatement(FETCH_NODE_TYPES);
								
						ResultSet rs = stmt.executeQuery();
						while (rs.next()) {
							NodeType type = new NodeType();
							type.setId( rs.getInt("id"));
							type.setCensusReg(rs.getString("census_reg"));
							type.setEIAZone(rs.getInt("EIA_zone"));
							
							nodeTypes.put(type.getId(), type);
						}
						
						// load intersects
						List<Pair<Integer, Integer>> intersectToStation = new ArrayList<>();
						
						stmt = conn.prepareStatement(FETCH_INTERSECTS);
						rs = stmt.executeQuery();
						while (rs.next()) {
							Intersect intersect = new Intersect();
							intersect.setId( rs.getInt("id"));
							intersectToStation.add( new Pair<Integer, Integer>( intersect.getId(), rs.getInt("station_id"))); 
							intersect.setTimezone( rs.getString("timezone"));
							
							intersects.put(intersect.getId(), intersect);
						}
						
						// load stations
						List<Pair<Integer, Integer>> stationToLocalNode = new ArrayList<>();
						
						stmt = conn.prepareStatement(FETCH_STATIONS);
						rs = stmt.executeQuery();
						while (rs.next()) {
							Station station = new Station();
							station.setId(rs.getInt("id"));
							station.setName(rs.getString("name"));
							station.setState(rs.getString("state"));
							station.setLat(rs.getDouble("lat"));
							station.setLon(rs.getDouble("lon"));
							station.setElevation(rs.getInt("elevation"));
							
							stationToLocalNode.add(new Pair<Integer, Integer>(station.getId(), rs.getInt("local_node_id")));
							stations.put(station.getId(), station);
						}

						// load nodes
						stmt = conn.prepareStatement(FETCH_NODES);
						rs = stmt.executeQuery();
						while (rs.next()) {
							Node node = new Node();
							node.setId(rs.getInt("id"));
							node.setX(rs.getDouble("x"));
							node.setY(rs.getDouble("y"));
							
							node.setState(rs.getString("state"));
							node.setCounty(rs.getString("county"));

							Intersect intersect = intersects.get(rs.getInt("intersect_id"));
							node.setIntersect(intersect);
							intersect.addNode(node);
							
//							Station station = stations.get(intersect.getStation());
//							station.addNode(node);
							
							nodes.put(node.getId(), node);
						}
		
						// associate stations and local nodes
						for (Pair<Integer, Integer> pair : stationToLocalNode) {
							stations.get(pair.getKey()).setLocalNode( nodes.get(pair.getValue()));
						}
						
						// associate the stations and intersects
						for (Pair<Integer, Integer> pair : intersectToStation) {
							Intersect intersect = intersects.get(pair.getKey());
							Station station = stations.get(pair.getValue());
							intersect.setStation(station);
							for (Node node : intersect.getNodes()) {
								station.addNode(node);
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					} 
					
					World world = new World();
					world.setStations(stations);
					world.setNodes(nodes);
					world.setNodeTypes(nodeTypes);
					world.setIntersects(intersects);
					
					return world;
				}	
			};
			
			Thread th = new Thread(task);
			th.start();
			
			_world.bind(task.valueProperty());
		}
		return _world;
	}

	public ReadOnlyObjectProperty<Weather> getWeather(final int timeId) {
		if (_weatherMap.get(timeId) != null) {
			return _weatherMap.get(timeId);
		}
		
		_weatherMap.put(timeId, new SimpleObjectProperty<Weather>());
		
		Task<Weather> task = new Task<Weather>() {

			@Override
			protected Weather call() throws Exception {
				Weather weather = new Weather();
				weather.setTimeId(timeId);
				
				World world = _world.get();
				
				System.out.println("get weather at "+timeId);
				try (Connection conn = _ds.getConnection())
				{
					PreparedStatement  stmt = conn.prepareStatement(FETCH_WEATHER_AT_TIME);
					stmt.setInt(1, timeId);
					
					ResultSet rs = stmt.executeQuery();
					
					while (rs.next()) {
						Date date = rs.getDate("time");
						int timeId = rs.getInt("time_id");
						int intersect_id = rs.getInt("intersect_id"); 
						
						double dry = rs.getDouble("dry_bulb");
						double dew = rs.getDouble("dew_point");
						int humidity = rs.getInt("relative_humidity");
						int atmospheric = rs.getInt("atmospheric_station");
						int hi = rs.getInt("h_i_radiation");
						int dn = rs.getInt("d_n_radiation");
						int dh = rs.getInt("d_h_radiation");
						int direction = rs.getInt("wind_direction");
						double speed = rs.getDouble("wind_speed");
						int cover = rs.getInt("opaque_sky_cover");
						
						Intersect intersect = world.getIntersect(intersect_id);
;
						
						WeatherData data = new WeatherData(date, timeId, intersect,
								dry, dew, humidity, atmospheric, hi, dn, dh, direction, speed, cover);
						
						weather.addData(data);
//						System.out.println("station "+stationId);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("fetched "+weather.getData().size()+" measurements");
				return weather;
			}
		};
		
		Thread th = new Thread(task);
		th.run();
		
		_weatherMap.put(timeId, task.valueProperty());
		return task.valueProperty();
	}
	
	public ReadOnlyObjectProperty<Collection<MonthDegreeDay>> getDegreeDays() {
		if (_degreeDays == null) {
			_degreeDays = new SimpleObjectProperty<>();
			
			Task<Collection<MonthDegreeDay>> task = new Task<Collection<MonthDegreeDay>>() {
	
				@Override
				protected Collection<MonthDegreeDay> call() throws Exception {
					Map<Integer, MonthDegreeDay> map = new HashMap<>();
					
					try (Connection conn = _ds.getConnection())
					{
						PreparedStatement  stmt = conn.prepareStatement(FETCH_DEGREE_DAYS);
						ResultSet rs = stmt.executeQuery();
						
						while (rs.next()) {
							int intersectId = rs.getInt("intersect_id");
							int month = rs.getInt("month")-1;
							double hdd = rs.getDouble("hdd");
							double cdd = rs.getDouble("cdd");
							int hddDays = rs.getInt("h_days");
							int cddDays = rs.getInt("c_days");
							
							System.out.println(intersectId+": "+month+"  hdd:"+hdd+"  cdd:"+cdd);
							MonthDegreeDay entry = map.get(intersectId);
							if (entry == null) {
								entry = new MonthDegreeDay();
								entry.setIntersect(_world.get().getIntersect(intersectId));
								map.put(intersectId, entry);
							}
							entry.setHDD(hdd, month);
							entry.setCDD(cdd, month);
							entry.setHDDCount(hddDays, month);
							entry.setCDDCount(cddDays, month);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					System.out.println("fetched "+map.size()+" dd");
					return map.values();
				}
				
			};
			
			Thread th = new Thread(task);
			th.run();
			
			_degreeDays.bind(task.valueProperty());
		}
		return _degreeDays;
	}

}
