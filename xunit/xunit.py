# wasRunがテストの実行状況の保持、テスト実行の２つの責務を持っていたため分離
class TestCase:
    def __init__(self, name):
        self.name = name
    def run(self):
        # Pluggable Selectorパターン
        #   テストケース名を示す属性を問い合わせ
        #   返ってきたオブジェクトを関数のように呼び出す
        method = getattr(self, self.name)
        method()
    
class WasRun(TestCase):
    def __init__(self, name):
        self.wasRun = None
        super().__init__(name)
    def testMethod(self):
        self.wasRun = 1

class TestCaseTest(TestCase):
    def testRunning(self):
        test = WasRun("testMethod")
        assert(not test.wasRun)
        test.run()
        assert(test.wasRun)

TestCaseTest("testRunning").run()
