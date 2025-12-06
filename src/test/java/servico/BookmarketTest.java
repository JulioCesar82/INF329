package servico;

import dominio.Book;
import dominio.Cart;
import dominio.Rating;
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
     * 
     * Testa os cenários de recomendação de preço (US3 & US4).
     *
     * Este método deve conter os testes para os seguintes cenários de negócio
     * definidos no `board.pdf`:
     * <ul>
     * <li><b>Cenário 1 (US3 - Cliente Regular):</b> Verifica se, para um cliente
     * regular, os livros recomendados vêm com o preço médio histórico.</li>
     * <li><b>Cenário 2 (US4 - Assinante):</b> Verifica se, para um assinante,
     * os livros recomendados vêm com o menor preço de estoque (promocional).</li>
     * <li><b>Cenário 3 (Fallback):</b> Verifica se, para um cliente novo (sem
     * avaliações), o sistema retorna a lista de bestsellers.</li>
     * </ul>
     */
    @Test
    public void testGetPriceBookRecommendationByUsers() {
        System.out.println("getPriceBookRecommendationByUsers");

        // Test of getPriceBookRecommendationByUsers method, of class Bookmarket.
        //
        // TODO: Cenário 1: Teste para Cliente Regular (US3)
        // 1. Escolha um ID de cliente que NÃO seja assinante (ex: discount == 0).
        // 2. Execute Bookmarket.getPriceBookRecommendationByUsers(c_id).
        // 3. Verifique se o resultado contém 5 livros.
        // 4. Para um dos livros, calcule manualmente o preço médio esperado
        //    com base nos dados populados no setUp.
        // 5. Compare o preço retornado no mapa com o preço médio calculado usando assertEquals.

        // TODO: Cenário 2: Teste para Assinante (US4)
        // 1. Escolha um ID de cliente que SEJA assinante (ex: discount > 0).
        // 2. Execute Bookmarket.getPriceBookRecommendationByUsers(c_id).
        // 3. Verifique se o resultado contém 5 livros.
        // 4. Para um dos livros, encontre o menor preço (`cost`) esperado no `Stock`.
        // 5. Compare o preço retornado no mapa com o menor preço usando assertEquals.

        // TODO: Cenário 3: Teste de Fallback para Cliente Novo
        // 1. Crie um novo cliente que não tenha nenhuma avaliação ou pedido.
        // 2. Execute Bookmarket.getPriceBookRecommendationByUsers(new_c_id).
        // 3. O resultado esperado é uma lista de bestsellers. Verifique se o resultado
        //    corresponde ao que o método getBestSellers retornaria.
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
    public void testGetBestsellers() {
        System.out.println("getBestsellers");

        // TODO: Cenário 1: Teste de Bestsellers com N válido
        // 1. Execute Bookmarket.getBestSellers(SUBJECTS.ARTS) (ou uma versão generalizada).
        // 2. Verifique se o mapa retornado não é nulo.
        // 3. Verifique se a quantidade de livros no mapa corresponde ao esperado (ex: 5).
        // 4. Verifique se os livros estão ordenados corretamente (o mais vendido primeiro).
        //    - Para isso, será preciso calcular o ranking esperado manualmente.

        // TODO: Cenário 2: Teste de Limite Inválido
        // 1. Use assertThrows para chamar o método com N=0 ou N=101.
        // 2. Verifique se uma `IllegalArgumentException` é lançada.

        // TODO: Cenário 3: Teste com Histórico de Vendas Vazio
        // 1. Prepare um ambiente de teste sem nenhuma ordem (`Order`).
        // 2. Execute o método de bestsellers.
        // 3. Verifique se o mapa retornado está vazio (`assertTrue(result.isEmpty())`).
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
        int nonExistentBookId = 9999; // ID de livro que sabidamente não existe
        int ratingValue = 4;

        // Act
        Bookmarket.rateBook(customerId, nonExistentBookId, ratingValue);
    }
}
