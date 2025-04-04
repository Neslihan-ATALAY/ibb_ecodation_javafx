# JavaFX Desktop
[Java FX GitHub Address](https://github.com/neslihanatalay/ibb_ecodation_javafx)
[Java Core](https://github.com/neslihanatalay/ibb_ecodation_javacore.git )

## Git
```sh 
git init
git add .
git commit -m "initalize javafx"
git remote add origin URL
git push -u origin master
```

## Git Clone 
```sh 
git clone https://github.com/neslihanatalay/ibb_ecodation_javafx
```

## JDK Dikkat
```sh 
JDK JavaFx bizlere önerdiği JDK sürümü 17'dir.
```

## Eğer JDK ile alakalı hatalar alırsak nereleri JDK 17 yapmalıyız ?
```sh 
Settings => Build, Execution => Compiler => Build Compiler (JDK 17 seçelim)
Projects Structure => Project (JDK 17 seçelim)
Projects Structure => Modules => Module,Source,Dependency (JDK 17 seçelim)
Projects Structure => SDK =>  (JDK 17 seçelim)

Build => Rebuild Project
```

## Eğer durduk yere veya JDK değiştirdikten sonra sistem çalışmazsa;
```sh 
Build => Rebuild Project
```

## Maven Codes
```sh 
mvn clean
mvn clean install
mvn clean package
mvn clean package -DskipTests
```

## SingletoDesign Pattern
```sh 

```

 **Singleton Design Pattern** kullanarak **H2 Database** için bir **Connection (Bağlantı) sınıfı** oluşturalım. 

---

### **Neden Singleton Pattern Kullanıyoruz?**
- **Bağlantı havuzu yönetimi**: Tek bir bağlantı örneği, kaynakların gereksiz yere tükenmesini engeller.
- **Bellek yönetimi**: Tek bir bağlantı nesnesi, bellek tüketimini optimize eder.
- **Senkronizasyon**: Eşzamanlı erişimlerin düzgün yönetilmesini sağlar.

---

### **Kodun İçeriği:**
1. **DatabaseConnection Singleton Sınıfı**
    - H2 veritabanına bağlanır.
    - `getInstance()` metodu ile tek bir örnek oluşturur.
    - `getConnection()` metodu ile bağlantıyı döndürür.
    - Uygulama kapatıldığında kaynakları temizler.

2. **TestDatabaseConnection Ana Sınıfı**
    - Singleton örneğini kullanarak H2'ye bağlanır.
    - Örnek bir tablo oluşturur ve veri ekler.
    - Eklenen veriyi okur ve görüntüler.

---

### **Kod:**
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Singleton Design Pattern ile H2 Database Bağlantı Yönetimi
public class DatabaseConnection {
    private static DatabaseConnection instance; // Singleton örneği
    private Connection connection;
    
    // H2 Database Bağlantı Bilgileri
    private static final String URL = "jdbc:h2:~/testdb"; // H2 dosya tabanlı veritabanı
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Özel Constructor (Dışarıdan erişilemez)
    private DatabaseConnection() {
        try {
            // JDBC sürücüsünü yükle
            Class.forName("org.h2.Driver");
            // Bağlantıyı oluştur
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("H2 Database bağlantısı başarılı!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Bağlantı oluşturulamadı!");
        }
    }

    // Singleton Instance Alma Metodu
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Bağlantıyı Alma Metodu
    public Connection getConnection() {
        return connection;
    }

    // Kaynakları Temizleme (Uygulama Kapanırken)
    public static void closeConnection() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

// Singleton Sınıfı Kullanan Test Sınıfı
class TestDatabaseConnection {
    public static void main(String[] args) {
        // Singleton Instance ile Bağlantıyı Al
        DatabaseConnection dbInstance = DatabaseConnection.getInstance();
        Connection conn = dbInstance.getConnection();

        try {
            Statement stmt = conn.createStatement();

            // Örnek bir tablo oluştur
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Users ("
                                  + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                  + "name VARCHAR(255), "
                                  + "email VARCHAR(255))";
            stmt.execute(createTableSQL);
            System.out.println("Users tablosu oluşturuldu!");

            // Veri Ekleme
            String insertDataSQL = "INSERT INTO Users (name, email) VALUES "
                                 + "('Ali Veli', 'ali@example.com'), "
                                 + "('Ayşe Fatma', 'ayse@example.com')";
            stmt.executeUpdate(insertDataSQL);
            System.out.println("Veriler eklendi!");

            // Veri Okuma
            String selectSQL = "SELECT * FROM Users";
            ResultSet rs = stmt.executeQuery(selectSQL);

            System.out.println("\nUsers Tablosu İçeriği:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                   ", Name: " + rs.getString("name") + 
                                   ", Email: " + rs.getString("email"));
            }

            // Bağlantıyı Kapat
            DatabaseConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

### **Kodun Çalışma Mantığı:**
1. **`DatabaseConnection` Singleton Sınıfı**
    - İlk çağrıldığında `instance` değişkeni `null` olduğu için `new DatabaseConnection()` ile nesne oluşturulur.
    - `getInstance()` ile bu tek örnek çağrılır.
    - `getConnection()` metodu ile bağlantı nesnesi döndürülür.

2. **H2 Veritabanı ile İşlemler**
    - `CREATE TABLE` ile `Users` tablosu oluşturulur (eğer yoksa).
    - `INSERT INTO` ile tabloya iki kullanıcı eklenir.
    - `SELECT * FROM` sorgusu ile eklenen veriler çekilir ve ekrana yazdırılır.

3. **Bağlantıyı Yönetme**
    - Program sonunda `DatabaseConnection.closeConnection()` ile bağlantı kapatılır.

---

### **Çıktı Örneği:**
```
H2 Database bağlantısı başarılı!
Users tablosu oluşturuldu!
Veriler eklendi!

Users Tablosu İçeriği:
ID: 1, Name: Ali Veli, Email: ali@example.com
ID: 2, Name: Ayşe Fatma, Email: ayse@example.com
Veritabanı bağlantısı kapatıldı.
```

---

### **Avantajlar:**
✅ **Singleton** kullanarak tek bir bağlantı nesnesiyle işlem yapıyoruz.  
✅ **H2 Database** ile bağımsız ve hafif bir test ortamı sağlıyoruz.  
✅ **Thread-Safe** ve **Lazy Initialization** sayesinde gereksiz bağlantı oluşturulmuyor.  
✅ **Bağlantı yönetimi** sayesinde kaynak israfı önleniyor.

Bu kodu kendi projelerinde rahatlıkla kullanabilirsin! 🚀


## Singleton Config Properties
```sh 

```
Mevcut **SingletonDBConnection** sınıfına ekleyebileceğin bazı geliştirmeler ve ekstra özellikler:

### **1. Daha Esnek ve Özelleştirilebilir Bağlantı Yönetimi**
Şu an bağlantı bilgileri sınıf içinde sabit olarak tanımlanmış. Aşağıdaki geliştirmeleri yapabilirsin:
- **Config dosyasından (properties veya environment) bağlantı bilgilerini okumak.**
- **Bağlantı zaman aşımı eklemek.**
- **Birden fazla veritabanı bağlantısını yönetmek (multi-database support).**

#### **Geliştirilmiş Bağlantı Yapılandırması**
```java
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingletonDBConnection {
    private static SingletonDBConnection instance;
    private Connection connection;

    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    // Constructor (private)
    private SingletonDBConnection() {
        try {
            loadDatabaseConfig(); // Konfigürasyonu oku
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Veritabanı bağlantısı başarılı");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı bağlantısı başarısız!");
        }
    }

    // Konfigürasyonu yükleme
    private static void loadDatabaseConfig() {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            URL = properties.getProperty("db.url", "jdbc:h2:~/h2db/user_management");
            USERNAME = properties.getProperty("db.username", "sa");
            PASSWORD = properties.getProperty("db.password", "");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı yapılandırması yüklenemedi!");
        }
    }

    // Singleton Instance
    public static synchronized SingletonDBConnection getInstance() {
        if (instance == null) {
            instance = new SingletonDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            } catch (SQLException e) {
                throw new RuntimeException("Bağlantı kapatılırken hata oluştu!", e);
            }
        }
    }
}
```
#### **Örnek `config.properties` Dosyası**
```
db.url=jdbc:h2:~/h2db/user_management
db.username=sa
db.password=
```
**➜ Avantajları:**
- Bağlantı bilgileri hard-coded yerine **config dosyasından** alınır.
- Farklı veritabanı bağlantılarını yönetmek daha kolay olur.
- Uygulamanın daha taşınabilir ve güvenli olması sağlanır.

---

### **2. Bağlantı Sağlığını Kontrol Etme (Connection Health Check)**
Bağlantının açık olup olmadığını anlamak için aşağıdaki metodu ekleyebilirsin:
```java
public boolean isConnectionValid() {
    try {
        return connection != null && !connection.isClosed();
    } catch (SQLException e) {
        return false;
    }
}
```
Kullanımı:
```java
if(SingletonDBConnection.getInstance().isConnectionValid()) {
    System.out.println("Bağlantı aktif!");
} else {
    System.out.println("Bağlantı kapalı!");
}
```
**➜ Avantajları:**
- Bağlantının düşüp düşmediğini anlayarak yeniden bağlanma stratejileri geliştirilebilir.

---

### **3. Bağlantı Yeniden Başlatma (Reconnect)**
Eğer bağlantı zamanla koparsa, aşağıdaki gibi bir **reconnect()** metodu ekleyebilirsin:
```java
public void reconnect() {
    try {
        if (connection == null || connection.isClosed()) {
            instance = new SingletonDBConnection();
            System.out.println("Veritabanına yeniden bağlanıldı.");
        }
    } catch (SQLException e) {
        throw new RuntimeException("Bağlantı yeniden başlatılamadı!", e);
    }
}
```
Kullanımı:
```java
SingletonDBConnection.getInstance().reconnect();
```
**➜ Avantajları:**
- Uygulama çalışırken bağlantı koparsa, programın çökmesini önler.
- Otomatik bağlantı yenileme özelliği eklenmiş olur.

---

### **4. Logging Mekanizması Ekleme**
Şu an hata yönetimi `System.out.println()` ile yapılıyor. Bunun yerine bir **Logger** kullanabilirsin:
```java
import java.util.logging.Logger;

private static final Logger LOGGER = Logger.getLogger(SingletonDBConnection.class.getName());

private SingletonDBConnection() {
    try {
        Class.forName("org.h2.Driver");
        this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        LOGGER.info("Veritabanı bağlantısı başarılı!");
    } catch (Exception e) {
        LOGGER.severe("Bağlantı hatası: " + e.getMessage());
        throw new RuntimeException("Veritabanı bağlantısı başarısız!", e);
    }
}
```
**➜ Avantajları:**
- Hataların **log dosyasına** yazılması sağlanır.
- Sistem takip edilebilir hale gelir.

---

### **5. Bağlantı Havuzu (Connection Pool) Kullanımı**
- Eğer uygulamada çok fazla eşzamanlı bağlantı gerekecekse, **Singleton yerine Connection Pool (HikariCP gibi kütüphaneler)** kullanılabilir.
- HikariCP gibi kütüphaneler kullanarak performansı artırabilirsin.

Örnek:
```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class HikariCPDatabase {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:~/h2db/user_management");
        config.setUsername("sa");
        config.setPassword("");
        config.setMaximumPoolSize(10); // Maksimum 10 bağlantı
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
```
**➜ Avantajları:**
- Tek bir bağlantı yerine, havuzdan **birden fazla bağlantı yönetilebilir**.
- Performans önemli ölçüde artar.

---

### **Sonuç ve Özet**
Mevcut **SingletonDBConnection** sınıfına ekleyebileceğin özellikler:
✅ **Bağlantı bilgilerini config dosyasından okumak.**  
✅ **Bağlantının durumunu kontrol etmek (`isConnectionValid()`).**  
✅ **Bağlantıyı yeniden başlatmak (`reconnect()`).**  
✅ **Logging mekanizması eklemek (`Logger`).**  
✅ **Bağlantı havuzu (HikariCP) kullanmak.**

**Gelecekte ekleyebileceğin özellikler:**
- **Bağlantı hatalarını otomatik olarak ele alan bir Retry mekanizması.**
- **Şifreleri güvenli hale getirmek için şifreleme mekanizması (örneğin, Java KeyStore kullanımı).**
- **Bağlantı yönetimini daha esnek hale getirmek için Dependency Injection (DI) ile yönetmek (Spring, Guice).**

Bu eklemelerle kodunu daha sağlam, esnek ve güvenli hale getirebilirsin! 🚀


## VBOX
```sh 

```
JavaFX’te `VBox`, bir **layout (yerleşim) yöneticisidir**. Yani sahnedeki bileşenlerin (button, label, textfield vb.) nasıl konumlandırılacağını belirleyen bir konteynerdir. `VBox` özel olarak **bileşenleri dikey (vertical)** olarak yerleştirir. Her yeni bileşen, bir öncekinin **altına** gelecek şekilde sıralanır.

---

## 🔷 Tanım

### 📌 `VBox (Vertical Box)`
`VBox`, JavaFX’te birden fazla UI bileşenini **dikey olarak (üstten alta)** sıralamak için kullanılır.

---

## 🧠 Temel Özellikleri

| Özellik             | Açıklama |
|---------------------|---------|
| Dikey hizalama      | Bileşenleri üstten alta sıralar. |
| `spacing`           | Bileşenler arasında boşluk bırakmak için kullanılır. |
| `alignment`         | İçeriklerin hizasını belirler (`CENTER`, `TOP_LEFT` vb.). |
| `padding`           | Kutu içindeki kenar boşluklarını ayarlar. |
| `setFillWidth(true)`| Bileşenlerin kutunun genişliğini doldurmasını sağlar. |

---

## 📎 FXML Kullanımı

```xml
<VBox spacing="10" alignment="CENTER" padding="20"
      xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml">
    
    <Label text="Kullanıcı Adı:" />
    <TextField fx:id="usernameField" />
    <Label text="Şifre:" />
    <PasswordField fx:id="passwordField" />
    <Button text="Giriş Yap" onAction="#handleLogin" />
    
</VBox>
```

### 🔍 Ne yapıyor bu VBox?

- Tüm bileşenler dikey sırayla gösteriliyor.
- Her bileşen arasında `10px` boşluk var (`spacing="10"`).
- İçerikler `CENTER` hizalanmış.
- Kenarlardan `20px` boşluk var (`padding="20"`).

---

## 📦 Java Koduyla VBox Kullanımı

```java
VBox vbox = new VBox();
vbox.setSpacing(10);
vbox.setAlignment(Pos.CENTER);
vbox.setPadding(new Insets(20));

Label label = new Label("Ad:");
TextField textField = new TextField();
Button button = new Button("Gönder");

vbox.getChildren().addAll(label, textField, button);

Scene scene = new Scene(vbox, 300, 200);
stage.setScene(scene);
stage.show();
```

---

## 🧱 VBox ile HBox Farkı

| Özellik      | VBox                        | HBox                        |
|--------------|-----------------------------|-----------------------------|
| Yerleşim     | Dikey (üstten alta)         | Yatay (soldan sağa)         |
| Kullanım Alanı | Formlar, liste görünümleri  | Menü çubukları, araç çubukları |

---

## 🎯 Nerede Kullanılır?

- Form tasarımlarında (Login ekranı, kayıt formu vb.)
- Dikey menülerde
- Liste veya kart görünümlerinde
- Sayfa düzenlemesi yaparken içeriği sırayla aşağı yerleştirmek istediğinde

---

## 📌 Özet

> `VBox`, JavaFX'te bileşenleri **üstten aşağı doğru sıralayan** bir layout yöneticisidir. İçerikleri düzenli, hizalı ve okunabilir şekilde yerleştirmede büyük kolaylık sağlar.

---

aynı örneği `HBox`, `GridPane` veya `BorderPane` gibi diğer layout’larla da karşılaştırmalı 


## HBox,GridPane,BorderPane
```sh 

```
Süper! O zaman şimdi JavaFX'teki en sık kullanılan **layout (yerleşim) yöneticilerini** senin için karşılaştıralım. Her birinin kullanım amacı farklıdır, tasarım yaparken hangisini seçeceğin bu farklara göre değişir.

---

## 🔧 JavaFX Layout Yöneticileri Karşılaştırması

| Layout       | Yerleşim Şekli                         | Avantajları                              | Ne Zaman Kullanılır?                  |
|--------------|----------------------------------------|------------------------------------------|---------------------------------------|
| `VBox`       | Dikey (üstten alta)                    | Basit form yapıları, kolay hizalama      | Formlar, login ekranları              |
| `HBox`       | Yatay (soldan sağa)                    | Menü barı, buton grupları için ideal     | ToolBar, navbar                       |
| `GridPane`   | Satır ve sütun (grid) tabanlı          | Hücre hücre kontrol, tablo görünümü      | Formlar, karmaşık düzenlemeler        |
| `BorderPane` | Beş bölge: Top, Bottom, Left, Right, Center | Sayfa iskeleti için ideal            | Ana pencere düzeni                    |
| `StackPane`  | Bileşenleri üst üste yerleştirir       | Basit overlay (kaplama) sistemleri       | Pop-up, yüzen içerikler               |
| `FlowPane`   | Bileşenleri otomatik sıraya dizer      | Responsive görünüm, sığmayanlar alt satıra geçer | Kartlar, dinamik liste                |
| `AnchorPane` | Bileşenleri kenarlara sabitler         | Özgür pozisyonlama, piksel hassasiyet    | Özgün tasarımlar, özel UI ihtiyaçları |

---

## 🔍 Aynı İçeriğin 4 Farklı Layout ile Uygulanışı

Örnek olarak: **Ad – Şifre – Giriş Butonu** olan bir login ekranı yapalım.

---

### ✅ VBox ile

```xml
<VBox spacing="10" alignment="CENTER" padding="20">
    <Label text="Ad" />
    <TextField />
    <Label text="Şifre" />
    <PasswordField />
    <Button text="Giriş Yap" />
</VBox>
```

- Kolay ve sade dikey form yapısı
- Sıralama çok net
- Mobil görünüme uygun

---

### ✅ HBox ile

```xml
<HBox spacing="10" alignment="CENTER" padding="20">
    <Label text="Ad" />
    <TextField />
    <Button text="Giriş Yap" />
</HBox>
```

- Her şey **aynı satırda**
- Küçük formlar veya hızlı işlem butonları için ideal
- Alanı yatay kullanan uygulamalarda avantajlı

---

### ✅ GridPane ile

```xml
<GridPane hgap="10" vgap="10" padding="20">
    <Label text="Ad" GridPane.rowIndex="0" GridPane.columnIndex="0"/>
    <TextField GridPane.rowIndex="0" GridPane.columnIndex="1"/>
    
    <Label text="Şifre" GridPane.rowIndex="1" GridPane.columnIndex="0"/>
    <PasswordField GridPane.rowIndex="1" GridPane.columnIndex="1"/>
    
    <Button text="Giriş Yap" GridPane.rowIndex="2" GridPane.columnIndex="1"/>
</GridPane>
```

- Hücre bazlı tasarım
- Kolon hizalamaları mükemmel olur
- Özellikle **karmaşık form** ya da tablolarda kullanılır

---

### ✅ BorderPane ile

```xml
<BorderPane padding="20">
    <top>
        <Label text="Giriş Paneli" />
    </top>
    <center>
        <VBox spacing="10">
            <TextField promptText="Ad" />
            <PasswordField promptText="Şifre" />
            <Button text="Giriş Yap" />
        </VBox>
    </center>
</BorderPane>
```

- Sayfayı 5'e böler: top, left, right, center, bottom
- Profesyonel bir uygulama iskeleti kurmak için ideal
- Menü üstte, içerik ortada, footer altta şeklinde yapılar kurulur

---

## 🧠 Hangi Layout Ne Zaman Kullanılır?

| Senaryo                           | Tercih Edilecek Layout  |
|----------------------------------|--------------------------|
| Basit form tasarımı              | `VBox` veya `GridPane`  |
| Yatay buton dizisi               | `HBox`                  |
| Sayfa iskeleti (başlık, içerik, altlık) | `BorderPane`           |
| Duyarlı ve dinamik içerikler     | `FlowPane`              |
| Özgürce konumlandırmak istiyorsan| `AnchorPane`            |
| Popup, modal gibi yüzen içerikler| `StackPane`             |

---

## 🎁 Bonus: VBox + HBox Kombinasyonu

```xml
<VBox spacing="15" padding="20">
    <Label text="Giriş Paneli" />
    
    <HBox spacing="10">
        <Label text="Kullanıcı Adı:" />
        <TextField />
    </HBox>

    <HBox spacing="10">
        <Label text="Şifre:" />
        <PasswordField />
    </HBox>

    <Button text="Giriş Yap" />
</VBox>
```

- Her satırda yatay düzen (`HBox`)
- Tüm satırlar dikey sıralanıyor (`VBox`)
- Bu kombinasyon çok kullanılır.

---



## HBox,GridPane,BorderPane (Örneği)
```sh 

```
Süper! O zaman sana **JavaFX layout’larını içeren mini bir örnek proje** hazırlayayım. Bu örnekte:

- Farklı layout türlerini (VBox, HBox, GridPane, BorderPane) kullanacağız,
- Her layout bir sekmede (TabPane içinde) yer alacak,
- Login formu örneğini her layout ile nasıl yaparız, onu göstereceğim.

---

## 🎯 Proje Yapısı

```
LayoutDemo/
│
├── src/
│   └── com/example/layoutdemo/
│       ├── MainApp.java
│       └── LayoutDemoView.java
```

---

## 1. `MainApp.java`

```java
package com.example.layoutdemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LayoutDemoView layoutDemoView = new LayoutDemoView();
        Scene scene = new Scene(layoutDemoView.getView(), 500, 350);

        primaryStage.setTitle("JavaFX Layout Örnekleri");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

## 2. `LayoutDemoView.java`

```java
package com.example.layoutdemo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LayoutDemoView {

    public Parent getView() {
        TabPane tabPane = new TabPane();

        Tab vboxTab = new Tab("VBox");
        vboxTab.setContent(createVBoxExample());
        vboxTab.setClosable(false);

        Tab hboxTab = new Tab("HBox");
        hboxTab.setContent(createHBoxExample());
        hboxTab.setClosable(false);

        Tab gridTab = new Tab("GridPane");
        gridTab.setContent(createGridExample());
        gridTab.setClosable(false);

        Tab borderTab = new Tab("BorderPane");
        borderTab.setContent(createBorderExample());
        borderTab.setClosable(false);

        tabPane.getTabs().addAll(vboxTab, hboxTab, gridTab, borderTab);

        return tabPane;
    }

    private VBox createVBoxExample() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Ad:");
        TextField nameField = new TextField();

        Label passLabel = new Label("Şifre:");
        PasswordField passField = new PasswordField();

        Button loginBtn = new Button("Giriş Yap");

        vbox.getChildren().addAll(nameLabel, nameField, passLabel, passField, loginBtn);
        return vbox;
    }

    private HBox createHBoxExample() {
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(20));
        hbox.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Ad:");
        TextField nameField = new TextField();

        Button loginBtn = new Button("Giriş Yap");

        hbox.getChildren().addAll(nameLabel, nameField, loginBtn);
        return hbox;
    }

    private GridPane createGridExample() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label nameLabel = new Label("Ad:");
        TextField nameField = new TextField();

        Label passLabel = new Label("Şifre:");
        PasswordField passField = new PasswordField();

        Button loginBtn = new Button("Giriş Yap");

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(loginBtn, 1, 2);

        return grid;
    }

    private BorderPane createBorderExample() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        Label header = new Label("Giriş Paneli");
        BorderPane.setAlignment(header, Pos.CENTER);
        borderPane.setTop(header);

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Ad");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Şifre");

        Button loginBtn = new Button("Giriş Yap");

        centerBox.getChildren().addAll(nameField, passField, loginBtn);
        borderPane.setCenter(centerBox);

        return borderPane;
    }
}
```

---

## 🧪 Projeyi Çalıştırınca Ne Olur?

- Ekranda bir sekme sistemi (`TabPane`) görünür,
- Her sekmede farklı bir layout türü kullanılarak yapılmış login formu gösterilir,
- Hepsini aynı veri yapısıyla karşılaştırma şansın olur.

---

## 🛠 Geliştirme Önerileri

- Her form girişine bir `Label`, `TextField`, `Button` kombinasyonu eklersen form kullanımı daha anlamlı olur.
- `onAction` eventleriyle butonlara işlev kazandırabilirsin.
- Layout’lara CSS ekleyerek görselliği güçlendirebilirsin.

---


## Other
```sh 

```






