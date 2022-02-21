package starter.stepdefinitions;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.thucydides.core.annotations.Managed;
import org.openqa.selenium.WebDriver;
import starter.navigation.NavigateTo;
import starter.pages.theAmazonHomePageElements;
import starter.tasks.Search;



public class SearchStepDefinitions {
    Actor actor = Actor.named("kemal");
    @Managed
    WebDriver webDriver;

    @Given("I am on the Amazon home page")
    public void ı_am_on_the_amazon_home_page() {
        actor.can(BrowseTheWeb.with(webDriver));
        actor.attemptsTo(NavigateTo.theAmazonHomePage());
    }
    @When("I search for {string}")
    public void ı_search_for(String string) {
        actor.attemptsTo(Search.search(string));
    }
    @Then("I should see {string} in the search results")
    public void ı_should_see_in_the_search_results(String string) {
        actor.attemptsTo(Ensure.that(theAmazonHomePageElements.SEARCH_RESULT_TITLE.resolveFor(actor).getText()).contains(string));
    }
}
