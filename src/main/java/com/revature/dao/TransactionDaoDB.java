package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.utils.ConnectionUtil;

public class TransactionDaoDB implements TransactionDao {
	
	private static PreparedStatement pstmt;
	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;
	
	AccountDao adao = new AccountDaoDB();

	public TransactionDaoDB() {
		conn = ConnectionUtil.getConnectionUtil().getConnection();
	}

	public List<Transaction> getAllTransactions() {

		List<Transaction> transactionList = new ArrayList<Transaction>();
		String query = "select * from p0_transactions";
				
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Transaction transaction = new Transaction();
				transaction.setSender(adao.getAccount(rs.getInt("from_account")));
				transaction.setRecipient(adao.getAccount(rs.getInt("to_account")));
				transaction.setAmount(rs.getDouble("amount"));
				transaction.setType(Transaction.TransactionType.valueOf(rs.getString("transaction_type")));
				transaction.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
				transactionList.add(transaction);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return transactionList;
	}
	
	
	
	public void updateTransactions(Transaction t) {
		
		if (t.getType().equals(TransactionType.TRANSFER)) {
			// "insert into p0_accounts (owner_id, balance, account_type, approved) values (?, ?, ?, ?)"
			String query = "insert into p0_transactions (from_account, to_account, amount, transaction_type, timestamp) values (?, ?, ?, ?, ?)";
			
			try {
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, t.getSender().getId());		
				pstmt.setInt(2, t.getRecipient().getId());		
				pstmt.setDouble(3, t.getAmount());		
				pstmt.setString(4, t.getType().toString());		
				pstmt.setTimestamp(5, Timestamp.valueOf(t.getTimestamp()));
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else {
			
			String query = "insert into p0_transactions (from_account, amount, transaction_type, timestamp) values (?, ?, ?, ?)";
			
			try {
				pstmt = conn.prepareStatement(query);
				pstmt.setObject(1, t.getSender().getId());		
				pstmt.setDouble(2, t.getAmount());		
				pstmt.setObject(3, t.getType().toString());		
				pstmt.setTimestamp(4, Timestamp.valueOf(t.getTimestamp()));
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
