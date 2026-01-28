package ru.netology.appbank;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testdata.AuthData;
import ru.netology.testdata.AuthPage;

public class AuthTestUI {

    private AuthPage page = new AuthPage();

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
        page.shouldBeOnDashboard();
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
        page.shouldNotBeOnDashboard();
    }

    // не логинит несуществующего
    @Test
    @DisplayName("ui: Should Not Login Non Existent User")
    void uiShouldNotLoginNonExistentUser() {
        AuthData.UserData user = AuthData.generateUser("active");
        // не регистрируем
        page.login(user.getLogin(), user.getPassword()); // заполняем поля формы
        page.shouldDisplayPageError("Ошибка! Неверно указан логин или пароль");
        page.shouldNotBeOnDashboard();
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
        page.shouldNotBeOnDashboard();
    }

    @Test
    @DisplayName("ui: Should Not Login When Wrong Login")
    void uiShouldNotLoginWhenWrongLogin() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        String wrongLogin = AuthData.generateLogin();
        page.login(wrongLogin, user.getPassword()); // заполняем поля формы
        page.shouldDisplayPageError("Ошибка! Неверно указан логин или пароль");
        page.shouldNotBeOnDashboard();
    }

    // пустые поля - логин, пароль или оба
    @Test
    @DisplayName("ui: Should Not Login When Empty Login")
    void uiShouldNotLoginWhenEmptyLogin() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        page.login("", user.getPassword()); // заполняем поля формы
        page.shouldDisplayLoginError("Поле обязательно для заполнения");
        page.shouldNotBeOnDashboard();
    }

    @Test
    @DisplayName("ui: Should Not Login When Empty Password")
    void uiShouldNotLoginWhenEmptyPassword() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        page.login(user.getLogin(), ""); // заполняем поля формы
        page.shouldDisplayPasswordError("Поле обязательно для заполнения");
        page.shouldNotBeOnDashboard();
    }

    @Test
    @DisplayName("ui: Should Not Login When All Fields Empty")
    void uiShouldNotLoginWhenAllFieldsEmpty() {
        AuthData.UserData user = AuthData.generateUser("active");
        AuthData.registerUser(user);
        page.login("", ""); // заполняем поля формы
        page.shouldDisplayLoginError("Поле обязательно для заполнения");
        page.shouldDisplayPasswordError("Поле обязательно для заполнения");
        page.shouldNotBeOnDashboard();
    }
}
