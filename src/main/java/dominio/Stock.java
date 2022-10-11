package dominio;

/**
 * <img src="./doc-files/Stock.png" alt="Bookmarket">
 * <br><a href="./doc-files/Stock.html"> code </a>
 *
 */
public class Stock {
    
    
    private final int idBookstore;
    private final Address address;
    private final Book book;
    private double cost;
    private int qty;

    /**
     *
     * @param idBookstore
     * @param address
     * @param book
     * @param cost
     * @param qty
     */
    public Stock(int idBookstore, Address address, Book book, double cost, int qty) {
        
        this.idBookstore = idBookstore;
        this.address = address;
        this.book = book;
        this.cost = cost;
        this.qty = qty;
    }
    
    


    /**
     *
     * @return
     */
    public Book getBook() {
        return book;
    }
    
    /**
     *
     * @param amount
     */
    public void addQty(int amount) {
        qty += amount;
    }

    /**
     *
     * @return
     */
    public double getCost() {
        return cost;
    }

    /**
     *
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     *
     * @return
     */
    public int getQty() {
        return qty;
    }

    /**
     *
     * @param qty
     */
    public void setQty(int qty) {
        this.qty = qty;
    }

    /**
     *
     * @return
     */
    public int getIdBookstore() {
        return idBookstore;
    }

    public Address getAddress() {
        return address;
    }
    
    

    @Override
    public String toString() {
        return "Stock{" + "idBookstore=" + idBookstore + ", book=" + book + ", cost=" + cost + ", qty=" + qty + '}';
    }
    
 
    
}
