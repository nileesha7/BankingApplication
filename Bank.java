
public class Bank implements Runnable {

	private String bankName; 
	private int totalAmount;
	public static boolean readyToEnd = false;
	
	public Bank(String bankName, int totalAmount) {
		this.bankName = bankName;
		this.totalAmount = totalAmount;
	}
	
	public String getBankName() {
		return bankName;
	}
	
	public synchronized boolean requestLoan(int amount, String customerName) {
		if(amount <= totalAmount) {
			money.print(this.bankName+ " approves a loan of " + amount + " dollars from "+ customerName);
			this.totalAmount = this.totalAmount - amount;
			return true;
		}
		money.print(this.bankName+ " denies a loan of " + amount + " dollars from "+ customerName);
		System.out.println(this.bankName+ " denies a loan of " + amount + " dollars from "+ customerName);
		return false;
	}

	@Override
	public void run() {
		
		while(money.getActiveCustomerCount()!=0) {
			System.out.print("");
			if(money.getActiveCustomerCount()==0) {
				money.print(this.bankName + " has " + this.totalAmount+" dollar(s) remaining.");				
			}
		}
	}
}
