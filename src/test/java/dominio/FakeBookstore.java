package servico;


import dominio.Address;
import dominio.CCTransaction;
import dominio.Cart;
import dominio.Customer;
import dominio.FakeOrder;
import dominio.Order;
import dominio.ShipTypes;
import dominio.StatusTypes;
import java.util.Date;

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

    @Override
    protected Order newOrderInstance(int idOrder, Customer customer, Date date, Cart cart,
            String comment, ShipTypes shipType, Date shipDate,
            StatusTypes status, Address billingAddress, Address shippingAddress,
            CCTransaction cc) {
        return new FakeOrder(idOrder, customer, date, cart, comment, shipType,
                shipDate, status, billingAddress, shippingAddress, cc);
    }    
}
