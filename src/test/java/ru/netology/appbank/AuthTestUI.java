package ru.netology.appbank;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testdata.AuthData;
import ru.netology.testdata.AuthPage;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

public class AuthTestUI {

    private AuthPage page = new AuthPage();
    private static Process server;

    /* настройки для локального запуска - для CI отключаем
    @BeforeAll
    public static void setup() throws IOException, InterruptedException {
        System.setProperty("selenide.holdBrowserOpen", "true");
        System.setProperty("selenide.browser", "chrome");
        System.setProperty("selenide.headless", "false");
        // запускаем сервер в тестовом режиме, чтоб не запускать вручную через консоль
        ProcessBuilder pb = new ProcessBuilder(
                "java", "-jar", "artifacts/app-ibank.jar",
                "-P:profile=test"
        );
        server = pb.start();
        // ожидание пока запустится
        Thread.sleep(10000);
    } */

    @BeforeEach
    public void open() {
        Selenide.open("http://localhost:9999");
    }

    // позитивная проверка
    @Test
    @DisplayName("ui: Should Login Valid User With Active Status")
    void uiShouldLoginValidUserWithActiveStatus() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user); // отправляем пост-запрос на сервер
        page.login(user.getLogin(), user.getPassword()); // заполняем поля формы
        // ассертим
        $("h2.heading")
                .shouldHave(Condition.text("Личный кабинет"));
    }

    // не логинится со статусом blocked
    @Test
    @DisplayName("ui: Should Not Login User With Blocked Status")
    void uiShouldNotLoginUserWithBlockedStatus() {
        AuthData.UserData user = AuthData.generateUser("blocked");
        AuthData.registerUser(user); // отправляем пост-запрос на сервер
        page.login(user.getLogin(), user.getPassword()); // заполняем поля формы
        // ассертим
        page.shouldDisplayPageError("Ошибка! Пользователь заблокирован");
        $("h2.heading").shouldNotHave(Condition.text("Личный кабинет"));
    }

    // не логинит несуществующего
    @Test
    @DisplayName("ui: Should Not Login Non Existent User")
    void uiShouldNotLoginNonExistentUser() {
        AuthData.UserData user = AuthData.generateUser("active");
        // не регистрируем
        page.login(user.getLogin(), user.getPassword()); // заполняем поля формы
        page.shouldDisplayPageError("Ошибка! Неверно указан логин или пароль");
        $("h2.heading").shouldNotHave(Condition.text("Личный кабинет"));
    }

    // не логинит неверный пароль и логин
    @Test
    @DisplayName("ui: Should Not Login When Wrong Password")
    void uiShouldNotLoginWhenWrongPassword() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        String wrongPassword = AuthData.generatePassword();
        page.login(user.getLogin(), wrongPassword); // заполняем поля формы
        page.shouldDisplayPageError("Ошибка! Неверно указан логин или пароль");
        $("h2.heading").shouldNotHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("ui: Should Not Login When Wrong Login")
    void uiShouldNotLoginWhenWrongLogin() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        String wrongLogin = AuthData.generateLogin();
        page.login(wrongLogin, user.getPassword()); // заполняем поля формы
        page.shouldDisplayPageError("Ошибка! Неверно указан логин или пароль");
        $("h2.heading").shouldNotHave(Condition.text("Личный кабинет"));
    }

    // пустые поля - логин, пароль или оба
    @Test
    @DisplayName("ui: Should Not Login When Empty Login")
    void uiShouldNotLoginWhenEmptyLogin() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        page.login("", user.getPassword()); // заполняем поля формы
        page.shouldDisplayLoginError("Поле обязательно для заполнения");
        $("h2.heading").shouldNotHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("ui: Should Not Login When Empty Password")
    void uiShouldNotLoginWhenEmptyPassword() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        page.login(user.getLogin(), ""); // заполняем поля формы
        page.shouldDisplayPasswordError("Поле обязательно для заполнения");
        $("h2.heading").shouldNotHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("ui: Should Not Login When All Fields Empty")
    void uiShouldNotLoginWhenAllFieldsEmpty() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        page.login("", ""); // заполняем поля формы
        page.shouldDisplayLoginError("Поле обязательно для заполнения");
        page.shouldDisplayPasswordError("Поле обязательно для заполнения");
        $("h2.heading").shouldNotHave(Condition.text("Личный кабинет"));
    }
}
