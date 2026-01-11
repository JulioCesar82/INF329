package servico;

import dominio.Book;
import dominio.Evaluation;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import java.util.stream.Collectors;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;

import java.util.Collections;
import java.util.List;

/**
 * Serviço dedicado a encapsular a lógica do motor de recomendação Apache Mahout.
 * <p>
 * Esta classe é responsável por inicializar o DataModel com as avaliações dos
 * usuários, configurar os algoritmos de similaridade e vizinhança, e gerar as
 * recomendações de livros "cruas" (sem a lógica de preço).
 */
public class RecommendationService {

    // Store the recommender and dataModel as fields
    private UserBasedRecommender recommender;
    private DataModel dataModel;
    private boolean isInitialized = false;

    /**
     * Construtor privado para encorajar a criação de uma única instância
     * gerenciada.
     */
    public RecommendationService() {
        // Inicialização pode ser feita aqui ou em um método `init`.
        
    }

    /**
     * Inicializa o serviço de recomendação com os dados de avaliação.
     * <p>
     * Este método deve ser chamado uma vez para configurar o DataModel do Mahout.
     *
     * @param evaluations A lista de todas as avaliações de usuários.
     */
    public void initialize(List<Evaluation> evaluations) {
        FastByIDMap<PreferenceArray> preferences = new FastByIDMap<>();
        evaluations.stream()
            .collect(Collectors.groupingBy(Evaluation::getUserId))
            .forEach((userId, userEvaluations) -> {
                PreferenceArray prefs = new GenericUserPreferenceArray(userEvaluations.size());
                for (int i = 0; i < userEvaluations.size(); i++) {
                    Evaluation eval = userEvaluations.get(i);
                    prefs.setUserID(i, userId);
                    prefs.setItemID(i, eval.getBookId());
                    prefs.setValue(i, eval.getRating());
                }
                preferences.put(userId, prefs);
            });
        
        dataModel = new GenericDataModel(preferences);
        
        try {
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);
            recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
            isInitialized = true;
            System.out.println("RecommendationService initialized successfully with " + evaluations.size() + " evaluations.");
        } catch (TasteException e) {
            System.err.println("Error initializing recommendation service: " + e.getMessage());
            e.printStackTrace();
            isInitialized = false;
        }
    }

    /**
     * Gera uma lista de recomendações de livros para um usuário.
     *
     * @param userId  O ID do usuário para o qual gerar as recomendações.
     * @param howMany O número de recomendações a serem geradas.
     * @return Uma lista de IDs de livros recomendados.
     */
    public List<Long> getRecommendations(long userId, int howMany) {
        if (!isInitialized) {
            System.out.println("Recommendation service not initialized.");
            return Collections.emptyList();
        }

        try {
            // Get recommendations from Mahout
            List<RecommendedItem> items = recommender.recommend(userId, howMany);
            
            // Extract book IDs from recommended items
            List<Long> bookIds = items.stream()
                    .map(RecommendedItem::getItemID)
                    .collect(Collectors.toList());
            
            System.out.println("Generated " + bookIds.size() + " recommendations for user " + userId);
            return bookIds;
            
        } catch (TasteException e) {
            System.err.println("Error generating recommendations for user " + userId + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
