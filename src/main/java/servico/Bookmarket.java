package servico;

import dominio.ShipTypes;
import dominio.Book;
import dominio.Address;
import dominio.BestsellerBook;
import dominio.Cart;
import dominio.CreditCards;
import dominio.Customer;
import dominio.Order;
import dominio.OrderLine;
import dominio.SUBJECTS;
import dominio.Stock;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import util.TPCW_Util;

/**
 * Fachada de serviço principal para o e-commerce BookMarket.
 * <p>
 * Esta classe expõe todas as operações de negócio e implementa a lógica
 * requerida pelas User Stories, orquestrando a obtenção de dados da classe
 * {@link Bookstore} que atua como um repositório em memória.
 * <p>
 * Os métodos aqui presentes devem ser implementados para cumprir os requisitos
 * de negócio, como a busca por bestsellers e a geração de recomendações
 * personalizadas.
 * <p>
 * <img src="./doc-files/Bookstore.png" alt="Bookmarket">
 * <br><a href="./doc-files/Bookmarket.html"> code </a>
 *
 */
public class Bookmarket {

    private interface Action<STATE> {

        Object executeOn(STATE sm);
    }

    static class StateMachine {

        private final List<Bookstore> state;

        public StateMachine(final List object) {
            this.state = object;
        }

        Object execute(Action action) {
            return action.executeOn(getStateStream());
        }

        void checkpoint() {

        }
        
        List<Bookstore> getState() {
            return state;
        }

        Stream getStateStream() {
            return state.stream();
        }

        static StateMachine create(Bookstore... state) {
            List list = new ArrayList();
            list.addAll(Arrays.asList(state));
            return new StateMachine(list);
        }

    }

    private class UmbrellaException extends RuntimeException {

    }
    private static Random random;
    private static StateMachine stateMachine;

    static StateMachine getStateMachine() {
        return stateMachine;
    }
    
    

