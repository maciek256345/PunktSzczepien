module edu.ib.punktszczepien {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql.rowset;


    opens edu.ib.punktszczepien to javafx.fxml;
    exports edu.ib.punktszczepien;
}