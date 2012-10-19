package cyclist.model.proxy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cyclist.model.filter.Param;
import cyclist.model.vo.Details;
import cyclist.model.vo.FilterSet;
import cyclist.model.vo.Details.Type;

public class QueryBuilder {

	private static final String FACILITY_TABLE		= "Facility";
	private static final String TRANSACTIONS_TABLE 	= "Transactions";
	private static final String RESOURCES_TABLE 	= "TransactedResources";
	private static final String HISTORY_TABLE 		= "MaterialHistory";
	private static final String ISOTOPES_TABLE 		= "IsotopicStates";
//	private static final String RESOURCE_TYPE_TABLE = "ResourceTypes";
	private static final String SENDER_TABLE		= "Facility as Sender";
	private static final String RECEIVER_TABLE		= "Facility as Receiver";
//	private static final String ELEMENTS_TABLE		= "Element";
	
			
	private Set<String> _tables = new HashSet<String>();
	private Set<String> _fields = new HashSet<String>();
	
	public QueryBuilder() {
	}
	
	public String create(List<FilterSet> filters, Details details) {
		init();
		
		determineTables(filters);
		determineFields(filters, details);
		String where = determineWhere(filters, details);
		String groups = determineGroups(details);
		
		StringBuilder sb = new StringBuilder();
		
		// select
		sb.append("select ");
		
		// fields
		boolean first = true;
		for (String field : _fields) {
			if (first) first = false;
			else sb.append(", ");
			
			sb.append(field);
			
		}		
		
		// from
		first = true;
		sb.append(" from ");
		for (String table : _tables) {
			if (first) first = false;
			else sb.append(", ");
			
			sb.append(table);
		}
		
		// where
		sb.append(" where ");
		sb.append(where);
		
		// group by
		sb.append(" ");
		sb.append(groups);
		
		return sb.toString();
	}
	
	private void init() {
		
	}
	
	private void determineTables(List<FilterSet> filters) {
		_tables.clear();
		_tables.add(TRANSACTIONS_TABLE);
		_tables.add(RESOURCES_TABLE);

		for (FilterSet set : filters) {
			String table = FACILITY_TABLE;
			switch (set.getType()) {
			case "src":
				table = SENDER_TABLE;
				break;
			case "dest":
				table = RECEIVER_TABLE;
				break;
			}
			
			for (Param.Type key : set.getKeys()) {
				switch (key) {
				case FACILITY:
				case FACILITY_TYPE:
				case INSTITUTE:
				case REGION:
					_tables.add(table);
					break;
				case INSTITUTE_TYPE:
				case MARKET:
					// not supported yet
					break;
				case ELEMENT:
				case ISOTOPE:
					_tables.add(HISTORY_TABLE);
					_tables.add(ISOTOPES_TABLE);
					break;
				case AGENT:
					break;
				case AGENT_TYPE:
					break;
				case MARKET_TYPE:
					break;
				case REGION_TYPE:
					break;
				default:
					break;
				}
			}
		}
	}
	
	private void determineFields(List<FilterSet> filters, Details details) {
		_fields.clear();
		_fields.add("2000+Transactions.time/12 as year");
		_fields.add("sum(quantity) as quantity");

//		for (FilterSet set : filters) {
//			for (Param.Type key : set.getKeys()) {
//				switch (key) {
//				case FACILITY:	
//				case FACILITY_TYPE:
//				case INSTITUTE:
//				case REGION:
//				case INSTITUTE_TYPE:
//				case MARKET:
//				case ELEMENT:
//				case ISOTOPE:
//				case AGENT:
//				case AGENT_TYPE:
//				case MARKET_TYPE:
//				case REGION_TYPE:
//				default:
//					break;
//				}
//			}
//		}
		
		if (details.type != Details.Type.NONE) {
			String table = details.type == Details.Type.SRC ? "Sender" : "Receiver";
			
			switch (details.param) {
			case FACILITY:
				String id = details.type == Details.Type.SRC ? "SenderID" : "ReceiverID";
				_fields.add(id + " as details");
				break;
			case FACILITY_TYPE:
				_fields.add(table+".Type as details");
				break;
			case INSTITUTE:
				_fields.add(table+".Institute as details");
				break;
			case REGION:
				_fields.add(table+".Region as details");
				break;
			case INSTITUTE_TYPE:
				// ignore for now		
				break;
			case REGION_TYPE:
				// ignore
				break;
			case MARKET:
				_fields.add("MarketID as details");
				break;
			case MARKET_TYPE:
				// ignore for now
				break;
			case ELEMENT:
			case ISOTOPE:
				_fields.add("IsoID as details");
				break;
			case AGENT:
				// ignore for now
				break;
			case AGENT_TYPE:
				// ignore for now
				break;
			case NONE:
				// nothing to do
				break;
			}
		}
		 
	}
	
