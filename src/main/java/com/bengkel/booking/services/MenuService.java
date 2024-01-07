package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private static List<BookingOrder> bookingOrders = new ArrayList<>();
	private static Customer loggedInCustomer;
	 
	private static Scanner input = new Scanner(System.in);
	
	
	public static void run() {
		boolean isLooping = true;
		do {
			login();
			mainMenu();
		} while (isLooping);
		
	}
	
	public static void login() {
	    int attemptCount = 0;
	    final int maxAttempts = 3;

	    while (attemptCount < maxAttempts) {
	        System.out.println("=== Login ===");
	        System.out.println("Masukan Customer Id:");
	        String username = input.nextLine();
	        System.out.println("Masukan Password:");
	        String password = input.nextLine();

	        // Lakukan validasi login di sini
	        boolean isValidLogin = Validation.validateLogin(username, password, listAllCustomers);

	        if (isValidLogin) {
	            System.out.println("Login successful!");
	            loggedInCustomer = getCustomerById(username);
	            return;
	        } else {
	            if (!isCustomerIdValid(username)) {
	                System.out.println("Customer Id Tidak Ditemukan atau Salah!");
	            } else {
	                System.out.println("Password yang anda Masukan Salah!");
	            }
	            attemptCount++;
	            System.out.println("Sisa percobaan: " + (maxAttempts - attemptCount));
	        }
	    }

	    System.out.println("Anda telah melebihi batas percobaan login. Aplikasi akan berhenti.");
	    System.exit(0);
	}

	private static boolean isCustomerIdValid(String username) {
	    for (Customer customer : listAllCustomers) {
	        if (customer.getCustomerId().equals(username)) {
	            return true;
	        }
	    }
	    return false;
	}


	
	 private static Customer getCustomerById(String customerId) {
	        for (Customer customer : listAllCustomers) {
	            if (customer.getCustomerId().equals(customerId)) {
	                return customer;
	            }
	        }
	        return null;
	 }
	/////
	
	public static void mainMenu() {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		int menuChoice = 0;
		boolean isLooping = true;
		
		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu:", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0);
			System.out.println(menuChoice);
			
			switch (menuChoice) {
			case 1:
				PrintService.printCustomerProfile(loggedInCustomer);
				break;
			case 2:
				BengkelService.BookingBengkel(input, listAllItemService, loggedInCustomer, bookingOrders);
				break;
			case 3:
				BengkelService.topUpSaldoCoin(loggedInCustomer);
				break;
			case 4:
				PrintService.printBookingOrderHistory(bookingOrders);
				break;
			default:
				System.out.println("Logout");
				isLooping = false;
				break;
			}
		} while (isLooping);
		
		
	}
	
	//Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
