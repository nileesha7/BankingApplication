import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class money {
	public static boolean Ready_To_End;
	public static int activeCustomersCount;
	
	public static void main(String[] args) throws FileNotFoundException {
		
		FileReadUtility fileReadUtility = new FileReadUtility("banks.txt", "customers.txt");
		
		List<Customer> customersList = fileReadUtility.getCustomersList();
		Map<Integer, Bank> bankMap = fileReadUtility.getBankMap();
		
		activeCustomersCount = customersList.size();
		
		for(Customer customer:customersList) {
			customer.setBankMap(bankMap);
			Thread customerThread = new Thread(customer);
			customerThread.start();
		}
		
		for(Bank bank: bankMap.values()) {
			Thread bankThread = new Thread(bank);
			bankThread.start();
		}
	}
	
	public static void print(String msg) {
		System.out.println(msg);
	}

	public synchronized static void decrementActiveCustomerCount() {
		activeCustomersCount --;
	}
	
	public static int getActiveCustomerCount() {
		return activeCustomersCount;
	}
}
