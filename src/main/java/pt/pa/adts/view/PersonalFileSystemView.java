package pt.pa.adts.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.pa.adts.controller.PersonalFileSystemController;
import pt.pa.adts.model.*;
import pt.pa.adts.observer.Observable;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The `PersonalFileSystemView` class represents the graphical user interface (GUI)
 * for a personal file system. It provides features such as a tree view of the file system,
 * toolbar with various actions, and settings for appearance customization.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class PersonalFileSystemView extends BorderPane implements FileSystemUI {

    private PersonalFileSystem model;
    private TreeView<FileSystemItem> treeView;
    private TreeView<FileSystemItem> alertTreeView;
    private Button undo;
    private Button copy;
    private Button move;
    private Button delete;
    private Button add;
    private Button graphics;
    private Button rename;
    private Button lock;
    private Button settings;
    private Button export;
    private Button hidden;
    private Button save;
    private TextField textFieldName;
    private TextField textFieldConteudo;
    private TextField textFieldPathName;
    private ComboBox<Boolean> isLocked;
    private ComboBox<Boolean> isHidden;
    private TextArea textAreaEditContent;
    private Label lblError;
    private Label labelName;
    private Label lblConteudo;
    private Label lblIsLocked;
    private Label lblIsHidden;
    private Label toolBarLabel;
    private RadioButton directoryRB;
    private RadioButton fileRB;
    private boolean focused = true;
    private ColorSettings colorSettings;
    private String backgroundColor;
    private String primaryColor;
    private String secondaryColor;

    /**
     * Constructs a new `PersonalFileSystemView` with the specified personal file system model.
     *
     * @param model The personal file system model to be associated with the view.
     */
    public PersonalFileSystemView(PersonalFileSystem model){
        this.colorSettings = new ColorSettings();
        this.model = model;
        createLayout();
    }

    /**
     * Updates the view when changes occur in the observed personal file system model.
     *
     * @param subject The observed personal file system model.
     * @param obj     An object containing additional information about the update (not used).
     */
    @Override
    public void update(Observable subject, Object obj) {
        if(subject == model){
            updateTreeView();
            //update treeView in alerts
            setAlertTreeView();
        }
    }

    /**
     * Creates the initial layout for the personal file system view.
     */
    private void createLayout() {

        undo = createToolbarButton("undo.png");
        rename = createToolbarButton("rename.png");
        copy = createToolbarButton("copy.png");
        move = createToolbarButton("move.png");
        graphics = createToolbarButton("chart.png");
        add = createToolbarButton("createNew.png");
        delete = createToolbarButton("deleteImg.png");
        settings = createToolbarButton("settings.png");

        setLeft(LeftSideCreate());
        setCenter(CenterCreate());


        setStyle("-fx-background-color: "+colorSettings.getBackgroundColor()+";");
    }

    /**
     * Reloads the graphical user interface (GUI) components and styles based on the current settings and state.
     * This method updates the styles of various buttons, sets the background color, and refreshes the central and left components.
     * Additionally, it handles the treeView's mouse click event to update the center component when the focus is gained.
     */
    public void reload(){
        setButtonStyles(undo);
        setButtonStyles(add);
        setButtonStyles(rename);
        setButtonStyles(delete);
        setButtonStyles(copy);
        setButtonStyles(move);
        setButtonStyles(settings);
        setButtonStyles(graphics);
        setLeft(LeftSideCreate());
        setCenter(CenterCreate());
        setStyle("-fx-background-color: "+colorSettings.getBackgroundColor()+";");
        treeView.setOnMouseClicked(event -> {
            if(!focused){
                focused = true;
                setCenter(null);
                setCenter(CenterCreate());
            }
        });
        updateTreeView();
    }

    /**
     * Creates the top bar of the view, including the toolbar with various action buttons.
     *
     * @return The configured toolbar as a `ToolBar` component.
     */
    private ToolBar topBar(){

        toolBarLabel = new Label("My Files");
        toolBarLabel.setAlignment(Pos.CENTER);
        toolBarLabel.setPrefWidth(110);
        toolBarLabel.setStyle("-fx-text-fill: white;");
        toolBarLabel.setFont(Font.font("Nunito", 16));

        Region separator1 = new Region();
        separator1.setPrefWidth(1);
        separator1.setPrefHeight(15);
        separator1.setStyle("-fx-background-color: grey;");

        ToolBar toolBar = new ToolBar(toolBarLabel, separator1, undo, rename, copy, move, graphics,
                add, delete, settings);

        toolBar.setStyle("-fx-background-color: "+ colorSettings.getPrimaryColor() +";");
        toolBar.setPrefSize(450,50);
        toolBar.setMinWidth(450);
        toolBar.resizeRelocate(350,0,450,60);

        BorderStroke borderStroke = new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1)
        );

        Border border = new Border(borderStroke);
        toolBar.setBorder(border);

        return toolBar;
    }

    /**
     * Creates a `VBox` containing the settings view, including the top toolbar.
     *
     * @return The `VBox` containing the settings view.
     */
    private VBox setSettings(){
        VBox settingsVbox = new VBox(topBar(),new SettingsView(colorSettings,this));
        settingsVbox.setStyle("-fx-background-color: " + colorSettings.getBackgroundColor() + ";");
        return settingsVbox;
    }

    /**
     * Creates the graphical part of the view, including the top bar and a metrics view.
     *
     * @return The VBox containing the graphical part of the view.
     */
    private VBox graphicPart(){
        VBox graphicPart = new VBox(topBar(),new MetricsView(model,colorSettings));
        graphicPart.setStyle("-fx-background-color: " + colorSettings.getBackgroundColor() + ";");
        return graphicPart;
    }

    /**
     * Creates the center part of the view, which includes the file information and properties.
     *
     * @return The VBox containing the center part of the view.
     */
    private VBox CenterCreate(){
        VBox panel;
        ImageView iconView = new ImageView();

        Label information = new Label();
        Label properties = new Label();

        Label selectedShowType= new Label();
        Label showSelectedName = new Label();
        Label showCreated = new Label();
        Label showModified = new Label();
        Label showUnlocked = new Label();
        Label showHidden = new Label();

        HBox topInfo = new HBox();
        VBox basicInfo = new VBox();
        VBox infoVbox = new VBox();
        VBox propVbox = new VBox();

        // Verificar se há um diretório ou arquivo selecionado antes de acessar informações
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Atualiza o conteúdo do centro com base no item selecionado
            if (newValue != null) {
                FileSystemItem selected = newValue.getValue();
                toolBarLabel.setText(newValue.getValue().getName());

                Image icon;
                if(!selected.isDirectory()) {
                    icon = new Image(getClass().getClassLoader().getResourceAsStream("file.png"));
                    iconView.setImage(icon);
                    iconView.setTranslateY(getTranslateY()+5);
                    iconView.setLayoutY(getLayoutY()+5);
                } else {
                    icon = new Image(getClass().getClassLoader().getResourceAsStream("openFolder.png"));
                    iconView.setImage(icon);
                }

                iconView.setFitWidth(70);
                iconView.setFitHeight(70);

                showSelectedName.setText(selected.getName());
                customizeLabelsCenter(showSelectedName);


                if(!selected.isDirectory())
                    selectedShowType.setText("Document" + " - " + selected.getSizeInBytes() + " bytes");
                else
                    selectedShowType.setText("Directory" + " - " + selected.getSizeInBytes() + " bytes");

                customizeLabelsCenter(selectedShowType);

                information.setText("Information");
                customizeLabelsCenter(information);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

                String formattedDateTime = selected.getCreationDate().toLocalDate().format(formatter);

                showCreated.setText("Created \t" + formattedDateTime +
                        " at " + selected.getCreationDate().getHour() + ":" + selected.getCreationDate().getMinute());
                customizeLabelsCenter(showCreated);

                formattedDateTime = selected.getLastModificationDate().toLocalDate().format(formatter);
                showModified.setText("Modified \t" + formattedDateTime +
                        " at " + selected.getLastModificationDate().getHour() + ":" + selected.getLastModificationDate().getMinute());
                customizeLabelsCenter(showModified);

                properties.setText("Properties");
                customizeLabelsCenter(properties);

                showUnlocked.setText("Locked: \t" + (selected.isLocked() ? "Yes" : "No"));
                customizeLabelsCenter(showUnlocked);

                showHidden.setText("Hidden: \t" + (selected.isHidden() ? "Yes" : "No"));
                customizeLabelsCenter(showHidden);
            }
            if(basicInfo.getChildren().isEmpty()) {
                basicInfo.getChildren().addAll(showSelectedName, selectedShowType);
                basicInfo.setPadding(new Insets(10));
            }

            if(topInfo.getChildren().isEmpty()) {
                topInfo.getChildren().addAll(iconView, basicInfo);
                topInfo.setPadding(new Insets(10));
            }
            if(infoVbox.getChildren().isEmpty()) {
                infoVbox.getChildren().addAll(information, showCreated, createLine(260), showModified);
                infoVbox.setPadding(new Insets(10));
                infoVbox.setStyle("-fx-background-color: "+ colorSettings.getPrimaryColor() +";");
            }

            if(propVbox.getChildren().isEmpty()) {
                propVbox.getChildren().addAll(properties, showUnlocked, createLine(260), showHidden);
                propVbox.setPadding(new Insets(0, 10, 10, 10));
            }

        });

        VBox container = new VBox(topInfo,infoVbox,propVbox);
        HBox center = new HBox();

        center.getChildren().addAll(container,rightSideCreate());
        panel = new VBox(topBar(), center);

        return panel;
    }

    /**
     * Update the layout of labels in the center
     * @param label The label to customize
     */
    private void customizeLabelsCenter(Label label){
        label.setStyle("-fx-text-fill: grey;");
        label.setPrefHeight(25);
        label.setFont(Font.font("Nunito", 14));
    }

    /**
     * Creates a line with the specified size and style.
     *
     * @param size The size of the line.
     * @return The created Line object.
     */
    private Line createLine(int size){
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(size);
        line.setStyle("-fx-stroke: grey;");
        return line;
    }

    /**
     * Creates a button with the specified icon file.
     *
     * @param iconFileName The name of the icon file.
     * @return The created Button object.
     */
    private Button createToolbarButton(String iconFileName) {
        Image icon = new Image(getClass().getClassLoader().getResourceAsStream(iconFileName));
        ImageView imageView = new ImageView(icon);
        Button button = new Button("", imageView);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        button.setStyle("-fx-background-color: " + colorSettings.getSecondaryColor() + ";");

        return button;
    }

    /**
     * Creates the right side of the view, including the preview content area and related buttons.
     *
     * @return The HBox containing the right side of the view.
     */
    private HBox rightSideCreate(){
        HBox panel = new HBox();
        panel.setVisible(false);
        panel.setManaged(false);

        VBox buttons = new VBox();
        buttons.setPadding(new Insets(10));

        Label label = new Label();
        label.setText("Preview content");
        label.setFont(Font.font("Nunito", FontWeight.BOLD,22));
        label.setStyle("-fx-text-fill:white;");

        textAreaEditContent = new TextArea();
        textAreaEditContent.setStyle("-fx-control-inner-background: "+ colorSettings.getBackgroundColor() +"; " +
                "-fx-text-fill: white; -fx-focus-color: #89508a; -fx-faint-focus-color: #89508a;");
        textAreaEditContent.setFont(Font.font("Nunito",14));

        Image image = new Image(getClass().getClassLoader().getResourceAsStream("view.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        hidden = new Button(null,imageView);
        hidden.setStyle("-fx-background-color:" + colorSettings.getSecondaryColor() + ";");


        Image image2 = new Image(getClass().getClassLoader().getResourceAsStream("lock.png"));
        ImageView imageView1 = new ImageView(image2);
        imageView1.setFitHeight(20);
        imageView1.setFitWidth(20);
        lock = new Button(null,imageView1);
        lock.setStyle("-fx-background-color:" + colorSettings.getSecondaryColor() + ";");
        Image image3 = new Image(getClass().getClassLoader().getResourceAsStream("export.png"));
        ImageView imageView2 = new ImageView(image3);
        imageView2.setFitWidth(20);
        imageView2.setFitHeight(20);
        export = new Button(null,imageView2);
        export.setStyle("-fx-background-color:" + colorSettings.getSecondaryColor() + ";");

        save = createToolbarButton("save.png");

        buttons.getChildren().addAll(hidden,lock,save,export);


        treeView.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
            if(newValue!=null) {
                panel.setVisible(!newValue.getValue().isDirectory());
                panel.setManaged(!newValue.getValue().isDirectory());


                if (newValue.getValue().isHidden()) {
                    textAreaEditContent.setText("Content Hidden");
                    hidden.setStyle("-fx-background-color: #89508a;");
                } else {
                    textAreaEditContent.setText(newValue.getValue().getContent());
                    hidden.setStyle("-fx-background-color: transparent;");
                }

                if (newValue.getValue().isLocked())
                    lock.setStyle("-fx-background-color: #89508a;");
                else
                    lock.setStyle("-fx-background-color: transparent;");

                textAreaEditContent.setEditable(!newValue.getValue().isLocked());

                hidden.setOnAction(event -> {
                    if (newValue.getValue().isHidden()) {
                        newValue.getValue().setHidden(false);
                        textAreaEditContent.setText(newValue.getValue().getContent());
                        if (!newValue.getValue().isLocked())
                            textAreaEditContent.setEditable(true);
                        hidden.setStyle("-fx-background-color: transparent; ");
                    } else {
                        newValue.getValue().setHidden(true);
                        textAreaEditContent.setText("Content hidden");
                        textAreaEditContent.setEditable(false);
                        hidden.setStyle("-fx-background-color: #89508a; ");
                    }
                });

                lock.setOnAction(event -> {
                    if (newValue.getValue().isLocked()) {
                        treeView.getSelectionModel().getSelectedItem().getValue().setIsLocked(false);
                        if (!newValue.getValue().isHidden())
                            textAreaEditContent.setEditable(true);
                        lock.setStyle("-fx-background-color: transparent; ");
                    } else {
                        treeView.getSelectionModel().getSelectedItem().getValue().setIsLocked(true);
                        textAreaEditContent.setEditable(false);
                        lock.setStyle("-fx-background-color: #89508a; ");
                    }
                });

                save.setOnAction(event -> {
                    newValue.getValue().setContent(textAreaEditContent.getText());
                });
                setButtonStylesInverted(save);
                setButtonStylesInverted(export);
            }
        });

        panel.setPrefHeight(555);
        panel.getChildren().addAll(buttons,textAreaEditContent);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color:"+ colorSettings.getSecondaryColor() +";");

        return panel;
    }

    /**
     * Creates the left side of the view, including the tree view displaying the file system structure.
     *
     * @return The VBox containing the left side of the view.
     */
    private VBox LeftSideCreate(){
        treeView = createTreeView();
        treeView.setPrefHeight(700);
        treeView.setPrefWidth(250);
        treeView.setStyle("-fx-control-inner-background: "+colorSettings.getSecondaryColor()+";-fx-font-family: 'Nunito'; -fx-font-size: 18;" +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: #89508a;");
        treeView.setShowRoot(false);

        Label label = new Label(model.getFileSystem().root().element().getName());
        label.setStyle("-fx-text-fill: grey;");
        label.setFont(Font.font("Nunito", FontWeight.BOLD,22));
        label.setPrefHeight(200);
        label.setPrefWidth(250);
        label.setPadding(new Insets(0,0,0,28));
        label.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(label,treeView);
        panel.setStyle("-fx-background-color: "+colorSettings.getSecondaryColor()+";");

        return panel;
    }

    /**
     * Creates a TreeView representing the file system structure.
     *
     * @return The created TreeView object.
     */
    private TreeView<FileSystemItem> createTreeView() {

        TreeLinked<FileSystemItem> treeLinked = model.getFileSystem();

        // Crie o item raiz da TreeView
        TreeItem<FileSystemItem> rootItem = createTreeItem(treeLinked.root(),treeLinked);

        return new TreeView<>(rootItem);
    }

    /**
     * Sets up the alertTreeView, which is a clone of the original treeView for displaying alerts.
     */
    private void setAlertTreeView(){
        alertTreeView = new TreeView<>(treeView.getRoot());
        treeView.getRoot().setExpanded(true);
        treeView.getRoot().expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                treeView.getRoot().setExpanded(true);
            }
        });
        alertTreeView.setStyle("-fx-control-inner-background: #2C262E;-fx-font-family: 'Nunito'; -fx-font-size: 18;");
    }

    /**
     * Updates the treeView to reflect changes in the file system.
     */
    private void updateTreeView() {
        TreeLinked<FileSystemItem> treeLinked = model.getFileSystem();
        TreeItem<FileSystemItem> newRootItem = createTreeItem(treeLinked.root(), treeLinked);
        treeView.setRoot(newRootItem);
        treeView.setStyle("-fx-control-inner-background: "+colorSettings.getSecondaryColor()+";-fx-font-family: 'Nunito'; -fx-font-size: 18;" +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: #89508a;");
        treeView.setCellFactory(tree -> new CustomTreeCell<>(colorSettings));
    }

    /**
     * Recursively creates a TreeItem structure for the given position and tree.
     *
     * @param position   The position in the file system tree.
     * @param treeLinked The file system tree.
     * @return The created TreeItem.
     */
    private TreeItem<FileSystemItem> createTreeItem(Position<FileSystemItem> position, TreeLinked<FileSystemItem> treeLinked) {
       TreeItem<FileSystemItem> item = new TreeItem<>(position.element());

        for (Position<FileSystemItem> child : treeLinked.children(position)) {
            item.getChildren().add(createTreeItem(child,treeLinked));
        }
        return item;
    }

    /**
     * Displays a confirmation dialog for adding a file or directory.
     *
     * @return The created Alert dialog.
     */
    private Alert showAddPopUp() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inserir Ficheiro ou diretória");
        alert.setHeaderText("");

        // Adiciona um campo de entrada
        textFieldName = new TextField();
        customizeTextAddPopUp(textFieldName);
        textFieldConteudo = new TextField();
        customizeTextAddPopUp(textFieldConteudo);
        labelName = new Label("Digite o Nome:");
        customizeAlertLabels(labelName);
        lblIsLocked = new Label("Is Locked:");
        customizeAlertLabels(lblIsLocked);
        lblIsHidden = new Label("Is Hidden");
        customizeAlertLabels(lblIsHidden);
        isLocked = new ComboBox<>();
        isHidden = new ComboBox<>();
        isLocked.getItems().addAll(Boolean.TRUE,Boolean.FALSE);
        isHidden.getItems().addAll(Boolean.TRUE,Boolean.FALSE);
        isLocked.setValue(Boolean.FALSE);
        isHidden.setValue(Boolean.FALSE);
        lblConteudo = new Label("Digite o conteudo:");
        customizeAlertLabels(lblConteudo);
        ToggleGroup toggleGroup = new ToggleGroup();

        directoryRB = new RadioButton("Directory");
        directoryRB.setToggleGroup(toggleGroup);
        directoryRB.setStyle("-fx-text-fill: white;");

        fileRB = new RadioButton("File");
        fileRB.setToggleGroup(toggleGroup);
        fileRB.setStyle("-fx-text-fill: white;");

        fileRB.setSelected(true);

        directoryRB.setOnAction(event -> {
            if (directoryRB.isSelected()) {
                textFieldConteudo.setEditable(false);
                textFieldConteudo.setStyle("-fx-opacity: 1.0; -fx-background-color: #999999;");
                textFieldConteudo.clear();
            }
        });

        fileRB.setOnAction(event -> {
            if (fileRB.isSelected()) {
                textFieldConteudo.setEditable(true);
                textFieldConteudo.setStyle("-fx-background-color: white;");
            }
        });

        VBox panel = new VBox(labelName,textFieldName,lblIsLocked,isLocked,lblIsHidden,isHidden,lblConteudo,textFieldConteudo,fileRB,directoryRB);
        alert.setGraphic(panel);
        panel.setStyle("-fx-background-color: #2C262E;");
        alert.getDialogPane().setStyle("-fx-background-color: #2C262E;");


        // Adiciona os botões padrão (OK e Cancelar)
        ButtonType buttonTypeOK = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        Button customButton = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
        customizeButton(customButton);
        Button customButton1 = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);
        customizeButton(customButton1);
        alert.getDialogPane().setMaxSize(300,600);

        return alert;
    }

    /**
     * Update the layout of the buttons
     * @param button to customize
     */
    private void customizeButton(Button button){
        button.setStyle("-fx-background-color: #2C262E; -fx-font-size: 14; -fx-border-radius: 5em;-fx-text-fill: grey; -fx-border-color:white; -fx-font-family: 'Nunito';");
        button.setPrefSize(40,40);
    }

    /**
     * Update the labels in the Alerts
     * @param label to customize
     */
    private void customizeAlertLabels(Label label){
        label.setStyle("-fx-text-fill: white;");
        label.setFont(Font.font("Nunito", 20));
    }

    /**
     * Update the textField on Add pop up
     * @param textField
     */
    private void customizeTextAddPopUp(TextField textField){
        textField.setMinHeight(30);
        textField.setMinWidth(250);
    }

    /**
     * Displays a confirmation dialog for removing a file or directory.
     *
     * @return The created Alert dialog.
     */
    private Alert showRemovePopUp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remover ficheiro ou diretória");
        alert.setHeaderText("");
        ButtonType yesButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        VBox panel = new VBox();
        alert.getDialogPane().setGraphic(panel);
        alert.getButtonTypes().setAll(yesButton, noButton);
        Button customButton = (Button) alert.getDialogPane().lookupButton(yesButton);
       customizeButton(customButton);
        Button customButton1 = (Button) alert.getDialogPane().lookupButton(noButton);
        customizeButton(customButton1);
        alert.getDialogPane().setStyle("-fx-background-color: #2C262E;");

        return alert;
    }

    /**
     * Displays a confirmation dialog for renaming a file.
     *
     * @return The created Alert dialog.
     */
    private Alert showRenamePopUp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Renomear ficheiro");
        alert.setHeaderText("");
        textFieldName = new TextField();
        textFieldName.setMinHeight(30);
        textFieldName.setMinWidth(250);
        labelName = new Label("Digite o Nome:");
        customizeAlertLabels(labelName);
        VBox panel = new VBox(labelName,textFieldName);
        alert.getDialogPane().setGraphic(panel);
        ButtonType yesButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);
        Button customButton = (Button) alert.getDialogPane().lookupButton(yesButton);
        customizeButton(customButton);
        Button customButton1 = (Button) alert.getDialogPane().lookupButton(noButton);
        customizeButton(customButton1);
        alert.getDialogPane().setStyle("-fx-background-color: #2C262E;");
        alert.getDialogPane().setMaxSize(300,600);
        return alert;
    }

    /**
     * Displays a confirmation dialog for copying a file.
     *
     * @return The created Alert dialog.
     */
    public Alert showCopyPopUp(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Copiar ficheiro");
        alert.setHeaderText("");
        //textFieldPathName = new TextField();
        labelName = new Label("Escolha a pasta para onde pretende copiar");
        customizeAlertLabels(labelName);
        VBox panel = new VBox(labelName,alertTreeView);
        alert.setGraphic(panel);
        ButtonType yesButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);
        Button customButton = (Button) alert.getDialogPane().lookupButton(yesButton);
        customizeButton(customButton);
        Button customButton1 = (Button) alert.getDialogPane().lookupButton(noButton);
        customizeButton(customButton1);
        alert.getDialogPane().setStyle("-fx-background-color: #2C262E;");
        alert.getDialogPane().setMaxSize(500,600);
        return alert;
    }

    /**
     * Displays a confirmation dialog for moving a file.
     *
     * @return The created Alert dialog.
     */
    public Alert showMovePopUp(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mover ficheiro");
        alert.setHeaderText("");

        labelName = new Label("Selecione a pasta para a qual quer mover o ficheiro");
        customizeAlertLabels(labelName);
        VBox panel = new VBox(labelName,alertTreeView);
        alert.setGraphic(panel);
        ButtonType yesButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);
        Button customButton = (Button) alert.getDialogPane().lookupButton(yesButton);
        customizeButton(customButton);
        Button customButton1 = (Button) alert.getDialogPane().lookupButton(noButton);
        customizeButton(customButton1);
        alert.getDialogPane().setStyle("-fx-background-color: #2C262E;");
        alert.getDialogPane().setMaxSize(500,600);
        return alert;
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param msg The error message to be displayed.
     */
    @Override
    public void displayError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Clears the error message label.
     */
    @Override
    public void clearError() {
        lblError.setText("");
    }

    /**
     * Clears the text fields for input controls.
     */
    @Override
    public void clearControls() {
    textFieldName.setText("");
    textFieldConteudo.setText("");
    }

    /**
     * Sets the default and hover styles for a button.
     *
     * @param button The button to set styles for.
     */
    private void setButtonStyles(Button button){
        button.setStyle("-fx-background-color: " + colorSettings.getPrimaryColor() + ";");

        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: "+colorSettings.getSecondaryColor()+";");
        });

        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: "+colorSettings.getPrimaryColor()+";");
        });
    }

    /**
     * Sets inverted default and hover styles for a button.
     *
     * @param button The button to set styles for.
     */
    private void setButtonStylesInverted(Button button){
        button.setStyle("-fx-background-color: #2C262E;");

        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: #2C262E;");
        });

        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: #231D25;");
        });
    }

    /**
     * Sets up event handlers for various buttons and keyboard shortcuts in the UI.
     *
     * @param controller The controller to handle the actions triggered by the UI.
     */
    @Override
    public void setTriggers(PersonalFileSystemController controller){
        undo.setOnAction(event -> {
                controller.undo();
        });
        setButtonStyles(undo);

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCodeCombination ctrlZ = new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN);
            if (ctrlZ.match(event)) {
                controller.undo();
            }
            KeyCodeCombination ctrlC = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN);
            if(ctrlC.match(event)){
                controller.copyWithCtrlC();
            }
            KeyCodeCombination ctrlV = new KeyCodeCombination(KeyCode.V, KeyCodeCombination.CONTROL_DOWN);
            if(ctrlV.match(event)){
                    controller.doAddWithCtrlV();
            }

        });

        copy.setOnAction(event -> {
                Alert alert = showCopyPopUp();
                alert.showAndWait().ifPresent(response ->{
                    if(response.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                        controller.copyFile();
                }
                );
        });
        setButtonStyles(copy);

        add.setOnAction(event -> {
            Alert alert = showAddPopUp();
            alert.showAndWait().ifPresent(
                    response ->{
                        if(response.getButtonData()==ButtonBar.ButtonData.OK_DONE)
                            controller.doAddFileOrDirectory();
                    }
            );
        });
        setButtonStyles(add);

        rename.setOnAction(event -> {
            Alert alert = showRenamePopUp();
            alert.showAndWait().ifPresent(response ->{
                if(response.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    controller.doRenameFileOrDirectory();
            });
        });
        setButtonStyles(rename);

        delete.setOnAction(event -> {
            Alert alert = showRemovePopUp();
            alert.showAndWait().ifPresent(response -> {
                if(response.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    controller.doRemoveFileOrDirectory();
            });
        });
        setButtonStyles(delete);

        move.setOnAction(event -> {
            Alert alert = showMovePopUp();
            alert.showAndWait().ifPresent(response ->{
                if(response.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    controller.moveFile();
            }
            );
        });
        setButtonStyles(move);

        graphics.setOnAction(event -> {
            focused = false;
            setCenter(null);
            setRight(null);
            setCenter(graphicPart());
            toolBarLabel.setText("Metrics");
        });
        setButtonStyles(graphics);

        settings.setOnAction(event ->{
            focused = false;
            setCenter(null);
            setRight(null);
            setCenter(setSettings());
            toolBarLabel.setText("Settings");
        });
        setButtonStyles(settings);

        export.setOnAction(event -> {
            controller.export();
        });

        treeView.setOnMouseClicked(event -> {
            if(!focused){
                focused = true;
                setCenter(null);
                //setRight(rightSideCreate());
                setCenter(CenterCreate());
            }
        });
    }

    /**
     * Gets the text from the name input field.
     *
     * @return The text from the name input field.
     */
    public String getTextFieldName(){ return textFieldName.getText(); }

    /**
     * Gets the text from the content input field.
     *
     * @return The text from the content input field.
     */
    public String getTextFieldContent(){ return textFieldConteudo.getText(); }

    /**
     * Gets the selected directory or file in the TreeView.
     *
     * @return The selected directory or file.
     */
    public FileSystemItem getSelectedDirectoryOrFile(){
        FileSystemItem selectedItem = treeView.getSelectionModel().getSelectedItem().getValue();
        if(selectedItem==null){
            return treeView.getRoot().getValue();
        } else {
            return selectedItem;
        }
    }

    /**
     * Gets a list of elements (directory or file) to move based on the selected items in the TreeView.
     *
     * @return A list of elements to move.
     */
    public List<FileSystemItem> getElementsToMove(){
        ArrayList<FileSystemItem> itemsToMove = new ArrayList<>();
        itemsToMove.add(treeView.getSelectionModel().getSelectedItem().getValue());
        if(treeView.getSelectionModel().getSelectedItem().getChildren()!=null){
            for (TreeItem<FileSystemItem> item : treeView.getSelectionModel().getSelectedItem().getChildren()) {
                    itemsToMove.add(item.getValue());
            }
        }
        return itemsToMove;
    }

    /**
     * Gets the selected directory or file in the alert TreeView.
     *
     * @return The selected directory or file in the alert TreeView.
     */
    public FileSystemItem getAlertSelectedDirectoryOrFile(){
        return alertTreeView.getSelectionModel().getSelectedItem().getValue();
    }

    /**
     * Gets the selected value for "Is Locked" in the ComboBox.
     *
     * @return The selected value for "Is Locked".
     */
    public boolean getSelectedIsLocked(){
       return isLocked.getSelectionModel().getSelectedItem();
    }

    /**
     * Gets the selected value for "Is Hidden" in the ComboBox.
     *
     * @return The selected value for "Is Hidden".
     */
    public boolean getSelectedIsHidden(){
        return isHidden.getSelectionModel().getSelectedItem();
    }

    /**
     * Checks if any element is selected in the TreeView.
     *
     * @return True if an element is selected, false otherwise.
     */
    public boolean isElementIsSelected(){return treeView.getSelectionModel().getSelectedItem() != null;}

    /**
     * Gets the text from the path name input field.
     *
     * @return The text from the path name input field.
     */
    public String getTextFieldPathName(){ return textFieldPathName.getText(); }

    /**
     * Checks if the directory radio button is selected.
     *
     * @return True if the directory radio button is selected, false otherwise.
     */
    public boolean getDirectoryIsSelected(){ return directoryRB.isSelected();}

    /**
     * Initializes the display of the TreeView.
     */
    public void initTreeViewDisplay() {
        treeView.layout();
        update(model,null);
    }

}
