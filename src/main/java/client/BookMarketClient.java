package client;

import servico.Bookmarket;
import servico.Bookstore;

import dominio.Book;

import java.util.List;
import java.util.ArrayList;
import java.lang.System;

public class BookMarketClient {

	public static void main(String[] args) {
		
		ArrayList<Book> books = new ArrayList<Book>();
		
		Bookmarket fleaMarket = new Bookmarket();
		
		fleaMarket.populate(10000, 10000, 10000, 1000, 10000);
		for (int i = 0; i < 100; i++) {
			books.add(i,fleaMarket.getBook(i));
			System.out.println(books.get(i).toString() + " " + books.get(i).getAuthor().toString());
		}
		


	}

}
