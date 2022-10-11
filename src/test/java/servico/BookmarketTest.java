package servico;

import dominio.Book;
import dominio.Cart;
import dominio.CreditCards;
import dominio.Customer;
import dominio.Order;
import dominio.SUBJECTS;
import dominio.ShipTypes;
import dominio.Stock;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
public class BookmarketTest {

    public BookmarketTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    /**
     * É necessário popular a Bookstore para realização dos testes.
     *
     * Criamos 5 Bookstores dentro do Bookmarket para validar o bestseller,
     * populando cada instância do Bookstore com dados diferentes, garantindo
     * assim o funcionamento correto dos métodos do bestseller.
     */
    public void setUp() {
        long seed = 0;
        long now = System.currentTimeMillis();

        int items = 500;
        int customers = 1000;
        int addresses = 1000;
        int authors = 1000;
        int orders = 1000;

        Bookstore.populate(seed, now, items, customers, addresses, authors);

        Bookstore[] bookstores = new Bookstore[5];

        for (int i = 0; i < 5; i++) {
            Bookstore bookstore = new Bookstore(i);
            bookstore.createCart(0);

            /**
             * Adicionamos este loop para adicionar todos os livros ao estoque
             * com preço 1 desta forma prevenimos exceptions em métodos que
             * adiciona livros aleatoriamente no carrinho
             */
            for (int j = 0; j < items; j++) {
                //bookstore.updateStock(j, 1);
            }

            bookstores[i] = bookstore;
        }

        Bookmarket.init(0, bookstores);
        Bookmarket.populate(items, customers, addresses, authors, orders);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPriceBookRecommendationByUsers method, of class Bookmarket.
     */
    @Test
    public void testGetPriceBookRecommendationByUsers() {
        System.out.println("getPriceBookRecommendationByUsers");

    }

    /**
     * Test of getPriceBookRecommendationByUsers method, of class Bookmarket.
     */
    @Test
    public void testGetStocksRecommendationByUsers() {
        System.out.println("getStocksRecommendationByUsers");
    }

}
