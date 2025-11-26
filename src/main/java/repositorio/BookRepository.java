package repositorio;

import dominio.Book;
import dominio.SUBJECTS;

import java.util.List;
import java.util.Optional;

/**
 * Interface de Repositório para a entidade {@link Book}.
 * <p>
 * Define o contrato para as operações de persistência de dados para livros,
 * abstraindo a lógica de negócio da implementação da fonte de dados (em memória,
 * banco de dados, etc.).
 * <p>
 * TODO: A classe {@code servico.Bookstore} deve ser refatorada no futuro para
 * implementar esta interface, centralizando o acesso aos dados de livros.
 */
public interface BookRepository {

    /**
     * Encontra um livro pelo seu ID.
     *
     * @param bookId O ID do livro.
     * @return Um {@link Optional} contendo o livro se encontrado, ou vazio caso
     * contrário.
     */
    Optional<Book> findById(long bookId);

    /**
     * Busca os 50 livros mais vendidos para um determinado assunto.
     * <p>
     * TODO: Esta é a assinatura ideal para a US1. A implementação deve conter a
     * lógica de contagem e ordenação.
     *
     * @param limit O número de bestsellers a retornar.
     * @return Uma lista de livros ordenada pela popularidade.
     */
    List<Book> findBestSellers(int limit);

    /**
     * Busca livros por assunto, ordenados pelo título.
     *
     * @param subject O assunto a ser buscado.
     * @return Uma lista de até 50 livros.
     */
    List<Book> findBySubject(SUBJECTS subject);

    /**
     * Busca livros que começam com um determinado título, ordenados pelo título.
     *
     * @param title O início do título a ser buscado.
     * @return Uma lista de até 50 livros.
     */
    List<Book> findByTitleStartsWith(String title);

    /**
     * Busca os lançamentos mais recentes de um determinado assunto.
     *
     * @param subject O assunto para filtrar os novos lançamentos.
     * @return Uma lista de até 50 livros, ordenada pela data de publicação.
     */
    List<Book> findNewReleasesBySubject(SUBJECTS subject);

}
