package ru.netology.testdata;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class AuthPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement loginSubField = $("[data-test-id=login].input .input__sub");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement passwordSubField = $("[data-test-id=password].input .input__sub");
    private SelenideElement continueButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private SelenideElement pageHeading = $("h2.heading");

    public void login(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        continueButton.click();
    }

    public void shouldNotBeOnDashboard() {
        pageHeading.shouldNotHave(Condition.text("Личный кабинет"));
    }

    public void shouldBeOnDashboard() {
        pageHeading.shouldHave(Condition.text("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    public void shouldDisplayPageError(String errorText) {
        errorNotification.shouldBe(Condition.visible).shouldHave(Condition.text(errorText));
    }

    public void shouldDisplayLoginError(String errorText) {
        loginSubField.shouldBe(Condition.visible).shouldHave(Condition.text(errorText));
    }

    public void shouldDisplayPasswordError(String errorText) {
        passwordSubField.shouldBe(Condition.visible).shouldHave(Condition.text(errorText));
    }
}
