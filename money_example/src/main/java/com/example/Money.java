package money;

// もともとDollarクラスとFrancクラスを定義していたが、途中から共通化できることに気づき抽出した
class Money implements Expression {
    protected int amount;
    protected String currency;
    Money (int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    public Expression times(int multiplier) {
        return new Money(amount * multiplier, currency);
    }
    // TODO: Sumのplusメソッドと重複している
    // TODO: 同じ通貨を足し算したらExpressionではなくMoneyを返したい
    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }
    public Money reduce(Bank bank, String to) {
        int rate = bank.rate(currency, to);
        return new Money(amount / rate, to);
    }
    String currency() { 
        return currency;
    }
    // Refactor: 派生クラスのメソッドを基底クラスに引き上げた
    public boolean equals(Object object) {
        Money money = (Money) object;
        return amount == money.amount
            && currency().equals(money.currency());
    }
    public String toString() {
        return amount + " " + currency;
    }
    // Factoryパターン
    static Money dollar(int amount) {
        return new Money(amount, "USD");
    }
    static Money franc(int amount) {
        return new Money(amount, "CHF");
    }
}
