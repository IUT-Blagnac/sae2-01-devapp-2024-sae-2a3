package model.data;

/**
 * Classe enum Theme
 * 
 */
public enum Theme {
    CLAIR("clair.css"),
    SOMBRE("sombre.css"),
    VERT("vert.css"),
    ROSE("rose.css");

    private final String cssFile;

    Theme(String cssFile) {
        this.cssFile = cssFile;
    }

    public String getCssFile() {
        return cssFile;
    }
}
