package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class BengkelService {
	
	public static void BookingBengkel(Scanner scanner, List<ItemService> listAllItemService, Customer loggedInCustomer, List<BookingOrder> bookingOrders) {
	    System.out.println("Masukan Vehicle Id:");
	    String vehicleId = scanner.nextLine();
	    if (!isVehicleFound(loggedInCustomer, vehicleId)) {
	        System.out.println("Notifikasi Pesan bahwa Kendaraan Tidak ditemukan.");
	        return;
	    }
	    String vehicleType = getVehicleType(loggedInCustomer, vehicleId);
	    List<ItemService> selectedServices = selectServices(scanner, listAllItemService, vehicleType, loggedInCustomer);
	    String paymentMethod = selectPaymentMethod(scanner, loggedInCustomer);
	    createBookingOrder(scanner, bookingOrders, loggedInCustomer, selectedServices, paymentMethod);
	}
	

	private static List<ItemService> selectServices(Scanner scanner, List<ItemService> listAllItemService, String vehicleType, Customer loggedInCustomer) {
	    List<ItemService> availableServices = listAllItemService.stream()
	            .filter(service -> service.getVehicleType().equals(vehicleType))
	            .collect(Collectors.toList());

	    PrintService.printService(vehicleType, listAllItemService);
	    List<ItemService> selectedServices = new ArrayList<>();
	    int maxServices = (loggedInCustomer instanceof MemberCustomer)? 2 : 1;

	    while (selectedServices.size() < maxServices) {
	        System.out.println("Enter Service Id (or 0 to finish selecting services):");
	        String serviceId = scanner.nextLine();

	        if (serviceId.equals("0")) {
	            break;
	        }

	        ItemService selectedService = findServiceById(serviceId, availableServices);

	        if (selectedService != null) {
	            selectedServices.add(selectedService);
	            System.out.println("Service added: " + selectedService.getServiceId());
	        } else {
	            System.out.println("Service with ID " + serviceId + " not found.");
	        }
	    }

	    return selectedServices;
	}



	private static String selectPaymentMethod(Scanner scanner, Customer loggedInCustomer) {
	    if (loggedInCustomer instanceof MemberCustomer) {
	        System.out.println("Silahkan Pilih Metode Pembayaran (Saldo Coin atau Cash) : ");
	        String method = scanner.nextLine().toLowerCase();
	        if (!method.equals("saldo coin") && !method.equals("cash")) {
	            System.out.println("Metode pembayaran tidak valid.");
	            return null;
	        }
	        return method;
	    } else {
	        System.out.println("Metode Pembayaran menggunakan Cash");
	        return "cash";
	    }
	}

	private static void createBookingOrder(Scanner scanner, List<BookingOrder> bookingOrders, Customer loggedInCustomer, List<ItemService> selectedServices, String paymentMethod) {
	    String bookingID = "Book-" + UUID.randomUUID().toString().substring(0, 3);
	    double bookingPrice = calculateReservationPrice(selectedServices);

	    BookingOrder bookingOrder = new BookingOrder();
	    bookingOrder.setBookingId(bookingID);
	    bookingOrder.setCustomer(loggedInCustomer);
	    bookingOrder.setServices(selectedServices);
	    bookingOrder.setPaymentMethod(paymentMethod);
	    bookingOrder.setTotalServicePrice(bookingPrice);
	    bookingOrder.calculatePayment();

	    try {
	        bookingOrders.add(bookingOrder);
	        System.out.println("Booking berhasil!!!");
	        System.out.println("Total Harga Service 	: Rp. " + bookingOrder.getTotalServicePrice());
	        System.out.println("Total Pembayaran 		: Rp. " + bookingOrder.getTotalPayment());

	        if (loggedInCustomer instanceof MemberCustomer) {
	            MemberCustomer memberCustomer = (MemberCustomer) loggedInCustomer;
	            memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() - bookingPrice);
	        }
	    } catch (Exception e) {
	        System.out.println("Gagal menambahkan bookingOrder: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	public static boolean isVehicleFound(Customer loggedInCustomer, String vehicleId) {
	    for (Vehicle vehicle : loggedInCustomer.getVehicles()) {
	        if (vehicle.getVehiclesId().equals(vehicleId)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static ItemService findServiceById(String serviceId, List<ItemService> listAllItemService) {
	    for (ItemService service : listAllItemService) {
	        if (service.getServiceId().equals(serviceId)) {
	            return service;
	        }
	    }
	    return null; 
	}

    public static int getHargaService(String serviceId, List<ItemService> listAllItemService) {
        for (ItemService service : listAllItemService) {
            if (service.getServiceId().equals(serviceId)) {
                return (int) service.getPrice();
            }
        }
        return 0; // Service ID tidak ditemukan
    }
    
    
    public static String getVehicleType(Customer loggedInCustomer, String vehicleId) {
        for (Vehicle vehicle : loggedInCustomer.getVehicles()) {
            if (vehicle.getVehiclesId().equals(vehicleId)) {
                return vehicle.getVehicleType(); // Ganti ini dengan method yang mengambil tipe kendaraan dari vehicle
            }
        }
        return null; // Jika vehicleId tidak ditemukan
    }
    
 
    
    public static double calculateReservationPrice(List<ItemService> selectedServices) {
        double totalPrice = 0;
        
        for (ItemService service : selectedServices) {
            totalPrice += service.getPrice();
        }
        
        return totalPrice;
    }
    
    
    public static void topUpSaldoCoin(Customer currentCustomer) {
        if (currentCustomer instanceof MemberCustomer) {
            MemberCustomer memberCustomer = (MemberCustomer) currentCustomer;
            int topUpAmount = Validation.validasiNumberWithRange(
                    "Masukkan nominal dana yang ingin di Top Up ke Saldo Coin : Rp. ",
                    "Input Tidak Valid",
                    "[0-9]+(,[0-9][0-9]*)*",
                    9999999,
                    1);

            memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() + topUpAmount);
            System.out.println("Saldo Coin berhasil ditambahkan. Saldo sekarang: " + memberCustomer.getSaldoCoin());
        } else {
            System.out.println("Maaf fitur ini hanya untuk Member saja!");
        }
    }

    
    

	//Silahkan tambahkan fitur-fitur utama aplikasi disini
	
	//Login
	
	//Info Customer
	
	//Booking atau Reservation
	
	//Top Up Saldo Coin Untuk Member Customer
	
	//Logout
	
}
