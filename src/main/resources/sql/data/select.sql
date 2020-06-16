SELECT * FROM Accounts WHERE ID IN (SELECT ID FROM Transfers WHERE TRANSFER_TIME >= '2019-01-01' GROUP BY ID HAVING SUM(AMOUNT) >= 1000);