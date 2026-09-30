package pt.pa.adts.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.pa.adts.controller.PersonalFileSystemController;
import pt.pa.adts.observer.Observable;
import pt.pa.adts.observer.Observer;

/**
 * An interface for a file system user interface.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public interface FileSystemUI extends Observer {

    /**
     * Displays an error message.
     * @param msg The error message to be displayed.
     */
    void displayError(String msg);

    /**
     * Clears the displayed error message.
     */
    void clearError();

    /**
     * Clears the controls in the user interface.
     */
    void clearControls();

    /**
     * Sets triggers for a PersonalFileSystemController.
     * @param controller The controller to set triggers for.
     */
    void setTriggers(PersonalFileSystemController controller);
}
