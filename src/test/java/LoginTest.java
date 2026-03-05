import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://askomdch.com/account/");
    }

    @Test
    @DisplayName("User receives an error message for invalid credentials")
    void testInvalidLogin() {
        page.locator("#username").fill("wrong_user");
        page.locator("#password").fill("wrong_password");
        page.locator("button[name='login']").click();

        Locator errorBox = page.locator(".woocommerce-error");
        assertThat(errorBox).isVisible();
        assertThat(errorBox).containsText("Error");
    }

    @Test
    @DisplayName("Successful login redirects to dashboard and maintains session")
    void testSuccessfulLogin() {
        page.locator("#username").fill("Naomy");
        page.locator("#password").fill("nana");
        page.locator("button[name='login']").click();

        assertThat(page).hasURL("https://askomdch.com/account/");

        Locator logoutLink = page.locator("text=Log out");
        assertThat(logoutLink).isVisible();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    static void closeBrowser() {
        browser.close();
        playwright.close();
    }
}