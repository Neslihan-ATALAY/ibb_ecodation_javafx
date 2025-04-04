module com.neslihanatalay.ibb_ecodation_javafx {

    requires javafx.controls;

    requires javafx.fxml;

    //requires javafx.web;

    requires org.controlsfx.controls;

    requires com.dlsc.formsfx;

    requires net.synedra.validatorfx;

    requires org.kordamp.ikonli.javafx;

    requires org.kordamp.bootstrapfx.core;

    requires static lombok;

    requires java.sql;
    requires com.h2database;
    requires jbcrypt;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires java.mail;
    //requires eu.hansolo.tilesfx;

    opens com.neslihanatalay.ibb_ecodation_javafx to javafx.fxml;

    opens com.neslihanatalay.ibb_ecodation_javafx.dto to javafx.base, lombok;

    opens com.neslihanatalay.ibb_ecodation_javafx.controller to javafx.fxml;

    opens com.neslihanatalay.ibb_ecodation_javafx.dao to java.sql;

    opens com.neslihanatalay.ibb_ecodation_javafx.database to java.sql;

    exports com.neslihanatalay.ibb_ecodation_javafx.dao;

    exports com.neslihanatalay.ibb_ecodation_javafx.database;

    exports com.neslihanatalay.ibb_ecodation_javafx;
    opens com.neslihanatalay.ibb_ecodation_javafx.utils to javafx.base, lombok;
}

//Default
/*
module com.neslihanatalay.ibb_ecodation_javafx {
        requires javafx.controls;
        requires javafx.fxml;
        //requires javafx.web;

        requires org.controlsfx.controls;
        requires com.dlsc.formsfx;
        requires net.synedra.validatorfx;
        requires org.kordamp.ikonli.javafx;
        requires org.kordamp.bootstrapfx.core;
        requires static lombok;
        requires java.sql;
        requires java.desktop;
        //requires eu.hansolo.tilesfx;

        opens com.neslihanatalay.ibb_ecodation_javafx to javafx.fxml;
        exports com.neslihanatalay.ibb_ecodation_javafx;
        }
*/
