import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartTest {
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
    void setup() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://askomdch.com");
    }

    @Test
    @DisplayName("Add product from Homepage and navigate via View Cart link")
    void testHomeAddToCartAndNavigate() {

        page.navigate("https://askomdch.com/");

        Locator addToCartBtn = page.locator("a.add_to_cart_button").first();
        addToCartBtn.click();
        Locator viewCartLink = page.locator("a.added_to_cart.wc-forward");
        viewCartLink.click();
        assertThat(page).hasURL("https://askomdch.com/cart/");

        Locator cartTable = page.locator("table.shop_table.cart");
        assertThat(cartTable).isVisible();


        assertThat(page.locator(".cart-subtotal")).isVisible();

        System.out.println("Customer Journey Successful: Added from home, clicked View Cart, and verified data.");
    }
    @AfterEach
    void closeContext() { context.close(); }


    @AfterAll
    static void closeBrowser() { playwright.close(); }
}