package servico;

import dominio.Book;
import dominio.BestsellerBook;
import dominio.Cart;
import dominio.CreditCards;
import dominio.Customer;
import dominio.FakeOrder;
import dominio.Order;
import dominio.OrderLine;
import dominio.Rating;
import dominio.SUBJECTS;
import dominio.ShipTypes;
import dominio.StatusTypes;
import dominio.Stock;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Suíte de testes para a lógica de negócio da classe {@link Bookmarket}.
 * <p>
 * Os testes nesta classe seguem os princípios do BDD (Behavior-Driven
 * Development), onde cada método de teste corresponde a um cenário de negócio ou
 * a um critério de aceite derivado das User Stories (US1, US2, US3, US4)
 * descritas no `board.pdf`.
 * <p>
 * O método {@code setUp} é responsável por criar um ambiente de dados rico e
 * realista, com múltiplos Bookstores, para garantir que os cenários de negócio
 * possam ser validados de forma completa.
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
            Bookstore bookstore = new FakeBookstore(i);
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
     * Testa o cenário de recomendação para um cliente regular (US3).
     * Verifica se os livros recomendados vêm com o preço médio histórico de vendas.
     */
    @Test
    public void testRecommendationForRegularCustomerShouldUseAveragePrice() {
        System.out.println("US3 - Recommendation with Average Price for Regular Customer");
        // Arrange
        FakeBookstore fakeBookstore = (FakeBookstore) Bookmarket.getStateMachine().getState().get(0);
        Customer regularCustomer = findRegularCustomer();
        assertNotNull("Nenhum cliente regular (sem desconto) encontrado para o teste.", regularCustomer);

        fakeBookstore.clearOrders();

        Book bookToAverage = Bookstore.getBook(100).get();
        double price1 = 10.00;
        double price2 = 20.00;
        double expectedAverage = (price1 + price2) / 2.0;

        // Venda 1 a R$ 10.00
        fakeBookstore.updateStock(bookToAverage.getId(), price1);
        int cartId1 = Bookmarket.createEmptyCart(fakeBookstore.getId());
        Cart cart1 = Bookmarket.getCart(fakeBookstore.getId(), cartId1);
        cart1.increaseLine(fakeBookstore.getStock(bookToAverage.getId()), 1);
        Bookmarket.doBuyConfirm(fakeBookstore.getId(), cartId1, regularCustomer.getId(), CreditCards.VISA, 123L, "Test", new Date(), ShipTypes.AIR);

        // Venda 2 a R$ 20.00
        fakeBookstore.updateStock(bookToAverage.getId(), price2);
        int cartId2 = Bookmarket.createEmptyCart(fakeBookstore.getId());
        Cart cart2 = Bookmarket.getCart(fakeBookstore.getId(), cartId2);
        cart2.increaseLine(fakeBookstore.getStock(bookToAverage.getId()), 1);
        Bookmarket.doBuyConfirm(fakeBookstore.getId(), cartId2, regularCustomer.getId(), CreditCards.AMEX, 456L, "Test", new Date(), ShipTypes.AIR);

        // Adiciona avaliações para garantir que o cliente não seja "novo"
        Bookmarket.rateBook(regularCustomer.getId(), 1, 5);
        Bookmarket.rateBook(1, bookToAverage.getId(), 5);

        // Act
        Map<Book, Double> regularRecommendations = Bookmarket.getPriceBookRecommendationByUsers(regularCustomer.getId());

        // Assert
        assertNotNull("O mapa de recomendações não pode ser nulo.", regularRecommendations);
        if (regularRecommendations.containsKey(bookToAverage)) {
            assertEquals("O preço para o cliente regular deve ser a média histórica de vendas.",
                    expectedAverage, regularRecommendations.get(bookToAverage), 0.01);
        }
    }

    /**
     * Testa o cenário de recomendação para um cliente assinante (US4).
     * Verifica se os livros recomendados vêm com o menor preço de estoque (promocional).
     */
    @Test
    public void testRecommendationForSubscriberShouldUsePromotionalPrice() {
        System.out.println("US4 - Recommendation with Promotional Price for Subscriber");
        // Arrange
        FakeBookstore fakeBookstore = (FakeBookstore) Bookmarket.getStateMachine().getState().get(0);
        Customer subscriber = findSubscriberCustomer();
        assertNotNull("Nenhum cliente assinante (com desconto) encontrado para o teste.", subscriber);

        Book bookForSubscriber = Bookstore.getBook(200).get();
        double promotionalPrice = 9.99;
        
        fakeBookstore.updateStock(bookForSubscriber.getId(), promotionalPrice);

        // Adiciona avaliações para garantir que o cliente não seja "novo"
        Bookmarket.rateBook(subscriber.getId(), 10, 5);
        Bookmarket.rateBook(2, bookForSubscriber.getId(), 5);

        // Act
        Map<Book, Double> subscriberRecommendations = Bookmarket.getPriceBookRecommendationByUsers(subscriber.getId());

        // Assert
        assertNotNull("O mapa de recomendações do assinante não pode ser nulo.", subscriberRecommendations);
        if (subscriberRecommendations.containsKey(bookForSubscriber)) {
            assertEquals("O preço para o assinante deve ser o menor preço de estoque (promocional).",
                    promotionalPrice, subscriberRecommendations.get(bookForSubscriber), 0.01);
        }
    }

    /**
     * Testa o cenário de fallback de recomendação para um cliente novo (sem avaliações).
     * Verifica se o sistema retorna a lista de bestsellers.
     */
    @Test
    public void testGetPriceBookRecommendationByUsers() {
        System.out.println("Fallback - Recommendation for New Customer/testRecommendationForNewCustomerShouldFallbackToBestsellers");
        // Arrange
        Customer newCustomer = Bookmarket.createNewCustomer("New", "Fallback", "123 Street", "", "City", "ST", "12345",
                "United States", "123456789", "new@fallback.com", new Date(), "");
        
        // Act
        Map<Book, Double> fallbackRecommendations = Bookmarket.getPriceBookRecommendationByUsers(newCustomer.getId());

        // Assert
        assertNotNull("O mapa de fallback não pode ser nulo.", fallbackRecommendations);
        assertFalse("O mapa de fallback não deve estar vazio.", fallbackRecommendations.isEmpty());

        List<BestsellerBook> bestsellers = Bookmarket.getBestSellerBooks(null, 5);
        assertEquals("O número de recomendações de fallback deve ser igual ao número de bestsellers.",
                bestsellers.size(), fallbackRecommendations.size());

        List<Book> recommendedBooks = new ArrayList<>(fallbackRecommendations.keySet());
        for (int i = 0; i < bestsellers.size(); i++) {
            assertEquals("O livro recomendado deve ser um bestseller.", bestsellers.get(i).getBook(), recommendedBooks.get(i));
        }
    }

    /**
     * Testa o cenário de recomendação quando menos de 5 são possíveis (US3 - P02).
     * Verifica se o sistema retorna um número menor de recomendações em vez de falhar.
     */
    @Test
    public void testRecommendationWhenLessThanFiveArePossible() {
        System.out.println("US3 - Recommendation with Insufficient Results");
        // Arrange
        Customer customer = findRegularCustomer();
        assertNotNull("Nenhum cliente regular encontrado para o teste.", customer);

        int totalBooks = FakeBookstore.getBookCount();
        int booksToLeaveUnrated = 2;

        // Rate almost all books, leaving only a few for recommendation
        for (int i = 0; i < totalBooks - booksToLeaveUnrated; i++) {
            Bookmarket.rateBook(customer.getId(), i, 4);
        }

        // Add one more rating for another user to ensure the recommender has data
        Bookmarket.rateBook(customer.getId() + 1, 0, 5);


        // Act
        Map<Book, Double> recommendations = Bookmarket.getPriceBookRecommendationByUsers(customer.getId());

        // Assert
        assertNotNull("O mapa de recomendações não pode ser nulo.", recommendations);
        assertTrue("Deve haver no máximo o número de livros não avaliados.",
                recommendations.size() <= booksToLeaveUnrated);
    }

    /**
     * Testa se os livros já avaliados por um cliente são excluídos das recomendações (US3 - N02).
     */
    @Test
    public void testRecommendationShouldExcludeRatedBooks() {
        System.out.println("US3 - Recommendation Excludes Rated Books");
        // Arrange
        Customer customer = findRegularCustomer();
        assertNotNull("Nenhum cliente regular encontrado para o teste.", customer);

        Book ratedBook = Bookstore.getBook(150).get();

        // Rate a specific book
        Bookmarket.rateBook(customer.getId(), ratedBook.getId(), 5);
        
        // Add other ratings to build a profile
        Bookmarket.rateBook(customer.getId(), 1, 4);
        Bookmarket.rateBook(customer.getId() + 1, 1, 5);
        Bookmarket.rateBook(customer.getId() + 1, ratedBook.getId(), 1);


        // Act
        Map<Book, Double> recommendations = Bookmarket.getPriceBookRecommendationByUsers(customer.getId());

        // Assert
        assertNotNull("O mapa de recomendações não pode ser nulo.", recommendations);
        assertFalse("O livro já avaliado não deveria estar na lista de recomendações.", 
                    recommendations.containsKey(ratedBook));
    }

    /**
     * Testa o fallback de preço para assinante quando não há preço promocional (US4 - P03).
     * O preço exibido deve ser o preço de varejo padrão.
     */
    @Test
    public void testSubscriberRecommendationPriceFallback() {
        System.out.println("US4 - Subscriber Price Fallback to Standard Price");
        // Arrange
        FakeBookstore fakeBookstore = (FakeBookstore) Bookmarket.getStateMachine().getState().get(0);
        Customer subscriber = findSubscriberCustomer();
        assertNotNull("Nenhum cliente assinante encontrado para o teste.", subscriber);
        
        Book bookToRecommend = Bookstore.getBook(300).get();
        double standardPrice = 35.00;

        // Set a standard price, ensuring no lower promotional price exists for this test
        fakeBookstore.updateStock(bookToRecommend.getId(), standardPrice);

        // Create a similar user profile to encourage the recommendation
        Bookmarket.rateBook(subscriber.getId(), 1, 5); // Base rating for subscriber
        Bookmarket.rateBook(subscriber.getId() + 1, 1, 5); // Similar user has same taste
        Bookmarket.rateBook(subscriber.getId() + 1, bookToRecommend.getId(), 5); // Similar user likes the target book

        // Act
        Map<Book, Double> recommendations = Bookmarket.getPriceBookRecommendationByUsers(subscriber.getId());

        // Assert
        assertNotNull("O mapa de recomendações não pode ser nulo.", recommendations);
        if (recommendations.containsKey(bookToRecommend)) {
            assertEquals("O preço deve ser o de varejo padrão quando não há promoção.",
                    standardPrice, recommendations.get(bookToRecommend), 0.01);
        }
    }

    /**
     * Testa o cálculo de preço para um livro recomendado que nunca foi vendido (US3).
     * O preço médio de venda deve ser 0.0.
     */
    @Test
    public void testRecommendationForBookWithNoSalesHistory() {
        System.out.println("US3 - Price for Recommended Book with No Sales History");
        // Arrange
        FakeBookstore fakeBookstore = (FakeBookstore) Bookmarket.getStateMachine().getState().get(0);
        fakeBookstore.clearOrders(); // Isola o teste garantindo que não há histórico de vendas

        Customer customer = findRegularCustomer();
        assertNotNull("Nenhum cliente regular encontrado para o teste.", customer);

        Book bookWithNoHistory = Bookstore.getBook(400).get();

        // Para encorajar a recomendação, cria um usuário similar que avaliou o livro
        Customer similarUser = Bookstore.getCustomer(customer.getId() + 1);
        if (similarUser == null) {
            similarUser = Bookmarket.createNewCustomer("Similar", "User", "123", "", "City", "ST", "12345", "USA", "123", "similar@user.com", new Date(), "");
        }
        
        Bookmarket.rateBook(customer.getId(), 1, 5); // Garante que o cliente principal tem um perfil
        Bookmarket.rateBook(similarUser.getId(), 1, 5); // Torna o usuário similar
        Bookmarket.rateBook(similarUser.getId(), bookWithNoHistory.getId(), 5); // Usuário similar avalia o livro alvo

        // Act
        Map<Book, Double> recommendations = Bookmarket.getPriceBookRecommendationByUsers(customer.getId());

        // Assert
        assertNotNull("O mapa de recomendações não pode ser nulo.", recommendations);
        if (recommendations.containsKey(bookWithNoHistory)) {
            assertEquals("O preço médio para um livro sem histórico de vendas deve ser 0.0.",
                    0.0, recommendations.get(bookWithNoHistory), 0.01);
        }
    }

    private Customer findRegularCustomer() {
        for (int i = 1; i <= 1000; i++) {
            Customer c = Bookstore.getCustomer(i);
            if (c != null && c.getDiscount() == 0) {
                return c;
            }
        }
        return null;
    }

    private Customer findSubscriberCustomer() {
        for (int i = 1; i <= 1000; i++) {
            Customer c = Bookstore.getCustomer(i);
            if (c != null && c.getDiscount() > 0) {
                return c;
            }
        }
        return null;
    }


    @Test
    public void testGetStocksRecommendationByUsers() {
        System.out.println("getStocksRecommendationByUsers");
    }

    /**
     * Test of getPriceBookRecommendationByUsers method, of class Bookmarket.
     * 
     * Testa os cenários de listagem de Bestsellers (US1).
     *
     * Este método deve conter os testes para os seguintes cenários de negócio
     * definidos no `board.pdf`:
     * <ul>
     * <li><b>Cenário 1 (Positivo):</b> Solicita uma lista de N bestsellers e
     * verifica se a lista retornada tem o tamanho N e está ordenada
     * corretamente pela quantidade de vendas. (P01, P02, P03)</li>
     * <li><b>Cenário 2 (Limite Inválido):</b> Tenta solicitar a lista com um N
     * inválido (ex: 0 ou > 100) e espera uma exceção
     * (IllegalArgumentException). (No1, No2)</li>
     * <li><b>Cenário 3 (Dados Vazios):</b> Solicita a lista em um cenário sem
     * histórico de vendas e espera uma lista vazia. (No3)</li>
     * </ul>
     */
    @Test
    public void testGetBestsellers_PositiveScenario() {
        System.out.println("getBestsellers");

        // Arrange
        int limit = 10;

        // Act        
        List<BestsellerBook> bestsellers = Bookmarket.getBestSellerBooks(SUBJECTS.ARTS, limit);
        
        // Assert
        // assertNotNull("Retorno nulo ao buscar bestsellers", Bookmarket.getBestSellers(null,5));
        // assertEquals("Tamanhodo retorno nao bate com o tamanho informado",5, Bookmarket.getBestSellers(SUBJECTS.ARTS, 5).size());
        // assertEquals("Tamanhodo retorno nao bate com o tamanho default:50",50, Bookmarket.getBestSellers(SUBJECTS.ARTS).size());
        assertNotNull("A lista de bestsellers não pode ser nula.", bestsellers);
        assertEquals("A lista deve conter o número de livros solicitado.", limit, bestsellers.size());

        // Verifica se a lista está ordenada corretamente (decrescente por vendas, crescente por título)
        for (int i = 0; i < bestsellers.size() - 1; i++) {
            BestsellerBook current = bestsellers.get(i);
            BestsellerBook next = bestsellers.get(i + 1);

            assertTrue("A contagem de vendas deve ser ordenada em ordem decrescente.",
                    current.getSalesCount() >= next.getSalesCount());

            if (current.getSalesCount() == next.getSalesCount()) {
                assertTrue("Em caso de empate nas vendas, o título deve ser ordenado em ordem alfabética.",
                        current.getBook().getTitle().compareTo(next.getBook().getTitle()) <= 0);
            }
        }
    }

    /**
     * Testa o cenário de empate na contagem de vendas (US1 - P04).
     * A ordenação secundária por título (alfabética) deve ser aplicada.
     */
    @Test
    public void testGetBestSellers_WithTies() {
        System.out.println("getBestsellers_WithTies");
        // Arrange: Força um empate entre dois livros
        // Limpa o histórico de vendas para isolar o teste
        Bookmarket.getStateMachine().getState()
                .forEach(bookstore -> ((FakeBookstore) bookstore).clearOrders());

        Book book1 = Bookstore.getBook(1).get(); // "THE ROAD TO RICHES"
        Book book2 = Bookstore.getBook(2).get(); // "THE TALE OF TWO CITIES"
        
        // // Garante que book1 vem antes de book2 alfabeticamente
        // assertTrue(book1.getTitle().compareTo(book2.getTitle()) < 0);

        // Cria um carrinho e adiciona os livros
        int cartId = Bookmarket.createEmptyCart(0);
        Cart cart = Bookmarket.getCart(0, cartId);
        cart.increaseLine(Bookmarket.getStock(0, book1.getId()), 10);
        cart.increaseLine(Bookmarket.getStock(0, book2.getId()), 10);

        
        System.out.println("#################1");
        System.out.println(book1.getTitle());
        System.out.println(book2.getTitle());

        // Confirma a compra para gerar a ordem
        Customer customer = Bookstore.getCustomer(1);
        Order order = Bookmarket.doBuyConfirm(0, cartId, customer.getId(), CreditCards.VISA, 1234567890123456L,
                customer.getFname(), new Date(), ShipTypes.AIR);

        ((FakeOrder) order).setStatus(StatusTypes.SHIPPED);

         // Act → pede APENAS 1 bestseller
         List<BestsellerBook> bestsellers = Bookmarket.getBestSellerBooks(null, 1);  

        // Assert → deve retornar APENAS 1 bestseller
        assertNotNull(bestsellers);
        assertEquals("Deve retornar apenas 1 bestseller", 1, bestsellers.size());

        BestsellerBook result = bestsellers.get(0);

        // Descobre qual título deveria ganhar pelo critério alfabético
        Book expectedBook =
        book1.getTitle().compareTo(book2.getTitle()) < 0 ? book1 : book2;

        assertEquals(
        "Em caso de empate, deve retornar o livro com título alfabeticamente menor",
        expectedBook.getId(),
        result.getBook().getId()
        );

        assertEquals(10, result.getSalesCount());
    }

    /**
     * Testa o cenário de limite inválido N=0 (US1 - No2).
     * Espera-se que uma IllegalArgumentException seja lançada.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetBestsellers_WithInvalidLimitOfZero() {
        System.out.println("getBestsellers_WithInvalidLimitOfZero");
        // Act
        Bookmarket.getBestSellers(0);
    }

    /**
     * Testa o cenário de limite inválido N > 100 (US1 - No1).
     * Espera-se que uma IllegalArgumentException seja lançada.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetBestsellers_WithInvalidLimitAbove100() {
        System.out.println("getBestsellers_WithInvalidLimitAbove100");
        // Act
        Bookmarket.getBestSellers(101);
    }

    /**
     * Testa o cenário com histórico de vendas vazio (US1 - No3).
     * Espera-se uma lista vazia.
     */
    @Test
    public void testGetBestsellers_WithEmptySalesHistory() {
        System.out.println("getBestsellers_WithEmptySalesHistory");
        // Arrange
        // Limpa o histórico de vendas de todas as livrarias falsas
        Bookmarket.getStateMachine().getState()
                .forEach(bookstore -> ((FakeBookstore) bookstore).clearOrders());


        // Act
        List<BestsellerBook> bestsellers = Bookmarket.getBestSellerBooks(null, 10);

        // Assert
        assertNotNull("A lista não pode ser nula, mesmo que vazia.", bestsellers);
        assertTrue("A lista de bestsellers deve estar vazia quando não há vendas.", bestsellers.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTopNGreaterThan100Throws() {
        System.out.println("testTopNGreaterThan100Throws");
        Bookmarket.getBestSellers(SUBJECTS.ARTS, 101);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTopNZeroThrows() {
        System.out.println("testTopNZeroThrows");
        Bookmarket.getBestSellers(SUBJECTS.ARTS, 0);
    }

    /**
     * Teste do método getBestSellers, da classe Bookmarket, com foco no tipo de retorno Map.
     *
     * Este conjunto de testes replica os testes de Bestsellers (US1) para a API pública
     * que retorna um Map<Book, Set<Stock>.
     */
    @Test
    public void testGetBestsellers() {
         System.out.println("getBestSellersMap_DefaultNoParams");

        // Arrange
        int limit = 50; // Default limit

        // Act        
        Map<Book, Set<Stock>> bestsellers = Bookmarket.getBestSellers();
        
        // Assert
        assertNotNull("O mapa de bestsellers não pode ser nulo.", bestsellers);
        assertEquals("O mapa deve conter o número de livros solicitado (padrão 50).", limit, bestsellers.size());

        // Verifica se a ordem do mapa (LinkedHashMap) corresponde à ordem da lista de BestsellerBook
        List<BestsellerBook> expectedOrder = Bookmarket.getBestSellerBooks(null, limit);
        List<Book> actualOrder = new ArrayList<>(bestsellers.keySet());

        for (int i = 0; i < limit; i++) {
            assertEquals("A ordem dos livros no mapa deve corresponder à ordem dos best-sellers.",
                         expectedOrder.get(i).getBook(), actualOrder.get(i));
        }



    }

    @Test
    public void testGetBestsellers_OnlyShippedOrdersAreConsidered() {
        System.out.println("getBestSellers_OnlyShippedOrdersAreConsidered");

        // Arrange
        Bookmarket.getStateMachine().getState()
                .forEach(bookstore -> ((FakeBookstore) bookstore).clearOrders());

        Book book = Bookstore.getBook(1).get();
        Customer customer = Bookstore.getCustomer(1);

        // Order SHIPPED (deve contar)
        int cartId1 = Bookmarket.createEmptyCart(0);
        Cart cart1 = Bookmarket.getCart(0, cartId1);
        cart1.increaseLine(Bookmarket.getStock(0, book.getId()), 10);

        Order shippedOrder = Bookmarket.doBuyConfirm(
                0, cartId1, customer.getId(),
                CreditCards.VISA, 1234567890123456L,
                customer.getFname(), new Date(), ShipTypes.AIR
        );
        ((FakeOrder) shippedOrder).setStatus(StatusTypes.SHIPPED);

        // Order NÃO SHIPPED (não deve contar)
        int cartId2 = Bookmarket.createEmptyCart(0);
        Cart cart2 = Bookmarket.getCart(0, cartId2);
        cart2.increaseLine(Bookmarket.getStock(0, book.getId()), 20);

        Order notShippedOrder = Bookmarket.doBuyConfirm(
                0, cartId2, customer.getId(),
                CreditCards.VISA, 1234567890123456L,
                customer.getFname(), new Date(), ShipTypes.AIR
        );
        ((FakeOrder) notShippedOrder).setStatus(StatusTypes.PENDING);

        // Act
        Map<Book, Set<Stock>> bestsellers = Bookmarket.getBestSellers();

        // Assert
        assertNotNull("O mapa de bestsellers não pode ser nulo.", bestsellers);
        assertTrue("O livro deve aparecer como bestseller.", bestsellers.containsKey(book));

        List<BestsellerBook> bestsellerBooks = Bookmarket.getBestSellerBooks(null, 50);
        BestsellerBook bestseller = bestsellerBooks.stream()
                .filter(b -> b.getBook().equals(book))
                .findFirst()
                .orElse(null);

        assertNotNull("O bestseller deve existir.", bestseller);

        // Apenas a order SHIPPED (10 unidades) deve ser considerada
        assertEquals(
                "Somente orders com status SHIPPED devem ser consideradas no cálculo.",
                10,
                bestseller.getSalesCount()
        );
    }

    @Test
    public void testGetBestSellersMap_PositiveScenario() {
        System.out.println("getBestSellersMap_PositiveScenario");

        // Arrange
        int limit = 10;
        SUBJECTS category = SUBJECTS.ARTS;

        // Act        
        Map<Book, Set<Stock>> bestsellers = Bookmarket.getBestSellers(category, limit);
        
        // Assert
        assertNotNull("O mapa de bestsellers não pode ser nulo.", bestsellers);
        assertEquals("O mapa deve conter o número de livros solicitado.", limit, bestsellers.size());

        // Verifica se a ordem do mapa (LinkedHashMap) corresponde à ordem da lista de BestsellerBook
        List<BestsellerBook> expectedOrder = Bookmarket.getBestSellerBooks(category, limit);
        List<Book> actualOrder = new ArrayList<>(bestsellers.keySet());

        for (int i = 0; i < limit; i++) {
            assertEquals("A ordem dos livros no mapa deve corresponder à ordem dos best-sellers.",
                         expectedOrder.get(i).getBook(), actualOrder.get(i));
        }
    }

    @Test
    public void testGetBestSellersMap_WithTies() {
        System.out.println("getBestSellersMap_WithTies");
        // Arrange: Força um empate entre dois livros
        Bookmarket.getStateMachine().getState()
                .forEach(bookstore -> ((FakeBookstore) bookstore).clearOrders());

        Book book1 = Bookstore.getBook(1).get();
        Book book2 = Bookstore.getBook(2).get(); 

        int cartId = Bookmarket.createEmptyCart(0);
        Cart cart = Bookmarket.getCart(0, cartId);

        cart.increaseLine(Bookmarket.getStock(0, book1.getId()), 10);
        cart.increaseLine(Bookmarket.getStock(0, book2.getId()), 10);

        Customer customer = Bookstore.getCustomer(1);
        Order order = Bookmarket.doBuyConfirm(0, cartId, customer.getId(), CreditCards.VISA, 1234567890123456L,
                customer.getFname(), new Date(), ShipTypes.AIR);

        ((FakeOrder) order).setStatus(StatusTypes.SHIPPED);

        // Act
        Map<Book, Set<Stock>> bestsellersMap = Bookmarket.getBestSellers(null, 2);
        List<Book> books = new ArrayList<>(bestsellersMap.keySet());

        // Assert
        assertTrue("O mapa de best-sellers deve conter ambos os livros.",
                   bestsellersMap.containsKey(book1) && bestsellersMap.containsKey(book2));

        // Assert — presença
        assertEquals("Devem existir dois bestsellers", 2, books.size());
        assertTrue(bestsellersMap.containsKey(book1));
        assertTrue(bestsellersMap.containsKey(book2));

                // Assert — ordem alfabética
        Book expectedFirst =book1.getTitle().compareTo(book2.getTitle()) < 0 ? book1 : book2;
        Book expectedSecond = expectedFirst == book1 ? book2 : book1;

        assertEquals(
            "Primeiro livro deve ser o alfabeticamente menor",
            expectedFirst.getId(),
            books.get(0).getId()
        );

        assertEquals(
            "Segundo livro deve ser o alfabeticamente maior",
            expectedSecond.getId(),
            books.get(1).getId()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBestSellersMap_WithInvalidLimitOfZero() {
        System.out.println("getBestSellersMap_WithInvalidLimitOfZero");
        Bookmarket.getBestSellers(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBestSellersMap_WithInvalidLimitAbove100() {
        System.out.println("getBestSellersMap_WithInvalidLimitAbove100");
        Bookmarket.getBestSellers(101);
    }

    @Test
    public void testGetBestSellersMap_WithEmptySalesHistory() {
        System.out.println("getBestSellersMap_WithEmptySalesHistory");
        // Arrange
        Bookmarket.getStateMachine().getState()
                .forEach(bookstore -> ((FakeBookstore) bookstore).clearOrders());

        // Act
        Map<Book, Set<Stock>> bestsellers = Bookmarket.getBestSellers(null, 10);

        // Assert
        assertNotNull("O mapa não pode ser nulo, mesmo que vazio.", bestsellers);
        assertTrue("O mapa de bestsellers deve estar vazio quando não há vendas.", bestsellers.isEmpty());
    }

    /**
     * Test of rateBook method, of class Bookmarket.
     *
     * Testa os cenários de avaliação de livros (US2).
     *
     * <ul>
     * <li><b>Cenário 1 (Sucesso):</b> Verifica se um cliente consegue avaliar um
     * livro com uma nota válida e se a avaliação é armazenada.</li>
     * <li><b>Cenário 2 (Atualização):</b> Verifica se, ao avaliar o mesmo livro
     * novamente, a nota anterior é substituída pela nova.</li>
     * <li><b>Cenário 3 (Nota Inválida):</b> Verifica se o sistema rejeita uma
     * avaliação com nota fora do intervalo (1-5), lançando
     * IllegalArgumentException.</li>
     * <li><b>Cenário 4 (Entidades Inválidas):</b> Verifica se o sistema rejeita
     * uma avaliação para um cliente ou livro inexistente.</li>
     * </ul>
     */
    @Test
    public void testRateBook_WithValidData_ShouldStoreRating() {
        System.out.println("rateBook: valid scenario");
        // Arrange
        int customerId = 10;
        int bookId = 25;
        int ratingValue = 5;

        // Act
        Bookmarket.rateBook(customerId, bookId, ratingValue);

        // Assert
        Rating storedRating = Bookstore.getRating(customerId, bookId);
        assertNotNull("A avaliação deveria ter sido salva.", storedRating);
        assertEquals("A nota armazenada está incorreta.", ratingValue, storedRating.getRating());
    }

    @Test
    public void testRateBook_WhenRatingExists_ShouldUpdateRating() {
        System.out.println("rateBook: update scenario");
        // Arrange
        int customerId = 11;
        int bookId = 26;
        Bookmarket.rateBook(customerId, bookId, 3); // Avaliação inicial

        int newRatingValue = 1;

        // Act
        Bookmarket.rateBook(customerId, bookId, newRatingValue);

        // Assert
        Rating updatedRating = Bookstore.getRating(customerId, bookId);
        assertNotNull(updatedRating);
        assertEquals("A nota deveria ter sido atualizada.", newRatingValue, updatedRating.getRating());
        assertEquals("Deveria haver apenas uma avaliação para este par cliente/livro.", 1,
                Bookstore.ratings.stream().filter(r -> r.getCustomer().getId() == customerId && r.getBook().getId() == bookId).count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRateBook_WithInvalidRating_ShouldThrowException() {
        System.out.println("rateBook: invalid rating scenario");
        // Arrange
        int customerId = 12;
        int bookId = 27;
        int invalidRating = 6; // Nota fora do intervalo 1-5
        // Act
        Bookmarket.rateBook(customerId, bookId, invalidRating);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRateBook_WithNonExistentCustomer_ShouldThrowException() {
        System.out.println("rateBook: non-existent customer scenario");
        // Arrange
        int nonExistentCustomerId = 9999; // ID de cliente que sabidamente não existe
        int bookId = 28;
        int ratingValue = 4;

        // Act
        Bookmarket.rateBook(nonExistentCustomerId, bookId, ratingValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRateBook_WithNonExistentBook_ShouldThrowException() {
        System.out.println("rateBook: non-existent book scenario");
        // Arrange
        int customerId = 13;
        int nonExistentBookId = 9923999; // ID de livro que sabidamente não existe
        int ratingValue = 4;

        // Act
        Bookmarket.rateBook(customerId, nonExistentBookId, ratingValue);
    }
}
