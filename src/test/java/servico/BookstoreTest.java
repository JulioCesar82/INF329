package servico;

import dominio.Address;
import dominio.Author;
import dominio.Book;
import dominio.Cart;
import dominio.CreditCards;
import dominio.Customer;
import dominio.Order;
import dominio.SUBJECTS;
import dominio.ShipTypes;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author INF329
 */
public class BookstoreTest {

    public BookstoreTest() {
    }

    static Bookstore instance;

    @BeforeClass
    public static void setUpClass() {
        
        long seed = 0;
        long now = System.currentTimeMillis();
        int items = 10000;
        int customers = 1000;
        int addresses = 1000;
        int authors = 100;
        int orders = 10000;
        Random rand = new Random(seed);
        Bookstore.populate(seed, now, items, customers, addresses, authors);
        instance = new Bookstore(0);
        instance.populateInstanceBookstore(orders, rand, now);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isPopulated method, of class Bookstore.
     */
    @Test
    public void testIsPopulated() {
        System.out.println("isPopulated");
        boolean expResult = true;
        boolean result = instance.isPopulated();
        assertEquals(expResult, result);
    }

    /**
     * Test of alwaysGetAddress method, of class Bookstore.
     */
    @Test
    public void testAlwaysGetAddress() {
        System.out.println("alwaysGetAddress");
        String street1 = "";
        String street2 = "";
        String city = "";
        String state = "";
        String zip = "";
        String countryName = "";
        Address result = instance.alwaysGetAddress(street1, street2, city, state, zip, countryName);
        Address expResult = new Address(0, street1, street2, city, state, zip, result.getCountry());
        assertEquals(expResult, result);

    }

    /**
     * Test of getCustomer method, of class Bookstore.
     */
    @Test
    public void testGetCustomer_int() {
        System.out.println("getCustomer");
        int cId = 0;
        Customer result = instance.getCustomer(cId);
        assertEquals(cId, result.getId());
    }

    /**
     * Test of getCustomer method, of class Bookstore.
     */
    @Test
    public void testGetCustomer_String() {
        System.out.println("getCustomer");
        String username = instance.getCustomer(10).getUname();
        Customer result = instance.getCustomer(username).get();
        assertEquals(username, result.getUname());

    }

    /**
     * Test of createCustomer method, of class Bookstore.
     */
    @Test
    public void testCreateCustomer() {
        System.out.println("createCustomer");
        String fname = "";
        String lname = "";
        String street1 = "";
        String street2 = "";
        String city = "";
        String state = "";
        String zip = "";
        String countryName = "";
        String phone = "";
        String email = "";
        double discount = 0.0;
        Date birthdate = null;
        String data = "";
        long now = 0L;

        Customer result = instance.createCustomer(fname, lname, street1, street2, city, state, zip, countryName, phone, email, discount, birthdate, data, now);
        int id = result.getId();
        String uname = result.getUname();
        Date since = result.getSince();
        Date lastVisit = result.getLastVisit();
        Date login = result.getLogin();
        Date expiration = result.getExpiration();
        Address address = result.getAddress();
        Customer expResult = new Customer(id, uname, uname.toLowerCase(), fname,
                lname, phone, email, since, lastVisit, login, expiration,
                discount, 0, 0, birthdate, data, address);
        assertEquals(expResult, result);

    }

    /**
     * Test of refreshCustomerSession method, of class Bookstore.
     */
    @Test
    public void testRefreshCustomerSession() {
        System.out.println("refreshCustomerSession");
        int cId = 0;
        long now = 0L;
        instance.refreshCustomerSession(cId, now);
    }

    /**
     * Test of getBook method, of class Bookstore.
     */
    @Test
    public void testGetBook() {
        System.out.println("getBook");
        int bId = 0;
        Book result = instance.getBook(bId).get();
        assertEquals(bId, result.getId());

    }

    /**
     * Test of getBooksBySubject method, of class Bookstore.
     */
    @Test
    public void testGetBooksBySubject() {
        System.out.println("getBooksBySubject");
        SUBJECTS subject = SUBJECTS.ARTS;
        List<Book> result = instance.getBooksBySubject(subject);
        assertEquals(result.size(), result.stream().filter(book -> book.getSubject().equals(subject)).count());

    }

    /**
     * Test of getBooksByTitle method, of class Bookstore.
     */
    @Test
    public void testGetBooksByTitle() {
        System.out.println("getBooksByTitle");
        String title = instance.getBook(0).get().getTitle().substring(0, 4);
        List<Book> result = instance.getBooksByTitle(title);
        assertEquals(result.size(), result.stream().filter(book -> book.getTitle().startsWith(title)).count());
    }

    /**
     * Test of getBooksByAuthor method, of class Bookstore.
     */
    @Test
    public void testGetBooksByAuthor() {
        System.out.println("getBooksByAuthor");
        Author author = instance.getBook(0).get().getAuthor();
        List<Book> result = instance.getBooksByAuthor(author.getLname());
        assertEquals(result.size(), result.stream().filter(book -> book.getAuthor().getLname().equals(author.getLname())).count());

    }

    /**
     * Test of getNewBooks method, of class Bookstore.
     */
    @Test
    public void testGetNewBooks() {
        System.out.println("getNewBooks");
        SUBJECTS subject = instance.getBook(0).get().getSubject();
        List<Book> result = instance.getNewBooks(subject);
        assertEquals(result.size(),
                result.stream().filter(book -> book.getSubject().equals(subject)).count());

    }

    /**
     * Test of updateBook method, of class Bookstore.
     */
    @Test
    public void testUpdateBook() {
        System.out.println("updateBook");
        int bId = 0;
        double cost = 0.0;
        String image = "";
        String thumbnail = "";
        long now = 0L;
        Book book = instance.getBook(bId).get();
        instance.updateBook(bId, image, thumbnail, now);
        assertEquals(bId, book.getId());
        //assertEquals(cost, book.getCost(), 0.0);
        assertEquals(image, book.getImage());
        assertEquals(thumbnail, book.getThumbnail());
    }

   
}
