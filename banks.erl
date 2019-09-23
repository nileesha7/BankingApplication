-module(banks).

-export([grantLoan/2]).

deductTotalAmount(RequestedAmount, TotalAmount) ->
    if RequestedAmount =< TotalAmount -> 
    	TotalAmount - RequestedAmount;
    true -> 
    	TotalAmount
    end.

grantLoan(MasterProcess, {BankName, TotalAmount}) ->
	receive 
		{Sender, {CustomerName, RequestedAmount}} ->
			if RequestedAmount =< TotalAmount ->
		 		MasterProcess ! {bank, approved, BankName, RequestedAmount, CustomerName},
		 		Sender ! {approved, RequestedAmount};
	     	true ->
	     		MasterProcess ! {bank, denied, BankName, RequestedAmount, CustomerName},
		 		Sender ! {denied, {self(), BankName}}
	  		end,
			grantLoan(MasterProcess, {BankName, deductTotalAmount(RequestedAmount, TotalAmount)})
	    after 2000 -> io:fwrite("~s has ~w dollar(s) remaining ~n", [BankName, TotalAmount])
	end.

