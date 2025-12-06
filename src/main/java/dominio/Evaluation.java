package dominio;

import java.io.Serializable;

/**
 * **Decisão Arquitetural: Objeto de Transferência de Dados (DTO) para Integração.**
 * <p>
 * Esta classe atua como um Data Transfer Object (DTO) e faz parte da camada
 * de "Anti-Corrupção" (Anti-Corruption Layer) do nosso sistema. Seu objetivo
 * é desacoplar nosso modelo de domínio (`Rating`) das dependências de
 * bibliotecas externas, como o Apache Mahout.
 * <p>
 * Enquanto a classe {@link Rating} é rica e utiliza objetos do nosso domínio
 * (`Customer`, `Book`), a classe `Evaluation` usa tipos primitivos (`long`, `float`),
 * que são o formato esperado pelo `DataModel` do Mahout.
 * <p>
 * **Uso Futuro:** Quando a funcionalidade de recomendação for implementada,
 * um "Mapper" será criado para converter objetos `Rating` do nosso domínio
 * em objetos `Evaluation`, que serão então usados para alimentar o motor de
 * recomendação.
 * <p>
 * Manter esta classe, mesmo que não utilizada no momento, é uma decisão
 * consciente para preservar a intenção de design para a futura integração.
 * Isso segue o princípio de ter modelos distintos para diferentes Bounded
 * Contexts (nosso domínio vs. o domínio da biblioteca de recomendação).
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
