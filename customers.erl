-module(customers).

-export([requestLoan/3]).

-import(lists, [delete/2]).

chooseTargetBank(BankList) ->
    Bank = lists:nth(rand:uniform(length(BankList)),
		     BankList),
    Bank.

requestAmount(Total) ->
    rand:uniform(erlang:min(Total, 50)).

startTransaction(MasterProcess,{CustomerName, TotalAmount}, BankList) ->
    RequestedAmount = requestAmount(TotalAmount),
    BankProcess = chooseTargetBank(BankList),
    {BankPId, BankName} = BankProcess,
    MasterProcess !
      {customer, CustomerName, RequestedAmount, BankName},
    timer:sleep(rand:uniform(100)),
    BankPId ! {self(), {CustomerName, RequestedAmount}}.

finalMessage(MasterProcess, TotalAmount, InitialAmount, CustomerName) ->
    if TotalAmount == 0 -> 
    	MasterProcess !{customer, successful, CustomerName, InitialAmount};
    true ->
		AmountBorrowed = InitialAmount - TotalAmount,
	   	MasterProcess ! {customer, unsuccessful, CustomerName, AmountBorrowed}
    end.

requestLoan(MasterProcess,{CustomerName, TotalAmount, InitialAmount}, BankList) ->
	receive
      {start} ->
	  	if TotalAmount == 0 -> 
	  		true;
	  	true ->
		 	timer:sleep(rand:uniform(100)),
		 	startTransaction(MasterProcess,{CustomerName, TotalAmount}, BankList),
		 	requestLoan(MasterProcess,{CustomerName, TotalAmount, InitialAmount},BankList)
	  	end;
      {approved, ApprovedAmount} ->
      	NewTotalAmount = TotalAmount - ApprovedAmount,
	  	if NewTotalAmount > 0 ->
			startTransaction(MasterProcess,{CustomerName, NewTotalAmount}, BankList);
	    true ->
			%io:fwrite("False\n")
		 	true
	  	end,
	  	requestLoan(MasterProcess,{CustomerName, NewTotalAmount, InitialAmount},BankList);
      {denied, {Sender, BankName}} ->
	  	BankProcess = {Sender, BankName},
	  	UpdatedList = delete(BankProcess, BankList),
	  	if length(UpdatedList) == 0 -> 
	  		true;
	    true ->
		 	startTransaction(MasterProcess,{CustomerName, TotalAmount}, UpdatedList)
	  	end,
	  	requestLoan(MasterProcess,{CustomerName, TotalAmount, InitialAmount}, UpdatedList)
      after 2000 ->
		finalMessage(MasterProcess, TotalAmount, InitialAmount,CustomerName)
    end.
