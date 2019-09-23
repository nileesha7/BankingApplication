-module(money).

-export([start/0]).

print_list([]) -> [];
print_list([H | T]) ->
    {A, B} = H,
    io:fwrite("~w: ~w ~n", [A, B]),
    [H | print_list(T)].

createBankProcessList([]) -> [];
createBankProcessList([H | T]) ->
	{BankName, _} = H,
    Pid = spawn(banks, grantLoan, [self(), H]),
    [{Pid, BankName} | createBankProcessList(T)].

startCustomerProcesses([], BankList) -> [];
startCustomerProcesses([H | T], BankList) ->
    {CustomerName, TotalAmount} = H,
    Pid = spawn(customers, requestLoan,
		[self(), {CustomerName, TotalAmount, TotalAmount}, BankList]),
    Pid ! {start},
    [[] | startCustomerProcesses(T, BankList)].

start() ->
	% Read customers and banks from text file and store in respective lists
    {_, Customers} = file:consult("customers.txt"),
    {_, Banks} = file:consult("banks.txt"),
    
    % Print customer and bank lists
    io:fwrite("** Customers and loan objectives ** \n"),
    print_list(Customers),
    io:fwrite("\n"),
    io:fwrite("** Banks and financial resources **\n"),
    print_list(Banks),
    io:fwrite("\n"),

    % Create bank processes
    BankProcessList = createBankProcessList(Banks),
    startCustomerProcesses(Customers, BankProcessList),

    display().

 display() ->
   receive
        {customer, successful, CustomerName, Amount} ->
            io:fwrite("~s has reached the objective of ~w dollar(s). Woo Hoo! ~n", [CustomerName, Amount]),
            display();
        {customer, unsuccessful, CustomerName, Amount} ->
            io:fwrite("~s was only able to borrow ~w dollar(s). Boo Hoo! ~n", [CustomerName, Amount]),
            display();
 		{customer, CustomerName, RequestedAmount, BankName} -> 
 			io:fwrite("~s requests a loan of ~w from ~s~n", [CustomerName, RequestedAmount, BankName]),
 		    display();
 		{bank, approved, BankName, RequestedAmount, CustomerName} ->
 			io:fwrite("~s approves a loan of ~w dollars from ~s~n", [BankName, RequestedAmount, CustomerName]),
 			display();
 		{bank, denied, BankName,RequestedAmount, CustomerName} ->
 			io:fwrite("~s denies a loan of ~w dollars from ~s~n", [BankName, RequestedAmount, CustomerName]),
 			display()
 	after 2500 -> true
 	end.







