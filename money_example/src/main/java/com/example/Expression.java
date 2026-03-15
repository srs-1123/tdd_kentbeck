package money;

// 様々な操作がこのオブジェクトとなり、このオブジェクトでは為替レートをもとに特定の数字に変換する
interface Expression {
    Expression times(int multiplier);
    Expression plus(Expression addend);
    Money reduce(Bank bank, String to);
}
