package dominio;

/*
 * Book.java - Class used to store all of the data associated with a single
 *             book. 
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

/**
 * *<img src="./doc-files/Book.png" alt="Book">
 * <br><a href="./doc-files/Book.html"> code </a>
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 6505830715531808617L;

    private final int id;
    private final String title;
    private Date pubDate;
    private final String publisher;
    private final SUBJECTS subject;
    private final String desc;
    private Book related1;
    private Book related2;
    private Book related3;
    private Book related4;
    private Book related5;
    private String thumbnail;
    private String image;
    private final double srp;
    private final Date avail;
    private final String isbn;
    private final int page;
    private final BACKINGS backing;
    private final int[] dimensions;
    private final double weight;
    private final Author author;

    /**
     *
     * @param id
     * @param title
     * @param pubDate
     * @param publisher
     * @param subject
     * @param desc
     * @param thumbnail
     * @param image
     * @param srp
     * @param avail
     * @param isbn
     * @param page
     * @param backing
     * @param dimensions
     * @param author
     */
    public Book(int id, String title, Date pubDate, String publisher,
            SUBJECTS subject, String desc, String thumbnail,
            String image, double srp, Date avail, String isbn,
            int page, BACKINGS backing, int[] dimensions,double weight, Author author
            ) {
        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.publisher = publisher;
        this.subject = subject;
        this.desc = desc;
        this.related1 = null;
        this.related2 = null;
        this.related3 = null;
        this.related4 = null;
        this.related5 = null;
        this.thumbnail = thumbnail;
        this.image = image;
        this.srp = srp;
        this.avail = avail;
        this.isbn = isbn;
        this.page = page;
        this.backing = backing;
        this.dimensions = dimensions;
        this.weight = weight;
        this.author = author;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     *
     * @param thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    /**
     *
     * @return
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return
     */
    public Author getAuthor() {
        return author;
    }

    /**
     *
     * @return
     */
    public double getSrp() {
        return srp;
    }


    /**
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     *
     * @return
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @return
     */
    public BACKINGS getBacking() {
        return backing;
    }

    /**
     *
     * @return
     */
    public Date getPubDate() {
        return pubDate;
    }

    /**
     *
     * @param pubDate
     */
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    /**
     *
     * @return
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     *
     * @return
     */
    public String getIsbn() {
        return isbn;
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
    public int[] getDimensions() {
        return dimensions;
    }

    /**
     * 
     * @return 
     */
    public double getWeight() {
        return weight;
    }
    
    

    /**
     *
     * @return
     */
    public SUBJECTS getSubject() {
        return subject;
    }

    /**
     *
     * @return
     */
    public Date getAvail() {
        return avail;
    }

    /**
     *
     * @return
     */
    public Book getRelated1() {
        return related1;
    }

    /**
     *
     * @return
     */
    public Book getRelated2() {
        return related2;
    }

    /**
     *
     * @return
     */
    public Book getRelated3() {
        return related3;
    }

    /**
     *
     * @return
     */
    public Book getRelated4() {
        return related4;
    }

    /**
     *
     * @return
     */
    public Book getRelated5() {
        return related5;
    }

    /**
     *
     * @param related1
     */
    public void setRelated1(Book related1) {
        this.related1 = related1;
    }

    /**
     *
     * @param related2
     */
    public void setRelated2(Book related2) {
        this.related2 = related2;
    }

    /**
     *
     * @param related3
     */
    public void setRelated3(Book related3) {
        this.related3 = related3;
    }

    /**
     *
     * @param related4
     */
    public void setRelated4(Book related4) {
        this.related4 = related4;
    }

    /**
     *
     * @param related5
     */
    public void setRelated5(Book related5) {
        this.related5 = related5;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Book) {
            Book book = (Book) o;
            return id == book.id;
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + '}';
    }
    
    

}
