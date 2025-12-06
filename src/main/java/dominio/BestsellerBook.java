package dominio;

import dominio.Book;

/**
 * Data Transfer Object (DTO) para representar um livro na lista de bestsellers.
 * <p>
 * Esta classe encapsula um objeto {@link Book} e a contagem total de suas vendas,
 * permitindo que a camada de serviço retorne dados específicos para o caso de uso
 * de "Bestsellers" sem modificar a entidade de domínio principal.
 */
public class BestsellerBook {

    private final Book book;
    private final long salesCount;

    /**
     * Constrói um BestsellerBook.
     *
     * @param book       O objeto do livro.
     * @param salesCount A contagem total de unidades vendidas.
     */
    public BestsellerBook(Book book, long salesCount) {
        this.book = book;
        this.salesCount = salesCount;
    }

    /**
     * @return O objeto do livro.
     */
    public Book getBook() {
        return book;
    }

    /**
     * @return A contagem de vendas.
     */
    public long getSalesCount() {
        return salesCount;
    }
}