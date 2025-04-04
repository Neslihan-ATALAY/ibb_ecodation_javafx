# JavaFX Desktop

[Java FX GitHub Address](https://github.com/neslihanatalay/ibb_ecodation_javafx)
[Java Core](https://github.com/hamitmizrak/ibb_ecodation_javacore.git )
[Java Core](https://github.com/neslihanatalay/ibb_ecodation_javacore.git )

## JavaFX

```sh 

```

JavaFX, **Java tabanlı bir kullanıcı arayüz (UI) kütüphanesidir** ve modern, zengin internet uygulamaları (RIA – Rich
Internet Applications) geliştirmek için kullanılır. JavaFX, Java'nın yerine geçmek üzere tasarlanmış **Swing ve AWT'nin
modern halefidir**, ancak halen Swing ile birlikte de kullanılabilir. Çok detaylı olarak JavaFX'in ne olduğunu,
mimarisini, kullanım alanlarını ve diğer detayları aşağıda açıklıyorum.

---

## 🔹 JavaFX Nedir?

JavaFX, **grafiksel kullanıcı arayüzleri (GUI)** oluşturmak için kullanılan bir Java kütüphanesidir. Masaüstü, mobil ve
gömülü sistemlerde çHamit MIZRAKşan **platformdan bağımsız** uygulamalar geliştirmek için uygundur. Modern UI
ihtiyaçlarına yönelik olarak **CSS, FXML ve multimedya desteğiyle** zenginleştirilmiştir.

JavaFX, ilk olarak **2008 yılında Sun Microsystems tarafından tanıtıldı**, Oracle’ın Java’yı devralmasıyla gelişmeye
devam etti. Java 8 sürümünden itibaren JDK içinde yer aldı ancak Java 11 ile birlikte ayrı bir modül (OpenJFX) hHamit
MIZRAKne geldi.

---

## 🔹 JavaFX'in Özellikleri

### 1. 🧱 **Modern GUI Bileşenleri**

- Düğme (Button), metin kutusu (TextField), tablo (TableView), ağaç görünümü (TreeView), sekmeler (TabPane) gibi birçok
  GUI bileşeni içerir.
- Tüm bileşenler **scene graph (sahne grafiği)** mantığıyla yönetilir.

### 2. 🎨 **CSS ile Stil Desteği**

- HTML/CSS'teki gibi JavaFX bileşenlerinin görünümü CSS ile özelleştirilebilir.
- Uygulamalara tematik görünüm kazandırmak kolaylaşır.

### 3. 📄 **FXML (XML tabanlı UI tanımı)**

- UI tasarımı XML ile yapılabilir, böylece tasarım ve mantık ayrımı sağlanır.
- FXML, Scene Builder (bir GUI aracı) ile kolayca oluşturulabilir.

### 4. 🎞️ **Multimedya Desteği**

- Video, ses ve resim dosyaları doğrudan JavaFX ile oynatılabilir ve görüntülenebilir.

### 5. 📐 **2D/3D Grafik Desteği**

- 2D çizimler (çizgiler, daireler, şekiller) ve basit 3D modeller oluşturulabilir.
- Grafikler animasyonlarla desteklenebilir.

### 6. 🧮 **Animasyonlar**

- Zaman çizelgesi ve geçiş efektleriyle kullanıcı dostu animasyonlar yapılabilir.
- Örneğin, nesne büyütme/küçültme, dönme, konum değiştirme vb.

### 7. 🔁 **Olay Yönetimi (Event Handling)**

- Buton tıklama, klavye, fare hareketleri gibi olaylara müdahale edilebilir.
- Lambda ifadeleri ile kısa, okunabilir kodlar yazılabilir.

---

## 🔹 JavaFX Mimarisi

JavaFX mimarisi şu temel katmanlardan oluşur:

1. **Scene Graph (Sahne Grafiği)**
    - UI bileşenlerinin hiyerarşik olarak temsil edildiği yapı.
    - Her şey bir "Node"dur (düğüm) ve sahneye eklenir.

2. **Stage ve Scene**
    - `Stage`: Ana pencereyi temsil eder.
    - `Scene`: Pencere içeriğini tutar, bir sahne birden fazla node içerebilir.

3. **Controls ve Layouts**
    - Kontroller: UI öğeleri (Button, Label, TableView...)
    - Layouts: UI öğelerinin nasıl yerleştirileceğini belirleyen yapılar (VBox, HBox, GridPane...)

4. **Media Engine**
    - Video ve ses çalma bileşenleri.

5. **WebView**
    - Web sayfalarını JavaFX arayüzü içinde görüntüleme.

---

## 🔹 JavaFX Kullanım Alanları

- **Masaüstü uygulamaları**
- **Veri görselleştirme araçları**
- **POS sistemleri**
- **Medikal yazılımlar**
- **Eğitim yazılımları**
- **Multimedya oynatıcılar**
- **Çizim/Simülasyon uygulamaları**

---

## 🔹 JavaFX ile Basit Bir Uygulama Örneği

```java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MerhabaJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Tıklayın");
        btn.setOnAction(e -> System.out.println("Butona tıklandı!"));

        StackPane root = new StackPane(btn);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("JavaFX Uygulaması");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

## 🔹 JavaFX vs Swing

| Özellik             | JavaFX                                    | Swing                                         |
|---------------------|-------------------------------------------|-----------------------------------------------|
| UI Modernliği       | Modern, CSS destekli                      | Eski ve temel stiller                         |
| Ayrıştırılabilirlik | FXML ile mantık/arayüz ayrımı             | Kod içinde UI tanımı                          |
| Multimedya Desteği  | Doğrudan var                              | Harici kütüphaneler gerekir                   |
| 2D/3D Grafik        | Var                                       | Sınırlı destek                                |
| Öğrenme Eğrisi      | Biraz daha dik                            | Daha basit                                    |
| Gelecek Desteği     | OpenJFX topluluğu tarafından sürdürülüyor | Swing hâlâ destekleniyor ancak geliştirme yok |

---

## 🔹 JavaFX ile Geliştirme Ortamları

- **IDE'ler**: IntelliJ IDEA, Eclipse, NetBeans
- **Scene Builder**: FXML dosyaları için görsel tasarım aracı

---

## 🔹 JavaFX’in Modüler Yapısı (Java 9+)

Java 9 sonrası gelen **modüler sistem (JPMS)** ile JavaFX ayrı modüllere ayrıldı:

```bash
--module-path /path/to/javafx-sdk/lib --add-modules=javafx.controls,javafx.fxml
```

---

## 🔹 OpenJFX Nedir?

JavaFX artık JDK ile birlikte gelmediğinden dolayı **OpenJFX** adıyla ayrı bir açık kaynak proje olarak devam
etmektedir.

GitHub: [https://github.com/openjdk/jfx](https://github.com/openjdk/jfx)

---

## Özetle

JavaFX, modern ve zengin masaüstü uygulamaları geliştirmek için güçlü bir Java kütüphanesidir. **Swing'e göre çok daha
esnek, şık ve geliştirici dostudur.** CSS, FXML, multimedya, animasyon ve olay yönetimi gibi birçok özelliği sayesinde
Java tabanlı modern GUI’ler oluşturmak mümkündür.

---

## JavaFX Örnek bir Proje

```sh 

```

Harika! Aşağıda JavaFX kullanarak hazırlanmış basit ama yapılandırılmış bir örnek masaüstü projesi sunuyorum. Bu
örnekte:

- FXML dosyasıyla arayüz tanımlandı.
- Bir controller sınıfıyla olay yönetimi sağlandı.
- Ana sınıf uygulamayı başlatıyor.
- Scene Builder ile çHamit MIZRAKşabilecek şekilde yapılandırılmıştır.

---

## 📁 Örnek Proje: JavaFX İsim Giriş ve Selamlama Uygulaması

### 🔹 Proje Yapısı

```
JavaFXExample/
├── src/
│   ├── application/
│   │   ├── Main.java
│   │   ├── Controller.java
│   │   └── view.fxml
└── lib/
```

---

### 🔸 `Main.java`

```java
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Selamlama Uygulaması");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

### 🔸 `Controller.java`

```java
package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField nameField;

    @FXML
    private Label greetingLabel;

    @FXML
    private void greetUser() {
        String name = nameField.getText();
        greetingLabel.setText("Merhaba, " + name + "!");
    }
}
```

---

### 🔸 `view.fxml`

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>

<VBox spacing="10.0" Hamit MIZRAKgnment="CENTER" xmlns:fx="http://javafx.com/fxml"
      fx:controller="application.Controller">
    <padding>
        <Insets top="20" right="20" bottom="20" left="20"/>
    </padding>

    <Label text="Adınızı girin:"/>
    <TextField fx:id="nameField" promptText="Adınız"/>
    <Button text="Selamla" onAction="#greetUser"/>
    <Label fx:id="greetingLabel" text=""/>
</VBox>
```

---

## 🧱 Gerekli Kurulumlar

### ✅ JavaFX SDK Yükle

[https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/) adresinden JavaFX SDK’yı indir.

### ✅ ÇHamit MIZRAKştırma Ayarları (IDE için VM options)

JavaFX modüllerini belirtmen gerekiyor:

```
--module-path "path/to/javafx-sdk-XX/lib" --add-modules javafx.controls,javafx.fxml
```

---

## ✅ Alternatif: Scene Builder ile Arayüz Geliştirme

FXML dosyasını Scene Builder ile açarak görsel olarak arayüzü düzenleyebilirsin.

İndir: [https://gluonhq.com/products/scene-builder/](https://gluonhq.com/products/scene-builder/)

---

## 🎯 Proje Ne Yapıyor?

Kullanıcıdan adını Hamit MIZRAKyor ve bir etiket üzerinde şu şekilde selamlıyor:

> "Merhaba, Hamit MIZRAK!"

---

## JavaFX MVC Mimarisi

```sh 

```

JavaFX ile geliştirilen uygulamalarda **MVC (Model-View-Controller)** mimarisi, kodun **daha sürdürülebilir, okunabilir,
test edilebilir ve geliştirilebilir** olmasını sağlar. JavaFX, GUI (grafik kullanıcı arayüzü) geliştirmek için
kullanılan güçlü bir Java kütüphanesidir. Bu yapıda **katmanlı mimari** (layered architecture) ile MVC'nin birleşimi,
yazılımın hem mantıksal hem de fiziksel olarak ayrılmasını sağlar.

Aşağıda **JavaFX MVC katmanlı mimari yapısını** tüm ayrıntılarıyla açıklıyorum:

---

## 1. MVC Mimarisi Nedir?

**MVC** (Model-View-Controller), bir yazılım tasarım desenidir. Üç ana bileşeni vardır:

- **Model**: Verilerin ve iş mantığının temsil edildiği katmandır.
- **View**: Kullanıcı arayüzünün (UI) tanımlandığı katmandır.
- **Controller**: View ile Model arasında köprü görevi görür. Olayları (event) yakalar, iş mantığını tetikler.

Bu yapı sayesinde, arayüzde yapılan bir değişiklik doğrudan veri katmanını bozmaz. Aynı şekilde veri tabanında yapılan
değişiklikler UI üzerinde doğrudan bir etkiye sahip değildir.

---

## 2. JavaFX’te MVC'nin Rolü

JavaFX, **FXML** ve **Java Controller sınıfları** ile çalışır. Bu nedenle, View (FXML) ve Controller (Java sınıfı) doğal
olarak ayrılmıştır. Bu ayrımı daha iyi yapılandırmak ve genişletilebilir hale getirmek için MVC ve katmanlı mimari
birlikte kullanılır.

---

## 3. Katmanlı Mimari ile JavaFX MVC Yapısı

JavaFX uygulamalarında genellikle aşağıdaki katmanlar kullanılır:

### 3.1. **Model Katmanı**

Bu katman:

- Uygulamanın veri yapısını temsil eder.
- Veri tabanı işlemlerini içerir.
- Entity sınıflarını, servisleri ve DAO sınıflarını barındırır.

**Alt yapılar:**

- `Entity` sınıfları (örn: `User`, `Product`)
- `DAO` (Data Access Object) sınıfları
- `Service` katmanı (iş mantığını içerir)
- Veritabanı bağlantı sınıfları (JDBC, Hibernate, JPA gibi)

**Örnek:**

```java
public class User {
    private int id;
    private String name;
    // Getter - Setter
}
```

```java
public interface UserDao {
    List<User> getAllUsers();

    void addUser(User user);
}
```

```java
public class UserService {
    private UserDao userDao = new UserDaoImpl();

    public List<User> getUsers() {
        return userDao.getAllUsers();
    }
}
```

---

### 3.2. **View Katmanı (Görünüm)**

- JavaFX’te View genellikle `.fxml` dosyaları ile tanımlanır.
- Butonlar, text field’lar, listeler, tablolar vb. burada yer alır.
- Görsel bileşenlerin konumu, stili, boyutu buradan belirlenir.

**Örnek: `user_view.fxml`**

```xml

<VBox fx:controller="controller.UserController" xmlns:fx="http://javafx.com/fxml">
    <TextField fx:id="txtName"/>
    <Button text="Add User" onAction="#handleAddUser"/>
</VBox>
```

Bu yapı sayesinde görünüm, Java kodlarından ayrılır. Daha okunabilir ve test edilebilir olur.

---

### 3.3. **Controller Katmanı**

- Kullanıcı arayüzündeki olayları (event) dinler.
- Butona tıklanması, formun gönderilmesi gibi olayları yakalar.
- Model (servis) katmanını çağırarak gerekli işlevleri yerine getirir.
- View ile Model arasında bir köprüdür.

**Örnek: `UserController.java`**

```java
public class UserController {

    @FXML
    private TextField txtName;

    private UserService userService = new UserService();

    @FXML
    private void handleAddUser(ActionEvent event) {
        String name = txtName.getText();
        User user = new User();
        user.setName(name);
        userService.addUser(user);
    }
}
```

---

### 3.4. **Application/Main Katmanı**

- JavaFX uygulamasının giriş noktasıdır.
- `start()` metodu içinde sahne ve sahne bileşenleri (Scene, Stage) başlatılır.
- Genellikle `FXMLLoader` ile `FXML` dosyası yüklenir ve arayüz oluşturulur.

**Örnek:**

```java
public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/user_view.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

## 4. Katmanlar Arası İletişim

- View, doğrudan Controller ile iletişim kurar.
- Controller, Service katmanını çağırır.
- Service katmanı DAO (veritabanı) ile konuşur.
- DAO, veritabanı ile CRUD işlemlerini yürütür.

Bu zincir sayesinde uygulama modüler hale gelir.

---

## 5. Paketleme Önerisi (Folder Structure)

```
src/
├── main/
│   ├── java/
│   │   ├── app/                 --> Uygulama giriş noktası
│   │   ├── model/               --> Entity sınıfları
│   │   ├── dao/                 --> DAO arayüz ve implementasyonları
│   │   ├── service/             --> İş mantığı (business logic)
│   │   ├── controller/          --> JavaFX controller sınıfları
│   ├── resources/
│   │   ├── view/                --> FXML dosyaları
│   │   ├── css/                 --> Stil dosyaları
│   │   ├── images/              --> UI görselleri
```

---

## 6. Avantajlar

- **Separation of Concerns**: Her katman kendi işinden sorumludur.
- **Test Edilebilirlik**: Servis ve DAO katmanları rahatça test edilebilir.
- **Bakım Kolaylığı**: Hataları izole etmek ve düzeltmek kolaydır.
- **Kod Tekrarını Azaltma**: Özellikle Service ve DAO katmanlarında generic yapılarla.

---

## 7. Gelişmiş Öneriler

- **Dependency Injection (DI)**: Spring Framework veya Guice ile bağımlılıkları dışardan enjekte etmek.
- **Singleton Pattern**: Service veya DAO sınıflarında tekil örnekler.
- **Custom Exceptions**: Hatalar için özel exception sınıfları tanımlamak.
- **ObservableList**: JavaFX’in table ve liste bileşenleri için dinamik veri yönetimi.
- **FXML ile Temiz UI**: UI kodlarını Java kodlarından ayırmak için her zaman FXML kullanmak.

---

## JavaFX MVC Mimarisi(Örnek)

```sh 

```

Harika! Sana adım adım **JavaFX MVC + Katmanlı Mimari** yapısını örnek bir **Kullanıcı Kayıt Uygulaması** üzerinden
göstereceğim. Uygulamada kullanıcı ismi girilecek, sistemde tutulacak ve TableView’da listelenecek.

---

## 🎯 Hedef:

- Kullanıcı adı girilecek.
- "Ekle" butonuna basıldığında kullanıcı listeye eklenecek.
- Tüm yapı MVC + Katmanlı mimariye göre organize edilecek.

---

## ✅ 1. Proje Yapısı

```
UserApp/
├── src/
│   ├── app/               --> Main sınıfı
│   ├── controller/        --> JavaFX Controller
│   ├── model/             --> Entity sınıfı
│   ├── service/           --> Servis sınıfı
│   ├── dao/               --> DAO sınıfı (veri işlemleri)
│   └── util/              --> Yardımcı sınıflar (isteğe bağlı)
├── resources/
│   ├── view/              --> FXML dosyası
│   └── css/               --> Stil dosyası (isteğe bağlı)
```

---

## ✅ 2. Model Katmanı (Entity Sınıfı)

`model/User.java`

```java
package model;

public class User {
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

---

## ✅ 3. DAO Katmanı

`dao/UserDao.java`

```java
package dao;

import model.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);

    List<User> getAllUsers();
}
```

`dao/UserDaoImpl.java`

```java
package dao;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }
}
```

---

## ✅ 4. Service Katmanı

`service/UserService.java`

```java
package service;