    /**
     *
     * @param state
     */
    public static void init(int seed, Bookstore... state) {
        random = new Random(seed);
        try {
            stateMachine = StateMachine.create(state);
        } catch (UmbrellaException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Bookstore> getBookstoreStream() {
        return (Stream) stateMachine.getStateStream();
    }

    /**
     *
     * @param UNAME
     * @return
     */
    public static Customer getCustomer(String UNAME) {
        return Bookstore.getCustomer(UNAME).get();
    }

    /**
     *
     * @param c_id
     * @return
     */
    public static String[] getName(int c_id) {

        Customer customer = Bookstore.getCustomer(c_id);

        String name[] = new String[3];
        name[0] = customer.getFname();
        name[1] = customer.getLname();
        name[2] = customer.getUname();
        return name;
    }

    /**
     *
     * @param C_ID
     * @return
     */
    public static String getUserName(int C_ID) {
        return Bookstore.getCustomer(C_ID).getUname();
    }

    /**
     *
     * @param C_UNAME
     * @return
     */
    public static String getPassword(String C_UNAME) {
        return Bookstore.getCustomer(C_UNAME).get().getPasswd();

    }

    /**
     *
     * @param c_uname
     * @return
     */
    public static Order getMostRecentOrder(String c_uname) {
        return Bookstore.getCustomer(c_uname).get().getMostRecentOrder();
    }

    /**
     *
     * @param fname
     * @param lname
     * @param street1
     * @param street2
     * @param city
     * @param state
     * @param zip
     * @param countryName
     * @param phone
     * @param email
     * @param birthdate
     * @param data
     * @return
     */
    public static Customer createNewCustomer(String fname, String lname,
            String street1, String street2, String city, String state,
            String zip, String countryName, String phone, String email,
            Date birthdate, String data) {
        double discount = (int) (Math.random() * 51);
        long now = System.currentTimeMillis();
        try {
            return (Customer) stateMachine.execute(new CreateCustomerAction(
                    fname, lname, street1, street2, city, state, zip,
                    countryName, phone, email, discount, birthdate, data, now));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param cId
     */
    public static void refreshSession(int cId) {
        try {
            stateMachine.execute(new RefreshCustomerSessionAction(cId,
                    System.currentTimeMillis()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param i_id
     * @return
     */
    public static Book getBook(int i_id) {

        return Bookstore.getBook(i_id).get();

    }

    /**
     *
     * @return
     */
    public static Book getABookAnyBook() {
        return Bookstore.getABookAnyBook(random);

    }

    /**
     *
     * @param search_key
     * @return
     */
    public static List<Book> doSubjectSearch(SUBJECTS search_key) {
        return Bookstore.getBooksBySubject(search_key);
    }

    /**
     *
     * @param search_key
     * @return
     */
    public static List<Book> doTitleSearch(String search_key) {
        return Bookstore.getBooksByTitle(search_key);
    }

    /**
     *
     * @param search_key
     * @return
     */
    public static List<Book> doAuthorSearch(String search_key) {
        return Bookstore.getBooksByAuthor(search_key);
    }

    /**
     *
     * @param subject
     * @return
     */
    public static List<Book> getNewProducts(SUBJECTS subject) {
        return Bookstore.getNewBooks(subject);
    }

    /**
     *
     * @param book
     * @return
     */
    public static List<Double> getCosts(Book book) {
        return getBookstoreStream().
                map(store -> store.getStock(book.getId())).
                map(stock -> stock.getCost()).
                collect(Collectors.toList());
    }

    /**
     * **US1: Listagem dos Bestsellers.**
     * <p>
     * Calcula e retorna os livros mais vendidos (bestsellers) com base na
     * contagem total de unidades vendidas em todos os pedidos históricos.
     * A lógica deve somar a quantidade (`qty`) de cada livro presente em
     * todas as {@link OrderLine}s.
     *
     * @param limit O número de bestsellers a serem retornados (deve estar entre 1 e 100).
     * @return Uma lista de {@link BestsellerBook} ordenada pela contagem de vendas.
     * @throws IllegalArgumentException se o limite for inválido.
     */
    public static List<BestsellerBook> getBestsellers(int limit) {
        // Regra de Negócio (US1 - No1, No2): Validar o limite N.
        if (limit <= 0 || limit > 100) {
            throw new IllegalArgumentException("O limite (N) deve ser um valor entre 1 e 100.");
        }

        // 1. Agrega a quantidade de vendas por livro.
        Map<Book, Long> salesByBook = getBookstoreStream()
            .flatMap(bookstore -> ((Bookstore) bookstore).getOrdersByCreation().stream())
            .flatMap(order -> ((Order) order).getLines().stream())
            .collect(Collectors.groupingBy(
                    OrderLine::getBook,
                    Collectors.summingLong(OrderLine::getQty)
            ));

        // 2. Cria os DTOs, ordena e retorna a lista final.
        return salesByBook.entrySet().stream()
            .map(entry -> new BestsellerBook(entry.getKey(), entry.getValue()))
            .sorted(
                // Regra de Negócio (US1): Ordenação decrescente por vendas.
                Comparator.comparingLong(BestsellerBook::getSalesCount).reversed()
                    // Regra de Negócio (US1 - P04): Empate, ordenação crescente por título.
                    .thenComparing(b -> b.getBook().getTitle())
            )
            // Regra de Negócio (US1): Retornar a quantidade N solicitada.
            .limit(limit)
            .collect(Collectors.toList());
    }

    /**
     *
     * @param c_id
     * @return
     *
     * @deprecated A recomendação baseada em itens não faz parte dos requisitos
     * definidos no `board.pdf`. A lógica de negócio está focada em
     * {@link #getPriceBookRecommendationByUsers(int)}. Este método é obsoleto.
     *
     * @param c_id O ID do cliente.
     * @return null.
     */
    @Deprecated
    public static List<Book> getRecommendationByItens(int c_id) {
        return Bookstore.getRecommendationByItens(c_id);
    }

    /**
     *
     * @param c_id
     * @return
     *
     * @deprecated A lógica de recomendação foi consolidada em
     * {@link #getPriceBookRecommendationByUsers(int)}. Este método é um
     * pass-through obsoleto e será removido em futuras versões.
     *
     * @param c_id O ID do cliente.
     * @return null.
     */
    @Deprecated
    public static List<Book> getRecommendationByUsers(int c_id) {
        return Bookstore.getRecommendationByUsers(c_id);
    }

    /**
     * Retornar os 5 livros recomendados e para cada livro o conjunto de
     * estoques (Stock) ordenados de forma crescente pelo preço do livro.
     *
     * @param c_id
     * @return
     * 
     * @deprecated A lógica de recomendação foi consolidada em
     * {@link #getPriceBookRecommendationByUsers(int)}, que retorna o preço
     * calculado em vez do objeto de estoque. Utilize o outro método para
     * implementar a lógica das US3 e US4.
     *
     * @param c_id O ID do cliente.
     * @return Um mapa de livros para seus estoques.
     */
    @Deprecated
    public static Map<Book, Set<Stock>> getStocksRecommendationByUsers(int c_id) {
        // TODO: A lógica principal de recomendação está em getPriceBookRecommendationByUsers.
        // Este método não é mais necessário para os requisitos atuais.
        return null;
    }

    /**
     * Se usuário for assinante retornar os 5 livros recomendados e para cada
     * livro o seu menor preço. Se usuário não for assinante retorno os 5 livros
     * recomendados e a média do preço.
     *
     * @param c_id
     * @return
     * 
     * **US3 & US4: Sugestão de Livros para Clientes Regulares e Assinantes.**
     * <p>
     * Gera uma lista de 5 livros recomendados para um determinado cliente e calcula
     * o preço a ser exibido com base no seu perfil (Regular vs. Assinante).
     * <p>
     * A lógica de negócio se divide em dois casos:
     * <ul>
     * <li><b>US3 - Cliente Regular:</b> Para clientes com `discount == 0`, o preço
     * exibido para cada livro recomendado deve ser a **média de seu preço de venda
     * histórico**, calculado a partir de todas as {@link OrderLine}s.</li>
     * <li><b>US4 - Assinante:</b> Para clientes com `discount > 0`, o preço
     * exibido deve ser o **menor valor promocional disponível** para o livro
     * naquele momento, obtido do {@link Stock}.</li>
     * </ul>
     *
     * @param c_id O ID do cliente para o qual a recomendação será gerada.
     * @return Um {@link Map} onde cada {@link Book} recomendado mapeia para o seu
     * preço calculado ({@code Double}).
     */
    public static Map<Book, Double> getPriceBookRecommendationByUsers(int c_id) {
        // TODO: Implementar a lógica das US3 e US4.
        // 1. Obter o cliente (Customer) usando `Bookstore.getCustomer(c_id)`.
        //
        // 2. Gerar a lista de livros recomendados (ex: 5 livros).
        //    - A lógica de recomendação (ex: usando Mahout ou outra estratégia) deve ser implementada aqui.
        //    - Para este guia, vamos assumir que uma `List<Book> recommendedBooks` foi gerada.
        //
        // 3. Verificar o tipo de cliente.
        //    boolean isSubscriber = customer.getDiscount() > 0;
        //
        // 4. Criar o mapa de resultado: `Map<Book, Double> result = new HashMap<>();`
        //
        // 5. Iterar sobre cada `book` em `recommendedBooks`.
        //    - IF (isSubscriber) -> Lógica da US4:
        //        a. Obter o `Stock` do livro a partir de `Bookstore.stockByBook`.
        //        b. Obter o preço promocional (`stock.getCost()`).
        //        c. Adicionar ao mapa: `result.put(book, stock.getCost());`
        //
        //    - ELSE -> Lógica da US3:
        //        a. Calcular o preço médio histórico do livro. Para isso, é preciso:
        //        b. Iterar por todas as `Order` em `Bookstore.ordersByCreation`.
        //        c. Dentro de cada ordem, iterar pelas `OrderLine`s.
        //        d. Se a `orderLine.getBook().getId()` for igual ao `book.getId()`,
        //           somar o `orderLine.getPrice()` e contar a `orderLine.getQty()`.
        //        e. Calcular a média: `totalPrice / totalQty`.
        //        f. Adicionar ao mapa: `result.put(book, averagePrice);`
        //
        // 6. Retornar o `result`.
        return null;
    }

    /**
     *
     * @param idBook
     * @return
     */
    public static List<Stock> getStocks(final int idBook) {
        return getBookstoreStream()
                .filter(store -> store.getStock(idBook) != null)
                // transforma o stream de bookstore em stream de stock
                .map(store -> store.getStock(idBook))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param idBookstore
     * @param idBook
     * @return
     */
    public static Stock getStock(final int idBookstore, final int idBook) {
        return getBookstoreStream()
                .filter(store -> store.getId() != idBookstore)
                // transforma o stream de bookstore em stream de stock
                .map(store -> store.getStock(idBook))
                .findFirst()
                .get();
    }

    /**
     *
     * @param i_id
     * @return
     */
    public static List<Book> getRelated(int i_id) {
        Book book = Bookstore.getBook(i_id).get();
        ArrayList<Book> related = new ArrayList<>(5);
        related.add(book.getRelated1());
        related.add(book.getRelated2());
        related.add(book.getRelated3());
        related.add(book.getRelated4());
        related.add(book.getRelated5());
        return related;
    }

    /**
     *
     * @param iId
     * @param cost
     * @param image
     * @param thumbnail
     */
    public static void adminUpdate(int iId, double cost, String image,
            String thumbnail) {
        try {
            stateMachine.execute(new UpdateBookAction(iId, cost, image,
                    thumbnail, System.currentTimeMillis()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param storeId
     * @return
     */
    public static int createEmptyCart(int storeId) {
        try {
            return ((Cart) stateMachine.execute(new CreateCartAction(storeId,
                    System.currentTimeMillis()))).getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param storeId
     * @param SHOPPING_ID
     * @param I_ID
     * @param ids
     * @param quantities
     * @return
     */
    public static Cart doCart(int storeId, int SHOPPING_ID, Integer I_ID, List<Integer> ids,
            List<Integer> quantities) {
        try {
            Cart cart = (Cart) stateMachine.execute(new CartUpdateAction(storeId,
                    SHOPPING_ID, I_ID, ids, quantities,
                    System.currentTimeMillis()));
            if (cart.getLines().isEmpty()) {
                Book book = Bookstore.getABookAnyBook(random);
                cart = (Cart) stateMachine.execute(new CartUpdateAction(storeId,
                        SHOPPING_ID, book.getId(), new ArrayList<>(),
                        new ArrayList<>(), System.currentTimeMillis()));
            }
            return cart;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param SHOPPING_ID
     * @param storeId
     * @return
     */
    public static Cart getCart(int storeId, int SHOPPING_ID) {
        Bookstore bookstore = getBookstoreStream()
                .filter(store -> store.getId() == storeId)
                .findFirst()
                .get();
        synchronized (bookstore) {
            return bookstore.getCart(SHOPPING_ID);
        }
    }

    /**
     *
     * @param storeId
     * @param shopping_id
     * @param customer_id
     * @param cc_type
     * @param cc_number
     * @param cc_name
     * @param cc_expiry
     * @param shipping
     * @return
     */
    public static Order doBuyConfirm(int storeId, int shopping_id, int customer_id,
            CreditCards cc_type, long cc_number, String cc_name, Date cc_expiry,
            ShipTypes shipping) {
        long now = System.currentTimeMillis();
        try {
            return (Order) stateMachine.execute(new ConfirmBuyAction(storeId,
                    customer_id, shopping_id, randomComment(),
                    cc_type, cc_number, cc_name, cc_expiry, shipping,
                    randomShippingDate(now), -1, now));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param storeId
     * @param shopping_id
     * @param customer_id
     * @param cc_type
     * @param cc_number
     * @param cc_name
     * @param cc_expiry
     * @param shipping
     * @param street_1
     * @param street_2
     * @param city
     * @param state
     * @param zip
     * @param country
     * @return
     */
    public static Order doBuyConfirm(int storeId, int shopping_id, int customer_id,
            CreditCards cc_type, long cc_number, String cc_name, Date cc_expiry,
            ShipTypes shipping, String street_1, String street_2, String city,
            String state, String zip, String country) {
        Address address = Bookstore.alwaysGetAddress(street_1, street_2,
                city, state, zip, country);
        long now = System.currentTimeMillis();
        try {
            return (Order) stateMachine.execute(new ConfirmBuyAction(storeId,
                    customer_id, shopping_id, randomComment(),
                    cc_type, cc_number, cc_name, cc_expiry, shipping,
                    randomShippingDate(now), address.getId(), now));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String randomComment() {
        return TPCW_Util.getRandomString(random, 20, 100);
    }

    private static Date randomShippingDate(long now) {
        return new Date(now + 86400000 /* a day */ * (random.nextInt(7) + 1));
    }

    /**
     *
     * @param items
     * @param customers
     * @param addresses
     * @param authors
     * @param orders
     * @return
     */
    public static boolean populate(int items, int customers, int addresses,
            int authors, int orders) {
        try {
            return (Boolean) stateMachine.execute(new PopulateAction(random.nextLong(),
                    System.currentTimeMillis(), items, customers, addresses,
                    authors, orders));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    public static void checkpoint() {
        try {
            stateMachine.checkpoint();
        } catch (UmbrellaException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    protected static abstract class BookstoreAction implements Action<Stream<Bookstore>>,
            Serializable {

        /**
         *
         * @param sm
         * @return
         */
        @Override
        public Object executeOn(Stream<Bookstore> sm) {
            return executeOnBookstore(sm);
        }

        /**
         *
         * @param bookstore
         * @return
         */
        public abstract Object executeOnBookstore(Stream<Bookstore> bookstore);
    }

    /**
     *
     */
    protected static class CreateCustomerAction extends BookstoreAction {

        private static final long serialVersionUID = 6039962163348790677L;

        String fname;
        String lname;
        String street1;
        String street2;
        String city;
        String state;
        String zip;
        String countryName;
        String phone;
        String email;
        double discount;
        Date birthdate;
        String data;
        long now;

        /**
         *
         * @param fname
         * @param lname
         * @param street1
         * @param street2
         * @param city
         * @param state
         * @param zip
         * @param countryName
         * @param phone
         * @param email
         * @param discount
         * @param birthdate
         * @param data
         * @param now
         */
        public CreateCustomerAction(String fname, String lname, String street1,
                String street2, String city, String state, String zip,
                String countryName, String phone, String email,
                double discount, Date birthdate, String data, long now) {
            this.fname = fname;
            this.lname = lname;
            this.street1 = street1;
            this.street2 = street2;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.countryName = countryName;
            this.phone = phone;
            this.email = email;
            this.discount = discount;
            this.birthdate = birthdate;
            this.data = data;
            this.now = now;
        }

        /**
         *
         * @param bookstore
         * @return
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            return Bookstore.createCustomer(fname, lname, street1, street2,
                    city, state, zip, countryName, phone, email, discount,
                    birthdate, data, now);
        }
    }

    /**
     *
     */
    protected static class RefreshCustomerSessionAction extends BookstoreAction {

        private static final long serialVersionUID = -5391031909452321478L;

        int cId;
        long now;

        /**
         *
         * @param id
         * @param now
         */
        public RefreshCustomerSessionAction(int id, long now) {
            cId = id;
            this.now = now;
        }

        /**
         *
         * @param bookstore
         * @return
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            Bookstore.refreshCustomerSession(cId, now);
            return null;
        }
    }

    /**
     *
     */
    protected static class UpdateBookAction extends BookstoreAction {

        private static final long serialVersionUID = -745697943594643776L;

        int bId;
        double cost;
        String image;
        String thumbnail;
        long now;

        /**
         *
         * @param id
         * @param cost
         * @param image
         * @param thumbnail
         * @param now
         */
        public UpdateBookAction(int id, double cost, String image,
                String thumbnail, long now) {
            bId = id;
            this.cost = cost;
            this.image = image;
            this.thumbnail = thumbnail;
            this.now = now;
        }

        /**
         *
         * @param bookstore
         * @return
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            Bookstore.updateBook(bId, image, thumbnail, now);
            return null;
        }
    }

    /**
     *
     */
    protected static class CreateCartAction extends BookstoreAction {

        private static final long serialVersionUID = 8255648428785854052L;

        long now, storeId;

        /**
         *
         * @param idBookstore
         * @param now
         */
        public CreateCartAction(int idBookstore, long now) {
            this.now = now;
            this.storeId = idBookstore;
        }

        /**
         *
         * @param bookstore
         * @return
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            return bookstore.filter(bs -> bs.getId() == this.storeId).findFirst().get().createCart(now);
        }
    }

    /**
     *
     */
    protected static class CartUpdateAction extends BookstoreAction {

        private static final long serialVersionUID = -6062032194650262105L;

        final int cId, storeId;
        final Integer bId;
        final List<Integer> bIds;
        final List<Integer> quantities;
        final long now;

        /**
         *
         * @param storeId
         * @param id
         * @param id2
         * @param ids
         * @param quantities
         * @param now
         */
        public CartUpdateAction(int storeId, int id, Integer id2, List<Integer> ids,
                List<Integer> quantities, long now) {
            this.storeId = storeId;
            cId = id;
            bId = id2;
            bIds = ids;
            this.quantities = quantities;
            this.now = now;
        }

        /**
         *
         * @param bookstore
         * @return
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            return bookstore.filter(bs -> bs.getId() == this.storeId).findFirst().get().cartUpdate(cId, bId, bIds, quantities, now);
        }
    }

    /**
     *
     */
    protected static class ConfirmBuyAction extends BookstoreAction {

        private static final long serialVersionUID = -6180290851118139002L;

        final int customerId, storeId, cartId;
        String comment;
        CreditCards ccType;
        long ccNumber;
        String ccName;
        Date ccExpiry;
        ShipTypes shipping;
        Date shippingDate;
        int addressId;
        long now;

        /**
         *
         * @param storeId
         * @param customerId
         * @param cartId
         * @param comment
         * @param ccType
         * @param ccNumber
         * @param ccName
         * @param ccExpiry
         * @param shipping
         * @param shippingDate
         * @param addressId
         * @param now
         */
        public ConfirmBuyAction(int storeId, int customerId, int cartId,
                String comment, CreditCards ccType, long ccNumber,
                String ccName, Date ccExpiry, ShipTypes shipping,
                Date shippingDate, int addressId, long now) {
            this.storeId = storeId;
            this.customerId = customerId;
            this.cartId = cartId;
            this.comment = comment;
            this.ccType = ccType;
            this.ccNumber = ccNumber;
            this.ccName = ccName;
            this.ccExpiry = ccExpiry;
            this.shipping = shipping;
            this.shippingDate = shippingDate;
            this.addressId = addressId;
            this.now = now;
        }

        /**
         *
         * @param bookstore
         * @return
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            return bookstore.filter(bs -> bs.getId() == this.storeId).findFirst().get().confirmBuy(customerId, cartId, comment, ccType,
                    ccNumber, ccName, ccExpiry, shipping, shippingDate,
                    addressId, now);
        }
    }

    /**
     *
     */
    protected static class PopulateAction extends BookstoreAction {

        private static final long serialVersionUID = -5240430799502573886L;

        long seed;
        long now;
        int items;
        int customers;
        int addresses;
        int authors;
        int orders;

        /**
         *
         * @param seed
         * @param now
         * @param items
         * @param customers
         * @param addresses
         * @param authors
         * @param orders
         */
        public PopulateAction(long seed, long now, int items, int customers,
                int addresses, int authors, int orders) {
            this.seed = seed;
            this.now = now;
            this.items = items;
            this.customers = customers;
            this.addresses = addresses;
            this.authors = authors;
            this.orders = orders;
        }

        /**
         *
         * @param bookstore
         * @return
         */
        @Override
        public Object executeOnBookstore(Stream<Bookstore> bookstore) {
            Bookstore.populate(seed, now, items, customers, addresses, authors);
            Random rand = new Random(seed);
            bookstore.forEach(instance -> instance.populateInstanceBookstore(orders, rand, now));
            return true;
        }
    }

}
