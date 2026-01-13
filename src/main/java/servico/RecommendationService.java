package servico;

import dominio.Book;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço dedicado a encapsular a lógica do motor de recomendação Apache Mahout.
 * <p>
 * Esta classe é responsável por:
 * <ul>
 *   <li>Gerar recomendações de livros usando User-Based Collaborative Filtering</li>
 *   <li>Construir e gerenciar o DataModel do Mahout a partir das avaliações</li>
 * </ul>
 */
public class RecommendationService {

    private static final int DEFAULT_NUM_RECOMMENDATIONS = 5;
    private static final int NEIGHBORHOOD_SIZE = 10;

    /**
     * Gera recomendações de livros para um cliente usando Apache Mahout.
     * <p>
     * Utiliza o número padrão de recomendações ({@value #DEFAULT_NUM_RECOMMENDATIONS}).
     *
     * @param c_id O ID do cliente.
     * @return Lista de livros recomendados.
     */
    public static List<Book> getRecommendedBooks(int c_id) {
        return getRecommendedBooks(c_id, DEFAULT_NUM_RECOMMENDATIONS);
    }

    /**
     * Gera recomendações de livros para um cliente usando Apache Mahout.
     * <p>
     * Utiliza:
     * <ul>
     *   <li>User-Based Collaborative Filtering</li>
     *   <li>Pearson Correlation Similarity</li>
     *   <li>Nearest N User Neighborhood (N={@value #NEIGHBORHOOD_SIZE})</li>
     * </ul>
     *
     * @param c_id O ID do cliente.
     * @param numBooks O número de livros a recomendar.
     * @return Lista de livros recomendados.
     */
    public static List<Book> getRecommendedBooks(int c_id, int numBooks) {
        try {
            DataModel dataModel = buildDataModel();

            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_SIZE, similarity, dataModel);
            Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

            List<RecommendedItem> recommendations = recommender.recommend(c_id, numBooks);

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