import dao.UserDao;
import dao.UserDaoImpl;
import model.User;

import java.util.List;

public class UserService {
    private final UserDao userDao = new UserDaoImpl();

    public void addUser(User user) {
        if (user.getName() != null && !user.getName().isBlank()) {
            userDao.addUser(user);
        }
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
```

---

## ✅ 5. View Katmanı

`resources/view/user_view.fxml`

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<VBox xmlns:fx="http://javafx.com/fxml" fx:controller="controller.UserController" spacing="10" alignment="CENTER"
      padding="20">
    <Label text="Kullanıcı Adı Girin"/>
    <TextField fx:id="txtName" promptText="Ad soyad"/>
    <Button text="Ekle" onAction="#handleAddUser"/>
    <TableView fx:id="tableView" prefHeight="200" prefWidth="300">
        <columns>
            <TableColumn fx:id="nameColumn" text="Ad Soyad" prefWidth="290"/>
        </columns>
    </TableView>
</VBox>
```

---

## ✅ 6. Controller Katmanı

`controller/UserController.java`

```java
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.User;
import service.UserService;

public class UserController {

    @FXML
    private TextField txtName;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> nameColumn;

    private final UserService userService = new UserService();
    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        tableView.setItems(userList);
        loadUsers();
    }

    @FXML
    private void handleAddUser() {
        String name = txtName.getText();
        if (!name.isBlank()) {
            User user = new User(name);
            userService.addUser(user);
            userList.add(user);
            txtName.clear();
        }
    }

    private void loadUsers() {
        userList.setAll(userService.getAllUsers());
    }
}
```

---

## ✅ 7. Main Sınıfı

`app/MainApp.java`

```java
package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/user_view.fxml"));
        primaryStage.setTitle("Kullanıcı Kayıt Uygulaması");
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

## ✅ 8. Derleme ve Çalıştırma

- JavaFX SDK'nın proje yapılandırmasına ekli olduğundan emin ol.
- IntelliJ, Eclipse veya NetBeans ile bu klasör yapısına göre projeyi oluşturabilirsin.
- `MainApp.java` sınıfını çalıştırarak uygulamayı başlatabilirsin.

---

## 🧠 Geliştirme Fikirleri

- Kullanıcı verilerini SQLite gibi bir veritabanına kaydet.
- Kullanıcıyı silme/güncelleme ekle.
- Katmanlara `Generic DAO` gibi yapılar uygula.
- Service katmanına validasyonlar, exception handling ekle.

---

## JavaFX Annotation

```sh 

```

JavaFX’te **annotations (notasyonlar)**, Java koduna meta bilgi eklemek için kullanılan yapılardır. JavaFX’in kendisi
çok sayıda annotation kullanmaz, ancak **özellikle `FXML` ile birlikte** kullanıldığında bazı önemli annotation’lar
karşımıza çıkar. Ayrıca JavaFX projelerinde Spring, Lombok gibi kütüphaneler de entegre edilebileceğinden, bu tür
anotasyonlara da rastlanabilir. Ancak bu açıklamada **özellikle JavaFX’e özgü veya JavaFX ile birlikte sık kullanılan
annotation'lara odaklanacağım.**

---

## 🔷 1. `@FXML` (JavaFX'e özgü en temel annotation)

### 📌 Açıklama:

`@FXML` annotation’ı, FXML dosyasındaki arayüz bileşenleriyle Java sınıfı (Controller) arasındaki bağlantıyı kurmak için
kullanılır. JavaFX, FXML dosyasındaki tanımlı nesneleri Java tarafında tanımak için bu anotasyonu kullanır.

### 🧠 Kullanım amacı:

FXML dosyasındaki UI bileşenlerini (Button, Label, TextField vb.) kontrol sınıfına bağlamak ve bu bileşenlere erişim
sağlamak.

### 📎 Kullanım örneği:

```java
public class MyController {

    @FXML
    private Button myButton;

    @FXML
    private TextField nameField;

    @FXML
    private void handleButtonClick() {
        System.out.println("Button clicked!");
    }
}
```

### ✅ Notlar:

- Bu annotation yalnızca `private` ya da `protected` erişim belirleyicili alanlar veya metodlar için gerekir.
- Eğer metod/alan `public` olarak tanımlanmışsa, `@FXML` yazılması şart değildir ama **iyi bir uygulama alışkanlığı
  olarak** yazılır.
- `FXMLLoader` sınıfı tarafından okunur.

---

## 🔷 2. `@Override`

### 📌 Açıklama:

Bu annotation JavaFX'e özel değil, Java’nın kendisine aittir. Ancak JavaFX uygulamalarında sıklıkla kullanılır. Bir
sınıfın üst sınıfından veya bir arayüzden miras alınan bir metodu geçersiz kıldığını belirtmek için kullanılır.

### 📎 Kullanım örneği:

```java

@Override
public void initialize(URL location, ResourceBundle resources) {
    // UI bileşenlerinin ilk ayarları burada yapılır
}
```

### ✅ Notlar:

- JavaFX'te `Initializable` arayüzünü uyguladığınızda `initialize()` metodu bu anotasyonla işaretlenir.
- Derleyiciye, bu metodun gerçekten bir override olup olmadığını kontrol ettirir.

---

## 🔷 3. `@Start` (Not JavaFX standardı, ama test kütüphanelerinde yer alabilir)

JavaFX unit testlerinde kullanılan `TestFX` gibi kütüphanelerde bu tür özel anotasyonlar olabilir. Örneğin bazı test
sınıflarında `@Start` metodu, testin başlatılacağı sahneyi belirtir.

### 📎 Kullanım örneği (TestFX):

```java

@Start
public void start(Stage stage) {
    // test için sahne yükleniyor
    stage.setScene(new Scene(new Button("Test")));
    stage.show();
}
```

---

## 🔷 4. `@SuppressWarnings`

### 📌 Açıklama:

Java'nın genel anotasyonlarından biridir ama JavaFX projelerinde çok sık görülür. IDE uyarılarını bastırmak için
kullanılır.

### 📎 Kullanım örneği:

```java

@SuppressWarnings("unchecked")
ObservableList<String> data = FXCollections.observableArrayList();
```

---

## 🔷 5. `@Deprecated`

### 📌 Açıklama:

JavaFX kütüphanesinde de bazı API'ler zamanla `deprecated` edilebilir. Kullanılmaması gereken metot veya sınıflar bu
anotasyonla işaretlenir.

### 📎 Kullanım örneği:

```java

@Deprecated
public void oldMethod() {
    // Eski kullanım
}
```

---

## 🔷 Diğer Anotasyonlar (3. Parti veya Entegre Kullanımlarda)

### 📌 `@Component`, `@Autowired`, `@Service`, `@PostConstruct`

Eğer JavaFX uygulamanızı **Spring Framework** ile entegre ediyorsanız, bu anotasyonlar da kullanılabilir. Bu durumda
controller sınıflarınız Spring bileşeni haline gelir.

### 📌 `@Data`, `@Getter`, `@Setter`, `@AllArgsConstructor`, `@NoArgsConstructor`

JavaFX ile birlikte **Lombok** kütüphanesi kullanıldığında bolca görülür. Özellikle veri model sınıflarında kullanılır.

---

## Özet Tablosu

| Annotation          | Açıklama                                                   | Kullanım Alanı           |
|---------------------|------------------------------------------------------------|--------------------------|
| `@FXML`             | FXML dosyasındaki bileşenleri Java koduyla eşlemek         | Controller sınıfları     |
| `@Override`         | Üst sınıftan/arayüzden gelen metodun ezildiğini belirtir   | initialize, EventHandler |
| `@Start`            | JavaFX testlerinde sahneyi başlatmak için kullanılır       | Test sınıfları (TestFX)  |
| `@SuppressWarnings` | Derleyici uyarılarını bastırır                             | Genel                    |
| `@Deprecated`       | Artık kullanılmaması gereken metod/sınıfları işaretler     | Genel                    |
| `@Component`        | (Spring) JavaFX + Spring projelerinde bean olarak tanımlar | Controller sınıfları     |
| `@Data` vs. Lombok  | (Lombok) Getter/Setter gibi metotları otomatik üretir      | Model sınıfları          |

---

## JavaFX Anonation Project

```sh 

```

Harika! O zaman sana küçük bir **JavaFX + FXML** projesi iskeleti çıkarayım. Bu projede:

- `@FXML` anotasyonu ile Controller'daki bileşen bağlantısını göstereceğim,
- `@Override` ile `initialize()` metodunu kullanacağız,
- `@SuppressWarnings` da kullanabileceğimiz bir örnek olacak,
- Ayrıca istersen **Spring** ya da **Lombok** entegrasyonlu örneği de ayrıca ekleyebilirim.

---

## 🔧 Proje Yapısı

```
JavaFXAnnotationDemo/
│
├── src/
│   └── com/example/demo/
│       ├── MainApp.java
│       ├── MainController.java
│       └── main_view.fxml
└── build.gradle (veya pom.xml)
```

---

## 1. `main_view.fxml`

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<VBox xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      fx:controller="com.example.demo.MainController"
      spacing="10" alignment="CENTER" padding="20">

    <Label text="Adınızı girin:"/>
    <TextField fx:id="nameField"/>
    <Button text="Gönder" onAction="#handleButtonClick"/>
    <Label fx:id="resultLabel"/>

</VBox>
```

---

## 2. `MainController.java`

```java
package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private Label resultLabel;

    // initialize() metodu, sahne yüklenirken çağrılır
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultLabel.setText("Hoş geldiniz!");
    }

    // FXML'deki onAction="#handleButtonClick" burayı tetikler
    @FXML
    private void handleButtonClick() {
        String name = nameField.getText();
        if (name.isBlank()) {
            resultLabel.setText("Lütfen adınızı girin.");
        } else {
            resultLabel.setText("Merhaba, " + name + "!");
        }
    }
}
```

---

## 3. `MainApp.java`

```java
package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_view.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("JavaFX Annotation Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

## 4. Gözlemler

| Bileşen           | Açıklama                                                                                        |
|-------------------|-------------------------------------------------------------------------------------------------|
| `@FXML`           | FXML içindeki `TextField`, `Label`, `Button` gibi bileşenleri Java'ya bağlamak için kullanıldı. |
| `@Override`       | `initialize()` metodunu ezerek sahne yüklendiğinde bir karşılama mesajı gösterdik.              |
| `onAction="#..."` | FXML'de bir butona tıklanınca Controller'daki metoda yönlendirme yaptık.                        |
| `fx:id`           | Bileşenlere Java'dan erişmek için kimlik atadık.                                                |

---

## ✅ Bonus: @SuppressWarnings örneği

Eğer liste tanımlaması gibi durumlarda uyarı almak istemezsen:

```java

@SuppressWarnings("unchecked")
public void exampleMethod() {
    ObservableList list = FXCollections.observableArrayList();
}
```

---

## ➕ Ekstra İstek?

İstersen bu projeyi:

- ✅ Spring Boot ile entegre edilmiş hale getirebiliriz (JavaFX + Spring),
- ✅ Lombok ile model sınıfı örneği ekleyebiliriz,
- ✅ CSS ile stillendirme gösterebiliriz,
- ✅ JavaFX içinde veri tabanı bağlantısı yapabiliriz.

Hangisine odaklanalım? Yoksa bu projeyi GitHub repo şeklinde mi istiyorsun?

## JavaFX

```sh 

```

