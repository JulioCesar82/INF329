package servico;

import dominio.Book;
import dominio.Order;
import dominio.OrderLine;
import dominio.Rating;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço dedicado a encapsular a lógica do motor de recomendação Apache Mahout.
 * <p>
 * Esta classe é responsável por:
 * <ul>
 *   <li>Gerar recomendações de livros usando User-Based Collaborative Filtering</li>
 *   <li>Calcular preços médios históricos de livros baseado em vendas passadas</li>
 *   <li>Construir e gerenciar o DataModel do Mahout a partir das avaliações</li>
 * </ul>
 */
public class RecommendationService {

    /**
     * **US3: Sugestão de Livros para Clientes Regulares (Valor Médio).**
     * <p>
     * Gera uma lista de 5 livros recomendados para um determinado cliente e calcula
     * o Valor Médio de Venda Histórica para cada livro.
     * <p>
     * O preço exibido para cada livro recomendado é a **média de seu preço de venda
     * histórico**, calculado a partir de todas as {@link OrderLine}s.
     *
     * @param c_id O ID do cliente para o qual a recomendação será gerada.
     * @return Um {@link Map} onde cada {@link Book} recomendado mapeia para o seu
     * Valor Médio de Venda Histórica ({@code Double}).
     */
    public static Map<Book, Double> getPriceBookRecommendationByUsers(int c_id) {
        List<Book> recommendedBooks = getRecommendedBooks(c_id);

        Map<Book, Double> result = new LinkedHashMap<>();

        for (Book book : recommendedBooks) {
            double averagePrice = calculateHistoricalAveragePrice(book);
            result.put(book, averagePrice);
        }

        return result;
    }

    /**
     * Calcula o Valor Médio de Venda Histórica para um livro.
     * <p>
     * O cálculo é feito somando (preço × quantidade) de todas as {@link OrderLine}s
     * que contêm este livro e dividindo pela quantidade total vendida.
     *
     * @param book O livro para o qual calcular o valor médio.
     * @return O valor médio de venda histórica. Se o livro nunca foi vendido,
     *         retorna o SRP (Suggested Retail Price) do livro.
     */
    private static double calculateHistoricalAveragePrice(Book book) {
        double totalPrice = 0.0;
        int totalQty = 0;

        List<Order> allOrders = Bookmarket.getBookstoreStream()
            .flatMap(bookstore -> bookstore.getOrdersByCreation().stream())
            .collect(Collectors.toList());

        for (Order order : allOrders) {
            for (OrderLine orderLine : order.getLines()) {
                if (orderLine.getBook().getId() == book.getId()) {
                    totalPrice += orderLine.getPrice() * orderLine.getQty();
                    totalQty += orderLine.getQty();
                }
            }
        }

        if (totalQty == 0) {
            return book.getSrp();
        }

        return totalPrice / totalQty;
    }

    /**
     * Gera recomendações de livros para um cliente usando Apache Mahout.
     * <p>
     * Utiliza:
     * <ul>
     *   <li>User-Based Collaborative Filtering</li>
     *   <li>Pearson Correlation Similarity</li>
     *   <li>Nearest N User Neighborhood (N=10)</li>
     * </ul>
     *
     * @param c_id O ID do cliente.
     * @return Lista de livros recomendados (até 5).
     */
    private static List<Book> getRecommendedBooks(int c_id) {
        try {
            DataModel dataModel = buildDataModel();

            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, dataModel);
            Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

            List<RecommendedItem> recommendations = recommender.recommend(c_id, 5);

            List<Book> recommendedBooks = new ArrayList<>();
            for (RecommendedItem recommendation : recommendations) {
                int bookId = (int) recommendation.getItemID();
                Bookstore.getBook(bookId).ifPresent(recommendedBooks::add);
            }

            return recommendedBooks;
        } catch (TasteException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Constrói o DataModel do Mahout a partir das avaliações armazenadas.
     * <p>
     * Agrupa as avaliações (ratings) por cliente e cria um GenericDataModel
     * que pode ser usado pelos algoritmos de recomendação do Mahout.
     *
     * @return O DataModel populado com todas as avaliações de usuários.
     */
    private static DataModel buildDataModel() {
        FastByIDMap<PreferenceArray> userData = new FastByIDMap<>();

        Map<Integer, List<Rating>> ratingsByCustomer = Bookstore.ratings.stream()
            .collect(Collectors.groupingBy(r -> r.getCustomer().getId()));

        for (Map.Entry<Integer, List<Rating>> entry : ratingsByCustomer.entrySet()) {
            int customerId = entry.getKey();
            List<Rating> ratings = entry.getValue();

            GenericUserPreferenceArray preferences = new GenericUserPreferenceArray(ratings.size());
            preferences.setUserID(0, customerId);

            for (int i = 0; i < ratings.size(); i++) {
                Rating rating = ratings.get(i);
                preferences.setItemID(i, rating.getBook().getId());
                preferences.setValue(i, rating.getRating());
            }

            userData.put(customerId, preferences);
        }

        return new GenericDataModel(userData);
    }
}
