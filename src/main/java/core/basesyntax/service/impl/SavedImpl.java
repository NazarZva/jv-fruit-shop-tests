package core.basesyntax.service.impl;

import core.basesyntax.db.FruitsTransactions;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.Saved;
import java.util.ArrayList;
import java.util.List;

public class SavedImpl implements Saved {
    private static final String MESSAGE_DELIMITER = "///";
    private static final String DELIMITER_BY_SENTENCE = ",";
    private static final String FIRST_SENTENCE_IN_FILE = "type,fruit,quantity";

    @Override
    public List<FruitTransaction> saveToDb(String string) {
        String[] sentence = string.split(MESSAGE_DELIMITER);
        if (string == null || !sentence[0].equals(FIRST_SENTENCE_IN_FILE)) {
            throw new RuntimeException("content of file is incorrect");
        }
        List<FruitTransaction> transactions = new ArrayList<>();
        String[] message;
        for (int i = 1; i < sentence.length; i++) {
            message = sentence[i].split(DELIMITER_BY_SENTENCE);
            FruitTransaction fruitTransaction = new FruitTransaction();
            fruitTransaction.setOperation(getOperation(message[0]));
            fruitTransaction.setFruit(message[1]);
            fruitTransaction.setQuantity(Integer.parseInt(message[2]));
            transactions.add(fruitTransaction);
        }
        FruitsTransactions.Storage.addAll(transactions);
        return transactions;
    }

    private FruitTransaction.Operation getOperation(String operation) {
        switch (operation) {
            case "b":
                return FruitTransaction.Operation.BALANCE;
            case "s":
                return FruitTransaction.Operation.SUPPLY;
            case "p":
                return FruitTransaction.Operation.PURCHASE;
            case "r":
                return FruitTransaction.Operation.RETURN;
            default:
                throw new RuntimeException("Don`t have this category: " + operation);
        }
    }
}
