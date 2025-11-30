package servico;

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
}