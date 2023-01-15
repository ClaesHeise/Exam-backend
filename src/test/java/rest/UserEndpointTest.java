//package rest;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import dtos.UserDTO;
//import entities.Role;
//import entities.User;
//import io.restassured.RestAssured;
//import io.restassured.parsing.Parser;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.junit.jupiter.api.*;
//import org.mindrot.jbcrypt.BCrypt;
//import utils.EMF_Creator;
//import static org.hamcrest.Matchers.equalTo;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.ws.rs.core.UriBuilder;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserEndpointTest {
//
//    private static final int SERVER_PORT = 7777;
//    private static final String SERVER_URL = "http://localhost/api";
//
//    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
//    private static HttpServer httpServer;
//    private static EntityManagerFactory emf;
//
//    static HttpServer startServer() {
//        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
//        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
//    }
//
//    @BeforeAll
//    public static void setUpClass() {
//        //This method must be called before you request the EntityManagerFactory
//        EMF_Creator.startREST_TestWithDB();
//        emf = EMF_Creator.createEntityManagerFactoryForTest();
//
//        httpServer = startServer();
//        //Setup RestAssured
//        RestAssured.baseURI = SERVER_URL;
//        RestAssured.port = SERVER_PORT;
//        RestAssured.defaultParser = Parser.JSON;
//    }
//
//    @AfterAll
//    public static void closeTestServer() {
//        //Don't forget this, if you called its counterpart in @BeforeAll
//        EMF_Creator.endREST_TestWithDB();
//
//        httpServer.shutdownNow();
//    }
//
//    @BeforeEach
//    void setUp() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            em.createQuery("delete from User ").executeUpdate();
//            em.createQuery("delete from Role").executeUpdate();
//            Role role1 = new Role("admin");
//            Role role2 = new Role("user");
//            User user1 = new User("admin1", "test1");
//            User user2 = new User("admin2", "test2");
//            User user3 = new User("user1", "test3");
//            user1.addRole(role1);
//            user2.addRole(role1);
//            user3.addRole(role2);
//            em.persist(role1);
//            em.persist(role2);
//            em.persist(user1);
//            em.persist(user2);
//            em.persist(user3);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }
//
//    private static String securityToken;
//
//    private static void login(String username, String password) {
//        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
//        securityToken = given()
//                .contentType("application/json")
//                .body(json)
//                .when().post("/login")
//                .then()
//                .extract().path("token");
//        //System.out.println("TOKEN ---> " + securityToken);
//    }
//
//    @Test
//    void updatePassword() {
//        login("admin1", "test1");
//
//        String password = given()
//                .contentType("application/json")
//                .header("x-access-token", securityToken)
//                .body("{\"password\": testing}")
//                .when().put("/user")
//                .then()
//                .statusCode(200)
//                .extract().path("password");
//
//        assertTrue(BCrypt.checkpw("testing", password));
////                .body("password", equalTo(BCrypt.hashpw("testing", BCrypt.gensalt())));
//    }
//
//    @Test
//    void addUser() {
//        login("admin1", "test1");
//        given()
//                .contentType("application/json")
//                .header("x-access-token", securityToken)
//                .body("{\"name\": testUser,\n\"password\": testing,\n\"roles\": ['admin']}")
//                .when().post("/user")
//                .then()
//                .statusCode(200)
//                .body("username", equalTo("testUser"));
//    }
//
//    @Test
//    void verifyUser() {
//        login("admin1", "test1");
//
//        given()
//                .contentType("application/json")
//                .header("x-access-token", securityToken)
//                .when().post("/user/verify")
//                .then()
//                .statusCode(200)
//                .body("username", equalTo("admin1"));
//    }
//
//    @Test
//    void getAllUsers() {
//        List<ArrayList> role = given()
//                .contentType("application/json")
//                .when()
//                .get("/user/all/admin")
//                .then()
//                .statusCode(200)
//                .extract().path("role");
//
//        assertEquals("admin", role.get(0).get(0));
//        assertEquals(2, role.size());
//    }
//
//    @Disabled
//    @Test
//    void deleteUser() {
//        login("admin1", "test1");
//
//        given()
//                .contentType("application/json")
//                .header("x-access-token", securityToken)
//                .body("{\"name\": admin1")
//                .when()
//                .delete("/user")
//                .then()
//                .statusCode(200);
//    }
//}