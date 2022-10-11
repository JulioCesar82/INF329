package dominio;

/* 
 * Customer.java - stores the important information for a single customer. 
 *
 ************************************************************************
 *
 * This is part of the the Java TPC-W distribution,
 * written by Harold Cain, Tim Heil, Milo Martin, Eric Weglarz, and Todd
 * Bezenek.  University of Wisconsin - Madison, Computer Sciences
 * Dept. and Dept. of Electrical and Computer Engineering, as a part of
 * Prof. Mikko Lipasti's Fall 1999 ECE 902 course.
 *
 * Copyright (C) 1999, 2000 by Harold Cain, Timothy Heil, Milo Martin, 
 *                             Eric Weglarz, Todd Bezenek.
 * Copyright © 2008 Gustavo Maciel Dias Vieira
 *
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 * Everyone is granted permission to copy, modify and redistribute
 * this code under the following conditions:
 *
 * This code is distributed for non-commercial use only.
 * Please contact the maintainer for restrictions applying to 
 * commercial use of these tools.
 *
 * Permission is granted to anyone to make or distribute copies
 * of this code, either as received or modified, in any
 * medium, provided that all copyright notices, permission and
 * nonwarranty notices are preserved, and that the distributor
 * grants the recipient permission for further redistribution as
 * permitted by this document.
 *
 * Permission is granted to distribute this code in compiled
 * or executable form under the same conditions that apply for
 * source code, provided that either:
 *
 * A. it is accompanied by the corresponding machine-readable
 *    source code,
 * B. it is accompanied by a written offer, with no time limit,
 *    to give anyone a machine-readable copy of the corresponding
 *    source code in return for reimbursement of the cost of
 *    distribution.  This written offer must permit verbatim
 *    duplication by anyone, or
 * C. it is distributed by someone who received only the
 *    executable form, and is accompanied by a copy of the
 *    written offer of source code that they received concurrently.
 *
 * In other words, you are welcome to use, share and improve this codes.
 * You are forbidden to forbid anyone else to use, share and improve what
 * you give them.
 *
 ************************************************************************/
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * *<img src="./doc-files/Customer.png" alt="Customer">
 * <br><a href="./doc-files/Customer.html"> code </a>
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = -7297414189618511748L;

    private final int id;
    private final String uname;
    private final String passwd;
    private final String fname;
    private final String lname;
    private final String phone;
    private final String email;
    private final Date since;
    private final Date lastVisit;
    private Date login;
    private Date expiration;
    private final double discount;
    private final double balance;
    private final double ytdPmt;
    private final Date birthdate;
    private final String data;
    private final Address address;
    private Order mostRecentOrder;

    /**
     *
     * @param id
     * @param uname
     * @param passwd
     * @param fname
     * @param lname
     * @param phone
     * @param email
     * @param since
     * @param lastVisit
     * @param login
     * @param expiration
     * @param discount
     * @param balance
     * @param ytdPmt
     * @param birthdate
     * @param data
     * @param address
     */
    public Customer(int id, String uname, String passwd, String fname,
            String lname, String phone, String email, Date since,
            Date lastVisit, Date login, Date expiration, double discount,
            double balance, double ytdPmt, Date birthdate, String data,
            Address address) {
        this.id = id;
        this.uname = uname;
        this.passwd = passwd;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.since = since;
        this.lastVisit = lastVisit;
        this.login = login;
        this.expiration = expiration;
        this.discount = discount;
        this.balance = balance;
        this.ytdPmt = ytdPmt;
        this.birthdate = birthdate;
        this.data = data;
        this.address = address;
        mostRecentOrder = null;
    }

    /**
     *
     * @param id
     * @param uname
     * @param toLowerCase
     * @param fname
     * @param lname
     * @param phone
     * @param email
     * @param since
     * @param lastVisit
     */
    public Customer(int id, String uname, String toLowerCase, String fname, String lname, String phone, String email, Date since, Date lastVisit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param login
     */
    public void setLogin(Date login) {
        this.login = login;
    }

    /**
     *
     * @param expiration
     */
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
     *
     * @return
     */
    public String getFname() {
        return fname;
    }

    /**
     *
     * @return
     */
    public String getLname() {
        return lname;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     *
     * @return
     */
    public double getDiscount() {
        return discount;
    }

    /**
     *
     * @return
     */
    public Address getAddress() {
        return address;
    }

    /**
     *
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public String getUname() {
        return uname;
    }

    /**
     *
     * @param order
     */
    public void logOrder(Order order) {
        mostRecentOrder = order;
    }

    /**
     *
     * @return
     */
    public Order getMostRecentOrder() {
        return mostRecentOrder;
    }

    /**
     *
     * @return
     */
    public Date getSince() {
        return since;
    }

    /**
     *
     * @return
     */
    public Date getLastVisit() {
        return lastVisit;
    }

    /**
     *
     * @return
     */
    public Date getLogin() {
        return login;
    }

    /**
     *
     * @return
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     *
     * @return
     */
    public double getBalance() {
        return balance;
    }

    /**
     *
     * @return
     */
    public double getYtdPmt() {
        return ytdPmt;
    }

    /**
     *
     * @return
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     *
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.uname);
        hash = 29 * hash + Objects.hashCode(this.passwd);
        hash = 29 * hash + Objects.hashCode(this.fname);
        hash = 29 * hash + Objects.hashCode(this.lname);
        hash = 29 * hash + Objects.hashCode(this.phone);
        hash = 29 * hash + Objects.hashCode(this.email);
        hash = 29 * hash + Objects.hashCode(this.since);
        hash = 29 * hash + Objects.hashCode(this.lastVisit);
        hash = 29 * hash + Objects.hashCode(this.login);
        hash = 29 * hash + Objects.hashCode(this.expiration);
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.discount) ^ (Double.doubleToLongBits(this.discount) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.balance) ^ (Double.doubleToLongBits(this.balance) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.ytdPmt) ^ (Double.doubleToLongBits(this.ytdPmt) >>> 32));
        hash = 29 * hash + Objects.hashCode(this.birthdate);
        hash = 29 * hash + Objects.hashCode(this.data);
        hash = 29 * hash + Objects.hashCode(this.address);
        hash = 29 * hash + Objects.hashCode(this.mostRecentOrder);
        return hash;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Double.doubleToLongBits(this.discount) != Double.doubleToLongBits(other.discount)) {
            return false;
        }
        if (Double.doubleToLongBits(this.balance) != Double.doubleToLongBits(other.balance)) {
            return false;
        }
        if (Double.doubleToLongBits(this.ytdPmt) != Double.doubleToLongBits(other.ytdPmt)) {
            return false;
        }
        if (!Objects.equals(this.uname, other.uname)) {
            return false;
        }
        if (!Objects.equals(this.passwd, other.passwd)) {
            return false;
        }
        if (!Objects.equals(this.fname, other.fname)) {
            return false;
        }
        if (!Objects.equals(this.lname, other.lname)) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.since, other.since)) {
            return false;
        }
        if (!Objects.equals(this.lastVisit, other.lastVisit)) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.expiration, other.expiration)) {
            return false;
        }
        if (!Objects.equals(this.birthdate, other.birthdate)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.mostRecentOrder, other.mostRecentOrder)) {
            return false;
        }
        return true;
    }

   



}
