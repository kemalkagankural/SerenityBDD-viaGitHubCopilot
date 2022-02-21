package starter.pages;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class theAmazonHomePageElements {
    //There was written by Github Copilot.
     public static final Target SEARCH_FIELD = Target.the("Search field").located(By.id("twotabsearchtextbox"));
     public static final Target SEARCH_BUTTON = Target.the("Search button").located(By.id("nav-search-submit-button"));
     public static final Target SEARCH_RESULT_TITLE = Target.the("Search result title").located(By.xpath("/html[1]/body[1]/div[1]/div[2]/span[1]/div[1]/h1[1]/div[1]/div[1]/div[1]/div[1]/span[3]"));
}
