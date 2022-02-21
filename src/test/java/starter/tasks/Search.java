package starter.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.SendKeys;
import net.serenitybdd.screenplay.waits.WaitUntil;
import starter.pages.theAmazonHomePageElements;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;


public class Search implements Task {
    private String searchTerm;
    public Search(String searchTerm) {
        this.searchTerm = searchTerm;
    }


    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                //There was written by Github Copilot
                WaitUntil.the(theAmazonHomePageElements.SEARCH_FIELD, isClickable()),
                Click.on(theAmazonHomePageElements.SEARCH_FIELD),
                SendKeys.of(searchTerm).into(theAmazonHomePageElements.SEARCH_FIELD),
                Click.on(theAmazonHomePageElements.SEARCH_BUTTON)
        );
    }

    public static Search search(String searchTerm) {return instrumented(Search.class,searchTerm);}

}
