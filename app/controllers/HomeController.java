//package controllers;
//
//import models.RealTweet;
//import play.data.Form;
//import play.data.FormFactory;
//import play.mvc.*;
//import org.mockito.Mockito.*;
////import views.html.Home.welcome;
//import views.html.index;
//import views.html.tweets.welcome;
//
//import views.html.Home.*;
//
//import javax.inject.Inject;
//import java.util.concurrent.CompletableFuture;
//
//import static org.mockito.Mockito.mock;
//
///**
// * This controller contains an action to handle HTTP requests
// * to the application's home page.
// */
//public class HomeController extends Controller {
//    @Inject
//    FormFactory formFactory;
//    Http.Context context;
//    /**
//     * An action that renders an HTML page with a welcome message.
//     * The configuration in the <code>routes</code> file means that
//     * this method will be called when the application receives a
//     * <code>GET</code> request with a path of <code>/</code>.
//     */
//    public HomeController(){
//        formFactory=new FormFactory(null,null,null);
//       context= mock(Http.Context.class);
//       Http.Context.current.set(context);
//    }
//    public Result index() {
//
//        int i = 5;
//        String name = "SuyashCode";
//
//        return ok("Hello Play!");
//    }
//    public Result welcome(String name, String lastName) {
//        Form<RealTweet> bookForm = formFactory.form(RealTweet.class);
//        return ok(views.html.Home.welcome.render(name, lastName,bookForm));
//    }
//
//}
