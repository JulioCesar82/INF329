package dominio;

import java.util.Date;

/**
 * Uma implementação "fake" da Order para uso em testes.
 * <p>
 * Esta classe estende a {@link Order} real, adicionando comportamentos
 * específicos para facilitar a automação de testes.
 */
public class FakeOrder extends Order {

    public FakeOrder(int id, Customer customer, Date date, Cart cart,
                     String comment, ShipTypes shipType, Date shipDate, StatusTypes status,
                     Address billingAddress, Address shippingAddress, CCTransaction cc) {
        super(id, customer, date, cart, comment, shipType, shipDate, status, billingAddress, shippingAddress, cc);
    }

    /**
     * Define o status do pedido.
     *
     * @param status o novo status do pedido
     */
    public void setStatus(StatusTypes status) {
        this.status = status;
    }
}