	private String determineWhere(List<FilterSet> filters, Details details) {
		StringBuilder sb = new StringBuilder();
		sb.append("Transactions.ID = TransactedResources.TransactionID ");
		
		boolean needIsotopicStates = false;
		
		for (FilterSet set : filters) {
			if (set.getEntries().size() == 0)  continue;
			if (set.containsKey(Param.Type.ELEMENT) || set.containsKey(Param.Type.ISOTOPE)) {
				needIsotopicStates = true;
				break;
			}
		}
			
		for (FilterSet set : filters) {
			if (set.getEntries().size() == 0)  continue;
			sb.append(" and ");
			sb.append(determineSetWhere(set));
		}
		
		switch (details.param) {
		case FACILITY:
			break;
		case FACILITY_TYPE:
			break;
		case INSTITUTE:
			break;
		case REGION:
			break;
		case INSTITUTE_TYPE:
			// ignore for now		
			break;
		case REGION_TYPE:
			// ignore
			break;
		case MARKET:
			// ignore for now
			break;
		case MARKET_TYPE:
			// ignore for now
			break;
		case ELEMENT:
		case ISOTOPE:
			needIsotopicStates = true;
			break;
		case AGENT:
			// ignore for now
			break;
		case AGENT_TYPE:
			// ignore for now
			break;
		case NONE:
			// nothing to do
			break;
		}
		
		if (needIsotopicStates) {
			sb.append(" and TransactedResources.ResourceID = MaterialHistory.ID ");
			sb.append(" and MaterialHistory.StateID = IsotopicStates.ID ");
			sb.append(" and MaterialHistory.Time = Transactions.Time");
		}
		
		return sb.toString();
	}
	
	private String determineSetWhere(FilterSet set) {
		String table = FACILITY_TABLE;
		StringBuilder sb = new StringBuilder();
		
		switch (set.getType()) {
		case "src":
			table = "Sender";
			sb.append("( "+TRANSACTIONS_TABLE+".SenderId = Sender.ID and (");
			break;
		case "dest":
			table = "Receiver";
			sb.append("( "+TRANSACTIONS_TABLE+".ReceiverId = Receiver.ID and (");
			break;
		}
		
		boolean first = true;
		for (Param.Type key : set.getKeys()) {
			if (first) first = false;
			else sb.append(" or ");
			
			switch (key) {
			case FACILITY:
				createWhere(sb, table,".ID", set.getItems(key));
				break;
			case FACILITY_TYPE:
				createWhere(sb, table,".Type", set.getItems(key));
				break;
			case INSTITUTE:
				createWhere(sb, table,".Institute", set.getItems(key));
				break;
			case REGION:
				createWhere(sb, table,".Region", set.getItems(key));
				break;
			case INSTITUTE_TYPE:
				// ignore for now		
				break;
			case REGION_TYPE:
				// ignore
				break;
			case MARKET:
				createWhere(sb, TRANSACTIONS_TABLE, ".MarketID", set.getItems(key));
				break;
			case MARKET_TYPE:
				// ignore for now
				break;
			case ELEMENT:
				createWhere(sb, "floor("+ISOTOPES_TABLE,".IsoID/10000)", set.getItems(key));
				break;
			case ISOTOPE:
				createWhere(sb, ISOTOPES_TABLE,".IsoID", set.getItems(key));
				break;
			case AGENT:
				// ignore for now
				break;
			case AGENT_TYPE:
				// ignore for now
				break;
			case NONE:
				break;
			default:
				break;
			}
		}
		
		switch (set.getType()) {
		case "src":
		case "dest":
			sb.append("))");
			break;
		default:
			// nothing to do
			break;
		}
		
		return sb.toString();
	}
	
	private void createWhere(StringBuilder sb, String table, String field, List<FilterSet.FilterEntry> items) {
		sb.append(table).append(field);
		
		if (items.size() == 1) 
			sb.append("=").append(escape(items.get(0).value));
		else {
			sb.append(" in (");
			boolean first = true;
			for (FilterSet.FilterEntry item : items) {
				if (first) first = false;
				else sb.append(",");
				
				sb.append(escape(item.value));
			}
			sb.append(")");
		}
	}
	
	private String determineGroups(Details details) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("group by ");
		sb.append("year ");
		
		if (details.type != Type.NONE) {
			String table = details.type == Details.Type.SRC ? SENDER_TABLE : RECEIVER_TABLE;
			switch (details.param) {
			case FACILITY:
				sb.append(", details");
				_tables.add(table);
				break;
			case FACILITY_TYPE:
				sb.append(", details");
				_tables.add(table);
				break;
			case INSTITUTE:
				sb.append(", details");
				_tables.add(table);
				break;
			case REGION:
				sb.append(", details");
				_tables.add(table);;
				break;
			case INSTITUTE_TYPE:
				// ignore for now		
				break;
			case REGION_TYPE:
				// ignore
				break;
			case MARKET:
				// ignore for now
				break;
			case MARKET_TYPE:
				// ignore for now
				break;
			case ELEMENT:
			case ISOTOPE:
				sb.append(", details");
				_tables.add(HISTORY_TABLE);
				_tables.add(ISOTOPES_TABLE);
				break;
			case AGENT:
				// ignore for now
				break;
			case AGENT_TYPE:
				// ignore for now
				break;
			case NONE:
				// nothing to do
				break;
			}
		}
		
		return sb.toString();
	}
	
	private String escape(Object obj) {
		return obj instanceof String ? "'"+(String)obj+"'" : obj.toString(); 
	}
}
