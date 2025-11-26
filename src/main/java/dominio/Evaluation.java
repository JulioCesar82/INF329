package dominio;

import java.io.Serializable;

/**
 * <img src="./doc-files/Evaluation.png" alt="Bookmarket">
 * <br><a href="./doc-files/Evaluation.html"> code </a>
 *
 */

/**
 * Representa a avaliação (rating) de um livro por um usuário.
 * <p>
 * Esta classe é o objeto de domínio central para a User Story US2, que trata
 * do registro de preferência de um cliente por um livro. Ela armazena a
 * relação entre um usuário, um item (livro) e a nota que ele atribuiu.
 * <p>
 * Objetos desta classe podem ser usados para popular o {@code DataModel} do
 * Apache Mahout.
 * <p>
 * <img src="./doc-files/Evaluation.png" alt="Bookmarket">
 * <br><a href="./doc-files/Evaluation.html"> code </a>
 *
 */
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long userId;
    private final long bookId;
    private final float rating;

    /**
     * Constrói uma nova avaliação.
     *
     * @param userId O ID do usuário que está avaliando.
     * @param bookId O ID do livro que está sendo avaliado.
     * @param rating A nota atribuída (ex: 0.0 a 5.0).
     */
    public Evaluation(long userId, long bookId, float rating) {
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
    }

    /**
     * @return O ID do usuário.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @return O ID do livro.
     */
    public long getBookId() {
        return bookId;
    }

    /**
     * @return A nota da avaliação.
     */
    public float getRating() {
        return rating;
    }
}
