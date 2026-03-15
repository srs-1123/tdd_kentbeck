package money;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    @Test
    void testMultiplication() {
        Money five = Money.dollar(5);
        assertEquals(Money.dollar(10), five.times(2));
        assertEquals(Money.dollar(15), five.times(3));
    }
    @Test
    public void TestEquality() {
        assertTrue(Money.dollar(5).equals(Money.dollar(5)));
        assertFalse(Money.dollar(5).equals(Money.dollar(6)));
        // ↑のテストと内容が重複しているので削除
        // 理由: 実際のコードでは currency()をみて比較しているのでdollar()だけで十分
        // assertTrue(Money.franc(5).equals(Money.franc(5)));
        // assertFalse(Money.franc(5).equals(Money.franc(6)));

        // 疑念をテストにする(FrancとDollarを比較するとどうなるのか)
        assertFalse(Money.franc(5).equals(Money.dollar(5)));
    }
    @Test
    public void testCurrency() {
        assertEquals("USD", Money.dollar(1).currency());
        assertEquals("CHF", Money.franc(1).currency());
    }
    @Test
    public void testSimpleAddition() {
        Money five = Money.dollar(5);
        // reduced: 換算結果
        // sum.reduce ではなく、あえて bank.reduce にした
        Bank bank = new Bank();
        Expression sum = five.plus(five);
        Money reduced = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(10), reduced); // 書籍ではまずassertを書きそこから逆算してact, arrangeを書いた
    }
    @Test
    public void testPlusReturnsSum() {
        // 2つのMoneyの合計はSumとなる
        Money five = Money.dollar(5);
        Expression result = five.plus(five);
        Sum sum = (Sum) result;
        assertEquals(five, sum.augend);
        assertEquals(five, sum.augend);
    }
    @Test 
    public void testReduceSum() {
        // testSimpleAddition では失敗するようなテスト
        Expression sum = new Sum(Money.dollar(3), Money.dollar(4));
        Bank bank = new Bank();
        Money result = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(7), result);
    }
    @Test
    public void testReduceMoney() {
        Bank bank = new Bank();
        Money result = bank.reduce(Money.dollar(1), "USD");
        assertEquals(Money.dollar(1), result);
    }
    @Test
    public void testReduceMoneyDifferentCurrency() {
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result = bank.reduce(Money.franc(2), "USD");
        assertEquals(Money.dollar(1), result);
    }
    // testReduceMoney で"USD", "USD" のレートが計算できずに例外が発生したため
    // 状況再現のためにテストを書いた
    @Test 
    public void testIdentityRate() {
        assertEquals(1, new Bank().rate("USD", "USD"));
    }
    // 学習用テスト: 配列のequalメソッドは配列の中まで見てくれるのかテスト
    // @Test
    // public void testArrayEqual() {
    //     assertTrue((new Object[] {"abc"}).equals(new Object[] {"abc"}));
    // }
    @Test
    public void testMixedAddition() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Money result = bank.reduce(fiveBucks.plus(tenFrancs), "USD");
        assertEquals(Money.dollar(10), result);
    }
    @Test
    public void testSumPlusMoney() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Expression sum = new Sum(fiveBucks, tenFrancs).plus(fiveBucks);
        Money result = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(15), result);
    }
    @Test
    public void testSumTimes() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate("CHF", "USD", 2);
        Expression sum = new Sum(fiveBucks, tenFrancs).times(2);
        Money result = bank.reduce(sum, "USD");
        assertEquals(Money.dollar(20), result);
    }
    // 同じ通貨を足し算したらMoneyを返すような実装は思いつかなかったので削除
    // @Test
    // public void testPlusSameCurrencyReturnsMoney() {
    //     Expression sum = Money.dollar(1).plus(Money.dollar(1));
    //     assertTrue(sum instanceof Money);
    // }
}
