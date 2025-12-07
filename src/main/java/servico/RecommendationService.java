package servico;

import dominio.Evaluation;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço dedicado a encapsular a lógica do motor de recomendação Apache Mahout.
 * <p>
 * Esta classe é responsável por inicializar o DataModel com as avaliações dos
 * usuários, configurar os algoritmos de similaridade e vizinhança, e gerar as
 * recomendações de livros "cruas" (sem a lógica de preço).
 */
public class RecommendationService {

    private UserBasedRecommender recommender;
    private DataModel dataModel;
    private boolean isInitialized = false;
    private static final int NEIGHBORHOOD_SIZE = 10;

    /**
     * Construtor padrão.
     */
    public RecommendationService() {
    }

    /**
     * Inicializa o serviço de recomendação com os dados de avaliação.
     * <p>
     * Este método deve ser chamado uma vez para configurar o DataModel do Mahout.
     *
     * @param evaluations A lista de todas as avaliações de usuários.
     */
    public void initialize(List<Evaluation> evaluations) {
        try {
            // 1. Converte a lista de `Evaluation` para o formato que o Mahout espera (FastByIDMap<PreferenceArray>).
            Map<Long, List<Preference>> preferencesByUser = evaluations.stream()
                .collect(Collectors.groupingBy(
                    Evaluation::getUserId,
                    Collectors.mapping(
                        eval -> new GenericPreference(eval.getUserId(), eval.getBookId(), eval.getRating()),
                        Collectors.toList()
                    )
                ));

            FastByIDMap<PreferenceArray> preferences = new FastByIDMap<>();
            for (Map.Entry<Long, List<Preference>> entry : preferencesByUser.entrySet()) {
                preferences.put(entry.getKey(), new GenericUserPreferenceArray(entry.getValue()));
            }

            this.dataModel = new GenericDataModel(preferences);

            // 2. Instancia a Similaridade (PearsonCorrelationSimilarity)
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);

            // 3. Instancia a Vizinhança (NearestNUserNeighborhood)
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_SIZE, similarity, dataModel);

            // 4. Instancia o Recomendador (GenericUserBasedRecommender)
            this.recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

            // 5. Marca o serviço como inicializado.
            this.isInitialized = true;
            System.out.println("RecommendationService inicializado com sucesso.");

        } catch (TasteException e) {
            // Em um cenário real, um logging mais robusto seria ideal.
            System.err.println("Falha ao inicializar o RecommendationService: " + e.getMessage());
            e.printStackTrace();
            this.isInitialized = false;
        }
    }

    /**
     * Gera uma lista de recomendações de livros para um usuário.
     *
     * @param userId  O ID do usuário para o qual gerar as recomendações.
     * @param howMany O número de recomendações a serem geradas.
     * @return Uma lista de IDs de livros recomendados.
     * @throws TasteException se o Mahout encontrar um erro, como um usuário desconhecido.
     */
    public List<Long> getRecommendations(long userId, int howMany) throws TasteException {
        if (!isInitialized) {
            System.err.println("Serviço de recomendação não foi inicializado corretamente.");
            return Collections.emptyList();
        }

        // 1. Chama o método recommender.recommend(userId, howMany).
        List<RecommendedItem> items = recommender.recommend(userId, howMany);

        // 2. Extrai os IDs dos itens (livros) da lista de RecommendedItem.
        return items.stream()
                .map(RecommendedItem::getItemID)
                .collect(Collectors.toList());
    }

    /**
     * Verifica se o serviço foi inicializado com sucesso.
     * @return {@code true} se o serviço estiver pronto, {@code false} caso contrário.
     */
    public boolean isInitialized() {
        return isInitialized;
    }
}
