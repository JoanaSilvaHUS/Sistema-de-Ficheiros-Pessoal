package pt.pa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.adts.controller.PersonalFileSystemController;
import pt.pa.adts.model.*;
import pt.pa.adts.view.PersonalFileSystemView;

import java.io.*;

/**
 * Main class
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class Main extends Application {

    @Override
    public void start(Stage ignored){

        PersonalFileSystem model = PersonalFileSystem.createModel();
        PersonalFileSystemView view = new PersonalFileSystemView(model);
        PersonalFileSystemController controller = new PersonalFileSystemController(view, model);

        Scene scene = new Scene(view, 900, 600);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Personal File System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
        controller.execute();
       stage.setOnCloseRequest(event -> {
            controller.saveToFile();
        });
    }

    /**
     * The default entry point of the application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


}
