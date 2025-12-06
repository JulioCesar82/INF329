package dominio;

import java.util.Objects;

/**
 * Representa a avaliação (rating) de um livro por um cliente.
 * <p>
 * Esta classe é o objeto de domínio central para a User Story US2, que trata
 * do registro de preferência de um cliente por um livro. Ela armazena a
 * relação entre um cliente, um livro e a nota que ele atribuiu.
 *
 * **Decisão Arquitetural: Objeto de Domínio Rico.**
 * <p>
 * Esta é a classe de domínio principal que representa o conceito de "Avaliação"
 * dentro do Bounded Context do BookMarket. Ela é um objeto rico, pois utiliza
 * outras entidades do domínio (`Customer`, `Book`) em sua composição,
 * refletindo as regras e o vocabulário do negócio.
 * <p>
 * Toda a lógica de negócio interna ao sistema que manipula avaliações deve
 * utilizar esta classe.
 * <p>
 * Para integração com sistemas externos (como o Mahout), a classe
 * {@link Evaluation} atua como um DTO, e um Mapper fará a tradução entre
 * `Rating` e `Evaluation`.
 */
public class Rating {
    private final Customer customer;
    private final Book book;
    private int rating;

    public Rating(Customer customer, Book book, int rating) {
        this.customer = customer;
        this.book = book;
        this.rating = rating;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Book getBook() {
        return book;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating1 = (Rating) o;
        return customer.equals(rating1.customer) && book.equals(rating1.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, book);
    }
}