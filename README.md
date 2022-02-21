
### The project directory structure
The project has build scripts for both Maven and Gradle, and follows the standard directory structure used in most Serenity projects:
```Gherkin
src
  + main
  + test
    + java                        Test runners and supporting code
    + resources
      + features                  Feature files
     + search                  Feature file subdirectories 
             search.feature
```

Serenity 2.2.13 introduced integration with WebdriverManager to download webdriver binaries.

## The sample scenario
Both variations of the sample project uses the sample Cucumber scenario. In this scenario, Sergey (who likes to search for stuff) is performing a search on the internet:

```Gherkin
Feature: Search product in Amazon

  Scenario: Searching a product in Amazon
    Given I am on the Amazon home page
    When I search for "iPhone"
    Then I should see "iPhone" in the search results

```

### The Screenplay implementation
The sample code in the master branch uses the Screenplay pattern. The Screenplay pattern describes tests in terms of actors and the tasks they perform. Tasks are represented as objects performed by an actor, rather than methods. This makes them more flexible and composable, at the cost of being a bit more wordy. Here is an example:
```java
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
```

Screenplay classes emphasise reusable components and a very readable declarative style, whereas Lean Page Objects and Action Classes (that you can see further down) opt for a more imperative style.

The `NavigateTo` class is responsible for opening the Wikipedia home page:
```java
public class NavigateTo {
    public static Performable theAmazonHomePage() {
        return Task.where("{0} opens the Amazon home page",
                Open.browserOn().the(AmazonHomePage.class));
    }
}
```

The `LookForInformation` class does the actual search:
```java
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

```

In Screenplay, we keep track of locators in light weight page or component objects, like this one:
```java
public class theAmazonHomePageElements {
    //There was written by Github Copilot.
    public static final Target SEARCH_FIELD = Target.the("Search field").located(By.id("twotabsearchtextbox"));
    public static final Target SEARCH_BUTTON = Target.the("Search button").located(By.id("nav-search-submit-button"));
    public static final Target SEARCH_RESULT_TITLE = Target.the("Search result title").located(By.xpath("/html[1]/body[1]/div[1]/div[2]/span[1]/div[1]/h1[1]/div[1]/div[1]/div[1]/div[1]/span[3]"));
}

```
The Screenplay DSL is rich and flexible, and well suited to teams working on large test automation projects with many team members, and who are reasonably comfortable with Java and design patterns.

## Generating the reports
Since the Serenity reports contain aggregate information about all of the tests, they are not generated after each individual test (as this would be extremenly inefficient). Rather, The Full Serenity reports are generated by the `serenity-maven-plugin`. You can trigger this by running `mvn serenity:aggregate` from the command line or from your IDE.

They reports are also integrated into the Maven build process: the following code in the `pom.xml` file causes the reports to be generated automatically once all the tests have completed when you run `mvn verify`?

```
             <plugin>
                <groupId>net.serenity-bdd.maven.plugins</groupId>
                <artifactId>serenity-maven-plugin</artifactId>
                <version>${serenity.maven.version}</version>
                <configuration>
                    <tags>${tags}</tags>
                </configuration>
                <executions>
                    <execution>
                        <id>serenity-reports</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>aggregate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

## Simplified WebDriver configuration and other Serenity extras
The sample projects both use some Serenity features which make configuring the tests easier. In particular, Serenity uses the `serenity.conf` file in the `src/test/resources` directory to configure test execution options.  
### Webdriver configuration
The WebDriver configuration is managed entirely from this file, as illustrated below:
```java
webdriver {
    driver = chrome
}
headless.mode = true

chrome.switches="""--start-maximized;--test-type;--no-sandbox;--ignore-certificate-errors;
                   --disable-popup-blocking;--disable-default-apps;--disable-extensions-file-access-check;
                   --incognito;--disable-infobars,--disable-gpu"""

```

Serenity uses WebDriverManager to download the WebDriver binaries automatically before the tests are executed.



