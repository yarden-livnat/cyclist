/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cyclist;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import cyclist.controller.ApplicationFacade;
import cyclist.view.component.MainScreen;

/**
 *
 * @author yarden
 */
public class Cyclist extends Application {

    public static final String TITLE = "Cyclist";
    static Logger log = Logger.getLogger(Cyclist.class);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * 
     */
    @Override
    public void start(Stage primaryStage) {
//    	PropertyConfigurator.configure("mylog4j.properties");
    	
        MainScreen root = new MainScreen(primaryStage);
        
        /* start facade */
        ApplicationFacade.getInstance().startup(root);
        
        Scene scene = new Scene(root, 800, 600);
        
        scene.getStylesheets().add(Cyclist.class.getResource("assets/Cyclist.css").toExternalForm());
        
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
