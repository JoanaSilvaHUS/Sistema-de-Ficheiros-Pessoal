package pt.pa.adts.view;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pt.pa.adts.model.FileSystemItem;

/**
 * A custom tree cell implementation for a JavaFX TreeView.
 * @param <T> The type of the tree cell item.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class CustomTreeCell<T> extends TreeCell<T> {
    private final Image expandedImage = new Image("openFolder.png");
    private final Image collapsedImage = new Image("folder.png");
    private final Image fileImage = new Image("file.png");

    private ColorSettings colorSettings;

    /**
     * Constructs a new CustomTreeCell with the specified color settings.
     *
     * @param colorSettings The color settings to be applied to the CustomTreeCell.
     */
    public CustomTreeCell(ColorSettings colorSettings){
        this.colorSettings = colorSettings;
    }

    /**
     * Updates the content of the tree cell.
     * @param item The item to be displayed in the cell.
     * @param empty Whether the cell is empty or not.
     */
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.toString());

            TreeItem<T> treeItem = getTreeItem();
            FileSystemItem file = (FileSystemItem) treeItem.getValue();

            Image image = treeItem.isExpanded() ? expandedImage : collapsedImage;

            if(!file.isDirectory()) image = fileImage;

            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(16);
            imageView.setFitWidth(16);
            setStyle("-fx-control-inner-background: "+ colorSettings.getSecondaryColor() +";-fx-font-family: 'Nunito'; -fx-font-size: 18;" +
                    "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: #89508a;");
            setGraphic(imageView);
        }
    }
}
