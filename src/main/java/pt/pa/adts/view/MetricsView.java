package pt.pa.adts.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.shape.Line;
import pt.pa.adts.model.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * A JavaFX view class for displaying file system metrics and charts.
 * This class extends VBox and includes buttons for various file system actions,
 * as well as charts for visualizing file system data.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class MetricsView extends VBox {

    private final PersonalFileSystem fileSystem;
    private Button descendantsButton;
    private Button lastDirectoriesCreatedBtn;
    private Button lastFilesCreatedBtn;
    private Button lastFilesModifiedBtn;
    private Button modificationsToFiles;
    private Button monthlyCreatedFilesBtn;
    private Button monthlyModifiedFilesBtn;
    private Button topDirectories;
    private final ColorSettings colorSettings;
    private static final int LINE_SIZE = 455;

    /**
     * Constructs a MetricsView with the specified PersonalFileSystem model and color settings.
     *
     * @param model          The PersonalFileSystem model to be associated with this view.
     * @param colorSettings  The color settings to be applied to the MetricsView.
     */
    public MetricsView(PersonalFileSystem model, ColorSettings colorSettings) {
        super(10);
        this.colorSettings = colorSettings;
        this.fileSystem = model;
        setPadding(new Insets(10));

        initializeViewButtons();
        addButtonsToViewLayout();
    }

    /**
     * Initializes the view buttons used in the application. Each button is created with a specific label
     * and associated action to perform when clicked.
     */
    private void initializeViewButtons() {
        descendantsButton = createButton("Show details of a specific directory (L1, E2, E3, E4)", event -> showSelectDirectoryAlert());
        lastDirectoriesCreatedBtn = createButton("Show last 20 directories created (L3)", event -> showLastDirectoriesCreatedAlert());
        modificationsToFiles = createButton("Total modifications to files (L2)", event -> showTotalModifiedFilesAlert());
        lastFilesCreatedBtn = createButton("Show last 20 files created (L3)", event -> showLastFilesCreatedAlert());
        lastFilesModifiedBtn = createButton("Show last 10 modified files (L4)", event -> showLastModifiedFilesAlert());
        monthlyCreatedFilesBtn = createButton("Show monthly created files/directories by selected year (G1)", event -> showMonthlyCreatedFilesByYearGraph());
        monthlyModifiedFilesBtn = createButton("Show monthly modified files/directories by selected year (G2)", event -> showMonthlyModifiedFilesChartByYearGraph());
        topDirectories = createButton("Top 5 directories with more descendants (E4)", event -> showTop5DirectoriesAlert());

        setButtonStyle(descendantsButton);
        setButtonStyle(lastDirectoriesCreatedBtn);
        setButtonStyle(modificationsToFiles);
        setButtonStyle(lastFilesCreatedBtn);
        setButtonStyle(lastFilesModifiedBtn);
        setButtonStyle(monthlyCreatedFilesBtn);
        setButtonStyle(monthlyModifiedFilesBtn);
        setButtonStyle(topDirectories);
    }

    /**
     * Adds the initialized view buttons and separator lines to the view layout. The buttons are added to
     * the layout in a specific order with separator lines between them.
     */
    private void addButtonsToViewLayout() {
        getChildren().addAll(
                descendantsButton, createLine(), modificationsToFiles,
                createLine(), lastDirectoriesCreatedBtn, createLine(),
                lastFilesCreatedBtn, createLine(), lastFilesModifiedBtn,
                createLine(), monthlyCreatedFilesBtn, createLine(), monthlyModifiedFilesBtn,
                createLine(), topDirectories
        );
    }

    //MÉTODOS PRINCIPAIS
    //ALERTS

    /**
     * Displays an alert for selecting a directory. If there are directories available,
     * a list of directories is shown along with corresponding action buttons.
     */
    public void showSelectDirectoryAlert() {
        Alert alert = createInformationAlert("Select directory");
        List<FileSystemItem> directories = fileSystem.getDirectories();

        if (!directories.isEmpty()) {
            ListView<FileSystemItem> directoryListView = createSelectDirectoryListView(directories);
            HBox panel = createSelectDirectoryListViewHbox(directoryListView);
            VBox buttonsBox = createSelectDirectoryButtons(directoryListView);

            configureSelectDirectoryButtonActions(directoryListView, buttonsBox);

            alert.getDialogPane().setContent(new HBox(panel, buttonsBox));
        }

        configureCloseButton(alert);
        alert.showAndWait();
    }

    /**
     * Displays an alert with a list of files inside the selected directory.
     *
     * @param selectedDirectory The directory for which to display the list of files.
     */
    public void showFilesAlert(FileSystemItem selectedDirectory) {
        Alert descendantsAlert = createInformationAlert("List of files inside " + selectedDirectory.getName());

        Label label = createLabel("List of files inside " + selectedDirectory.getName());
        TextArea textArea = createTextArea();

        int count = showInformation("files", selectedDirectory, textArea);

        showTotalItemsMessage("files", count, textArea);

        VBox panel = createPanel(label, textArea);
        descendantsAlert.setGraphic(panel);

        configureCloseButton(descendantsAlert);
        showStyledAlert(descendantsAlert);
    }

    /**
     * Displays an alert with a list of directories inside the selected directory.
     *
     * @param selectedDirectory The directory for which to display the list of directories.
     */
    public void showDirectoriesAlert(FileSystemItem selectedDirectory) {
        Alert descendantsAlert = createInformationAlert("List of directories inside " + selectedDirectory.getName());

        Label label1 = createLabel("List of directories inside " + selectedDirectory.getName());

        TextArea textArea = createTextArea();

        int count = showInformation("directories", selectedDirectory, textArea);

        showTotalItemsMessage("directories", count, textArea);

        configureCloseButton(descendantsAlert);

        VBox panel = createPanel(label1, textArea);
        descendantsAlert.setGraphic(panel);

        showStyledAlert(descendantsAlert);
    }

    /**
     * Displays an alert with a list of files and directories inside the selected directory.
     *
     * @param selectedDirectory The directory for which to display the list of files and directories.
     */
    public void showBothAlert(FileSystemItem selectedDirectory) {
        Alert descendantsAlert = createInformationAlert("List of files and directories inside " + selectedDirectory.getName());

        Label label1 = createLabel("List of files and directories inside " + selectedDirectory.getName());
        TextArea textArea = createTextArea();

        int count = showInformation("all", selectedDirectory, textArea);

        showTotalItemsMessage("files and directories", count, textArea);

        configureCloseButton(descendantsAlert);

        VBox panel = createPanel(label1, textArea);
        descendantsAlert.setGraphic(panel);

        showStyledAlert(descendantsAlert);
    }

    /**
     * Displays an alert with the top 5 directories having the most descendants in the file system.
     */
    public void showTop5DirectoriesAlert() {
        Alert alert = createInformationAlert("Top 5 directories with more descendants");

        Label label1 = createLabel("Top 5 directories with more descendants");
        TextArea textArea = createTextArea();

        HashMap<String, Integer> directoryNdescendants = new HashMap<>();

        textArea.appendText("----------------------------------------\n");
        fileSystem.getDirectories().forEach(f -> directoryNdescendants.put(f.getName(), fileSystem.countDescendants(f)));

        textArea.appendText(directoryNdescendants.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> entry.getKey() + ": " + entry.getValue() + " descendants")
                .collect(Collectors.joining("\n")));
        textArea.appendText("\n----------------------------------------");

        // Set the content of the Alert to the TextArea
        VBox panel = createPanel(label1, textArea);
        alert.setGraphic(panel);

        configureCloseButton(alert);
        showStyledAlert(alert);
    }

    /**
     * Displays an alert showing the top 5 directories with the most descendants inside the specified directory.
     *
     * @param directory The directory for which to display the top 5 directories.
     */
    public void showTop5DirectoriesAlert(FileSystemItem directory) {
        Alert alert = createInformationAlert("Top 5 directories with more descendants");

        Label label1 = createLabel("Top 5 directories inside " + directory.getName());
        TextArea textArea = createTextArea();

        AtomicInteger count = new AtomicInteger(0);
        HashMap<String, Integer> directoryNdescendants = new HashMap<>();

        fileSystem.findChildren(directory).stream()
                .filter(position -> position.element().isDirectory())
                .forEach(position -> {
                    count.incrementAndGet();
                    directoryNdescendants.put(position.element().getName(), fileSystem.countDescendants(position.element()));
                });

        int finalCount = count.get();
        textArea.appendText("----------------------------------------\n");
        if (finalCount == 0) {
            textArea.appendText("No directories found within this directory");
        } else {
            textArea.appendText(directoryNdescendants.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(5)
                    .map(entry -> entry.getKey() + ": " + entry.getValue() + " descendants")
                    .collect(Collectors.joining("\n")));
        }
        textArea.appendText("\n----------------------------------------");

        // Set the content of the Alert to the TextArea
        VBox panel = createPanel(label1, textArea);
        alert.setGraphic(panel);

        configureCloseButton(alert);
        showStyledAlert(alert);
    }

    /**
     * Displays an alert with the depth of a selected directory in the file system.
     *
     * @param f The directory for which to calculate the depth.
     */
    public void showDepthAlert(FileSystemItem f) {
        Alert alert = createInformationAlert("Depth");

        Label label1 = createLabel("The depth of directory " + f.getName() + " is: \n");

        TextArea textArea = createTextArea();
        textArea.appendText("----------------------------------------\n");
        textArea.appendText("Depth: " + fileSystem.getFileSystem().getDepth(fileSystem.findPosition(f)));

        VBox panel = createPanel(label1, textArea);

        configureCloseButton(alert);

        alert.setGraphic(panel);
        showStyledAlert(alert);
    }

    /**
     * Shows an alert with information about the last 20 created directories.
     */
    public void showLastDirectoriesCreatedAlert() {
        Alert lastDirectoriesCreated = createInformationAlert("List of last 20 created directories");

        Label label1 = createLabel("List of last 20 created directories:");

        TextArea textArea = createTextArea();
        textArea.appendText("----------------------------------------\n");

        List<FileSystemItem> last20Items = fileSystem.getDirectories().stream()
                .sorted(Comparator.comparing(FileSystemItem::getCreationDate).reversed())
                .limit(20)
                .toList();

        // Append content to TextArea
        if (last20Items.isEmpty()) {
            textArea.appendText("NO DIRECTORIES CREATED");
        } else {
            for (FileSystemItem item : last20Items) {
                String creationDateFormatted = item.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                textArea.appendText(item.getName() + " - " + creationDateFormatted + "\n");
            }
        }

        VBox panel = createPanel(label1, textArea);
        configureCloseButton(lastDirectoriesCreated);

        lastDirectoriesCreated.setGraphic(panel);
        showStyledAlert(lastDirectoriesCreated);
    }

    /**
     * Shows an alert with information about the last 20 created files.
     */
    public void showLastFilesCreatedAlert() {
        Alert lastFilesCreated = createInformationAlert("List of last 20 created files");

        Label label1 = createLabel("List of last 20 created files:");

        TextArea textArea = createTextArea();
        textArea.appendText("----------------------------------------\n");

        List<FileSystemItem> files = fileSystem.getFiles();

        List<FileSystemItem> last20Items = files.stream()
                .sorted(Comparator.comparing(FileSystemItem::getCreationDate).reversed())
                .limit(20)
                .toList();

        // Append content to TextArea
        if (files.isEmpty()) {
            textArea.appendText("NO FILES CREATED");
        } else {
            for (FileSystemItem item : last20Items) {
                String creationDateFormatted = item.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                textArea.appendText(item.getName() + " - " + creationDateFormatted + "\n");
            }
        }

        // Make the TextArea scrollable
        textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox panel = createPanel(label1, textArea);
        configureCloseButton(lastFilesCreated);

        lastFilesCreated.setGraphic(panel);
        showStyledAlert(lastFilesCreated);
    }

    /**
     * Shows an alert with information about the last 10 modified files.
     */
    public void showLastModifiedFilesAlert() {
        Alert lastModifiedFilesAlert = createInformationAlert("List of the last 10 modified files");

        Label label1 = createLabel("List of the last 10 modified files:");

        TextArea textArea = createTextArea();
        textArea.appendText("----------------------------------------\n");

        List<FileSystemItem> files = fileSystem.getFiles();

        List<FileSystemItem> last10Items = files.stream()
                .sorted(Comparator.comparing(FileSystemItem::getLastModificationDate).reversed())
                .limit(10)
                .toList();

        // Append content to TextArea
        if (last10Items.isEmpty()) {
            textArea.appendText("NO FILES MODIFIED");
        } else {
            for (FileSystemItem item : last10Items) {
                String lastModificationDateFormatted = item.getLastModificationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                textArea.appendText(item.getName() + " - " + lastModificationDateFormatted + "\n");
            }
        }

        // Make the TextArea scrollable
        textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox panel = createPanel(label1, textArea);
        configureCloseButton(lastModifiedFilesAlert);

        lastModifiedFilesAlert.setGraphic(panel);
        showStyledAlert(lastModifiedFilesAlert);
    }

    /**
     * Shows an alert with information about the total number of modified files
     * and the count of modifications for each file.
     */
    public void showTotalModifiedFilesAlert() {
        Alert totalModifiedFilesAlert = createInformationAlert("Total Modified files");

        Label label1 = createLabel("Number of modifications by the number of files\nmodified that amount of times:");

        TextArea textArea = createTextArea();
        textArea.appendText("----------------------------------------\n");

        // Create a Map to store the count of modifications for each file
        Map<Integer, List<FileSystemItem>> modificationsCountMap = new HashMap<>();

        fileSystem.getFiles().forEach(file -> {
            int modifications = file.getChangeCount();
            // Update the Map based on the count of modifications
            modificationsCountMap.computeIfAbsent(modifications, k -> new ArrayList<>()).add(file);
        });

        // Sort the Map by the number of modifications in descending order
        List<Map.Entry<Integer, List<FileSystemItem>>> sortedEntries = modificationsCountMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, List<FileSystemItem>>comparingByKey().reversed())
                .toList();

        // Append content to TextArea
        sortedEntries.forEach(entry -> {
            int modifications = entry.getKey();
            List<FileSystemItem> files = entry.getValue();
            int numberOfFiles = files.size();
            textArea.appendText("Modifications: " + modifications + " - Number of Files: " + numberOfFiles + "\n");
        });

        // Make the TextArea scrollable
        textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox panel = createPanel(label1, textArea);
        configureCloseButton(totalModifiedFilesAlert);

        totalModifiedFilesAlert.setGraphic(panel);
        showStyledAlert(totalModifiedFilesAlert);
    }

    // graficos

    /**
     * Creates a bar chart with a specified title, showing the number of files and directories created or modified each month.
     *
     * @param title The title of the bar chart.
     * @return A BarChart instance displaying monthly statistics.
     */
    private BarChart<String, Number> createBarChart(String title) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
        xAxis.setLabel("Month");
        yAxis.setLabel("Number of Files + directories");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        barChart.getData().add(series);

        return barChart;
    }

    /**
     * Creates a ComboBox for selecting a year with default values from the current year to the next five years.
     *
     * @return A ComboBox instance for selecting a year.
     */
    private ComboBox<String> createYearSelector() {
        ObservableList<String> years = FXCollections.observableArrayList(
                "2022", "2023", "2024", "2025" // Add the necessary years
        );

        ComboBox<String> yearSelector = new ComboBox<>(years);
        yearSelector.setValue("2022"); // Set default value
        return yearSelector;
    }

    /**
     * Creates an alert dialog containing a year selector ComboBox and a bar chart for displaying monthly statistics.
     * The chart is styled with white text color.
     *
     * @param title The title of the alert dialog and the bar chart.
     * @return An Alert instance containing the year selector and the styled bar chart.
     */
    private Alert createGraphAlert(String title) {
        Alert alert = createInformationAlert("");

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(createYearSelector(), createBarChart(title));

        BarChart<String, Number> chart = (BarChart<String, Number>) vBox.lookup(".chart");
        if (chart != null) {
            chart.lookup(".chart-title").setStyle("-fx-text-fill: white;");
            chart.lookup(".axis-label").setStyle("-fx-text-fill: white;");
            chart.getYAxis().lookup(".axis-label").setStyle("-fx-text-fill: white;");
        }

        alert.setGraphic(vBox);
        return alert;
    }

    /**
     * Displays a monthly chart for files and directories statistics by year, allowing the user to select a year.
     *
     * @param title          The title of the chart and the alert dialog.
     * @param updateFunction A BiConsumer for updating the chart based on the selected year.
     */
    private void showMonthlyChartByYear(String title, BiConsumer<Integer, BarChart<String, Number>> updateFunction) {
        Alert alert = createGraphAlert(title);

        ComboBox<String> yearSelector = (ComboBox<String>) alert.getDialogPane().lookup(".combo-box");
        BarChart<String, Number> barChart = (BarChart<String, Number>) alert.getDialogPane().lookup(".chart");

        yearSelector.setOnAction(event -> {
            int selectedYear = Integer.parseInt(yearSelector.getValue());
            updateFunction.accept(selectedYear, barChart);
        });

        showStyledAlert(alert);
    }

    /**
     * Displays a chart for files and directories created each month by year.
     */
    public void showMonthlyCreatedFilesByYearGraph() {
        showMonthlyChartByYear("Files + directories created each month by year", this::updateBarChartCreated);
    }

    /**
     * Displays a chart for files and directories modified each month by year.
     */
    public void showMonthlyModifiedFilesChartByYearGraph() {
        showMonthlyChartByYear("Files + directories modified each month by year", this::updateBarChartModified);
    }

    /**
     * Updates the bar chart with counts of files and directories created each month for the specified year.
     *
     * @param year      The selected year for which to update the values.
     * @param barChart  The BarChart instance to be updated.
     */
    private void updateBarChartCreated(int year, BarChart<String, Number> barChart) {
        Map<String, Integer> monthlyCount = updateBarChartValuesCreated(year);

        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : monthlyCount.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
    }

    /**
     * Updates the bar chart with counts of files and directories modified each month for the specified year.
     *
     * @param year      The selected year for which to update the values.
     * @param barChart  The BarChart instance to be updated.
     */
    private void updateBarChartModified(int year, BarChart<String, Number> barChart) {
        Map<String, Integer> monthlyChangeCount = updateBarChartValuesModified(year);

        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : monthlyChangeCount.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
    }

    /**
     * Updates the map with counts of files and directories created each month for the specified year.
     *
     * @param year The selected year for which to update the values.
     * @return A map containing the count of monthly created files and directories.
     */
    private Map<String, Integer> updateBarChartValuesCreated(int year) {
        Map<String, Integer> monthlyCount = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            monthlyCount.put(month.toString(), 0);
        }

        for (FileSystemItem f : fileSystem.getFileSystem()) {
            LocalDateTime creationDate = f.getCreationDate();
            if (creationDate.getYear() == year) {
                String monthName = creationDate.getMonth().toString();
                monthlyCount.put(monthName, monthlyCount.get(monthName) + 1);
            }
        }

        return monthlyCount;
    }

    /**
     * Updates the map with counts of files and directories modified each month for the specified year.
     *
     * @param year The selected year for which to update the values.
     * @return A map containing the count of monthly modified files and directories.
     */
    private Map<String, Integer> updateBarChartValuesModified(int year) {
        Map<String, Integer> monthlyChangeCount = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            monthlyChangeCount.put(month.toString(), 0);
        }

        for (FileSystemItem f : fileSystem.getFileSystem()) {
            int changeCount = f.getChangeCount();
            if (f.getLastModificationDate() != null && f.getLastModificationDate().getYear() == year) {
                String monthName = f.getLastModificationDate().getMonth().toString();
                monthlyChangeCount.put(monthName, monthlyChangeCount.get(monthName) + changeCount);
            }
        }

        return monthlyChangeCount;
    }
    //
    //MÉTODOS DE PERSONALIZAÇÃO DE/PARA ELEMENTOS JAVAFX
    //

    /**
     * Creates a horizontal line with the specified size and styling.
     *
     * @return A Line object representing a horizontal line.
     */
    private Line createLine(){
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(MetricsView.LINE_SIZE);
        line.setStyle("-fx-stroke: grey;");
        return line;
    }

    /**
     * Creates an information alert with the specified title. The alert is of type INFORMATION
     * and has a customized style with a dark background color.
     *
     * @param title The title of the information alert.
     * @return The created information alert.
     */
    private Alert createInformationAlert(String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.getDialogPane().setGraphic(null); // Remove default icon
        alert.getDialogPane().setStyle("-fx-background-color:#2C262E;");
        return alert;
    }

    /**
     * Creates a JavaFX label with the given text and applies a specific style to the label.
     *
     * @param text The text to be displayed on the label.
     * @return The created label with the specified text and style.
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        configureLabelStyle(label);
        return label;
    }

    /**
     * Creates a JavaFX text area with a customized style and settings. The text area is configured
     * for read-only display and has a dark background color.
     *
     * @return The created text area with the specified style and settings.
     */
    private TextArea createTextArea() {
        TextArea textArea = new TextArea();
        configureTextAreaStyle(textArea);
        return textArea;
    }

    /**
     * Creates a vertical box (VBox) containing a label and a text area. The label and text area
     * are provided as parameters, and the VBox is styled accordingly.
     *
     * @param label The label to be included in the panel.
     * @param textArea The text area to be included in the panel.
     * @return The created VBox panel containing the label and text area.
     */
    private VBox createPanel(Label label, TextArea textArea) {
        VBox panel = new VBox(label, textArea);
        configurePanelStyle(panel);
        return panel;
    }

    /**
     * Creates a JavaFX button with the specified text and associates it with the given event handler.
     *
     * @param text           The text to be displayed on the button.
     * @param eventHandler   The event handler to be executed when the button is clicked.
     * @return The created button with the specified text and event handler.
     */
    private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
        return createButton(text, -1, -1, eventHandler);
    }

    /**
     * Creates a JavaFX button with the specified text, preferred width, and preferred height. The
     * button is associated with the given event handler and is styled based on the provided size.
     *
     * @param text           The text to be displayed on the button.
     * @param prefWidth      The preferred width of the button.
     * @param prefHeight     The preferred height of the button.
     * @param eventHandler   The event handler to be executed when the button is clicked.
     * @return The created button with the specified text, size, and event handler.
     */
    private Button createButton(String text, double prefWidth, double prefHeight, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(text);
        if (prefWidth >= 0 && prefHeight >= 0) {
            configureButtonStyles(button, prefWidth, prefHeight);
        }
        button.setOnAction(eventHandler);
        return button;
    }

    /**
     * Creates a JavaFX ListView for displaying FileSystemItem objects, adding the specified
     * directories to the list. The ListView is styled according to the configured styles.
     *
     * @param directories    The list of directories to be displayed in the ListView.
     * @return The created ListView with the specified directories and styles.
     */
    private ListView<FileSystemItem> createSelectDirectoryListView(List<FileSystemItem> directories) {
        ListView<FileSystemItem> directoryListView = new ListView<>();
        directoryListView.getItems().addAll(directories);
        configureListViewStyles(directoryListView);
        return directoryListView;
    }

    /**
     * Creates an HBox (horizontal box) containing a ListView for displaying FileSystemItem objects.
     *
     * @param directoryListView The ListView to be included in the HBox.
     * @return The created HBox containing the specified ListView.
     */
    private HBox createSelectDirectoryListViewHbox(ListView<FileSystemItem> directoryListView) {
        return new HBox(directoryListView);
    }

    /**
     * Creates a VBox (vertical box) containing buttons for interacting with a directory ListView.
     * The buttons are associated with specific actions like showing files, directories, etc.
     *
     * @param directoryListView The ListView for which the buttons are created.
     * @return The created VBox containing buttons for interacting with the directory ListView.
     */
    private VBox createSelectDirectoryButtons(ListView<FileSystemItem> directoryListView) {
        VBox buttonsBox = new VBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setVisible(false);

        Button button1 = createButton("Show files", 100, 40, event -> showFilesAlert(directoryListView.getSelectionModel().getSelectedItem()));
        Button button2 = createButton("Show directories", 150, 40, event -> showDirectoriesAlert(directoryListView.getSelectionModel().getSelectedItem()));
        Button button3 = createButton("Show files and directories", 250, 40, event -> showBothAlert(directoryListView.getSelectionModel().getSelectedItem()));
        Button button4 = createButton("Show top 5 directories", 200, 40, event -> showTop5DirectoriesAlert(directoryListView.getSelectionModel().getSelectedItem()));
        Button button5 = createButton("Show depth", 100, 40, event -> showDepthAlert(directoryListView.getSelectionModel().getSelectedItem()));

        buttonsBox.getChildren().addAll(button1, button2, button3, button4, button5);
        return buttonsBox;
    }
    //

    //SHOW

    /**
     * Displays a styled JavaFX alert with a specified width and height. The alert is shown
     * and waits for user interaction before returning.
     *
     * @param alert The JavaFX alert to be displayed.
     */
    private void showStyledAlert(Alert alert) {
        alert.getDialogPane().setPrefWidth(500);
        alert.getDialogPane().setPrefHeight(400);
        alert.showAndWait();
    }

    /**
     * Appends a message to the given TextArea based on the type and count of items found.
     * The message includes information about the total number of items.
     *
     * @param type     The type of items (e.g., "files", "directories").
     * @param count    The count of items found.
     * @param textArea The TextArea to which the message is appended.
     */
    private void showTotalItemsMessage(String type, int count, TextArea textArea) {
        String message;

        if (count == 0) {
            message = "NO " + type.toUpperCase() + " FOUND";
        } else {
            message = "\n___________________________________";
            message += "\nTotal number of " + type + ": " + count;
            message += "\n___________________________________";
        }

        textArea.appendText(message);
    }

    /**
     * Displays information about files and directories in a specified directory
     * in a TextArea. The information is based on the specified type of items.
     *
     * @param type              The type of items to display ("all", "files", "directories").
     * @param selectedDirectory The directory for which information is displayed.
     * @param textArea          The TextArea to display the information.
     * @return The count of items based on the specified type.
     */
    private int showInformation(String type, FileSystemItem selectedDirectory, TextArea textArea) {
        int count = 0;
        String header = "___________________________________\n";

        textArea.appendText(header);

        for (Position<FileSystemItem> f : fileSystem.findChildren(selectedDirectory)) {
            boolean isDirectory = f.element().isDirectory();
            String itemType = isDirectory ? "Directory" : "File";

            if (type.equals("all") || (type.equals("files") && !isDirectory) || (type.equals("directories") && isDirectory)) {
                count++;
                textArea.appendText(itemType + ": " + f.element().getName() + "\n");
            }
        }

        return count;
    }
    //
    //SET

    /**
     * Sets the style for a JavaFX Button based on the color settings and predefined styling.
     * The button's style is adjusted for normal, mouse-entered, and mouse-exited states.
     *
     * @param button The JavaFX Button for which to set the style.
     */
    private void setButtonStyle(Button button) {
        String baseStyle = "-fx-background-color:" + colorSettings.getBackgroundColor() + ";-fx-font-family: 'Nunito';" +
                "-fx-font-size: 14;-fx-text-fill:white;";

        button.setStyle(baseStyle);

        button.setOnMouseEntered(event -> button.setStyle(baseStyle + "-fx-background-color:#89508a;-fx-content-display: bottom;"));

        button.setOnMouseExited(event -> button.setStyle(baseStyle));

        button.setAlignment(Pos.BASELINE_LEFT);
        button.setPrefHeight(25);
        button.setPrefWidth(415);
    }
    //
    //CONFIGURE

    /**
     * Configures the visual styles for a JavaFX Button, including background color, font size, border radius, text fill,
     * border color, and font family. Additionally, sets the preferred width and height of the button.
     *
     * @param button     The JavaFX Button to configure.
     * @param prefWidth  The preferred width of the button.
     * @param prefHeight The preferred height of the button.
     */
    private void configureButtonStyles(Button button, double prefWidth, double prefHeight) {
        button.setStyle("-fx-background-color: #2C262E; -fx-font-size: 14; -fx-border-radius: 5em;-fx-text-fill: grey; -fx-border-color:white; -fx-font-family: 'Nunito';");
        button.setPrefSize(prefWidth, prefHeight);
    }

    /**
     * Configures the actions to be performed when an item is selected in the ListView. It controls the visibility of a
     * container (VBox) based on the selected item's presence.
     *
     * @param directoryListView The ListView containing directory items.
     * @param buttonsBox        The VBox container containing buttons.
     */
    private void configureSelectDirectoryButtonActions(ListView<FileSystemItem> directoryListView, VBox buttonsBox) {
        directoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> buttonsBox.setVisible(newValue != null));
    }

    /**
     * Configures the visual style for the close button of an Alert dialog, including background color, font size, and
     * border radius.
     *
     * @param alert The Alert dialog for which to configure the close button.
     */
    private void configureCloseButton(Alert alert) {
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(closeButton);
        Button customButton = (Button) alert.getDialogPane().lookupButton(closeButton);

        configureButtonStyles(customButton, 40, 40);
    }

    /**
     * Configures the visual styles for a JavaFX ListView, including inner background color, font family, font size,
     * focus color, faint focus color, and selection bar color.
     *
     * @param directoryListView The JavaFX ListView to configure.
     */
    private void configureListViewStyles(ListView<FileSystemItem> directoryListView) {
        directoryListView.setStyle("-fx-control-inner-background: #2C262E;-fx-font-family: 'Nunito'; -fx-font-size: 18;" +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: #89508a;");
    }

    /**
     * Configures the visual styles for a JavaFX TextArea, including inner background color, font family, font size,
     * text fill, focus color, faint focus color, and selection bar color. Additionally, adjusts layout and translation
     * properties.
     *
     * @param textArea The JavaFX TextArea to configure.
     */
    private void configureTextAreaStyle(TextArea textArea) {
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-control-inner-background: #2C262E;-fx-font-family: 'Nunito'; -fx-font-size: 14; -fx-text-fill:grey;" +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: #2C262E;");
        textArea.setLayoutY(textArea.getLayoutY() + 2);
        textArea.setTranslateY(textArea.getTranslateY() + 2);
        textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    /**
     * Configures the visual styles for a JavaFX VBox (panel), including layout and translation properties for adjusting
     * position.
     *
     * @param panel The JavaFX VBox (panel) to configure.
     */
    private void configurePanelStyle(VBox panel) {
        panel.setLayoutX(panel.getLayoutX() - 12);
        panel.setTranslateX(panel.getTranslateX() - 12);
        panel.setLayoutY(panel.getLayoutY() - 7);
        panel.setTranslateY(panel.getTranslateY() - 7);
    }

    /**
     * Configures the visual styles for a JavaFX Label, including background color, text fill, font family, font size,
     * layout, and translation properties.
     *
     * @param label The JavaFX Label to configure.
     */
    private void configureLabelStyle(Label label) {
        label.setStyle("-fx-background-color: #2C262E; -fx-text-fill:grey;-fx-font-family: 'Nunito'; -fx-font-size: 18;");
        label.setLayoutX(label.getLayoutX() + 12);
        label.setTranslateX(label.getTranslateX() + 12);
        label.setLayoutY(label.getLayoutY() + 6);
        label.setTranslateY(label.getTranslateY() + 6);
    }

}
