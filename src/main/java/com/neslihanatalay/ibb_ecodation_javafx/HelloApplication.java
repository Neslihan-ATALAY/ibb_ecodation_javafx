package com.neslihanatalay.ibb_ecodation_javafx;

import com.neslihanatalay.ibb_ecodation_javafx.dao.ResourceBundleBinding;
import com.neslihanatalay.ibb_ecodation_javafx.database.SingletonPropertiesDBConnection;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Locale;

public class HelloApplication extends Application {

    private final ResourceBundleBinding resourceBundleBinding;
    @FXML private ComboBox<Locale> languageSelectComboBox;
	
    public HelloApplication() {
        resourceBundleBinding = new ResourceBundleBinding();
    }
	
    private static final String RESOURCE_NAME = Resources.class.getTypeName();
	
    private static final ObservableResourceFactory RESOURCE_FACTORY = new ObservableResourceFactory();
	
    static {
	RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME));
    }
	
    @FXML
    public void initialize() {
	languageSelectComboBox = new ComboBox<>();
	languageSelectComboBox.getItems().add(null);
        languageSelectComboBox.getItems().addAll(Locale.ENGLISH, Locale.TURKISH);
        languageSelectComboBox.setValue(Locale.TURKISH);
	languageSelectComboBox.setCellFactory(lv -> new LocaleCell());
	languageSelectComboBox.setButtonCell(new LocaleCell());
		
	//languageSelectComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {		
	languageSelectComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
	    if (newValue != null) {
	        RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME, newValue));
	    }
	});
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        dataSet();
	//if (languageSelectComboBox.getValue() == Locale.TURKISH)
	if (languageSelectComboBox.getValue().equals(Locale.TURKISH)) {
	    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/loginTR.fxml"));
	} else if (languageSelectComboBox.getValue().equals(Locale.ENGLISH)) {
	    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/loginEN.fxml"));
	}
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/login.fxml"));
        Parent parent = fxmlLoader.load();
        //stage.setTitle("Kullanıcı Yönetimi Login Sayfası");
	stage.setTitle(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıyönetimigiriş"));
        stage.setScene(new Scene(parent));
        stage.show();
    }


    public static void dataSet() throws SQLException {
        Connection connection = SingletonPropertiesDBConnection.getInstance().getConnection();

        try (Statement stmt = connection.createStatement()) {
		
        String createUserTableSQL = """
        CREATE TABLE IF NOT EXISTS usertable (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(50) NOT NULL UNIQUE,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(100) NOT NULL UNIQUE,
            role VARCHAR(50) DEFAULT 'USER'
			count INT DEFAULT '0'
        );
    """;
            stmt.execute(createUserTableSQL);

        String createKdvTableSQL = """
        CREATE TABLE IF NOT EXISTS kdv_table (
            id INT AUTO_INCREMENT PRIMARY KEY,
            amount DOUBLE NOT NULL,
            kdvRate DOUBLE NOT NULL,
            kdvAmount DOUBLE NOT NULL,
            totalAmount DOUBLE NOT NULL,
            receiptNumber VARCHAR(100) NOT NULL,
            transactionDate DATE NOT NULL,
            description VARCHAR(255),
            exportFormat VARCHAR(50)
        );
    """;
            stmt.execute(createKdvTableSQL);
			
	String createNotebookTableSQL = """
        CREATE TABLE IF NOT EXISTS notebooktable (
            id INT AUTO_INCREMENT PRIMARY KEY,
            title VARCHAR(80),
            content VARCHAR(MAX),
            createdDate DATE,
			updateddDate DATE,
			category VARCHAR(50) DEFAULT 'PERSONAL',
			pinned BIT,
			userId INT,
			//CONSTRAINT FK_NOTEBOOK_USER
			FOREIGN KEY (userId) 
				REFERENCES usertable(id)			
        );
    """;
            stmt.execute(createNotebookTableSQL);
        }

        String insertSQL = """
            MERGE INTO usertable (username, password, email, role, count)
            KEY(username) VALUES (?, ?, ?, ?, ?);
        """;

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, "neslihanatalay");
            ps.setString(2, BCrypt.hashpw("root", BCrypt.gensalt()));
            ps.setString(3, "atalay.neslihan.2015@gmail.com");
            ps.setString(4, "USER");
	    ps.setInt(5, 0);
            ps.executeUpdate();

            // 2. kullanıcı
            ps.setString(1, "admin");
            //ps.setString(2, BCrypt.hashpw("root", BCrypt.gensalt()));
            ps.setString(2, BCrypt.hashpw("root", BCrypt.gensalt()));
            ps.setString(3, "admin@gmail.com");
            ps.setString(4, "ADMIN");
	    ps.setInt(5, 0);
            ps.executeUpdate();

            // 3. kullanıcı
            ps.setString(1, "root");
            //ps.setString(2, BCrypt.hashpw("root", BCrypt.gensalt()));
            ps.setString(2, BCrypt.hashpw("root", BCrypt.gensalt()));
            ps.setString(3, "root@gmail.com");
            ps.setString(4, "ADMIN");
	    ps.setInt(5, 0);
            ps.executeUpdate();
        }

        //System.out.println("✅ BCrypt ile şifrelenmiş ve roller atanmış kullanıcılar başarıyla eklendi.");
    }

    public static void main(String[] args) {
        launch();
    }
}
