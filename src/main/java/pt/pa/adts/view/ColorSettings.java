package pt.pa.adts.view;

/**
 * The {@code ColorSettings} class represents a set of color configurations, including background color, primary color, and secondary color.
 * It allows customization of these colors and provides default values.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class ColorSettings {

    private String backgroundColor;
    private String primaryColor;
    private String secondaryColor;

    private static final String backgroundDefault = "#19151B";
    private static final String primaryDefault = "#231D25";
    private static final String secondaryDefault = "#2C262E";

    /**
     * Constructs a new {@code ColorSettings} instance with default color values.
     */
    public ColorSettings(){
        this.backgroundColor = "#19151B";
        this.secondaryColor = "#2C262E";
        this.primaryColor = "#231D25";
    }

    /**
     * Sets the color settings to their default values.
     */
    public void setDefault(){
        this.backgroundColor = backgroundDefault;
        this.primaryColor = primaryDefault;
        this.secondaryColor = secondaryDefault;
    }

    /**
     * Sets the background color.
     *
     * @param backgroundColor The new background color.
     */
    public void setBackgroundColor(String backgroundColor){ this.backgroundColor = backgroundColor;}

    /**
     * Sets the primary color.
     *
     * @param primaryColor The new primary color.
     */
    public void setPrimaryColor(String primaryColor){this.primaryColor = primaryColor;}

    /**
     * Sets the secondary color.
     *
     * @param secondaryColor The new secondary color.
     */
    public void setSecondaryColor(String secondaryColor){this.secondaryColor = secondaryColor;}

    /**
     * Gets the background color.
     *
     * @return The background color.
     */
    public String getBackgroundColor(){return backgroundColor;}

    /**
     * Gets the primary color.
     *
     * @return The primary color.
     */
    public String getPrimaryColor(){return primaryColor;}

    /**
     * Gets the secondary color.
     *
     * @return The secondary color.
     */
    public String getSecondaryColor(){return secondaryColor;}

}
