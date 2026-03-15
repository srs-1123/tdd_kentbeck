package money;

class Sum implements Expression {
    // Money -> Expression にして一般化した
    // Money augend;
    // Money addend;
    Expression augend;
    Expression addend;
    Sum(Expression augend, Expression addend) {
        this.augend = augend;
        this.addend = addend;
    }
    public Expression times(int multiplier) {
        return new Sum(augend.times(multiplier), addend.times(multiplier));
    }
    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }
    // Bankクラスから委譲されたreduceメソッド
    public Money reduce(Bank bank, String to) {
        int amount = augend.reduce(bank, to).amount 
            + addend.reduce(bank, to).amount;
        return new Money(amount, to);
    }
}
