package pt.pa.adts.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The `SettingsView` class represents a JavaFX component for configuring appearance settings.
 * It includes options for selecting background color, primary color, and secondary color.
 * The appearance settings are organized in a vertical box (`VBox`).
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class SettingsView extends VBox {

    private Button cancel;
    private Button apply;
    private Button restore;
    private ColorPicker backgroundPicker;
    private ColorPicker primaryColorPicker;
    private ColorPicker secondaryColorPicker;
    private ColorSettings colorSettings;
    private PersonalFileSystemView view;

    /**
     * Constructs a SettingsView with the specified ColorSettings and PersonalFileSystemView.
     *
     * @param colorSettings The ColorSettings object to manage color configurations.
     * @param view          The PersonalFileSystemView associated with the color settings.
     */
    public SettingsView(ColorSettings colorSettings,PersonalFileSystemView view) {
        this.colorSettings = colorSettings;
        this.view = view;
        getChildren().addAll(appearanceSection());
    }

    /**
     * Creates and returns the VBox representing the appearance section of the SettingsView.
     * This section includes labels, color pickers, and buttons for adjusting color settings.
     *
     * @return The VBox containing the appearance section components.
     */
    private VBox appearanceSection(){
        VBox section = new VBox();

        setSpacing(10);
        setPadding(new Insets(10));

        Label appearanceLabel = new Label("Appearance");
        appearanceLabel.setStyle("-fx-text-fill: white;");
        appearanceLabel.setFont(Font.font("Nunito", FontWeight.BOLD, 18));

        Line line = new Line();
        line.setStartX(0);
        line.setEndX(400);
        line.setStyle("-fx-stroke: grey;");


        Label backgroundLabel = new Label("Background:");
        backgroundLabel.setStyle("-fx-text-fill: grey;");
        backgroundLabel.setFont(Font.font("Arial", 16));

        Label primaryColorLabel = new Label("Primary Color:");
        primaryColorLabel.setStyle("-fx-text-fill: grey;");
        primaryColorLabel.setFont(Font.font("Arial", 16));

        Label secondaryColorLabel = new Label("Secondary Color:");
        secondaryColorLabel.setStyle("-fx-text-fill: grey;");
        secondaryColorLabel.setFont(Font.font("Arial", 16));

         backgroundPicker = new ColorPicker();
         primaryColorPicker = new ColorPicker();
         secondaryColorPicker = new ColorPicker();

        HBox colorPickersRow = new HBox(10);
        colorPickersRow.getChildren().addAll(
                new VBox(backgroundLabel, backgroundPicker),
                new VBox(primaryColorLabel, primaryColorPicker),
                new VBox(secondaryColorLabel, secondaryColorPicker)
        );
        section.setPrefHeight(455);

        updateColorPickers();

        section.getChildren().addAll(
                appearanceLabel,
                line,
                colorPickersRow,
                buttonSection()
        );

        return section;
    }

    /**
     * Sets up event triggers for the Apply, Restore, and Cancel buttons.
     * The Apply button applies color changes, the Restore button resets to default values,
     * and the Cancel button discards unsaved changes.
     */
    private void setTriggers(){
        apply.setOnMouseClicked(event -> {
            colorSettings.setBackgroundColor(toHexCode(backgroundPicker.getValue()));
            colorSettings.setPrimaryColor(toHexCode(primaryColorPicker.getValue()));
            colorSettings.setSecondaryColor(toHexCode(secondaryColorPicker.getValue()));
            view.reload();
        });

        restore.setOnMouseClicked(event -> {
            colorSettings.setDefault();
            updateColorPickers();
            view.reload();
        });

        cancel.setOnMouseClicked(event -> {
            updateColorPickers();
        });
    }

    /**
     * Updates the color pickers to reflect the current color settings.
     */
    private void updateColorPickers(){
        backgroundPicker.setValue(Color.web(colorSettings.getBackgroundColor()));
        primaryColorPicker.setValue(Color.web(colorSettings.getPrimaryColor()));
        secondaryColorPicker.setValue(Color.web(colorSettings.getSecondaryColor()));
    }

    /**
     * Converts a JavaFX Color object to its hexadecimal representation.
     *
     * @param color The Color object to convert.
     * @return The hexadecimal representation of the color.
     */
    private String toHexCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * Creates and returns the HBox containing Apply, Restore, and Cancel buttons.
     * Each button has a specified style and triggers specific actions when clicked.
     *
     * @return The HBox containing the button components.
     */
    private HBox buttonSection(){
        HBox container = new HBox(10);
        apply = new Button("Apply");
        setButtonStyle(apply);
        restore = new Button("Restore");
        setButtonStyle(restore);
        cancel = new Button("Cancel");
        setButtonStyle(cancel);

        setTriggers();

        container.getChildren().addAll(apply,restore,cancel);
        container.setAlignment(Pos.BOTTOM_RIGHT);

        return container;
    }

    /**
     * Sets the style of the specified button, including background color and hover effects.
     *
     * @param button The Button whose style needs to be set.
     */
    private void setButtonStyle(Button button){
        button.setStyle("-fx-background-color:"+colorSettings.getSecondaryColor() +";-fx-font-family: 'Nunito';" +
                "-fx-font-size: 14;-fx-text-fill:white;");

        button.setOnMouseEntered(event -> button.setStyle("-fx-font-family: 'Nunito';" +
                "-fx-font-size: 14;-fx-text-fill:white;-fx-background-color:#89508a;-fx-content-display: bottom;"));

        button.setOnMouseExited(event -> button.setStyle("-fx-font-family: 'Nunito';" +
                "-fx-font-size: 14;-fx-text-fill:white;-fx-background-color:"+colorSettings.getSecondaryColor()+";-fx-content-display: bottom;"));
        button.setAlignment(Pos.CENTER);
        button.setPrefHeight(25);
        button.setPrefWidth(115);
    }

}
