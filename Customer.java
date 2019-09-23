import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Customer implements Runnable{
	
	private String customerName; 
	private int totalAmount;
	private final int INITIAL_AMOUNT;
	private List<Integer> potentialBankList;
	private Map<Integer, Bank> bankMap; 

	public Customer(String customerName, int totalAmount, int bankListSize) {
		this.customerName = customerName;
		this.totalAmount = totalAmount;
		this.INITIAL_AMOUNT = totalAmount;
		this.potentialBankList = createPotentialBankList(bankListSize);
	}
	
	private List<Integer> createPotentialBankList(int size) {
		List<Integer> potentialBankList = new ArrayList<Integer>();
		for(int value=1; value<=size; value++) {
			potentialBankList.add(value);
		}	
		return potentialBankList;
	}

	public void setBankMap(Map<Integer, Bank> bankMap) {
		this.bankMap = bankMap;
	}
	
	private int getRandomNumber(int min, int max) {
		Random rand = new Random();
		int num = rand.nextInt((max-min)+1)+min;
		return num;
	}
	
	private int getRequestedAmount() {
	    int requestAmount = getRandomNumber(1, Math.min(this.totalAmount, 50));
	    return requestAmount;
	}
	

	
	public void deductTotalAmount(int requestedAmount) {
		this.totalAmount = this.totalAmount - requestedAmount;
	}

	public int getBankIndex() {
		int index = getRandomNumber(0, potentialBankList.size()-1);
		return index;
	}
	
	@Override
	public void run() {
		
		while(this.totalAmount>0 && !this.potentialBankList.isEmpty()) {
			int requestAmount = getRequestedAmount();
			int bankIndex = getBankIndex();
			
			try {
				Thread.sleep(getRandomNumber(10, 100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Bank bank = bankMap.get(potentialBankList.get(bankIndex));
			
			money.print(this.customerName +" requests a loan of "+ requestAmount +" dollar(s) from " + bank.getBankName());
			
			//print("Requested "+requestAmount+" dollars from "+bank.getBankName()+" bank.");
			boolean response = bank.requestLoan(requestAmount, this.customerName);
			if(response) {
				//print("Approved. Total before deducting was "+ this.totalAmount);
				this.totalAmount = this.totalAmount - requestAmount;
				//print("Approved. Total after deducting is "+ this.totalAmount);
			} else {
				//print("Tries to remove rbc from list. Is list empty? "+this.potentialBankList.isEmpty());
				this.potentialBankList.remove(bankIndex);
				//print("Successfully removes rbc from list. Is list empty?"+this.potentialBankList.isEmpty());
			}
		}
		tryExiting();
	}
	
	private void tryExiting(){
		money.decrementActiveCustomerCount();
		while(true) {
			System.out.print("");
			if(money.getActiveCustomerCount()==0) {
				if(this.totalAmount == 0) {
					money.print(this.customerName+ " has reached the objective of "+ this.INITIAL_AMOUNT +" dollar(s). Woo Hoo!");
				}else {
					money.print(this.customerName+" was only able to borrow "+ (this.INITIAL_AMOUNT - this.totalAmount)+" dollar(s). Boo Hoo!");
				}
				break;
			}
		}
	}

	private void print(String msg) {
		System.out.println(this.customerName+": "+msg);
	}

}
