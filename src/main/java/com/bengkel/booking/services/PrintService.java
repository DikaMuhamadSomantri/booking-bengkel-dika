package com.bengkel.booking.services;

import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class PrintService {
	
	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s. %-25s %n";
		
		System.out.printf("%-25s %n", title);
		System.out.println(line);
		
		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number, data);
			}else {
				System.out.printf(formatTable, 0, data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}
	
	public static void printCustomerProfile(Customer loggedInCustomer) {
	    if (loggedInCustomer != null) {
	        System.out.println("=== Customer Profile ===");
	        System.out.println("Customer Id		: " + loggedInCustomer.getCustomerId());
	        System.out.println("Nama			: " + loggedInCustomer.getName());
	        System.out.println("Customer Status	: " + loggedInCustomer.getCustomerStatus());
	        System.out.println("Alamat			: " + loggedInCustomer.getAddress());

	        if (loggedInCustomer instanceof MemberCustomer) {
	            MemberCustomer memberCustomer = (MemberCustomer) loggedInCustomer;
	            System.out.println("Saldo Koin: " + memberCustomer.getSaldoCoin());
	        }

	        List<Vehicle> customerVehicles = loggedInCustomer.getVehicles();
	        if (customerVehicles != null && !customerVehicles.isEmpty()) {
	            System.out.println("List Kendaraan:");
	            PrintService.printVechicle(customerVehicles);
	        } else {
	            System.out.println("Tidak ada kendaraan terdaftar.");
	        }
	    } else {
	        System.out.println("Data Customer tidak ditemukan.");
	    }
	}

	
	public static void printVechicle(List<Vehicle> listVehicle) {
		String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-15s | %-5s | %-15s |%n";
		String line = "+----+-----------------+------------+-----------------+-----------------+-------+-----------------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Vechicle Id", "Warna", "Brand", "Transmisi", "Tahun", "Tipe Kendaraan");
	    System.out.format(line);
	    int number = 1;
	    String vehicleType = "";
	    for (Vehicle vehicle : listVehicle) {
	    	if (vehicle instanceof Car) {
				vehicleType = "Mobil";
			}else {
				vehicleType = "Motor";
			}
	    	System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getBrand(), vehicle.getTransmisionType(), vehicle.getYearRelease(), vehicleType);
	    	number++;
	    }
	    System.out.printf(line);
	}
	
	public static void printService(String vehicleType, List<ItemService> listAllItemService) {
		String formatTable = "| %-2s | %-10s | %-20s | %-15s | %-15s |%n";
		String line = "+----+-----------------+------------+-----------------+-----------------+-------+-----------------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Service Id", "Nama Service", "Tipe Kendaraan", "Harga");
	    System.out.format(line);
	    int number = 1;
	    for (ItemService service : listAllItemService) {
	    	
	    	 if (service.getVehicleType().equals(vehicleType)) {
	    		 System.out.format(formatTable, number, service.getServiceId(), service.getServiceName(), service.getVehicleType(), service.getPrice());
	 	    	number++;
	         }

	    }
	    System.out.printf(line);
	}
	////////////////////history
	
	public static void printBookingOrderHistory(List<BookingOrder> bookingOrder) {
	    String formatTable = "| %-2s | %-20s | %-20s | %-15s | %-15s | %-50s |%n";
	    String line = "+----+----------------------+----------------------+-----------------+-----------------+----------------------------------------------------+%n";
	    System.out.format(line);
	    System.out.format(formatTable, "No", "Booking Id", "Nama Customer", "Payment Method", "Total Service", "List Service");
	    System.out.format(line);
	    int number = 1;
	    for (BookingOrder booking : bookingOrder) {
	        System.out.format(formatTable, number, booking.getBookingId(), booking.getCustomer().getName(), booking.getPaymentMethod(), booking.getTotalServicePrice(), getListServiceNames(booking.getServices()));
	        number++;
	    }
	    System.out.printf(line);
	}

	// Method to get service names as a string
	private static String getListServiceNames(List<ItemService> services) {
	    StringBuilder serviceNames = new StringBuilder();
	    for (ItemService itemService : services) {
	        serviceNames.append(itemService.getServiceName()).append(", ");
	    }
	    return serviceNames.toString();
	}

	
	//Silahkan Tambahkan function print sesuai dengan kebutuhan.
	
}
