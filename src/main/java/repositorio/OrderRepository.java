package repositorio;

import dominio.Order;
import java.util.List;
import java.util.Optional;

/**
 * Interface de Repositório para a entidade {@link Order}.
 * <p>
 * Define o contrato para as operações de persistência de dados para pedidos.
 * Essencial para funcionalidades que dependem do histórico de vendas, como o
 * cálculo do preço médio para a US3.
 * <p>
 * TODO: A classe {@code servico.Bookstore} deve ser refatorada no futuro para
 * implementar esta interface.
 */
public interface OrderRepository {

    /**
     * Encontra um pedido pelo seu ID.
     *
     * @param orderId O ID do pedido.
     * @return Um {@link Optional} contendo o pedido se encontrado, ou vazio caso
     * contrário.
     */
    Optional<Order> findById(long orderId);

    /**
     * Retorna todos os pedidos registrados no sistema.
     * <p>
     * Este método é fundamental para a US3, pois permite o acesso ao histórico
     * completo de vendas para o cálculo do preço médio de cada livro.
     *
     * @return Uma lista de todos os pedidos, preferencialmente ordenada pela
     * data de criação decrescente.
     */
    List<Order> findAll();

    /**
     * Salva um novo pedido no repositório.
     *
     * @param order O pedido a ser salvo.
     * @return O pedido salvo (pode incluir um ID gerado).
     */
    Order save(Order order);
}
