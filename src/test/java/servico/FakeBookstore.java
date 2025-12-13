package servico;

import static servico.Bookstore.booksById;

/**
 * Uma implementação "fake" da Bookstore para uso em testes.
 * <p>
 * Esta classe estende a {@link Bookstore} real, adicionando comportamentos
 * específicos para facilitar a automação de testes, como a capacidade de
 * limpar o histórico de pedidos.
 */
public class FakeBookstore extends Bookstore {

    public FakeBookstore(int id) {
        super(id);
    }

    /**
     * Limpa o histórico de pedidos da livraria para isolar cenários de teste.
     */
    public void clearOrders() {
        this.ordersById.clear();
        this.ordersByCreation.clear();
    }

    
    /**
     * Retorna o número total de livros no sistema.
     *
     * @return O número total de livros.
     */
    public static int getBookCount() {
        return booksById.size();
    }

}