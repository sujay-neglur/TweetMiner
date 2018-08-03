import controllers.TweetsController;
import models.Details;
import models.FakeTwitter;
import models.RealTweet;
import org.junit.BeforeClass;
import org.junit.Test;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.core.j.JavaContextComponents;
import play.core.j.JavaHelpers$;
import play.data.FormFactory;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import twitter4j.HashtagEntity;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.route;
import static twitter4j.HttpResponseCode.OK;
public class FunctionalTest extends WithApplication {

    private static FakeTwitter fakeTwitter = new FakeTwitter();
    private static TweetsController client;
    private RealTweet realTweet = new RealTweet();
    static Http.RequestBuilder request;
    static Http.RequestBuilder tokenRequest;
    static JavaContextComponents contextComponents;
    static HttpExecutionContext ec;

    @Test
    public void testCreate() {
        FormFactory formFactory = mock(FormFactory.class);
//        TweetsController t= new TweetsController(new FakeTwitter(),formFactory);
        Result r = client.create();
        assertThat(OK).isEqualTo(r.status());
    }


    @Test
    public void testCalcStats() throws Exception {
        Details one = new Details("Syash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details two = new Details("yash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details three = new Details("ash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details four = new Details("sh", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        List<Details> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);

        HashMap<String, Integer> result = new HashMap<>();
        result.put("ben", 4);

        CompletableFuture<List<Details>> listDetails = CompletableFuture.completedFuture(list); //new CompletableFuture.completedFuture(list);
        CompletableFuture<HashMap<String, Integer>> newList = realTweet.calculateStatistics(listDetails);
        assertEquals(result, newList.get());


    }

    @BeforeClass
    public static void init() {
        System.out.println("Static init");
        request = Helpers.fakeRequest("POST", "/tweets/create");
        tokenRequest = play.api.test.CSRFTokenHelper.addCSRFToken(request);
        contextComponents = JavaHelpers$.MODULE$.createContextComponents();
        Http.Context.current.set(JavaHelpers$.MODULE$.createJavaContext(tokenRequest.build()._underlyingRequest(), contextComponents));
        FormFactory formFactory = mock(FormFactory.class);
        ec= new HttpExecutionContext(ForkJoinPool.commonPool());
        client = new TweetsController(fakeTwitter, formFactory, ec);
    }

    @Test
    public void testSave() throws IOException {

//        request = Helpers.fakeRequest("GET", "/tweets/create");
//        tokenRequest = play.api.test.CSRFTokenHelper.addCSRFToken(request);
//        contextComponents = JavaHelpers$.MODULE$.createContextComponents();
//        Http.Context.current.set(JavaHelpers$.MODULE$.createJavaContext(tokenRequest.build()._underlyingRequest(), contextComponents));
//        FormFactory formFactory = mock(FormFactory.class);
//        ec= new HttpExecutionContext(ForkJoinPool.commonPool());
//        client = new TweetsController(fakeTwitter, formFactory, ec);
//       Result result= client.save();
//       assertEquals(OK,result.status());
        Helpers helpers = new Helpers();

//        Request request = mock(Request.class);
//        Map<String, String> newww=new HashMap<>();
//        helpers.invokeWithContext(Helpers.fakeRequest("/tweets/create","").bodyForm(newww),contextComponents,()->client.save());
        Call action = controllers.routes.TweetsController.save();
        route(Helpers.fakeRequest(action).bodyJson(Json.toJson(null)));




    }

    @Test
    public void testGetHashTag(){
        Result r = client.getHashtag("one");
        assertEquals(OK,r.status());
        r= client.getHashtag("two");
        assertEquals(OK,r.status());
        r=client.getHashtag("three");
        assertEquals(OK,r.status());
        r=client.getHashtag(null);
        assertEquals(OK,r.status());

    }
    @Test
    public void testGetLatLongPositions() {
        try {
            CompletableFuture<String[]> r = realTweet.getLatLongPositions("Montreal");
            String[] rArray = r.get();
            String[] latLongsArray = {"45.5016889", "-73.5672560"};
            assertEquals(latLongsArray, rArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCallStatistics(){
        Result r = client.callStatistics("hello");

        assertEquals(OK, r.status());
    }

    @Test
    public void testShowUser(){
        Result r = client.showUser("Suyash Malthankar");
        assertEquals(OK, r.status());
    }

    @Test
    public void testShowLocation(){
        Result r = client.showLocation("Montreal");

        assertEquals(OK, r.status());
    }
    @Test
    public void testCalculateStatistics() {
        try {

            CompletableFuture<HashMap<String, Integer>> checkHappyResult = fakeTwitter.calculateStatistics(CompletableFuture.supplyAsync(() -> Arrays.asList(
                    new Details("user1 ", "montreal", 2, "u1", "This is user tweet", new HashtagEntity[10]),
                    new Details("user2 ", "toronto", 2, "u1", "This is user tweet", new HashtagEntity[10]),
                    new Details("user3 ", "mumbai", 2, "u1", "This is user tweet", new HashtagEntity[10]),
                    new Details("user4 ", "delhi", 2, "u1", "This is user tweet", new HashtagEntity[10]),
                    new Details("user5 ", "vancouver", 2, "u1", "This is user tweet", new HashtagEntity[10]),
                    new Details("user6 ", "chennai", 2, "u1", "This is user tweet 1", new HashtagEntity[10]),
                    new Details("user7 ", "indore", 2, "u1", "This is user tweet 1", new HashtagEntity[10]),
                    new Details("user8 ", "montreal", 2, "u1", "This is user tweet 1", new HashtagEntity[10]),
                    new Details("user9 ", "montreal", 2, "u1", "This is user tweet 1", new HashtagEntity[10]),
                    new Details("user10 ", "montreal", 2, "u1", "This is user tweet 1", new HashtagEntity[10]))));

            HashMap<String, Integer> happyResult = checkHappyResult.get();
            System.out.println("Happy result" + happyResult);
            System.out.println("Check happy " + checkHappyResult);
            int countHappySmiles = happyResult.get("tweet");
            int countSadSmiles = happyResult.get("1");

            assertEquals(10, countHappySmiles);
            assertEquals(5, countSadSmiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAnalyzeSentiment() {
        try {
            List<Details> listTweetsOne = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                listTweetsOne.add(new Details("user1 ", "montreal", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]));
            }

            CompletableFuture<String> cfResultOne = fakeTwitter.analyzeSentiment(listTweetsOne);

            String stringResultOne = cfResultOne.get();

            List<Details> listTweetsTwo = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                listTweetsTwo.add(new Details("user1 ", "montreal", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]));
            }
            for (int i = 0; i < 5; i++) {
                listTweetsTwo.add(new Details("user1 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]));
            }


            CompletableFuture<String> cfResultTwo = fakeTwitter.analyzeSentiment(listTweetsTwo);

            String stringResultTwo = cfResultTwo.get();
            System.out.println(stringResultTwo);

            List<Details> listTweetThree = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                listTweetThree.add(new Details("user1 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]));
            }

            CompletableFuture<String> cfResultThree = fakeTwitter.analyzeSentiment(listTweetThree);
            String stringResultThree = cfResultThree.get();
            assertEquals(":-)", stringResultOne);
            assertEquals(":-|", stringResultTwo);
            assertEquals(":-(", stringResultThree);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSetMap()
    {
        fakeTwitter.setDataStatMap("one",CompletableFuture.supplyAsync(() -> new HashMap<>()));
    }


    public void testReducedTweet() throws Exception {
        Details one = new Details("Suyash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details two = new Details("Sujay", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details three = new Details("Ayush", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details four = new Details("Wastaabir", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details five = new Details("Jaymin", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details six = new Details("Suyash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details seven = new Details("Suyash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details eight = new Details("Suyash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details nine = new Details("Suyash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details ten = new Details("Suyash", "Indore", 0, "das", "ben", new HashtagEntity[10]);
        Details eleven = new Details("Suyash", "Indore", 0, "das", "ben", new HashtagEntity[10]);

        List<Details> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
        list.add(five);
        list.add(six);
        list.add(seven);
        list.add(eight);
        list.add(nine);
        list.add(ten);
        list.add(eleven);
        CompletableFuture<List<Details>> listofElevenDetails = CompletableFuture.completedFuture(list);
        CompletableFuture<List<Details>> newList = realTweet.reduceTweets(listofElevenDetails);

        assertEquals(10, newList.get().size());
    }

    @Test
    public void testConstructGeoCode() {
        try {
            String[] latLongsArray = {"45.5016889", "-73.5672560"};
            CompletableFuture<String[]> cfLatLongsArray = CompletableFuture.completedFuture(latLongsArray);

            CompletableFuture<String> cfResult = realTweet.constructGeoString(cfLatLongsArray);
            String result = cfResult.get();

            assertEquals("geocode:" + latLongsArray[0] + "," + latLongsArray[1] + ",50km", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}