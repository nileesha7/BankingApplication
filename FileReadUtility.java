import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileReadUtility {
	
	private Map<Integer, Bank> bankMap;
	private List<Customer> customersList;
	private String bankFile;
	private String customerFile;
	
	public FileReadUtility(String bankFile, String customerFile) throws FileNotFoundException {
		this.bankFile = bankFile;
		this.customerFile = customerFile;
		this.bankMap = createBankMap();
		this.customersList = createCustomersList();
	}
	
	private  Map<Integer, Bank> createBankMap() throws FileNotFoundException {
		money.print("** Banks and financial resources **");
		Scanner sc = new Scanner(new File(this.bankFile));
		bankMap = new HashMap<Integer, Bank>();
		int key = 1;
		while(sc.hasNextLine()) {
			String [] inputArray = getInputArray(sc.nextLine().trim());
			String bankName = inputArray[0];
			int amount = Integer.parseInt(inputArray[1]);
			money.print(bankName+": "+amount);
			Bank bank = new Bank(bankName, amount);
			bankMap.put(key++, bank);
		}
		sc.close();
		System.out.println();
		return bankMap;
	}
	
	public Map<Integer, Bank> getBankMap(){
		return this.bankMap;
	}
	
	private List<Customer> createCustomersList() throws FileNotFoundException {
		money.print("** Customers and loan objectives **");
		List<Customer> customersList = new ArrayList<Customer>();
		Scanner sc = new Scanner(new File(this.customerFile));
		while(sc.hasNextLine()) {
			String [] inputArray = getInputArray(sc.nextLine().trim());
			String customerName = inputArray[0];
			int amount = Integer.parseInt(inputArray[1]);
			money.print(customerName+": "+amount);
			Customer customer = new Customer(customerName, amount, this.bankMap.size());
			customersList.add(customer);
		}
		sc.close();
		System.out.println();
		return customersList;
	}
	
	public List<Customer> getCustomersList() {
		return this.customersList;
	}
	
	private static String[] getInputArray(String line) {
		String [] array = line.replaceAll("[{}.]","").split(",");
		return array;
	}
	/*
	public static void printBankList(String file) throws FileNotFoundException {
		System.out.println("** Banks and financial resources **");
		List<Bank> banksList = new ArrayList<Bank>();
		Scanner sc = new Scanner(new File(file));
		while(sc.hasNextLine()) {
			String [] inputArray = getInputArray(sc.nextLine().trim());
			String bankName = inputArray[0];
			int amount = Integer.parseInt(inputArray[1]);
			System.out.println(bankName+": "+amount);
			Bank bank = new Bank(bankName, amount);
			banksList.add(bank);
		}
		sc.close();
		System.out.println();
	}
	
	public static List<Customer> getCustomersList(String customerFile) throws FileNotFoundException {
		System.out.println("** Customers and loan objectives **");
		List<Customer> customersList = new ArrayList<Customer>();
		Scanner sc = new Scanner(new File(customerFile));
		while(sc.hasNextLine()) {
			String [] inputArray = getInputArray(sc.nextLine().trim());
			String customerName = inputArray[0];
			int amount = Integer.parseInt(inputArray[1]);
			System.out.println(customerName+": "+amount);
			Customer customer = new Customer(customerName, amount);
			customersList.add(customer);
		}
		sc.close();
		System.out.println();
		return customersList;
	}
	
	public static List<Bank> getBanksList(String bankFile) throws FileNotFoundException {
		List<Bank> banksList = new ArrayList<Bank>();
		Scanner sc = new Scanner(new File(bankFile));
		while(sc.hasNextLine()) {
			String [] inputArray = getInputArray(sc.nextLine().trim());
			String bankName = inputArray[0];
			int amount = Integer.parseInt(inputArray[1]);
			Bank bank = new Bank(bankName, amount);
			banksList.add(bank);
		}
		sc.close();
		return banksList;
	}
	
	private static String[] getInputArray(String line) {
		String [] array = line.replaceAll("[{}.]","").split(",");
		return array;
	}
	*/

}
