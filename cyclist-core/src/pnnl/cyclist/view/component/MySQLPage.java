package pnnl.cyclist.view.component;

import pnnl.cyclist.model.vo.CyclistDataSource;

public class MySQLPage extends DatabasePage {

	public MySQLPage(CyclistDataSource ds) {
		super(ds);
	}
	
	@Override
	protected void init() {
		super.init();
		_driver = "mysql";
		_type = "MySQL";
		if (_port.getText() == null || _port.getText().equals(""))
			_port.setText("3306");
	}
}
